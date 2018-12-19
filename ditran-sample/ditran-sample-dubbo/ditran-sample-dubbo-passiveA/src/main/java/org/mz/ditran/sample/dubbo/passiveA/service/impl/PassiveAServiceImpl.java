package org.mz.ditran.sample.dubbo.passiveA.service.impl;

import org.mz.ditran.core.DiTransactional;
import org.mz.ditran.sample.dubbo.passiveA.service.PassiveAService;
import org.mz.ditran.sample.dubbo.passiveB.service.PassiveBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:18
 */
@Service("passiveAService")
public class PassiveAServiceImpl implements PassiveAService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    private PassiveBService passiveBService;

    @Override
    public int receiveMoney(BigDecimal amount) {
        return jdbcTemplate.update("update t_passive_a_account set amount = amount + ? where account = 'lisi'", amount);
    }

    @Override
    @DiTransactional
    public int passMoney(BigDecimal amount) {
        passiveBService.receiveMoney(amount);
        return this.receiveMoney(amount);
    }

}
