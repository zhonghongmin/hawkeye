package com.avengers.hawkeye.server.domain.model;

import com.avengers.hawkeye.core.TraceItem;

/**
 * Created by 鸿敏 on 2017/3/8.
 */
public class TraceServerItem extends TraceItem {

    /**
     * 远程方法实际执行时间
     */
    private long actualExeTime;

    /**
     * 该本地方法对应的远程方法栈
     */
    private TraceServerNode child;

    public long getActualExeTime() {
        return actualExeTime;
    }

    public void setActualExeTime(long actualExeTime) {
        this.actualExeTime = actualExeTime;
    }

    public TraceServerNode getChild() {
        return child;
    }

    public void setChild(TraceServerNode child) {
        this.child = child;
    }
}
