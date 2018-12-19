package org.mz.ditran.common.blocking;

/**
 * @Author: jsonz
 * @Date: 2018-12-19 16:07
 */
public interface Opt<RES> {
    RES operation() throws Exception;
}
