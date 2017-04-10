package com.avengers.hawkeye.utils;

/**
 * Created by 鸿敏 on 2017/2/27.
 */
public class ReflectionUtils {

    /**
     * 获取方法执行结果
     * @param obj
     * @param methodName
     * @param parameterTypes
     * @return
     */
    public static Object invokeMethod(Object obj, String methodName, Class[]  parameterTypes, Object[] parameters){
        Object value = null;
        if(obj != null){
            try {
                value = obj.getClass().getMethod(methodName, parameterTypes).invoke(obj,parameters);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
