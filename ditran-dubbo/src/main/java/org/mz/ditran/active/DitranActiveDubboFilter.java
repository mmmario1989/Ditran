package org.mz.ditran.active;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import org.mz.ditran.DitranDubboFilter;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 16:50
 */
public class DitranActiveDubboFilter extends DitranDubboFilter {


    @Override
    protected boolean isDitran() {
        return false;
    }

    @Override
    public Result doInvoke(Invoker<?> invoker, Invocation invocation) {
        return null;
    }


}
