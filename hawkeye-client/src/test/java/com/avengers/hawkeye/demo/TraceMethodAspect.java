package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.tools.TraceTools;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Created by 鸿敏 on 2017/2/15.
 * 使用方可以通过2种方式定义拦截规则：
 * 1.在需要追踪的方法上加上@Trace的注解，默认实现
 * 2.在自己的工程新建com.avengers.hawkeye.pointcut.MyPointcut类，重新指定规则
 * <p/>
 * 注意，注意，注意：
 * 当开启方式追踪时，必须在最外层开始拦截即业务调用入口，不允许最外层同时有1个以上的入口，否则会导致日志打印多次，比如
 * 正确：最外层只有一个方法(outerMostMethod)入口
 *
 * @Trace outerMostMethod --> innerMethod1
 * --> innerMethod2
 * 错误：最外层有一个以上的方法（outerMethod1,outerMethod2）入口
 * @Trace outerMethod1 --> innerMethod11
 * --> innerMethod12
 * @Trace outerMethod2 --> innerMethod21
 * --> innerMethod22
 */
@Aspect
@Component
public class TraceMethodAspect {
    @Pointcut("execution(* com.avengers.hawkeye.demo..*.*(..)) || @annotation(com.avengers.hawkeye.annotation.Trace)")
    public void baseTraceMethodPointcut() {
    }

    @Around("baseTraceMethodPointcut()")
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
            TraceTools.setExceptionMsg(index,e.getMessage());
            throw e;
        } finally {
            Long afterTime = System.currentTimeMillis();
            //4.退阶
            TraceTools.backward(index, afterTime - beforeTime);
        }
        return result;
    }
}
