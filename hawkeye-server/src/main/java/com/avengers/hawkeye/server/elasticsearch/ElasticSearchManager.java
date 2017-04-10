package com.avengers.hawkeye.server.elasticsearch;

import com.avengers.hawkeye.server.config.ElasticSearchConfig;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by 鸿敏 on 2017/3/8.
 */
@Component
public class ElasticSearchManager {

    @Autowired
    private ElasticSearchConfig esConf;

    private Client client;

    public Client getClient(){
        if(client == null) {
            synchronized (this){
                InetAddress inetAddress;
                try {
                    inetAddress = InetAddress.getByName(esConf.getIp());
                } catch (UnknownHostException e) {
                    throw new RuntimeException(e);
                }
                Settings settings = Settings.builder()
                        .put("cluster.name", esConf.getClusterName())
                        .put("client.transport.sniff", true) //自动嗅探整个集群的状态，把集群中其他ES节点的ip添加到本地的客户端列表中，后面只需要加一个IP就够了
                        .build();
                client = new PreBuiltTransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(inetAddress, esConf.getPort()));
            }
        }
        return client;
    }


}
