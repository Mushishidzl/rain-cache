package com.mushi.raincache.redis;

import com.mushi.raincache.Cache;
import com.mushi.raincache.config.CacheConfig;
import com.mushi.raincache.serializer.HessianSerializer;
import com.mushi.raincache.serializer.StringSerializer;
import com.mushi.raincache.to.CacheKey;
import com.mushi.raincache.to.CacheWrapper;
import com.mushi.raincache.util.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ShardedJedis;

import java.util.Collection;
import java.util.Iterator;

/**
 * 〈ShardedJedisCache分区分片〉
 *
 * @author mushi
 * @create 2018/12/7
 * @since 1.0.0
 */
public class ShardedJedisCache implements Cache {

    private static final Logger logger      = LoggerFactory.getLogger(ShardedJedisCache.class);

    /** 缓存配置类 */
    private final CacheConfig                     config;

    private final ShardedJedis shardedJedis;

    private final StringSerializer stringSerializer;

    private final HessianSerializer<CacheWrapper> hessianSerializer;

    /** mutex过期时间,单位：秒 */
    private static final int                      EXPIRE_TIME = 20;

    private static final String                   MUTEX_VAL   = "temp_mutex_val";

    public ShardedJedisCache(ShardedJedis shardedJedis) {
        this(shardedJedis, new CacheConfig());
    }

    public ShardedJedisCache(ShardedJedis shardedJedis, CacheConfig config) {
        LogUtils.info(logger, "ShardedJedisCache init...");
        this.config = config;
        this.shardedJedis = shardedJedis;
        stringSerializer = new StringSerializer(null);
        hessianSerializer = new HessianSerializer<CacheWrapper>();
        LogUtils.info(logger, "ShardedJedisCache init success!");
    }

    @Override
    public void set(CacheKey cacheKey, CacheWrapper wrapper) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey()) || wrapper == null) {
            return;
        }

        int exp = wrapper.getExpireTime();
        if (exp > 0) {
            shardedJedis.setex(stringSerializer.serialze(key), exp, hessianSerializer.serialze(wrapper));
        } else {
            shardedJedis.set(stringSerializer.serialze(key), hessianSerializer.serialze(wrapper));
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
            byte[] bytes = shardedJedis.get(stringSerializer.serialze(key));
            wrapper = hessianSerializer.deserialize(bytes);
        } catch (Exception e) {
            LogUtils.error(e, logger, "查询Redis缓存失败,cacheKey={0}", cacheKey);
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
        return shardedJedis.del(stringSerializer.serialze(key));
    }

    @Override
    public void clear() {
        Collection<Jedis> shards = shardedJedis.getAllShards();
        Iterator<Jedis> iterator = shards.iterator();
        while (iterator.hasNext()) {
            Jedis jedis = iterator.next();
            jedis.flushDB();
        }
    }

    @Override
    public void shutdown() {
        shardedJedis.close();
    }

    public ShardedJedis getShardedJedis() {
        return shardedJedis;
    }

    @Override
    public CacheConfig getConfig() {
        return config;
    }

    @Override
    public Long setMutex(CacheKey cacheKey) {
        String key;
        if (cacheKey == null || StringUtils.isEmpty(key = cacheKey.getKey())) {
            return 0L;
        }
        Long ret = shardedJedis.setnx(stringSerializer.serialze(key), stringSerializer.serialze(MUTEX_VAL));
        shardedJedis.expire(stringSerializer.serialze(key), EXPIRE_TIME);

        return ret;
    }
}