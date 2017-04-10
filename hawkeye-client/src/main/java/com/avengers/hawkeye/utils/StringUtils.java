package com.avengers.hawkeye.utils;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by 鸿敏 on 2017/2/23.
 */
public class StringUtils {
    /**
     * 判断字段串是否为空串，包括空格
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        return str == null || str.trim().length() == 0;
    }

    /**
     * 以字符向左补齐字符串到指定长度
     * @param source 需要补齐的字符串
     * @param length 补齐长度
     * @param c 补齐字符
     * @return
     */
    public static String fillStrLeft(String source,int length,char c){
        StringBuilder sb = new StringBuilder();
        if(source.length() < length){
            for(int i = 0 ; i < length - source.length(); i++){
                sb.append(c);
            }
        }
        sb.append(source);
        return sb.toString();
    }


    /**
     * 获取格式化的自增数字（7位）,不足位左边补0
     * @param count
     * @return
     */
    public  static String getFormatIncrementInt(AtomicInteger count){
        int i = count.incrementAndGet() % 9999999;
        return fillStrLeft(String.valueOf(i),7,'0');
    }

    public static void main(String[] args){
        AtomicInteger count = new AtomicInteger(9999999);
        System.out.println(getFormatIncrementInt(count));
        System.out.println(getFormatIncrementInt(count));
        System.out.println(getFormatIncrementInt(count));
    }
}
