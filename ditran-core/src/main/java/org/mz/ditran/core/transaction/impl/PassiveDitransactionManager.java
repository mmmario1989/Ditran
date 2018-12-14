package org.mz.ditran.core.transaction.impl;

import org.mz.ditran.common.entity.DitranInfo;
import org.mz.ditran.common.entity.ZkPath;
import org.mz.ditran.core.transaction.DitransactionManagerAdapter;
import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 12:46 PM
 * @Description:
 */
public class PassiveDitransactionManager extends DitransactionManagerAdapter {

    public PassiveDitransactionManager(PlatformTransactionManager transactionManager, DitranZKClient zkClient) {
        super(transactionManager, zkClient);
    }

    @Override
    public DitranInfo begin(String methodName, Propagation propagation) {
        return null;
    }

    @Override
    public boolean listen(ZkPath zkPath) {
        return false;
    }

    @Override
    public void commit(DitranInfo ditranInfo) {

    }
}
