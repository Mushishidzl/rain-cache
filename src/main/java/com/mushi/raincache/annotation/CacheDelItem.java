package com.mushi.raincache.annotation;

import java.lang.annotation.*;

/**
 * 缓存删除注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheDelItem {


    /**
     * 要删除的key
     * @return
     */
    String key() default "";
}
