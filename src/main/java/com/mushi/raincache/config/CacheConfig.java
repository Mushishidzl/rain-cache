package com.mushi.raincache.config;

import com.mushi.raincache.enums.CacheDiscardPolicyEnum;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 〈缓存配置〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class CacheConfig {


    /**
     * 命名空间，避免多个应用使用的缓存的时候key冲突
     */
    private String nameSpace;

    /**
     * 缓存条数，最大
     */
    private Integer maxCacheNums = 3000;


    /**
     * 当缓存的数量达到maxCacheNums是采用的缓存丢弃策略
     */
    private String discardPolicy = CacheDiscardPolicyEnum.FIFO.name();


    /**
     * 缓存 的存储路径
     */
    private String persistecePath = File.separatorChar + "tmp" + File.separatorChar + "rain-cache";


    /**
     * 是否进行持久化
     */
    private boolean isPersist = true;


    /**
     * 每隔多长时间持久化缓存 秒
     */
    private Integer timeBetweenPersist = 5;


    public String getNameSpace() {
        return nameSpace;
    }

    public CacheConfig setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
        return this;

    }

    public Integer getMaxCacheNums() {
        return maxCacheNums;
    }

    public CacheConfig setMaxCacheNums(Integer maxCacheNums) {
        if (maxCacheNums > 0) {
            this.maxCacheNums = maxCacheNums;
        }
        return this;
    }

    public String getDiscardPolicy() {
        return discardPolicy;
    }

    public void setDiscardPolicy(String discardPolicy) {
        this.discardPolicy = discardPolicy;
    }

    public String getPersistecePath() {
        return persistecePath;
    }

    public CacheConfig setPersistecePath(String persistecePath) {
        if (StringUtils.isNotBlank(persistecePath)) {
            this.persistecePath = persistecePath;
        }
        return this;
    }

    public boolean isPersist() {
        return isPersist;
    }

    public CacheConfig setPersist(boolean persist) {
        this.isPersist = persist;
        return this;
    }

    public Integer getTimeBetweenPersist() {
        return timeBetweenPersist;
    }

    public CacheConfig setTimeBetweenPersist(Integer timeBetweenPersist) {
        this.timeBetweenPersist = timeBetweenPersist;
        return this;
    }
}