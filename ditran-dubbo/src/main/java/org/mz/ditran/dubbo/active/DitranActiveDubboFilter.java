package org.mz.ditran.dubbo.active;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import org.mz.ditran.dubbo.DitranDubboFilter;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 16:50
 */
@Activate(group = Constants.CONSUMER)
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
