package com.avengers.hawkeye.tools;

import com.avengers.hawkeye.core.TraceContext;
import com.avengers.hawkeye.core.TraceNode;
import com.avengers.hawkeye.facade.TraceParamInterface;
import com.avengers.hawkeye.utils.ReflectionUtils;
import com.avengers.hawkeye.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by 鸿敏 on 2017/2/15.
 * 注意，注意，注意：
 * 当开启@Trace方式追踪时，必须在最外层开始拦截即业务调用入口，不允许最外层同时有1个以上的入口，否则会导致日志打印多次，比如
 * 正确：最外层只有一个方法(outerMostMethod)入口
 *
 * @Trace outerMostMethod --> innerMethod1
 *                        --> innerMethod2
 * 错误：最外层有一个以上的方法（outerMethod1,outerMethod2）入口
 * @Trace outerMethod1 --> innerMethod11
 *                     --> innerMethod12
 * @Trace outerMethod2 --> innerMethod21
 *                     --> innerMethod22
 *
 * SpringBoot拦截器例子
    static public Object trace(ProceedingJoinPoint pjp) throws Throwable {
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
 */
public class TraceTools {

    private static final Logger TraceErrorLogger = LoggerFactory.getLogger("TRACE_ERROR_LOGGER");

    /**
     * 设置系统端口号，后面的GUID生成依赖于端口
     * @param port
     */
    public static void setServerPort(String port){
        if(StringUtils.isEmpty(TraceContext.getCurrentNode().getPort())) {
            ServerInfoTools.PORT = StringUtils.isEmpty(port) ? "8080" : port;
            TraceContext.getCurrentNode().setPort(ServerInfoTools.PORT);
        }
    }
    /**
     * 继承UID，如果调用的方法参数对象实现了TraceParamInterface，
     * 则自动获取非空uid赋值TraceContext.TraceStack.globalUniqueId，
     * 否则需要调用方手动赋值
     * @param args
     */
    public static void extendsUid(Object[] args){
        //只需要在入口处执行一次就可以了
        TraceNode traceNode = TraceContext.getCurrentNode();
        if(traceNode.getCurrentLevel() == 0
                && StringUtils.isEmpty(traceNode.getGlobalUniqueId())){
            Object value = null;
            for(Object arg : args){
                if(arg instanceof TraceParamInterface){
                    value = ReflectionUtils.invokeMethod(arg, TraceParamInterface.GET_METHOD_NAME, null,null);
                    //如果参数对象实现了TraceParamInterface，但UID为空，则说明是业务源头，自动生成UID并赋值
                    if(value == null){
                        String uid = GuidTools.getGuid42();
                        traceNode.setGlobalUniqueId(uid);
                        ReflectionUtils.invokeMethod(arg, TraceParamInterface.SET_METHOD_NAME, new Class[]{String.class} ,new String[]{uid});
                    }
                    break;
                }
            }
            if(value != null){
                TraceContext.getCurrentNode().setGlobalUniqueId(value.toString());
            }
        }
    }

    /**
     * 进阶
     * @param signature 方法签名 eg:void com.JLA.member.demo.DemoService.userDo()
     * @return 当前方法列表下标
     */
    public static int forward(String signature){
        int index = 0;
        try {
            index = TraceContext.getCurrentNode().forward(signature);
        } catch (Throwable throwable) {
            TraceErrorLogger.error("sre:{},idx:{},gid{},bid{}",signature,index,TraceContext.getCurrentNode().getGlobalUniqueId(),TraceContext.getCurrentNode().getBizId());
            TraceErrorLogger.error("forward error",throwable);
        }
        return index;
    }

    /**
     * 设置方法执行过程中的异常信息
     * @param index 当前方法列表下标
     * @param eMsg 方法执行简要异常信息
     */
    public static void setExceptionMsg(int index,String eMsg){
        try {
            if (!TraceContext.getCurrentNode().isExceptionFlag()) {
                TraceContext.getCurrentNode().setExceptionFlag(true);
                TraceContext.getCurrentNode().getTraceItem(index).setExceptionMsg(eMsg);
            }
        } catch (Throwable throwable) {
            TraceErrorLogger.error("idx:{},gid{},bid{}",index,TraceContext.getCurrentNode().getGlobalUniqueId(),TraceContext.getCurrentNode().getBizId());
            TraceErrorLogger.error("setExceptionMsg error", throwable);
        }
    }

    /**
     * 退阶
     * @param index 当前方法列表下标
     * @param execTime 方法执行时间ms
     */
    public static void backward(int index,long execTime){
        try {
            TraceContext.getCurrentNode().backward(index, execTime);
            //最上层时进行处理
            if (TraceContext.getCurrentNode().getCurrentLevel() == 0) {
                TraceContext.getCurrentNode().printStack();
                TraceContext.getCurrentNode().printJsonStack();
            }
        } catch (Throwable throwable) {
            TraceErrorLogger.error("idx:{},gid{},bid{}",index,TraceContext.getCurrentNode().getGlobalUniqueId(),TraceContext.getCurrentNode().getBizId());
            TraceErrorLogger.error("backward error", throwable);
        }
    }
}
