package org.mz.ditran.core.transaction;

import org.apache.zookeeper.CreateMode;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.common.exception.DitranZKException;
import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:42 PM
 * @Description: implement the common methods
 */
public abstract class DitransactionManagerAdapter implements DitransactionManager {

    protected PlatformTransactionManager transactionManager;

    protected DitranZKClient zkClient;

    protected DitranInfo ditranInfo;

    public DitransactionManagerAdapter(PlatformTransactionManager transactionManager, DitranZKClient zkClient) {
        this.transactionManager = transactionManager;
        this.zkClient = zkClient;
    }

    @Override
    public void register() throws Exception {
        String path = zkClient.getClient().create()
                .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                .forPath(ditranInfo.getZkPath().getFullPath(), ditranInfo.getNodeInfo().toBytes());
        ditranInfo.setZkPath(new ZkPath(path));
    }

    @Override
    public void prepare() throws Exception {
        zkClient.update(ditranInfo.getZkPath().getFullPath(), ditranInfo.getNodeInfo().setSucceed().toString());
    }

    @Override
    public void rollback() throws DitranZKException {
        transactionManager.rollback(ditranInfo.getTransactionStatus());
        zkClient.update(ditranInfo.getZkPath().getFullPath(), ditranInfo.getNodeInfo().setFailed().toString());
    }


    protected TransactionStatus beginLocal(Propagation propagation) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(propagation.value());
        return transactionManager.getTransaction(definition);
    }
}
