package org.mz.ditran.common.blocking;

import java.util.concurrent.TimeUnit;

/**
 * @Author: jsonz
 * @Date: 2018-12-19 10:04
 */
public interface Blocking<OPT extends Opt, CONDITION extends Condition, RES> {
    /**
     * 默认的阻塞睡眠时间
     */
    long DEFAULT_TIME_OUT =  10 * 1000;

    /**
     * 默认的阻塞睡眠时间级别
     */
    TimeUnit DEFAULT_TIME_UNIT = TimeUnit.MILLISECONDS;

    /**
     * 阻塞时间采用默认值
     *
     * @param opt
     * @param condition
     * @return
     * @throws Exception
     */

    RES blocking(OPT opt, CONDITION condition) throws Exception;

    /**
     *
     * 阻塞式服务接口
     *
     * @param opt 需要阻塞的操作
     * @param condition 阻塞退出条件
     * @param timeout 阻塞超时时间
     * @return
     * @throws Exception
     */
    RES blocking(OPT opt, CONDITION condition, long timeout, TimeUnit timeUnit) throws Exception;
}
