package com.mushi.raincache.annotation;

import java.lang.annotation.*;

/**
 * 缓存删除
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheDel {


    /**
     * 缓存删除条件
     */
    String condition() default "";


    /**
     * 数组,可以一个或多个,表示删除一个或多个缓存
     * @return
     */
    CacheDelItem [] items();

}
