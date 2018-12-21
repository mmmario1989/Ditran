package org.mz.ditran.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.mz.ditran.common.DitranConstants;
import org.mz.ditran.common.DitranContext;
import org.mz.ditran.common.Handler;
import org.mz.ditran.common.entity.NodeInfo;
import org.mz.ditran.core.conf.DitranActiveContainer;
import org.mz.ditran.core.conf.DitranContainer;
import org.mz.ditran.core.transaction.DitransactionManager;
import org.mz.ditran.core.transaction.DitransactionWrapper;
import org.mz.ditran.core.transaction.impl.ActiveDitransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;

import java.lang.reflect.Method;
import java.net.InetAddress;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 3:37 PM
 * @Description:
 */
@Aspect
public class DitranAspect {


    @Around("@annotation(org.mz.ditran.core.DiTransactional))")
    public Object ditranAround(final ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature)point.getSignature()).getMethod();
        Object[] args = point.getArgs();
        String[] paramTypes = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            paramTypes[i] = args[i].getClass().getSimpleName();
        }
        DiTransactional ditranAnn =  method.getAnnotation(DiTransactional.class);
        Propagation propagation = ditranAnn.propagation();
        long timeout = ditranAnn.timeout();
        DitranContext.setTimeout(timeout);
        DitranContainer container = DitranContainer.getConfig(DitranActiveContainer.class);
        PlatformTransactionManager platformTransactionManager = container.getTransactionManager();
        DitransactionManager manager = new ActiveDitransactionManager(platformTransactionManager, container.getZkClient());
        try {
            NodeInfo nodeInfo = NodeInfo.builder()
                    .className(point.getSignature().getDeclaringType().getName())
                    .host(InetAddress.getLocalHost().getHostAddress())
                    .methodName(point.getSignature().getName())
                    .paramTypes(paramTypes)
                    .pTransactionPath(DitranContext.get().getPTransactionPath())
                    .status(DitranConstants.ZK_NODE_START_VALUE).build();
            return DitransactionWrapper.wrap(new Handler<Object, Object>() {
                @Override
                public Object handle(Object o) throws Throwable {
                    return point.proceed();
                }
            }).with(manager).start(nodeInfo, propagation, null);
        } finally {
            DitranContext.clear();
        }

    }
}
