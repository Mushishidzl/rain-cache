package com.mushi.raincache.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 〈获取切点类的详细信息工具类〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class TargetDetailUtil {

    /**
     * 获取切点方法
     *
     * @param point
     * @return
     */
    public static Method getMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getMethod();
    }

    /**
     * 获取切点方法的返回值类型
     *
     * @param point
     * @return
     */
    public static Class<?> getReturnType(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class<?> returnType = signature.getReturnType();
        return returnType;

    }


}