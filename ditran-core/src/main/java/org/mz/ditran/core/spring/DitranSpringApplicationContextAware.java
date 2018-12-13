package org.mz.ditran.core.spring;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 17:24
 */
@Slf4j
public class DitranSpringApplicationContextAware {
    private static ApplicationContext applicationContext;

    public void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static boolean publishEvent(ApplicationEvent event) {
        try {
            applicationContext.publishEvent(event);
            return true;
        } catch (Exception e) {
            log.error("publish event exception: {}", e.getMessage(), e);
            return false;
        }
    }
}
