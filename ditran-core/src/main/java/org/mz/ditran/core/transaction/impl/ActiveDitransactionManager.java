package org.mz.ditran.core.transaction.impl;

import org.apache.zookeeper.CreateMode;
import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.DitranContext;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.NodeInfo;
import org.mz.ditran.common.entity.ZkPath;
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
        String path = zkClient.getClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(ZkPath.PREFIX+nodeInfo.getClassName()+"_"+nodeInfo.getMethodName());
        ZkPath zkPath = new ZkPath(path);
        zkPath.setNode(DitranConstants.ACTIVE_NODE);
        DitranContext.setZkPath(zkPath);
        TransactionStatus transactionStatus = beginLocal(propagation);
        ditranInfo =  DitranInfo.builder()
                .nodeInfo(nodeInfo)
                .zkPath(zkPath)
                .transactionStatus(transactionStatus)
                .build();
    }

    @Override
    public void regist() throws Exception {
        super.regist();
        DitranContext.setZkPath(ditranInfo.getZkPath());
    }

    @Override
    public void prepare() throws Exception {
        //do nothing
    }

    @Override
    public NodeInfo listen() throws Exception{
        List<String> childs = zkClient.getClient().getChildren().forPath(ditranInfo.getZkPath().getTransactionPath());
        for (String child : childs) {
            ZkPath childPath = new ZkPath(ditranInfo.getZkPath().getTransactionPath()+"/"+child);
            if(child.startsWith(DitranConstants.ACTIVE_NODE)){
                continue;
            }
            NodeInfo nodeInfo = zkClient.getNodeInfo(childPath.getFullPath());
            if(!DitranConstants.ZK_NODE_SUCCESS_VALUE.equals(nodeInfo.getStatus())){
                return nodeInfo;
            }
        }

        // 遍历完了返回成功节点.构造一个成功节点
        return NodeInfo.builder().status(DitranConstants.ZK_NODE_SUCCESS_VALUE).build();
    }

    @Override
    public void commit() throws Exception{
        transactionManager.commit(ditranInfo.getTransactionStatus());
        super.prepare();
    }
}
