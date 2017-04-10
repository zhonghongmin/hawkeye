package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.annotation.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
@Service
public class LocalBService {

    @Autowired
    private RemoteBService remoteBService;

    @Trace
    public void method1(){
        System.out.println("LocalBService.method1()");
        remoteBService.method1();
    }

    @Trace
    public void method2(){
        System.out.println("LocalBService.method2()");
        remoteBService.method2();
    }
}
