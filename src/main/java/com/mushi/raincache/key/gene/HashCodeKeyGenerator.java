package com.mushi.raincache.key.gene;

import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.lang.reflect.Method;

/**
 * 〈生成对象的HashCode值作为缓存的key〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class HashCodeKeyGenerator implements KeyGenerator<Integer> {

    @Override
    public Integer getKey(Object target, Method method, Object... params) {
        KeyWrapper keyWrapper = new KeyWrapper(target.getClass().getName(), method.getName(), params);
        int code = HashCodeBuilder.reflectionHashCode(keyWrapper.toString());
        return code;
    }
}