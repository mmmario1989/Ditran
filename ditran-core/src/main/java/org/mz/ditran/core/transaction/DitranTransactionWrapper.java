package org.mz.ditran.core.transaction;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.Constants;
import org.mz.ditran.common.entity.DitranRpcResultHolder;
import org.mz.ditran.core.zk.DitranZKHandler;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 10:12
 */
@Slf4j
public abstract class DitranTransactionWrapper<T> {

    private PlatformTransactionManager transactionManager;

    private DitranZKHandler handler;

    private TransactionStatus status;

    private DitranRpcResultHolder<T> holder;

    private CountDownLatch latch = new CountDownLatch(1);

    public DitranTransactionWrapper(PlatformTransactionManager transactionManager, DitranZKHandler handler) {
        this.transactionManager = transactionManager;
        this.handler = handler;
    }

    public DitranTransactionWrapper(PlatformTransactionManager transactionManager, DitranZKHandler handler, DitranRpcResultHolder<T> holder) {
        this(transactionManager, handler);
        this.holder = holder;
    }

    /**
     * 进行事务处理
     * @param
     */
    public void doTransaction() {
        handler.register();
        begin();
        try {
            if (holder != null) holder.setT(doBusiness());
            else doBusiness();
            this.latch.countDown();
        } catch (Exception e) {
            rollBack();
            return;
        }

        handler.write(Constants.ZK_NODE_SUCCESS_VALUE);
        if (handler.check()) {
            commit();
            return;
        }

        rollBack();
        handler.write(Constants.ZK_NODE_FAIL_VALUE);
    }

    /**
     * 等待holder中设置值
     */
    public void await() {
        try {
            this.latch.await();
        } catch (Exception e) {
            log.error("Thread interrupted.", e);
        }
    }

    /**
     * 具体的业务逻辑代码
     * @return
     */
    public abstract T doBusiness();



    /**
     * 获取并开始一个新的本地事务
     * @return
     */
    public void begin() {
        TransactionDefinition def = new DefaultTransactionDefinition();
        this.status = transactionManager.getTransaction(def);
    }

    /**
     * 提交事务
     */
    public void commit() {
        this.transactionManager.commit(status);
    }

    /**
     * 回滚事务
     */
    public void rollBack() {
        this.transactionManager.rollback(status);
    }

    public DitranRpcResultHolder<T> getHolder() {
        return this.holder;
    }
}
