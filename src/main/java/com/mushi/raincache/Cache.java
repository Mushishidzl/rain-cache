package com.mushi.raincache;

import com.mushi.raincache.config.CacheConfig;
import com.mushi.raincache.to.CacheKey;
import com.mushi.raincache.to.CacheWrapper;

/**
 * 〈缓存类接口〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public interface Cache {


    /**
     * 设置缓存
     *
     * @param cacheKey 缓存的key,不能为空
     * @param wrapper  缓存的对象,不能为空
     */
    void set(CacheKey cacheKey, CacheWrapper wrapper);

    /**
     * 查询缓存
     *
     * @param cacheKey 缓存的key,不能为空
     * @return 缓存的对象
     */
    CacheWrapper get(CacheKey cacheKey);

    /**
     * 删除缓存
     *
     * @param cacheKey 缓存的key,不能为空
     * @return 删除缓存的数量
     */
    Long delete(CacheKey cacheKey);

    /**
     * 清空缓存
     */
    void clear();

    /**
     * 关闭缓存
     */
    void shutdown();

    /**
     * 获取缓存配置
     */
    CacheConfig getConfig();

    /**
     * 设置Mutex,Map类型缓存不支持此方法,Redis支持
     */
    Long setMutex(CacheKey cacheKey);


}
