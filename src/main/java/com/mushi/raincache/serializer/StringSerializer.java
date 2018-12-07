package com.mushi.raincache.serializer;

import com.mushi.raincache.exception.SerializationException;

import java.nio.charset.Charset;

/**
 * 〈字符串序列化〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class StringSerializer implements Serialzer<String> {

    private final Charset charset;

    public StringSerializer(Charset charset) {
        this.charset = Charset.forName("UTF-8");
    }

    @Override
    public byte[] serialze(String t) throws SerializationException {
        return t == null ? null : t.getBytes(charset);
    }

    @Override
    public String deserialize(byte[] bytes) throws SerializationException {
        return bytes == null ? null : new String(bytes, charset);
    }
}