package org.mz.ditran.sample.dubbo.passiveA.service.impl;

import org.mz.ditran.sample.dubbo.passiveA.service.PassiveAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:18
 */
@Service("passiveAService")
public class PassiveAServiceImpl implements PassiveAService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int receiveMoney(BigDecimal amount) {
        return jdbcTemplate.update("update t_passive_a_account set amount = amount + ? where account = 'lisi'", amount);
    }
}
