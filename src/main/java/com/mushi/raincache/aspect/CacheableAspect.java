package com.mushi.raincache.aspect;

import com.mushi.raincache.handler.Handler;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 〈一句话功能简述〉<br>
 * 〈拦截注解〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class CacheableAspect {

    private Handler handler;

    public CacheableAspect(Handler handler) {
        this.handler = handler;
    }

    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return handler.handlerCacheable(proceedingJoinPoint);
    }
}