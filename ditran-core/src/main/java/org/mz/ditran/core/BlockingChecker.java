package org.mz.ditran.core;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.exception.DitransactionException;
import org.mz.ditran.core.blocking.Blocking;
import org.mz.ditran.core.checker.Checker;

import java.util.concurrent.*;

/**
 *
 * 阻塞式检查服务
 *
 * @Author: jsonz
 * @Date: 2018-12-14 19:40
 */
@Slf4j
public class BlockingChecker<T> implements Blocking<Checker<T>, Boolean> {

    private ExecutorService executor;


    /**
     * 阻塞调用
     * @param checker
     * @param timeout
     * @return
     * @throws Exception
     */
    @Override
    public Boolean blocking(Checker<T> checker, long timeout) throws Exception {
        executor = Executors.newFixedThreadPool(1);
        Future<Boolean> future = executor.submit(new BlockingTask<>(checker));
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.error("Blocking checker time out.");
            return false;
        } catch (Exception e1) {
            throw new DitransactionException(e1);
        } finally {
            future.cancel(true);
            if (executor != null) {
                executor.shutdown();
            }
        }
    }


    /**
     * 阻塞式任务.
     * @param <T>
     */
    private static class BlockingTask<T> implements Callable<Boolean> {
        private Checker<T> checker;

        public BlockingTask(Checker<T> checker) {
            this.checker = checker;
        }

        @Override
        public Boolean call() throws Exception {
            try {
                return checker.check();
            } catch (Throwable throwable) {
                throw new DitransactionException(throwable);
            }
        }
    }
}
