package org.mz.ditran.sample.dubbo.active.service.impl;

import org.mz.ditran.core.DiTransactional;
import org.mz.ditran.sample.dubbo.active.service.ActiveService;
import org.mz.ditran.sample.dubbo.passiveA.service.PassiveAService;
import org.mz.ditran.sample.dubbo.passiveB.service.PassiveBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:18
 */
@Service
public class ActiveServiceImpl implements ActiveService {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Resource
    private PassiveAService passiveAService;
    @Resource
    private PassiveBService passiveBService;

    @Override
    @DiTransactional
    public void transMoney2AB(BigDecimal amount) {
        passiveAService.receiveMoney(amount);
        passiveBService.receiveMoney(amount);
        jdbcTemplate.update("update t_active_account set amount = amount - ? where account='zhangsan'", amount.multiply(BigDecimal.valueOf(2)));
    }

    @Override
    @DiTransactional
    public void transMoney2A2B(BigDecimal amount) {
        passiveAService.passMoney(amount);
        jdbcTemplate.update("update t_active_account set amount = amount - ? where account='zhangsan'", amount);
    }



}
