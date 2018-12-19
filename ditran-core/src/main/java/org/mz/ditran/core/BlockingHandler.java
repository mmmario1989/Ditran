package org.mz.ditran.core;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.Handler;
import org.mz.ditran.common.exception.DitransactionException;
import org.mz.ditran.core.blocking.Condition;
import org.mz.ditran.core.blocking.Blocking;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @Author: jsonz
 * @Date: 2018-12-19 10:07
 */
@Slf4j
public class BlockingHandler<PARAM, RES> implements Blocking<Handler<PARAM, RES>, Condition<RES>, RES> {

    private PARAM p;

    public BlockingHandler(PARAM p) {
        this.p = p;
    }

    @Override
    public RES blocking(Handler<PARAM, RES> handler, Condition<RES> condition, long timeout) throws Exception {
        FutureTask<RES> task = new FutureTask<>(new BlockingTask<>(handler, condition, p));
        try {
            new Thread(task).start();
            return task.get(timeout, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            log.error("Blocking handler exception.Msg:{}", e.getMessage());
            throw e;
        } finally {
            task.cancel(true);
        }
    }


    /**
     * 阻塞式任务
     * @param <PARAM>
     * @param <RES>
     */
    private static class BlockingTask<PARAM, RES> implements Callable<RES> {
        private Handler<PARAM, RES> handler;

        private Condition<RES> condition;

        private PARAM p;

        public BlockingTask(Handler<PARAM, RES> handler, Condition<RES> condition, PARAM p) {
            this.handler = handler;
            this.condition = condition;
            this.p = p;
        }

        @Override
        public RES call() throws Exception {
            try {
                RES res;
                do {
                    TimeUnit.MILLISECONDS.sleep(100);
                    res = handler.handle(p);
                } while (!condition.onCondition(res));
                return res;
            } catch (Throwable throwable) {
                throw new DitransactionException(throwable);
            }
        }
    }
}
