package com.mushi.raincache.handler;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 〈缓存处理接口〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public interface Handler {


    /**
     * 缓存处理
     * @param proceedingJoinPoint
     * @return
     */
    Object handlerCacheable(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;


    /**
     * 缓存处理删除
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    Object handlerCacheDel(ProceedingJoinPoint proceedingJoinPoint) throws Throwable;


}
