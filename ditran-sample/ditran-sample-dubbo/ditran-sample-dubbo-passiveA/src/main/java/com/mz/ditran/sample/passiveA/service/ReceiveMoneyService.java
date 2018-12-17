package com.mz.ditran.sample.passiveA.service;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:17
 */
public interface ReceiveMoneyService {
    int receive(String name, BigDecimal amount);
}
