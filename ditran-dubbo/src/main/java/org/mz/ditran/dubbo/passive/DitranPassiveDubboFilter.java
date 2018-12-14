package org.mz.ditran.dubbo.passive;

import com.alibaba.dubbo.rpc.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.mz.ditran.common.Handler;
import org.mz.ditran.common.entity.ResultHolder;
import org.mz.ditran.core.conf.DitranActiveContainer;
import org.mz.ditran.core.conf.DitranContainer;
import org.mz.ditran.core.transaction.DitransactionManager;
import org.mz.ditran.core.transaction.DitransactionWrapper;
import org.mz.ditran.dubbo.DitranDubboFilter;
import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import org.springframework.transaction.annotation.Propagation;

import java.util.concurrent.Executors;

/**
 *
 * 事务被动接受端
 *
 * DUBBO服务提供者
 *
 * @Author: jsonz
 * @Date: 2018-12-13 16:51
 */
@Activate(group = Constants.PROVIDER)
@Slf4j
public class DitranPassiveDubboFilter extends DitranDubboFilter {


    @Override
    protected boolean isDitran() {
        return StringUtils.isNotBlank(RpcContext.getContext().getAttachment(org.mz.ditran.common.Constants.DUBBO_ATTACHMENTS_KEY));
    }

    @Override
    public Result doInvoke(Invoker<?> invoker, Invocation invocation) {
        String transactionId = RpcContext.getContext().getAttachment(org.mz.ditran.common.Constants.DUBBO_ATTACHMENTS_KEY);
        DitransactionManager manager = DitranContainer.getConfig(DitranActiveContainer.class).getDitransactionManager();
        ResultHolder<Result> holder = new ResultHolder<>();
        Executors.newFixedThreadPool(1).execute(new Runnable() {
            @Override
            public void run() {
                try {
                    DitransactionWrapper.wrap(new Handler<Invocation, Result>() {
                        @Override
                        public Result handle(Invocation invocation) throws Throwable {
                            Result result = invoker.invoke(invocation);
                            holder.setT(result);
                            holder.setEmpty(false);
                            return result;
                        }
                    }).with(manager).start(transactionId + "/" + invocation.getMethodName(), Propagation.REQUIRED, invocation);
                } catch (Throwable e) {
                    log.error(e.getMessage(), e);
                }
            }
        });

        while (holder.isEmpty());
        return holder.getT();
    }

}
