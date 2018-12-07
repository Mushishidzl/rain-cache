package com.mushi.raincache.discard;

import com.mushi.raincache.map.MapCache;
import com.mushi.raincache.to.CacheKey;
import com.mushi.raincache.to.CacheWrapper;
import com.mushi.raincache.util.LogUtils;
import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈最近最少使用丢弃策略〉
 *
 * @author mushi
 * @create 2018/12/7
 * @since 1.0.0
 */
public class LruCacheDiscard implements CacheDiscard {

    private static final Logger logger = LoggerFactory.getLogger(LruCacheDiscard.class);


    @Override
    public void discard(MapCache mapCache) {
        if (mapCache == null || MapUtils.isEmpty(mapCache.getCache())) {
            return;
        }

        ConcurrentHashMap<String, Object> cache = mapCache.getCache();
        Iterator<Map.Entry<String, Object>> iterator = cache.entrySet().iterator();

        String minAcsKey = null;
        CacheWrapper minAcsWrapper = new CacheWrapper(null);
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object obj = entry.getValue();
            if (obj instanceof CacheWrapper) {
                CacheWrapper wrapper = (CacheWrapper) obj;
                if (wrapper.getLastAccessTime() < minAcsWrapper.getLastAccessTime()) {
                    minAcsKey = key;
                    minAcsWrapper = wrapper;
                }
            }
        }
        //构造需要被删除的缓存CacheKey
        CacheKey delKey = new CacheKey(mapCache.getConfig().getNameSpace(), minAcsKey);

        mapCache.delete(delKey);

        LogUtils.info(logger, "LRU被删除对象key={0}", delKey);

    }
}