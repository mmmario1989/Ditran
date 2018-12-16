package org.mz.ditran.core;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.Handler;
import org.mz.ditran.common.exception.DitransactionException;
import org.mz.ditran.core.blocking.Blocking;

import java.util.concurrent.*;

/**
 *
 * 阻塞式检查服务
 *
 * @Author: jsonz
 * @Date: 2018-12-14 19:40
 */
@Slf4j
public class BlockingHandler<PARAM, RES> implements Blocking<Handler<PARAM, RES>, RES> {

    private ExecutorService executor;

    private PARAM p;

    private RES defaultValue;

    public BlockingHandler(PARAM p, RES defaultValue) {
        this.p = p;
        this.defaultValue = defaultValue;
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
        executor = Executors.newFixedThreadPool(1);
        Future<RES> future = executor.submit(new BlockingTask<>(handler, p));
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            log.error("Blocking checker time out.");
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
     * 阻塞式任务
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
}
