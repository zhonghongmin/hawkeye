package com.avengers.hawkeye.server.controller;

import com.avengers.hawkeye.server.domain.model.TraceServerNode;
import com.avengers.hawkeye.server.service.HawkeyeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 鸿敏 on 2017/3/7.
 */
@RestController
@RequestMapping("/avengers/hawkeye")
public class TraceController {

    @Autowired
    HawkeyeService hawkeyeService;

    @RequestMapping("/{guid}")
    public TraceServerNode getTraceData(@PathVariable("guid") String guid){

        return hawkeyeService.getTraceStackByGuid(guid);
    }
}
