package org.mz.ditran.dubbo.active;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcContext;
import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.DitranContext;
import org.mz.ditran.dubbo.DitranDubboFilter;

import java.util.Map;

/**
 * @Author: jsonz
 * @Date: 2018-12-13 16:50
 */
@Activate(group = Constants.CONSUMER)
public class DitranActiveDubboFilter extends DitranDubboFilter {


    @Override
    protected boolean isDitran() {
        return DitranContext.get().getZkPath()!=null;
    }

    @Override
    public Result doInvoke(Invoker<?> invoker, Invocation invocation) {
        Map<String,String> attach = RpcContext.getContext().getAttachments();
        attach.put(DitranConstants.ACTIVE_PATH_KEY,DitranContext.get().getZkPath().getFullPath());
        attach.put(DitranConstants.TIMEOUT_KEY,String.valueOf(DitranContext.get().getTimeout()));
        return invoker.invoke(invocation);
    }


}
