package com.avengers.hawkeye.core;

/**
 * Created by 鸿敏 on 2017/2/20.
 * 存放方法的信息
 */
public class TraceItem {
    /**
     * 方法签名
     */
    private String signature;

    /**
     * 栈里的层次
     */
    private int level;

    /**
     * 执行时间
     */
    private long exeTime;

    /**
     * 异常信息
     */
    private String exceptionMsg = "";

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getExeTime() {
        return exeTime;
    }

    public void setExeTime(long exeTime) {
        this.exeTime = exeTime;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }
}
