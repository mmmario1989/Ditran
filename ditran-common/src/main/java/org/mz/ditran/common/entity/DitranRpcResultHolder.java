package org.mz.ditran.common.entity;

import lombok.Data;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 09:59
 */
@Data
public class DitranRpcResultHolder<T> {
    /**
     * 数据结果
     *
     * 如果是DUBBO，数据类型即为Result
     * 如果是SpringCloud，暂时未知
     *
     */
    private T t;

}
