package org.mz.ditran.sample.dubbo.passiveA;

import org.mz.ditran.core.conf.DitranPassiveContainer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 10:36
 */
@Configuration
public class PassiveConfig {

    @Bean
    public DitranPassiveContainer ditranPassiveContainer(@Value("${ditran.zookeeper}") String zkServserList){
        return new DitranPassiveContainer(zkServserList);
    }

}
