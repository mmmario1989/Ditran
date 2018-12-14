package org.mz.ditran.core.blocking;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 17:53
 */
public interface Blocking<OPT, RES> {
    RES blocking(OPT opt, long timeout) throws Exception;
}
