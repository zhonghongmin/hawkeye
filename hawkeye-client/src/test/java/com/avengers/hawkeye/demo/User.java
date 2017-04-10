package com.avengers.hawkeye.demo;

import com.avengers.hawkeye.facade.BaseTraceParam;

/**
 * Created by 鸿敏 on 2017/2/14.
 */
public class User extends BaseTraceParam{

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户名称
     */
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
