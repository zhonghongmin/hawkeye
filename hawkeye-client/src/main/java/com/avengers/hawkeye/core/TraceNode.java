package com.avengers.hawkeye.core;

import com.avengers.hawkeye.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 鸿敏 on 2017/2/14.
 * 方法调用链追踪栈
 */
public class TraceNode {
    protected static final String NEW_LINE = "\n";
    protected static final String TAB = "  ";

    protected static final Logger TraceLogger = LoggerFactory.getLogger("TRACE_LOGGER");
    protected static final Logger TraceDataLogger = LoggerFactory.getLogger("TRACE_DATA_LOGGER");
    protected static final ObjectMapper ObjectMapper = new ObjectMapper();

    /**
     * 一次业务调用全局唯一ID，如果是分布式系统，则根据此ID进行关联
     */
    private String globalUniqueId;

    /**
     * 业务ID
     */
    private String bizId;

    /**
     * 当前方法调用所在指标
     */
    @JsonIgnore
    private int currentLevel = 0;

    /**
     * 当前服务器IP
     */
    private String ip = "";

    /**
     * 当前服务器端口号
     */
    private String port = "";

    /**
     * 当前服务器机器名
     */
    private String hostName = "";

    /**
     * 当前机器层级，用来记录结点的树状结构
     */
    private int level;

    /**
     * 用来标识同一层级结点的前后顺序
     */
    private long timestamp = 0L;

    /**
     * 当前异常方法已标志
     */
    private boolean exceptionFlag;

    /**
     * 存放每个调用方法信息
     */
    private List<TraceItem> traceItems = new ArrayList<TraceItem>(32);

    /**
     * 存放远程方法调用下标，value存放在方法在traceItems里的下标
     */
    private Map<String,Integer> remoteMethodMap = new HashMap<String,Integer>();

    /**
     * 指标前移，同时返回元素所在层级
     *
     * @param info 方法信息
     * @return
     */
    public int forward(String info) {
        TraceItem item = new TraceItem();
        item.setSignature(info);
        item.setLevel(currentLevel);
        traceItems.add(item);
        ++currentLevel;
        return traceItems.size() - 1;
    }

    /**
     * 指标后移，同时更新元素的执行时间
     *
     * @param index 元素下标
     * @param time  执行时间
     */
    public void backward(int index, long time) {
        traceItems.get(index).setExeTime(time);
        --currentLevel;
    }

    /**
     * 获取当前方法元素
     *
     * @param index
     * @return
     */
    public TraceItem getTraceItem(int index) {
        return traceItems.get(index);
    }

    /**
     * 打印单前方法调用链信息
     */
    public void printStack() {
        StringBuilder stackInfo = new StringBuilder("globalUniqueId:").append(getGlobalUniqueId())
                .append(",bizId:").append(getBizId()).append(NEW_LINE);
        int size = traceItems.size();
        for (int i = 0; i < size; i++) {
            stackInfo.append(this.getPrefix(traceItems.get(i).getLevel()))
                    .append(traceItems.get(i).getSignature()).append(TAB)
                    .append(traceItems.get(i).getExeTime()).append("ms").append(TAB);
            if (traceItems.get(i).getLevel() == 0) {
                stackInfo.append(getHostName()).append("(").append(getIp()).append(":").append(getPort()).append(")").append(TAB);
            }
            stackInfo.append(traceItems.get(i).getExceptionMsg()).append(NEW_LINE);
        }
        TraceLogger.info(stackInfo.toString());
    }

    /**
     * 打印堆栈数据，该日志需独立生成日志文件用于Agent收集
     */
    public void printJsonStack() {
        try {
            TraceDataLogger.info(ObjectMapper.writeValueAsString(this));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 返回方法信息前缀
     *
     * @param level
     * @return
     */
    protected String getPrefix(int level) {
        StringBuilder tempInfo = new StringBuilder();
        if (level > 0) {
            for (int i = 0; i < level; i++) {
                tempInfo.append("    ");
            }
            tempInfo.append("|--");
        }
        return tempInfo.toString();
    }

    /**
     * 返回当前调用栈阶层
     *
     * @return
     */
    public int getCurrentLevel() {
        return this.currentLevel;
    }

    public String getGlobalUniqueId() {
        return globalUniqueId;
    }

    public void setGlobalUniqueId(String globalUniqueId) {
        if (!StringUtils.isEmpty(globalUniqueId)
                && StringUtils.isEmpty(this.globalUniqueId)) {
            this.globalUniqueId = globalUniqueId;
            this.level = 0;
        }
    }

    public String getBizId() {
        return bizId;
    }

    public void setBizId(String bizId) {
        if (!StringUtils.isEmpty(bizId)
                && StringUtils.isEmpty(this.bizId)) {
            this.bizId = bizId;
        }
    }

    public boolean isExceptionFlag() {
        return exceptionFlag;
    }

    public void setExceptionFlag(boolean exceptionFlag) {
        this.exceptionFlag = exceptionFlag;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<TraceItem> getTraceItems() {
        return traceItems;
    }

    public Map<String, Integer> getRemoteMethodMap() {
        return remoteMethodMap;
    }

    public void setRemoteMethodMap(Map<String, Integer> remoteMethodMap) {
        this.remoteMethodMap = remoteMethodMap;
    }
}
