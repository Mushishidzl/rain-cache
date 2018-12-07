package com.mushi.raincache.handler;

import com.mushi.raincache.Cache;
import com.mushi.raincache.annotation.CacheDel;
import com.mushi.raincache.annotation.CacheDelItem;
import com.mushi.raincache.annotation.Cacheable;
import com.mushi.raincache.key.gene.HashCodeKeyGenerator;
import com.mushi.raincache.key.gene.KeyGenerator;
import com.mushi.raincache.key.gene.SpringELParser;
import com.mushi.raincache.map.MapCache;
import com.mushi.raincache.redis.ShardedJedisCache;
import com.mushi.raincache.to.CacheKey;
import com.mushi.raincache.to.CacheWrapper;
import com.mushi.raincache.util.AsserUtils;
import com.mushi.raincache.util.LogUtils;
import com.mushi.raincache.util.TargetDetailUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * 〈缓存处理类〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class DefaulthandLer implements Handler{

    private static final Logger logger = LoggerFactory.getLogger(DefaulthandLer.class);

    private Cache cache;

    private final String nameSpace;

    private final KeyGenerator<Integer> keyGenerator;

    private final SpringELParser elParser;

    private static  final String MUTEX_PREFIX = "mutex_temp_";

    public DefaulthandLer(Cache cache) {
        this.cache = cache;
        nameSpace = cache.getConfig().getNameSpace();
        keyGenerator = new HashCodeKeyGenerator();
        elParser = new SpringELParser();
    }

    @Override
    public Object handlerCacheable(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        Method method = TargetDetailUtil.getMethod(proceedingJoinPoint);
        Cacheable cacheable = method.getAnnotation(Cacheable.class);

        // 获取注解参数
        String key = cacheable.key();
        int expireTime = cacheable.expireTime();
        String condition = cacheable.condition();
        boolean sync = cacheable.sync();
        Object[] args = proceedingJoinPoint.getArgs();
        AsserUtils.assertTrue(expireTime >= 0,"超时时间不能为负");


        LogUtils.info(logger,"key={0},expireTime={1},condition={2},sync={3},args={4}", key, expireTime, condition, sync, args);

        //如果不满足缓存条件直接返回
        if (StringUtils.isNotBlank(condition) && !isCacheable(condition, args)) {
            return proceedingJoinPoint.proceed();
        }

        //组装CacheKey
        if (StringUtils.isBlank(key)) {
            key = (keyGenerator.getKey(proceedingJoinPoint.getTarget(), method, args)).toString();
        } else {
            key = getKeySpELValue(key, args);
        }
        CacheKey cacheKey = new CacheKey(nameSpace, key);

        return getValue(proceedingJoinPoint, cacheKey, sync, expireTime);

    }

    private Object getValue(ProceedingJoinPoint proceedingJoinPoint, CacheKey cacheKey, boolean sync, int expireTime) throws Throwable {
        CacheWrapper cacheWrapper = cache.get(cacheKey);
        if (cacheWrapper != null) {//缓存命中
            return cacheWrapper.getObj();
        }

        Object proceedRet = null;
        if (sync) {
            if (cache instanceof MapCache) {
                return getFromMapCache(proceedingJoinPoint, cacheKey, expireTime);
            } else if (cache instanceof ShardedJedisCache) {
                CacheKey mutexKey = new CacheKey(cacheKey.getNameSpace(), MUTEX_PREFIX + cacheKey.getKey());
                if (cache.setMutex(mutexKey) == 1L) {
                    proceedRet = proceedingJoinPoint.proceed();
                    //放入缓存
                    if (proceedRet != null) {
                        CacheWrapper newWrapper = new CacheWrapper(proceedRet, expireTime);
                        cache.set(cacheKey, newWrapper);
                        cache.delete(mutexKey);
                    }
                    return proceedRet;
                } else {
                    Thread.sleep(50);
                    return getValue(proceedingJoinPoint, cacheKey, sync, expireTime);
                }
            }
        } else {
            proceedRet = proceedingJoinPoint.proceed();
            //放入缓存
            if (proceedRet != null) {
                CacheWrapper newWrapper = new CacheWrapper(proceedRet, expireTime);
                cache.set(cacheKey, newWrapper);
            }
            return proceedRet;
        }
        return proceedRet;
    }

    /**
     * 缓存删除的逻辑:
     * 先操作数据库,成功以后再删除缓存
     */
    @Override
    public Object handlerCacheDel(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object proceedRet = proceedingJoinPoint.proceed();

        Method method = TargetDetailUtil.getMethod(proceedingJoinPoint);
        CacheDel cacheDel = method.getAnnotation(CacheDel.class);
        String condition = cacheDel.condition();

        if (StringUtils.isNotBlank(condition) && !isDel(condition, proceedRet)) {//不满足缓存删除条件
            return proceedRet;
        }

        try {
            CacheDelItem[] items = cacheDel.items();
            for (CacheDelItem item : items) {
                String keySpEL = item.key();
                if (StringUtils.isBlank(keySpEL)) {
                    continue;
                }
                String key = getKeySpELValue(keySpEL, proceedingJoinPoint.getArgs());
                CacheKey cacheKey = new CacheKey(nameSpace, key);
                cache.delete(cacheKey);
            }
        } catch (Exception e) {
            logger.error("删除缓存出错", e);
        }

        return proceedRet;
    }

    private Object getFromMapCache(final ProceedingJoinPoint jp, final CacheKey cacheKey, final int expireTime) {
        CacheWrapper _wrapper = ((MapCache) cache).get(cacheKey, new Callable<CacheWrapper>() {
            @Override
            public CacheWrapper call() {
                Object obj = null;
                try {
                    obj = jp.proceed();
                } catch (Throwable e) {
                    logger.error("", e);
                }

                CacheWrapper wrapper = null;
                if (obj != null) {
                    wrapper = new CacheWrapper(obj, expireTime);
                    cache.set(cacheKey, wrapper);
                }
                return wrapper;
            }
        });
        if (_wrapper != null) {
            return _wrapper.getObj();
        }

        return null;
    }

    private boolean isCacheable(String conditionSpEl, Object[] args) {
        return elParser.getELBooleanValue(conditionSpEl, args);
    }

    private String getKeySpELValue(String keySpEl, Object[] args) {
        return elParser.getELStringValue(keySpEl, args);
    }

    private boolean isDel(String conditionSpE, Object retVal) {
        return elParser.getELRetVal(conditionSpE, null, retVal);
    }
}