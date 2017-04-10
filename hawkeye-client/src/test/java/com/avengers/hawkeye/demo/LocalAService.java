package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.annotation.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
@Service
public class LocalAService{

    @Autowired
    private RemoteAService remoteAService;

    @Trace
    public void method1(String id){
        System.out.println("LocalAService.method1()");
        remoteAService.method1();
        remoteAService.method2(id,"id2");
    }
}
