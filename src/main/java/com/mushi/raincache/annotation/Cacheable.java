package com.mushi.raincache.annotation;

import java.lang.annotation.*;

/**
 * 缓存注解
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Cacheable {

    /**
     * 缓存key，为空的时候使用自动生成
     * 支持Spring EL表达式
     *
     * @return
     */
    String key() default "";


    /**
     * 超时时间，默认不超时 0
     *
     * @return
     */
    int expireTime() default 0;


    /**
     * 当有多个线程访问同一个缓存时是否同步 ,避免当缓存查询为空时大量请求访问数据库
     * 默认为false表示不同步,如果为true,则意味着同一时间只能有一个线程访问cache,这将降低性能
     *
     * @return
     */
    boolean sync() default false;


    /**
     * 缓存条件,使用Spring EL表达式编写,可以为空
     * 返回true表示缓存,false表示不缓存
     *
     * @return
     */
    String condition() default "";


}
