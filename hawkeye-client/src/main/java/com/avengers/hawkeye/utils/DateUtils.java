package com.avengers.hawkeye.utils;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by 鸿敏 on 2017/2/26.
 * 时间串工具类
 */
public class DateUtils {

    /**
     * 获取8位日期，yyyyMMdd
     * @return
     */
    public static String getDateStr8(){
        FastDateFormat fdf = FastDateFormat.getInstance("yyyyMMdd", TimeZone.getDefault(), Locale.getDefault());
        return fdf.format(new Date());
    }

    /**
     * 获取14位日期，yyyyMMddHHmmss
     * @return
     */
    public static String getDateStr14(){
        FastDateFormat fdf = FastDateFormat.getInstance("yyyyMMddHHmmss", TimeZone.getDefault(), Locale.getDefault());
        return fdf.format(new Date());
    }

    /**
     * 获取17位日期，yyyyMMddHHmmssfff
     * @return
     */
    public static String getDateStr17(){
        FastDateFormat fdf = FastDateFormat.getInstance("yyyyMMddHHmmssSSS", TimeZone.getDefault(), Locale.getDefault());
        return fdf.format(new Date());
    }

    public static void main(String[] args){
        System.out.println(getDateStr8());
        System.out.println(getDateStr14());
        System.out.println(getDateStr17());
        System.out.println(getDateStr17());
        System.out.println(getDateStr17());
    }
}
