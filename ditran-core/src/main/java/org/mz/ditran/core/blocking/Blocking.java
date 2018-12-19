package org.mz.ditran.core.blocking;

/**
 * @Author: jsonz
 * @Date: 2018-12-19 10:04
 */
public interface Blocking<OPT, CONDITION extends Condition, RES> {
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
    RES blocking(OPT opt, CONDITION condition, long timeout) throws Exception;
}
