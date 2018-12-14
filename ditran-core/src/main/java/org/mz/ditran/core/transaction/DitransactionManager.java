package org.mz.ditran.core.transaction;

import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 11:20 AM
 * @Description:
 */
public interface DitransactionManager  {

    void begin(String methodName, Propagation propagation) throws Exception;

    void regist() throws Exception;

    void prepare() throws Exception;

    boolean listen() throws Exception;

    void commit() throws Exception;

    void rollback() throws Exception;

}
