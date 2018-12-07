package com.mushi.raincache.serializer;

import com.mushi.raincache.exception.SerializationException;

/**
 * 〈序列化接口〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public interface Serialzer<T> {

    /**
     * 将制定对象序列化为二进制数据
     * @param t
     * @return
     * @throws SerializationException
     */
    byte[] serialze(T t) throws SerializationException;


    /**
     * 将二进制数据反序列化为对象
     */
    T deserialize(byte[] bytes) throws SerializationException;

}
