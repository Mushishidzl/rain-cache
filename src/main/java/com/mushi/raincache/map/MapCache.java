package com.mushi.raincache.map;

import com.mushi.raincache.Cache;
import com.mushi.raincache.config.CacheConfig;
import com.mushi.raincache.to.CacheKey;
import com.mushi.raincache.to.CacheWrapper;
import com.mushi.raincache.util.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈使用Map实现本地缓存〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class MapCache implements Cache {

    private static final Logger logger = LoggerFactory.getLogger(MapCache.class);

    private final CacheConfig cacheConfig;

    private final MapCacheDaemon mapCacheDaemon;

    private final ConcurrentHashMap<String,Object> cache;

    private final MapCacheChangeListener mapCacheChangeListener;

    private Thread daemonThread;

    public MapCache() {
        this(new CacheConfig());
    }



    public MapCache(CacheConfig cacheConfig) {
        LogUtils.info(logger, "Map Cache initing...");

        this.cacheConfig = cacheConfig;
        cache = new ConcurrentHashMap<String, Object>(cacheConfig.getMaxCacheNums());
        mapCacheDaemon = new MapCacheDaemon(this, cacheConfig);

        //读取磁盘缓存
        mapCacheDaemon.readCacheFromDisk();

        //启动守护线程
        startDaemon();

        //创建缓存更改监听器
        mapCacheChangeListener = new MapCacheChangeListener(cacheConfig, this);

        LogUtils.info(logger, "Map Cache init success!");
    }

    @Override
    public void set(CacheKey cacheKey, CacheWrapper wrapper) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey()) || wrapper == null) {
            return;
        }
        cache.put(key, wrapper);

        if (cache.size() > cacheConfig.getMaxCacheNums()) {
            mapCacheChangeListener.discard();
        }
    }

    @Override
    public CacheWrapper get(CacheKey cacheKey) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey())) {
            return null;
        }

        CacheWrapper wrapper = null;
        try {
            Object value = cache.get(key);
            if (value != null) {
                wrapper = (CacheWrapper) value;
                wrapper.setLastAccessTime(System.currentTimeMillis());
            }
        } catch (Exception e) {
            LogUtils.error(e, logger, "查询Map缓存失败,cacheKey={0}", cacheKey);
        }

        if (wrapper != null && wrapper.isExpire()) {
            cache.remove(key);
            return null;
        }

        return wrapper;
    }

    public CacheWrapper get(CacheKey cacheKey, Callable<CacheWrapper> callable) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey())) {
            return null;
        }

        CacheWrapper wrapper = null;
        if (cache.containsKey(key)) {
            wrapper = (CacheWrapper) cache.get(key);
        } else {
            synchronized (cache) {
                try {
                    if (!cache.containsKey(key)) {
                        wrapper = callable.call();
                    } else {
                        wrapper = (CacheWrapper) cache.get(key);
                    }
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }

        if (wrapper != null) {
            wrapper.setLastAccessTime(System.currentTimeMillis());
        }
        return wrapper;
    }

    @Override
    public Long delete(CacheKey cacheKey) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey())) {
            return 0L;
        }
        Object obj = cache.remove(key);

        return obj == null ? 0L : 1L;
    }

    public ConcurrentHashMap<String, Object> getCache() {
        return cache;
    }

    @Override
    public void clear() {
        if (cache != null) {
            cache.clear();
        }
    }

    @Override
    public void shutdown() {
        interruptDaemon();
        mapCacheDaemon.persistCache();
        clear();
    }

    private synchronized void startDaemon() {
        if (daemonThread == null) {
            daemonThread = new Thread(mapCacheDaemon, "map-cache-daemon");
            daemonThread.setDaemon(true);
            daemonThread.start();
        }
    }

    private synchronized void interruptDaemon() {
        mapCacheDaemon.setRun(false);
        if (daemonThread != null) {
            daemonThread.interrupt();
        }
    }

    @Override
    public CacheConfig getConfig() {
        return cacheConfig;
    }

    @Override
    public Long setMutex(CacheKey cacheKey) {
        throw new UnsupportedOperationException("Map缓存不支持此方法");
    }

}