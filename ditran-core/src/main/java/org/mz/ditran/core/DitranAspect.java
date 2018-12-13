package org.mz.ditran.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.mz.ditran.common.DitranThreadContext;
import org.mz.ditran.common.enums.RpcType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 3:37 PM
 * @Description:
 */
@Aspect
public class DitranAspect {

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Around("@annotation(org.mz.ditran.core.DitranTransactional))")
    public Object ditranAround(ProceedingJoinPoint point)throws Throwable{
        Class claz = point.getSignature().getDeclaringType();
        DitranTransactional ditranAnn = (DitranTransactional) claz.getAnnotation(DitranTransactional.class);
        RpcType rpcType = ditranAnn.value();
        Propagation propagation = ditranAnn.propagation();
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(propagation.value());
        //begin transaction
        TransactionStatus status = platformTransactionManager.getTransaction(definition);
        //set context
        DitranThreadContext.set(rpcType, propagation != Propagation.NEVER);
        try{
            Object res = point.proceed();
            //todo block here waiting for passives success
            platformTransactionManager.commit(status);
            //todo notice zk the success
            return res;
        }catch (Exception e){
            //todo notice zk the failure
            platformTransactionManager.rollback(status);
            throw e;
        }finally {
            DitranThreadContext.clear();
        }
    }
}
