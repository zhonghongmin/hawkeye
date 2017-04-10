package com.avengers.hawkeye.server.service;

import com.avengers.hawkeye.server.config.ElasticSearchConfig;
import com.avengers.hawkeye.server.elasticsearch.ElasticSearchManager;
import com.avengers.hawkeye.server.domain.model.TraceServerItem;
import com.avengers.hawkeye.server.domain.model.TraceServerNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 鸿敏 on 2017/3/8.
 */
@Component
public class HawkeyeService {
    private static final Logger TraceLogger = LoggerFactory.getLogger("TRACE_LOGGER");

    private static final String INDEX = "avengers.hawkeye";
    private static final String TYPE = "tracer";
    private static final String GUID = "globalUniqueId";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Map<String,TraceServerNode> cache = new ConcurrentHashMap<String, TraceServerNode>();

    @Autowired
    private ElasticSearchManager elasticSearchManager;

    /**
     * 通过GUID获取方法栈信息
     * @param guid
     * @return
     */
    public TraceServerNode getTraceStackByGuid(String guid){
        TraceServerNode result = cache.get(guid);
        if(result == null){
            result = combindTraceStack(getEsData(guid));
            if(result != null) {
                cache.put(guid, result);
            }
        }
        TraceLogger.debug(result.toString());
        return result;
    }

    /**
     * 按guid获取ES数据
     * @param guid
     * @return
     */
    private SearchHits getEsData(String guid){
        String[] excludes = new String[]{"path","host","@version","@timestamp"};
        Client client = elasticSearchManager.getClient();
        SearchResponse response = client.prepareSearch().setIndices(INDEX).setTypes(TYPE)
                .setFetchSource(null,excludes)
                .setQuery(QueryBuilders.termQuery(GUID, guid))
                .addSort("timestamp", SortOrder.DESC)
                .get();
        return response.getHits();
    }

    /**
     * 组装方法调用栈
     * @param searchHits
     * @return
     */
    private TraceServerNode combindTraceStack(SearchHits searchHits){
        if (searchHits.totalHits() == 0) return null;
        List<TraceServerNode> traceServerNodes = new ArrayList<TraceServerNode>();
        for (SearchHit hit : searchHits) {
            try {
                traceServerNodes.add(objectMapper.readValue(hit.getSourceAsString(), TraceServerNode.class));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        merge(traceServerNodes);
        return traceServerNodes.size() > 0 ? traceServerNodes.get(0) : null;
    }

    /**
     * 拼接每个系统结点数据
     * @param traceServerNodes
     */
    private void merge(List<TraceServerNode> traceServerNodes){
        int size = traceServerNodes.size();
        if(size < 1) return;

        if(size == 1){
            TraceServerNode traceServerNode = traceServerNodes.get(0);
            //根结点level为0
            traceServerNode.setLevel(0);
            updateActualLevel(traceServerNode);
            return;
        }

        int index, levelA, levelB;
        TraceServerNode traceServerNodeA, traceServerNodeB;
        TraceServerItem traceServerItem;
        for(int i = 0 ; i < size ; i++){
            if(i < size - 1){
                traceServerNodeA = traceServerNodes.get(i);
                traceServerNodeB = traceServerNodes.get(i + 1);
                levelA = traceServerNodeA.getLevel();
                levelB = traceServerNodeB.getLevel();
                //如果levelB>=levelA则继续往上移
                if(levelA > levelB){
                    index = traceServerNodeB.getRemoteMethodMap().get(traceServerNodeA.getTraceItem(0).getSignature());
                    traceServerItem = traceServerNodeB.getTraceItem(index);
                    traceServerItem.setChild(traceServerNodeA);
                    traceServerItem.setActualExeTime(traceServerNodeA.getTraceItem(0).getExeTime());
                    traceServerNodeA.removeTraceItem(0);
                    traceServerNodes.remove(i);
                    break;
                }
            }
        }
        merge(traceServerNodes);
    }

    /**
     * 更新每个方法的实际阶级
     */
    private void updateActualLevel(TraceServerNode traceServerNode){
        List<TraceServerItem> traceServerItems = traceServerNode.getTraceServerItems();
        TraceServerItem traceServerItem;
        TraceServerNode child;
        int size = traceServerItems.size();
        for (int i = 0; i < size; i++) {
            traceServerItem = traceServerItems.get(i);
            child = traceServerItem.getChild();
            if(child != null){
                child.setLevel(traceServerNode.getLevel() + traceServerItem.getLevel());
                //递归处理
                updateActualLevel(child);
            }
        }
    }


    /**
     * 缓存暂时方案，后面进行迁移
     * @param guid
     * @param result
     */
    private void addCache(String guid, TraceServerNode result){
        if(cache.size() > 1000){
            cache.clear();
        }
        cache.put(guid,result);
    }
}
