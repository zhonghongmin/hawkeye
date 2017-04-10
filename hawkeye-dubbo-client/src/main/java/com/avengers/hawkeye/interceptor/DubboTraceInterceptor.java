package com.avengers.hawkeye.interceptor;

import com.alibaba.dubbo.rpc.*;
import com.alibaba.dubbo.rpc.protocol.dubbo.filter.TraceFilter;
import com.avengers.hawkeye.constants.Constants;
import com.avengers.hawkeye.core.TraceContext;
import com.avengers.hawkeye.tools.TraceTools;

/**
 * Created by 鸿敏 on 2017/2/28.
 *
 * spring拦截器无法拦截dubbo调用，需要用dubbo的filter实现，在接口调用前后进行方法调用信息的补充
 */
public class DubboTraceInterceptor extends TraceFilter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        int index;
        Result result = null;
        if (RpcContext.getContext().isProviderSide()) {//服务提供端
            //继承globalUniqueId，当前结点顺序+1
            TraceContext.getCurrentNode().setGlobalUniqueId(RpcContext.getContext().getAttachment(Constants.GUID));
            TraceContext.getCurrentNode().setLevel(new Integer(RpcContext.getContext().getAttachment(Constants.LEVEL)) + 1);
            result = invoker.invoke(invocation);
        } else {//服务消费端
            //以防万一
            TraceContext.start();
            RpcContext.getContext().setAttachment(Constants.GUID, TraceContext.getCurrentNode().getGlobalUniqueId());
            RpcContext.getContext().setAttachment(Constants.LEVEL, new Integer(TraceContext.getCurrentNode().getLevel()).toString());
            //1.进阶
            String methodSingnature = getMethodSignature(invoker, invocation);
            index = TraceTools.forward(methodSingnature);
            TraceContext.getCurrentNode().getRemoteMethodMap().put(methodSingnature,index);
            Long beforeTime = System.currentTimeMillis();
            try {
                result = invoker.invoke(invocation);
            } catch (Throwable e) {
                //2.设置异常信息
                TraceTools.setExceptionMsg(index, e.getMessage());
                throw new RpcException(e);
            } finally {
                Long afterTime = System.currentTimeMillis();
                //3.退阶
                TraceTools.backward(index, afterTime - beforeTime);
            }
        }
        return result;
    }

    private String getMethodSignature(Invoker<?> invoker, Invocation invocation) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(invoker.getInterface().getMethod(invocation.getMethodName(), invocation.getParameterTypes()).getReturnType().getSimpleName());
            sb.append(" ").append(invoker.getInterface().getName()).append(".").append(invocation.getMethodName()).append("(");
            Class<?>[] classes = invocation.getParameterTypes();
            int len = classes.length;
            for (int i = 0; i < len; i++) {
                sb.append(classes[i].getSimpleName());
                if (i < len - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }
}
