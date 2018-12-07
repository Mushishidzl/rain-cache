package com.mushi.raincache.aspect;

import com.mushi.raincache.handler.Handler;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 〈拦截删除注解〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class CacheDelAspect {

    private Handler handler;

    public CacheDelAspect(Handler handler) {
        this.handler = handler;
    }

    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return handler.handlerCacheDel(proceedingJoinPoint);
    }
}