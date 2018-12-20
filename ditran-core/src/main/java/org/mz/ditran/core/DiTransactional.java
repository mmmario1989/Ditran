package org.mz.ditran.core;

import org.springframework.transaction.annotation.Propagation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 3:36 PM
 * @Description:
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DiTransactional {

    Propagation propagation() default Propagation.REQUIRED;

    /**
     * unit milliseconds
     * @return
     */
    long timeout() default 10000;

}
