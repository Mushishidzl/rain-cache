package com.mushi.raincache.discard;

import com.mushi.raincache.map.MapCache;

/**
 * 〈丢弃缓存〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public interface CacheDiscard {

    /**
     * 丢弃缓存的方法
     * @param mapCache
     */
    void discard(MapCache mapCache);

}
