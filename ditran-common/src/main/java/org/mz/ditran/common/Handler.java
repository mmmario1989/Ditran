package org.mz.ditran.common;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-14 1:58 PM
 * @Description:
 */
public interface Handler<PARAM,RES> {

    RES handle(PARAM param) throws Throwable;
}
