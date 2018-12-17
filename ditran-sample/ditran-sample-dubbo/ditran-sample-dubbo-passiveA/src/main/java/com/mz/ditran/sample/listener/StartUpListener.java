package com.mz.ditran.sample.listener;

import com.mz.ditran.sample.util.SqlUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import javax.sql.DataSource;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 10:42
 */
public class StartUpListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        DataSource dataSource = (DataSource) contextRefreshedEvent.getApplicationContext().getBean("dataSource");
        try {
            SqlUtils.runSqlBySpringUtils(dataSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
