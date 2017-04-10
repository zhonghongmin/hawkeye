package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.annotation.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by 鸿敏 on 2017/2/15.
 */
@Service
public class UserService {
    @Autowired
    private LocalAService localAService;

    @Autowired
    private LocalBService localBService;

    @Trace
    public void userDo(String id){
        System.out.println("UserService.userDo()");
        localAService.method1(id);
        localBService.method2();
    }
}
