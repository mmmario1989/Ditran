package com.mz.ditran.sample.passiveA.service.impl;

import com.mz.ditran.sample.passiveA.service.ReceiveMoneyService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:18
 */
@Service
public class ReceiveMoneyServiceImpl implements ReceiveMoneyService {
    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public int receive(String name, BigDecimal amount) {
        String sql = String.format("receive t_passive_a_account set amount = amount + %s where account = '%s'", amount, name);
        return jdbcTemplate.update(sql);
    }
}
