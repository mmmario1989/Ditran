package org.mz.ditran.core;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * @Author: mario
 * @Email: mmmario@foxmail.com
 * @Date: 2018-12-13 3:37 PM
 * @Description:
 */
@Aspect
public class DitranAspect {

    @Around("@annotation(org.mz.ditran.core.DitranTransactional))")
    public Object ditranAround(ProceedingJoinPoint point)throws Throwable{
        //todo
        return null;
    }
}
