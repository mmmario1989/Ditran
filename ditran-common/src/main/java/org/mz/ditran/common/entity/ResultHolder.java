package org.mz.ditran.common.entity;

import lombok.Data;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 09:59
 */
@Data
public class ResultHolder<T> {
    /**
     * 判断是否holder是否为空.由启动的线程设置.
     */
    private volatile boolean empty = true;

    /**
     * 数据结果
     *
     * 如果是DUBBO，数据类型即为Result
     * 如果是SpringCloud，暂时未知
     *
     */
    private T t;

    /**
     * catch住异常，在外部抛出感知异常.
     */
    private Throwable e;

}
