package org.mz.ditran.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mz.ditran.common.DitranContext;
import org.mz.ditran.common.Handler;
import org.mz.ditran.common.enums.RpcType;
import org.mz.ditran.core.conf.DitranActiveContainer;
import org.mz.ditran.core.conf.DitranContainer;
import org.mz.ditran.core.transaction.DitransactionManager;
import org.mz.ditran.core.transaction.DitransactionWrapper;
import org.springframework.transaction.annotation.Propagation;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 3:37 PM
 * @Description:
 */
@Aspect
public class DitranAspect {




    @Around("@annotation(org.mz.ditran.core.DiTransactional))")
    public Object ditranAround(final ProceedingJoinPoint point)throws Throwable{
        Class claz = point.getSignature().getDeclaringType();
        DiTransactional ditranAnn = (DiTransactional) claz.getAnnotation(DiTransactional.class);
        RpcType rpcType = ditranAnn.value();
        Propagation propagation = ditranAnn.propagation();
        //set context
        DitranContext.setRpcType(rpcType);
        DitransactionManager manager = DitranContainer.getConfig(DitranActiveContainer.class).getDitransactionManager();
        try{
            return  DitransactionWrapper.wrap(new Handler<Object, Object>() {
                @Override
                public Object handle(Object o) throws Throwable {
                    return point.proceed();
                }
            }).with(manager).start(point.getSignature().getName(),propagation,null);
        }finally {
            DitranContext.clear();
        }

    }
}
