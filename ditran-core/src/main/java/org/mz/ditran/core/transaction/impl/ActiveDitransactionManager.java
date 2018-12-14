package org.mz.ditran.core.transaction.impl;

import org.apache.zookeeper.CreateMode;
import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.core.transaction.DitransactionManager;
import org.mz.ditran.core.transaction.DitransactionManagerAdapter;
import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;

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
    public DitranInfo begin(String methodName, Propagation propagation) throws Exception {
        String path = zkClient.getClient().create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath(zkClient.getPrefix()+"/"+methodName);
        TransactionStatus transactionStatus = beginLocal(propagation);
        return DitranInfo.builder()
                .methodName(methodName)
                .zkPath(new ZkPath(path))
                .transactionStatus(transactionStatus)
                .build();
    }

    @Override
    public boolean listen(ZkPath zkPath) {
        return false;
    }

    @Override
    public void commit(DitranInfo ditranInfo) {

    }
}
