package com.avengers.hawkeye.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by 鸿敏 on 2017/3/8.
 */
@ConfigurationProperties(prefix = "elasticSearch",locations = "classpath:hawkeye.properties")
public class ElasticSearchConfig {

    private String ip;

    private int port;

    private String clusterName;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }
}
