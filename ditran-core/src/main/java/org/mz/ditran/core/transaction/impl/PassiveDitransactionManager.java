package org.mz.ditran.core.transaction.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.blocking.BlockingOpt;
import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.blocking.Condition;
import org.mz.ditran.common.blocking.Opt;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.NodeInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.common.exception.DitranZKException;
import org.mz.ditran.common.exception.DitransactionException;
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
        ZkPath zkPath = new ZkPath(activePath.getFullPath());
        zkPath.setNode(DitranConstants.PASSIVE_NODE);
        TransactionStatus transactionStatus = beginLocal(propagation);
        ditranInfo = DitranInfo.builder()
                .transactionStatus(transactionStatus)
                .zkPath(zkPath)
                .nodeInfo(nodeInfo)
                .build();
    }

    @Override
    public NodeInfo listen() throws Exception {
        // passive端需要阻塞，直到active端写zk成功。如果超时直接返回false，进行回滚.
        return new BlockingOpt<NodeInfo>().blocking(new Opt<NodeInfo>() {
            @Override
            public NodeInfo operation() throws Exception {
                NodeInfo info = zkClient.getNodeInfo(activePath.getFullPath());
                if (DitranConstants.ZK_NODE_FAIL_VALUE.equals(info.getStatus()) ||
                        DitranConstants.ZK_NODE_START_VALUE.equals(info.getStatus())) {
                    return info;
                }
                return recursive(activePath.getTransactionPath());
            }

            /**
             * 递归查询父节点
             * @param path
             * @return
             * @throws DitranZKException
             */
            public NodeInfo recursive(String path) throws Exception {
                String result = zkClient.get(path);
                if (DitranConstants.NULL.equals(result)) {
                    return zkClient.getNodeInfo(path + ZkPath.PREFIX + DitranConstants.ACTIVE_NODE);
                }
                // 如果节点的值不是以NAMESPACE开头的事务节点，则退出递归，防止无穷递归
                if (!result.startsWith(DitranConstants.NAMESPACE)) {
                    log.error("The node value is invalid.Path:[{}], Value:[{}]", result, path);
                    throw new DitransactionException(String.format("The node value is invalid.Path:[%s], Value:[%s]", result, path));
                }
                return recursive(ZkPath.PREFIX+result);
            }
        }, new Condition<NodeInfo>() {
            @Override
            public boolean onCondition(NodeInfo info) {
                return !DitranConstants.ZK_NODE_START_VALUE.equals(info.getStatus());
            }
        }, timeout, TimeUnit.MILLISECONDS);
    }


    @Override
    public void commit() throws Exception {
        transactionManager.commit(ditranInfo.getTransactionStatus());
    }
}
