package org.mz.ditran.core.blocking;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 17:53
 */
public interface Blocking<PARAM, RES> {
    RES blocking(PARAM p, long timeout) throws Exception;
}
