package org.mz.ditran.common;

import org.mz.ditran.common.exception.DitransactionException;

import java.util.concurrent.*;

/**
 * @Author: jsonz , mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-19 10:57 AM
 * @Description: Block to checking the result that Fetcher returns and return when Checker returns true.
 */
public class BlockingChecker<RES> {

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    private long timeout;
    private long sleep = 100;

    public BlockingChecker(long timeout) {
        this.timeout = timeout;
    }

    public BlockingChecker(long timeout, long sleep) {
        this.timeout = timeout;
        this.sleep = sleep;
    }

    public RES check(final Fetcher<RES> fetcher, final Checker<RES> checker) throws Exception {
        Future<RES> future = executor.submit(new Callable<RES>() {
            @Override
            public RES call() throws Exception {
                RES res = fetcher.fetch();
                while (!checker.check(res)) {
                    Thread.sleep(sleep);
                    res = fetcher.fetch();
                }
                return res;
            }
        });
        try {
            return future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            throw new DitransactionException("Ditransaction blocking check interrupted", e);
        } catch (ExecutionException e) {
            throw new DitransactionException("Ditransaction blocking check failed", e);
        } catch (TimeoutException e) {
            throw new DitransactionException("Ditransaction time out:" + timeout + " ms", e);
        } finally {
            future.cancel(true);
        }
    }


    public interface Fetcher<T> {
        T fetch() throws Exception;
    }

    public interface Checker<T> {
        boolean check(T t) throws Exception;
    }
}
