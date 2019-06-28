package org.mz.ditran.core.transaction.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.blocking.BlockingOpt;
import org.mz.ditran.common.blocking.Opt;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.NodeInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.common.exception.DitranZKException;
import org.mz.ditran.core.transaction.DitransactionManagerAdapter;
import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;

import java.util.concurrent.TimeUnit;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:46 PM
 * @Description:
 */
@Data
@Slf4j
public class PassiveDitransactionManager extends DitransactionManagerAdapter {

    private ZkPath activePath;

    private long timeout;


    public PassiveDitransactionManager(PlatformTransactionManager transactionManager, DitranZKClient zkClient, ZkPath activePath, long timeout) {
        super(transactionManager, zkClient);
        this.activePath = activePath;
        this.timeout = timeout;
    }

    @Override
    public void begin(NodeInfo nodeInfo, Propagation propagation) throws Exception {
        ZkPath zkPath = new ZkPath(activePath.getActivePath());
        zkPath.setPassive(DitranConstants.PASSIVE_NODE);
        TransactionStatus transactionStatus = beginLocal(propagation);
        ditranInfo = DitranInfo.builder()
                .transactionStatus(transactionStatus)
                .zkPath(zkPath)
                .nodeInfo(nodeInfo)
                .build();
    }

    @Override
    public void register() throws Exception {
        String path = zkClient.getClient().create()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(ditranInfo.getZkPath().getPassivePath(), ditranInfo.getNodeInfo().toBytes());
        ditranInfo.setZkPath(new ZkPath(path));
    }

    @Override
    public void prepare() throws Exception {
        zkClient.update(ditranInfo.getZkPath().getPassivePath(), ditranInfo.getNodeInfo().setSucceed().toString());
    }


    @Override
    public NodeInfo listen() throws Exception {
        // passive端需要阻塞，直到active端写zk成功。如果超时直接返回false，进行回滚.
        return new BlockingOpt<NodeInfo>().blocking(new Opt<NodeInfo>() {
            @Override
            public NodeInfo operation() throws Exception {
                NodeInfo info = zkClient.getNodeInfo(activePath.getActivePath());
                if (DitranConstants.ZK_NODE_FAIL_VALUE.equals(info.getStatus()) ||
                        DitranConstants.ZK_NODE_START_VALUE.equals(info.getStatus())) {
                    return info;
                }
                return recursive(activePath.getActivePath());
            }

            /**
             * 递归查询父节点
             * @param path
             * @return
             * @throws DitranZKException
             */
            public NodeInfo recursive(String path) throws Exception {
                NodeInfo result = zkClient.getNodeInfo(path);
                if (StringUtils.isBlank(result.getPTransactionPath())) {
                    return result;
                }
                return recursive(result.getPTransactionPath());
            }
        }, info -> !DitranConstants.ZK_NODE_START_VALUE.equals(info.getStatus()), timeout, TimeUnit.MILLISECONDS);
    }


    @Override
    public void commit() throws Exception {
        transactionManager.commit(ditranInfo.getTransactionStatus());
    }


    @Override
    public void rollback() throws DitranZKException {
        transactionManager.rollback(ditranInfo.getTransactionStatus());
        zkClient.update(ditranInfo.getZkPath().getPassivePath(), ditranInfo.getNodeInfo().setFailed().toString());
    }
}
