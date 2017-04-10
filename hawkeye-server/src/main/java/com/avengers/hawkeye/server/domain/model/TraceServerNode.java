package com.avengers.hawkeye.server.domain.model;

import com.avengers.hawkeye.core.TraceNode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 鸿敏 on 2017/3/8.
 */
public class TraceServerNode extends TraceNode {
    /**
     * 存放每个调用方法信息
     */
    private List<TraceServerItem> traceItems = new ArrayList<TraceServerItem>(32);

    private int level;

    /**
     * 获取指定方法元素
     *
     * @param index
     * @return
     */
    @Override
    public TraceServerItem getTraceItem(int index) {
        return traceItems.get(index);
    }

    /**
     * 移除指定方法元素
     * @param index
     */
    public void removeTraceItem(int index){
        traceItems.remove(index);
    }

    /**
     * 不符合重载条件，只能改方法名
     * @return
     */
    public List<TraceServerItem> getTraceServerItems() {
        return traceItems;
    }

    @Override
    public String toString() {
        //header
        StringBuilder stackInfo = new StringBuilder("globalUniqueId:").append(getGlobalUniqueId())
                .append(",bizId:").append(getBizId()).append(NEW_LINE);
        //body
        combined(stackInfo, this);
        return stackInfo.toString();
    }

    /**
     * 拼装方法体
     * @param stackInfo
     * @param traceServerNode
     */
    private void combined(StringBuilder stackInfo, TraceServerNode traceServerNode){
        List<TraceServerItem> traceServerItems = traceServerNode.getTraceServerItems();
        TraceServerItem traceServerItem;
        int size = traceServerItems.size();
        for (int i = 0; i < size; i++) {
            traceServerItem = traceServerItems.get(i);
            stackInfo.append(this.getPrefix(traceServerNode.getLevel() + traceServerItem.getLevel()))
                    .append(traceServerItem.getSignature()).append(TAB)
                    .append(traceServerItem.getExeTime()).append("ms").append(TAB);
            if(traceServerItem.getChild() != null) {
                stackInfo.append((traceServerItem.getActualExeTime())).append("ms").append(TAB);
            }
            if (traceServerItem.getLevel() == 0) {
                stackInfo.append(getHostName())
                        .append("(").append(getIp()).append(":").append(getPort()).append(")")
                        .append(TAB);
            }
            if(traceServerItem.getChild() != null){
                TraceServerNode child = traceServerItem.getChild();
                stackInfo.append(child.getHostName())
                        .append("(").append(child.getIp()).append(":").append(child.getPort()).append(")")
                        .append(TAB);
            }
            stackInfo.append(traceServerItems.get(i).getExceptionMsg()).append(NEW_LINE);

            if(traceServerItem.getChild() != null){
                combined(stackInfo, traceServerItem.getChild());
            }
        }
    }

    @Override
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
