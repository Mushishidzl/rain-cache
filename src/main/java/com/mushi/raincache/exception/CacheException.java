package com.mushi.raincache.exception;

/**
 * 〈缓存异常处理〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class CacheException extends RuntimeException {

    private static final long serialVersionUID = -4572885532138725767L;


    public CacheException() {
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}