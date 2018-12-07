package com.mushi.raincache.key.gene;

import java.lang.reflect.Method;

/**
 * 〈key生成器〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public interface KeyGenerator<T> {

    /**
     * key生成
     * @param target
     * @param method
     * @param params
     * @return
     */
    T getKey(Object target, Method method, Object... params);

}
