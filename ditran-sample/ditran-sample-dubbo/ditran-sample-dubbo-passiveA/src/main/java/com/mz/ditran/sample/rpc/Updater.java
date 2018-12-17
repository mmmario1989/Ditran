package com.mz.ditran.sample.rpc;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:17
 */
public interface Updater {
    Boolean update(String name, BigDecimal amount);
}
