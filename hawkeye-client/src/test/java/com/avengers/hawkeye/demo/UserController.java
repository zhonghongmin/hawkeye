package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.core.TraceContext;
import com.avengers.hawkeye.tools.GuidTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
@RestController
@RequestMapping("/trace/demo")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("/{id}")
    public User view(@PathVariable("id") String id) {
        User user = new User();
        user.setId(id);
        user.setName("zhonghongmin");
        userService.userDo(id);
        TraceContext.start(GuidTools.getGuid42());
        TraceContext.fillBizId(id);
        return user;
    }
}
