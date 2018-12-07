package com.mushi.raincache.discard;

import com.mushi.raincache.enums.CacheDiscardPolicyEnum;

/**
 * 〈生成CacheDiscard工厂方法〉
 *
 * @author mushi
 * @create 2018/12/7
 * @since 1.0.0
 */
public class CacheDiscardFactory {


    private CacheDiscard fifoDiscard, lruDiscard;


    public CacheDiscardFactory() {
        this.fifoDiscard = new FifoCacheDiscard();
        this.lruDiscard = new LruCacheDiscard();
    }


    public static CacheDiscardFactory getInstance() {
        return FactoryHolder.instance;
    }

    /**
     * 根据丢弃策略的枚举值获取策略的实现类
     *
     * @param dpEnum
     * @return
     */
    public CacheDiscard getCacheDiscard(CacheDiscardPolicyEnum dpEnum) {
        if (dpEnum == CacheDiscardPolicyEnum.FIFO) {
            return fifoDiscard;
        } else if (dpEnum == CacheDiscardPolicyEnum.LRU) {
            return lruDiscard;
        }
        return null;
    }


    /**
     * 私有方法
     */
    private static class FactoryHolder {
        private static final CacheDiscardFactory instance = new CacheDiscardFactory();
    }


}