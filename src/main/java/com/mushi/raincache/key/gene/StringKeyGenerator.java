package com.mushi.raincache.key.gene;

import java.lang.reflect.Method;

/**
 * 〈字符串key生成〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class StringKeyGenerator implements KeyGenerator<String>{

    @Override
    public String getKey(Object target, Method method, Object... params) {
        return null;
    }
}