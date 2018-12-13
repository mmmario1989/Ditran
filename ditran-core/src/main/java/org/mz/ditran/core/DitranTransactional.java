package org.mz.ditran.core;

import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 3:36 PM
 * @Description:
 */
public @interface DitranTransactional {

    RpcType value();

    Propagation propagation() default Propagation.REQUIRED;

}
