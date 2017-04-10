package com.avengers.hawkeye.facade;

import java.io.Serializable;

/**
 * Created by 鸿敏 on 2017/2/26.
 * 继承该类的接口参数为自动赋值分布式ID
 */
public class BaseTraceParam implements TraceParamInterface , Serializable {

    /**
     * 分布式ID
     */
    protected String globalUniqueId;

    @Override
    public String getGlobalUniqueId() {
        return globalUniqueId;
    }

    @Override
    public void setGlobalUniqueId(String globalUniqueId) {
        this.globalUniqueId = globalUniqueId;
    }
}
