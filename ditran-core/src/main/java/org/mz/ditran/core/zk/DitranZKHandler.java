package org.mz.ditran.core.zk;

import org.mz.ditran.common.exception.DitranZKException;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 21:03
 */
public abstract class DitranZKHandler {

    protected DitranZKClient client;

    public DitranZKHandler(DitranZKClient client) {
        this.client = client;
    }

    /**
     * 注册节点到ZK
     * @return
     */
    public abstract void register() throws DitranZKException;

    /**
     * 检查节点状态
     * @return
     */
    public abstract boolean check() throws DitranZKException;

    /**
     * 写节点状态到ZK
     * @param value
     * @return
     */
    public abstract void write(String value) throws DitranZKException;
}
