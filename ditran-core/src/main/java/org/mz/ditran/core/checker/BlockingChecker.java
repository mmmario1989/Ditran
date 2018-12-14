package org.mz.ditran.core.checker;

import org.mz.ditran.common.Handler;
import org.mz.ditran.common.exception.DitransactionException;

import java.util.concurrent.*;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 16:39
 */
public class BlockingChecker<PARAM, RES> implements Checker<Handler<PARAM, RES>, Future<RES>> {

    private Future<RES> future;

    private PARAM p;

    public BlockingChecker(PARAM p) {
        this.p = p;
    }

    public RES check0(Handler<PARAM, RES> handler, long timeout) throws DitransactionException {
        this.future = check(handler);
        try {
            return this.future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            return null;
        } catch (Exception e1) {
            throw new DitransactionException(e1);
        }
    }

    @Override
    public Future<RES> check(Handler<PARAM, RES> handler) {
        return Executors.newFixedThreadPool(1).submit(new BlockingTask<>(handler, p));
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
}
