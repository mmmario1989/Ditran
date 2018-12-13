package org.mz.ditran.dubbo;

import com.alibaba.dubbo.rpc.*;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 16:40
 */
public abstract class DitranDubboFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (isDitran()) {
            return doInvoke(invoker, invocation);
        }
        return invoker.invoke(invocation);
    }

    /**
     * 判断是否需要启用分布式事务.
     */
    protected abstract boolean isDitran();


    /**
     * 业务逻辑处理.
     * @return
     */
    public abstract Result doInvoke(Invoker<?> invoker, Invocation invocation);

}
