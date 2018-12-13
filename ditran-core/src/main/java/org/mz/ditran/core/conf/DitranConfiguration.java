package org.mz.ditran.core.conf;

import org.mz.ditran.core.zk.DitranZKClient;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 6:29 PM
 * @Description:
 */
public class DitranConfiguration implements ApplicationContextAware {

    private static  ApplicationContext context;

    protected String zookeeper;

    public DitranZKClient getZkClient(){
        //todo    
        return null;
    }
    @PostConstruct
    protected void init(){
        //todo
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static <T extends DitranConfiguration> T getConfig(Class<T> config){
        return context.getBean(config);
    }
}
