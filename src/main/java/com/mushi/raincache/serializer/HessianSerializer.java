package com.mushi.raincache.serializer;

import com.caucho.hessian.io.*;
import com.mushi.raincache.exception.SerializationException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 〈HessianSerializer序列化〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class HessianSerializer<T> implements Serialzer<T> {


    private static final Logger logger = LoggerFactory.getLogger(HessianSerializer.class);
    private final SerializerFactory serializerFactory = new SerializerFactory();


    @Override
    public byte[] serialze(T t) throws SerializationException {
        if (t == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        AbstractHessianOutput out = new Hessian2Output(os);
        out.setSerializerFactory(serializerFactory);
        try {
            out.writeObject(t);
        } catch (Exception e) {
            logger.info("hessian serialize failed");
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return os.toByteArray();
    }


    @Override
    @SuppressWarnings("unchecked")
    public T deserialize(byte[] bytes) throws SerializationException {
        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        AbstractHessianInput in = new Hessian2Input(is);
        in.setSerializerFactory(serializerFactory);
        T t = null;
        try {
            t = (T) in.readObject();
        } catch (Exception e) {
            logger.error("hessian deserialize failed", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("", e);
            }
        }
        return t;
    }
}