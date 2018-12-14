package org.mz.ditran.core;

import org.mz.ditran.common.Handler;
import org.mz.ditran.common.exception.DitransactionException;
import org.mz.ditran.core.blocking.Blocking;
import org.mz.ditran.core.checker.Checker;

import java.util.concurrent.*;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 16:39
 */
public class BlockingChecker<PARAM, RES> implements Checker<Handler<PARAM, RES>, Future<RES>>, Blocking<Handler<PARAM, RES>, RES> {

    private ExecutorService executor;

    private Future<RES> future;

    private PARAM p;

    private RES defaultValue;

    public BlockingChecker(PARAM p, RES defaultValue) {
        this.p = p;
        this.defaultValue = defaultValue;
    }

    /**
     * 执行检查操作
     * @param handler
     * @return
     */
    @Override
    public Future<RES> check(Handler<PARAM, RES> handler) {
        executor = Executors.newFixedThreadPool(1);
        return executor.submit(new BlockingTask<>(handler, p));
    }

    /**
     * 阻塞调用
     * @param handler
     * @param timeout
     * @return
     * @throws Exception
     */
    @Override
    public RES blocking(Handler<PARAM, RES> handler, long timeout) throws Exception {
        this.future = check(handler);
        try {
            return this.future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return defaultValue;
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
     * @param <PARAM>
     * @param <RES>
     */
    private static class BlockingTask<PARAM, RES> implements Callable<RES> {
        private Handler<PARAM, RES> handler;
        private PARAM p;

        public BlockingTask(Handler<PARAM, RES> handler, PARAM p) {
            this.handler = handler;
            this.p = p;
        }

        @Override
        public RES call() throws Exception {
            try {
                return handler.handle(p);
            } catch (Throwable throwable) {
                throw new DitransactionException(throwable);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        boolean result = new BlockingChecker<String, Boolean>("123", false).blocking(new Handler<String, Boolean>() {
            @Override
            public Boolean handle(String key) throws Throwable {
                return true;
            }
        }, 1000);
        System.out.println(result);
    }

}
