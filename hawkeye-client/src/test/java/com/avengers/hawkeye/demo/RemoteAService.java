package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.annotation.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
@Service
public class RemoteAService {

    @Autowired
    private LoveService loveService;

    @Trace
    public void method1(){
        System.out.println("RemoteAService.method1()");
        loveService.loveYouHua();
    }

    @Trace
    public void method2(String id,String id2){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("RemoteAService.method2()" + id2);
        if("99".equals(id)) {
            throw new RuntimeException("some thing wrong,help me!");
        }
    }
}
