package com.avengers.hawkeye.tools;

import com.avengers.hawkeye.utils.DateUtils;
import com.avengers.hawkeye.utils.StringUtils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 鸿敏 on 2017/2/26.
 * <p/>
 * 获取网内维一ID工具类
 */
public class GuidTools {

    private static AtomicInteger count = new AtomicInteger(0);

    /**
     * 获取42位GUID,IP（12位）+ ,端口（5位）+ 毫秒（17位）+ 自增数字（8位）
     * 保证系统内唯一，支持每秒100亿并发
     *
     * @return
     */
    public static String getGuid42() {
        StringBuilder sb = new StringBuilder();
        sb.append(ServerInfoTools.FORMAT_IP).
                append(ServerInfoTools.FORMAT_PORT).
                append(DateUtils.getDateStr17()).
                append(StringUtils.getFormatIncrementInt(count));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getGuid42());
        System.out.println(getGuid42());
        System.out.println(getGuid42());
        System.out.println(getGuid42());
    }
}
