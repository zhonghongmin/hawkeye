package com.avengers.hawkeye.server;

import com.avengers.hawkeye.server.config.ElasticSearchConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 鸿敏 on 2017/3/8.
 */
@Configuration
@ComponentScan
@EnableConfigurationProperties({ElasticSearchConfig.class})
@EnableAutoConfiguration
public class SpringBootHawkeyeServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootHawkeyeServerApplication.class);
    }
}
