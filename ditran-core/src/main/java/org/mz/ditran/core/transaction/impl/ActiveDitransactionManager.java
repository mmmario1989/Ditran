package org.mz.ditran.core.transaction.impl;

import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.DitranContext;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.NodeInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.common.exception.DitranZKException;
import org.mz.ditran.core.transaction.DitransactionManagerAdapter;
import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 11:27 AM
 * @Description:
 */
public class ActiveDitransactionManager extends DitransactionManagerAdapter {


    public ActiveDitransactionManager(PlatformTransactionManager transactionManager, DitranZKClient zkClient) {
        super(transactionManager, zkClient);
    }

    @Override
    public void begin(NodeInfo nodeInfo, Propagation propagation) throws Exception {
        String path = zkClient.createTransaction(nodeInfo);
        ZkPath zkPath = new ZkPath(path);
        DitranContext.setZkPath(zkPath);
        TransactionStatus transactionStatus = beginLocal(propagation);
        ditranInfo = DitranInfo.builder()
                .nodeInfo(nodeInfo)
                .zkPath(zkPath)
                .transactionStatus(transactionStatus)
                .build();
    }

    @Override
    public void register() throws Exception {
        //设置active nodeInfo
        zkClient.update(ditranInfo.getZkPath().getActivePath(),ditranInfo.getNodeInfo().toString());
    }

    @Override
    public void prepare() throws Exception {
        //do nothing
    }

    @Override
    public NodeInfo listen() throws Exception {
        List<String> childs = zkClient.getClient().getChildren().forPath(ditranInfo.getZkPath().getActivePath());
        for (String child : childs) {
            ZkPath childPath = new ZkPath(ditranInfo.getZkPath().getActivePath() + ZkPath.PREFIX + child);

            NodeInfo nodeInfo = zkClient.getNodeInfo(childPath.getPassivePath());
            if (!DitranConstants.ZK_NODE_SUCCESS_VALUE.equals(nodeInfo.getStatus())) {
                return nodeInfo;
            }
        }
        // 遍历完了返回成功节点.构造一个成功节点
        return NodeInfo.builder().status(DitranConstants.ZK_NODE_SUCCESS_VALUE).build();
    }

    @Override
    public void commit() throws Exception {
        transactionManager.commit(ditranInfo.getTransactionStatus());
        zkClient.update(ditranInfo.getZkPath().getActivePath(), ditranInfo.getNodeInfo().setSucceed().toString());
    }


    @Override
    public void rollback() throws DitranZKException {
        transactionManager.rollback(ditranInfo.getTransactionStatus());
        zkClient.update(ditranInfo.getZkPath().getActivePath(), ditranInfo.getNodeInfo().setFailed().toString());
    }


}
