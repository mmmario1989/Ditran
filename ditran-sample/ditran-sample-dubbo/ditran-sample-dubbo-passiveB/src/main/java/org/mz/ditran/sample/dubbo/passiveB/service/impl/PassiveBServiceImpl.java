package org.mz.ditran.sample.dubbo.passiveB.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.mz.ditran.sample.dubbo.passiveB.service.PassiveBService;

import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:18
 */
@Service("passiveBService")
public class PassiveBServiceImpl implements PassiveBService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int receiveMoney( BigDecimal amount) {
        return jdbcTemplate.update("update t_passive_b_account set amount = amount + ? where account = 'wangwu'",amount);
    }
}
