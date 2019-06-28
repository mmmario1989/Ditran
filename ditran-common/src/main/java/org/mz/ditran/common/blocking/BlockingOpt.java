package org.mz.ditran.common.blocking;

import lombok.extern.slf4j.Slf4j;
import org.mz.ditran.common.exception.DitransactionException;

import java.util.concurrent.*;

/**
 * @Author: jsonz , mario
 * @Date: 2018-12-19 10:07
 */
@Slf4j
public class BlockingOpt<RES> implements Blocking<Opt<RES>, Condition<RES>, RES> {

    @Override
    public RES blocking(Opt<RES> opt, Condition<RES> condition) throws Exception {
        return blocking(opt, condition, DEFAULT_TIME_OUT, DEFAULT_TIME_UNIT);
    }

    @Override
    public RES blocking(Opt<RES> opt, Condition<RES> condition, long timeout, TimeUnit timeUnit) throws Exception {
        FutureTask<RES> task = new FutureTask<>(new BlockingTask<>(opt, condition));
        try {
            new Thread(task).start();
            return task.get(timeout, timeUnit);
        } catch (InterruptedException e) {
            throw new DitransactionException("Ditransaction blocking check interrupted", e);
        } catch (ExecutionException e) {
            throw new DitransactionException("Ditransaction blocking check failed", e);
        } catch (TimeoutException e) {
            throw new DitransactionException("Ditransaction time out:" + timeout + " ms", e);
        } finally {
            task.cancel(true);
        }
    }


    /**
     * 阻塞式任务
     *
     * @param <RES>
     */
    private static class BlockingTask<RES> implements Callable<RES> {
        private Opt<RES> opt;

        private Condition<RES> condition;

        private int cnt = 0;

        private BlockingTask(Opt<RES> opt, Condition<RES> condition) {
            this.opt = opt;
            this.condition = condition;
        }

        @Override
        public RES call() throws Exception {
            RES res = opt.operation();
            while (!condition.onCondition(res)) {
//                Thread.sleep(100);
                await();
                res = opt.operation();
            }
            return res;
        }

        private void await() throws InterruptedException {
            if (cnt < 20) {
                Thread.yield();
                System.out.println(String.format("await cnt[%d]: yield", cnt));
            } else {
                long millis = 100 + (cnt - 20) / 3 * 100;
                Thread.sleep(millis);
                System.out.println(String.format("await cnt[%d]: %dms", cnt, millis));
            }
            cnt++;
        }

    }

    public static void main(String[] args) throws Exception {
        new BlockingOpt().blocking(() -> 1, o -> false,5,TimeUnit.SECONDS);

    }
}
