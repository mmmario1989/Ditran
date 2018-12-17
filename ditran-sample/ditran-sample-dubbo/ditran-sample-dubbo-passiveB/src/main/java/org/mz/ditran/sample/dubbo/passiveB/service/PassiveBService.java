package org.mz.ditran.sample.dubbo.passiveB.service;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:17
 */
public interface PassiveBService {
    int receiveMoney(BigDecimal amount);
}
