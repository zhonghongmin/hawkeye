package com.avengers.hawkeye.interceptor;

import com.avengers.hawkeye.tools.TraceTools;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Created by 鸿敏 on 2017/3/2.
 *
 * 调用链跟踪切面，会根据com.avengers.hawkeye.pointcut.TraceMethodPointcut.traceMethodPointcut()
 * 的规则进行拦截，该pointcut规则需要使用方自己定，注意保持全类名和方法不变
 */
@Aspect
@Component
public class TraceMethodAspect{
    @Around("com.avengers.hawkeye.pointcut.TraceMethodPointcut.traceMethodPointcut()")
    public Object interceptor(ProceedingJoinPoint pjp) throws Throwable {
        //以下顺序不能乱
        //1.继承globalUniqueId
        TraceTools.extendsUid(pjp.getArgs());

        //2.进阶
        int index = TraceTools.forward(pjp.getSignature().toString());

        Object result;
        Long beforeTime = System.currentTimeMillis();
        try {
            result = pjp.proceed();
        } catch (Throwable e) {
            //3.设置异常信息
            TraceTools.setExceptionMsg(index, e.getMessage());
            throw e;
        } finally {
            Long afterTime = System.currentTimeMillis();
            //4.退阶
            TraceTools.backward(index, afterTime - beforeTime);
        }
        return result;
    }
}
