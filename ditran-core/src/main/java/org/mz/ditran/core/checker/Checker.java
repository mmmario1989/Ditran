package org.mz.ditran.core.checker;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 19:16
 */
public abstract class Checker<T> {

    private T t;

    public Checker(T t) {
        this.t = t;
    }

    public abstract boolean check() throws Throwable;

    public T getT() {
        return t;
    }
}
