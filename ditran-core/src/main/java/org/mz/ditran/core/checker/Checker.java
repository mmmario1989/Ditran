package org.mz.ditran.core.checker;

/**
 * @Author: jsonz
 * @Date: 2018-12-14 16:36
 */
public interface Checker<PARAM, RES> {
    RES check(PARAM p);
}
