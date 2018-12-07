package com.mushi.raincache;

import com.mushi.raincache.config.CacheConfig;
import com.mushi.raincache.redis.ShardedJedisCache;
import com.mushi.raincache.to.CacheKey;
import com.mushi.raincache.to.CacheWrapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RainCacheApplicationTests {

    @Test
    public void contextLoads() {
    }

    private ShardedJedisCache cache;

    private CacheKey cacheKey1, cacheKey2;

    private CacheWrapper wrapper1, wrapper2;

    private int               expTime = 3;

    @Before
    public void init() {

        CacheConfig config = new CacheConfig();

        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
        JedisShardInfo info1 = new JedisShardInfo("127.0.0.1", 6379, "redis-machine-1");
        JedisShardInfo info2 = new JedisShardInfo("127.0.0.1", 6379, "redis-machine-2");
        shards.add(info1);
        shards.add(info2);

        ShardedJedis shardedJedis = new ShardedJedis(shards);
        cache = new ShardedJedisCache(shardedJedis, config);

        //初始化数据
        Employee emp1 = new Employee(1L, "卢俊义");
        Employee emp2 = new Employee(2L, "李逵");

        cacheKey1 = new CacheKey(emp1.getId() + "");
        wrapper1 = new CacheWrapper(emp1);
        cacheKey2 = new CacheKey(emp2.getId() + "");
        wrapper2 = new CacheWrapper(emp2, expTime);//expire

        cache.set(cacheKey1, wrapper1);
        cache.set(cacheKey2, wrapper2);
    }

    @Test
    public void testCache() throws Exception {
        Assert.assertNotNull(cache);

        //测试get
        CacheWrapper cacheWrapper1 = cache.get(cacheKey1);
        CacheWrapper cacheWrapper2 = cache.get(cacheKey2);
        Assert.assertTrue(wrapper1.getCreateTime() == cacheWrapper1.getCreateTime());
        Assert.assertTrue(wrapper1.getExpireTime() == cacheWrapper1.getExpireTime());
        Assert.assertTrue(((Employee) wrapper1.getObj()).getId() == ((Employee) cacheWrapper1.getObj()).getId());

        Assert.assertTrue(wrapper2.getCreateTime() == cacheWrapper2.getCreateTime());
        Assert.assertTrue(wrapper2.getExpireTime() == cacheWrapper2.getExpireTime());
        Assert.assertTrue(((Employee) wrapper2.getObj()).getId() == ((Employee) cacheWrapper2.getObj()).getId());

        //测试expire
        Thread.sleep(expTime * 1000 + 100);

        cacheWrapper2 = cache.get(cacheKey2);
        Assert.assertNull(cacheWrapper2);

        //测试del
        Long delRet = cache.delete(cacheKey1);
        Assert.assertTrue(delRet == 1L);
        cacheWrapper1 = cache.get(cacheKey1);
        Assert.assertNull(cacheWrapper1);

    }

    @Test
    public void testClear() {
        CacheWrapper cacheWrapper1 = cache.get(cacheKey1);
        Assert.assertNotNull(cacheWrapper1);

        cache.clear();

        cacheWrapper1 = cache.get(cacheKey1);
        Assert.assertNull(cacheWrapper1);
    }



}
