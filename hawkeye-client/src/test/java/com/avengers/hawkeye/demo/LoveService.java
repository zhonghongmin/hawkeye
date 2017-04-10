package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.annotation.Trace;
import org.springframework.stereotype.Service;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
@Service
public class LoveService {

    @Trace
    public void loveYouHua(){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("LoveService.loveYouHua()");
    }

}
