package org.mz.ditran.core.blocking;

/**
 * @Author: jsonz
 * @Date: 2018-12-19 10:08
 */
public interface Condition<VALUE> {
    /**
     * 判断条件
     * @param value
     * @return
     */
    boolean onCondition(VALUE value);
}
