package com.mushi.raincache.exception;

/**
 * 〈序列化异常处理〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class SerializationException extends RuntimeException{


    private static final long serialVersionUID = 2502081267366885369L;


    public SerializationException() {
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public SerializationException(Throwable cause) {
        super(cause);
    }
}