package org.mz.ditran.core.transaction;

import org.mz.ditran.common.entity.DitranInfo;
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

    protected TransactionStatus beginLocal(Propagation propagation) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(propagation.value());
        return transactionManager.getTransaction(definition);
    }
}
