package org.mz.ditran.sample.dubbo.active.service;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:17
 */
public interface ActiveService {
    void transMoney(String account, BigDecimal amount);
}
