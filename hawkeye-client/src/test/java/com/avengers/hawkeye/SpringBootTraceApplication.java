package com.avengers.hawkeye;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class SpringBootTraceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTraceApplication.class);
    }
}
