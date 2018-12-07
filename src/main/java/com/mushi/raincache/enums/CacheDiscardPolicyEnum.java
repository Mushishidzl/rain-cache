package com.mushi.raincache.enums;

/**
 * 〈丢失策略〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public enum CacheDiscardPolicyEnum {

    /**
     * 先进先出
     */
    FIFO,

    /**
     * 最少使用策略
     */
    LRU;

}
