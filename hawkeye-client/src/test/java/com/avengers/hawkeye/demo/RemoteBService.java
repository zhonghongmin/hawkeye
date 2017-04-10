package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.annotation.Trace;
import org.springframework.stereotype.Service;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
@Service
public class RemoteBService {

    @Trace
    public void method1(){
        System.out.println("RemoteBService.method1()");
    }

    @Trace
    public void method2(){
        System.out.println("RemoteBService.method2()");
    }
}
