package com.mushi.raincache.map;

import com.mushi.raincache.config.CacheConfig;
import com.mushi.raincache.discard.CacheDiscard;
import com.mushi.raincache.discard.CacheDiscardFactory;
import com.mushi.raincache.enums.CacheDiscardPolicyEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈〉
 *
 * @author mushi
 * @create 2018/12/7
 * @since 1.0.0
 */
public class MapCacheChangeListener {

    private static final Logger logger  = LoggerFactory.getLogger(MapCacheChangeListener.class);

    private final MapCache               cache;

    private final CacheDiscardPolicyEnum policyEnum;

    private final CacheDiscardFactory    factory = CacheDiscardFactory.getInstance();

    public MapCacheChangeListener(CacheConfig config, MapCache cache) {
        this.cache = cache;
        this.policyEnum = CacheDiscardPolicyEnum.valueOf(config.getDiscardPolicy().toUpperCase());
    }

    /**
     * 根据策略丢弃缓存
     */
    public void discard() {
        CacheDiscard cacheDiscard = factory.getCacheDiscard(policyEnum);
        cacheDiscard.discard(cache);
    }


}