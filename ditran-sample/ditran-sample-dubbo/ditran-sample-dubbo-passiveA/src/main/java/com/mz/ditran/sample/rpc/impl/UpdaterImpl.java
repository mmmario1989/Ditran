package com.mz.ditran.sample.rpc.impl;

import com.mz.ditran.sample.rpc.Updater;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.math.BigDecimal;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 14:18
 */
@Component
public class UpdaterImpl implements Updater {
    @Resource
    private DataSource dataSource;

    @Override
    public Boolean update(String name, BigDecimal amount) {
        JdbcTemplate template = new JdbcTemplate(dataSource);
        String sql = String.format("update t_passive_a_account set amount = amount + %s where account = '%s'", amount, name);
        int result = template.update(sql);
        return result == 1;
    }
}
