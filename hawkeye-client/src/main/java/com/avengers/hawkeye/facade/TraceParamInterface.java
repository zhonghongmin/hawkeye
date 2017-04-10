package com.avengers.hawkeye.facade;

/**
 * Created by 鸿敏 on 2017/2/25.
 */
public interface TraceParamInterface {
    public static final String GET_METHOD_NAME = "getGlobalUniqueId";
    public static final String SET_METHOD_NAME = "setGlobalUniqueId";

    /**
     * 获取分布式全局唯一ID
     * @return
     */
    public String getGlobalUniqueId();

    /**
     * 设置分布式全局唯一ID
     * @param globalUniqueId
     */
    public void setGlobalUniqueId(String globalUniqueId);
}
