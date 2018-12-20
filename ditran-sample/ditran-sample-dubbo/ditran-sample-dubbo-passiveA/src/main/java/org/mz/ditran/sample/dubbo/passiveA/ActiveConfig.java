package org.mz.ditran.sample.dubbo.passiveA;

import org.mz.ditran.core.DitranAspect;
import org.mz.ditran.core.conf.DitranActiveContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: jsonz
 * @Date: 2018-12-17 10:36
 */
@Configuration
public class ActiveConfig {
    @Bean
    public DitranAspect ditranAspect(){
        return new DitranAspect();
    }

    @Bean
    public DitranActiveContainer ditranActiveContainer(@Value("${ditran.zookeeper}") String zkServserList,@Autowired DitranAspect ditranAspect){
        return new DitranActiveContainer(zkServserList,ditranAspect);
    }
}
