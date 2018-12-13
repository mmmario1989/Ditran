package org.mz.ditran.dubbo.passive;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.core.spring.DitranSpringApplicationContextAware;
import org.mz.ditran.core.zk.DitranZKPassiveHandler;
import org.mz.ditran.dubbo.entity.DitranDubboResultHolder;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.CountDownLatch;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 17:30
 */
@Slf4j
public class DitranPassiveWorker implements Runnable {

    private DitranDubboResultHolder holder;
    private Invoker<?> invoker;
    private Invocation invocation;
    private CountDownLatch latch = new CountDownLatch(1);
    private boolean localStatus = true;
    private DitranZKPassiveHandler handler;

    public DitranPassiveWorker(DitranDubboResultHolder holder, Invoker<?> invoker, Invocation invocation) {
        this.holder = holder;
        this.invoker = invoker;
        this.invocation = invocation;
    }

    @Override
    public void run() {
        DataSourceTransactionManager manager = DitranSpringApplicationContextAware.getBean(DataSourceTransactionManager.class);
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus txStatus = manager.getTransaction(def);
        try {
            Result result = invoker.invoke(invocation);
            holder.setResult(result);
        } catch (Exception e) {
            log.error("Invoke Exception.", e);
            this.localStatus = false;
            manager.rollback(txStatus);
            // 向zk写失败.
            handler.write();
        } finally {
            latch.countDown();
        }

        if (localStatus && handler.check()) {
            manager.commit(txStatus);
        } else {
            manager.rollback(txStatus);
        }
    }

    public void await() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("Integerrupted Exception.", e);
        }
    }
}
