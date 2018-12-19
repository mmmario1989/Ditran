package org.mz.ditran.sample.dubbo.passiveA.service;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:17
 */
public interface PassiveAService {
    int receiveMoney(BigDecimal amount);

    int passMoney(BigDecimal amount);

}
