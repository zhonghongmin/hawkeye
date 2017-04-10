package com.avengers.hawkeye.core;

import com.avengers.hawkeye.tools.ServerInfoTools;
import com.avengers.hawkeye.utils.StringUtils;
import com.avengers.hawkeye.tools.GuidTools;

/**
 * Created by 鸿敏 on 2017/2/15.
 * 追踪器上下文
 */
public class TraceContext {
    static private ThreadLocal<TraceNode> TraceNodeHolder = new ThreadLocal<TraceNode>();

    /**
     * 如果你的程序是第一入口且上下文中的GUID为空，调用此方法可由系统自动生成GUID，如果你想生成自己格式的GUID，可以调用fillGlobalUniqueId
     */
    static public void start(){
        //别问为什么start里已经有非空判断为什么还要多此一举判断，问了也不告诉你，反正就是不想浪费GUID
        if(StringUtils.isEmpty(getCurrentNode().getGlobalUniqueId())){
            start(GuidTools.getGuid42());
        }
    }

    /**
     * 显示调用该方法时，请确保业务上是第一入口，且上下文中不存在GUID，否则不生效
     * @param globalUniqueId
     */
    static public void start(String globalUniqueId) {
        getCurrentNode().setGlobalUniqueId(globalUniqueId);
    }

    /**
     * 不想解释
     * @param bizId
     */
    static public void fillBizId(String bizId) {
        getCurrentNode().setBizId(bizId);
    }

    /**
     * 获取当前线程的信息栈
     *
     * @return
     */
    static public TraceNode getCurrentNode() {
        return init();
    }

    /**
     * 初始化上下文，TraceContext所有方法调用的前提条件
     */
    static private TraceNode init() {
        TraceNode traceNode = TraceNodeHolder.get();
        if (traceNode == null) {
            traceNode = new TraceNode();
            traceNode.setTimestamp(System.currentTimeMillis());
            traceNode.setIp(ServerInfoTools.IP);
            traceNode.setPort(ServerInfoTools.PORT);
            traceNode.setHostName(ServerInfoTools.HOST_NAME);
            TraceNodeHolder.set(traceNode);
        }
        return traceNode;
    }

}
