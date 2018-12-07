package com.mushi.raincache.to;

/**
 * 〈对缓存对象的封装类〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class CacheWrapper extends BaseTO {

    private static final long serialVersionUID = -2513394423692386824L;

    /**
     * 缓存的对象
     */
    private Object obj;

    /**
     * 过期时间
     */
    private Integer expireTime = 0;

    /**
     * 创建时间
     */
    private long createTime;


    /**
     * 最后一次访问的时间
     */
    private long lastAccessTime;


    public CacheWrapper(Object obj) {
        this(obj, 0);
    }


    public CacheWrapper(Object obj, int expireTime) {
        this.obj = obj;
        this.expireTime = expireTime;
        this.createTime = System.currentTimeMillis();
        this.lastAccessTime = System.currentTimeMillis();
    }


    /**
     * 判断该缓存是否已过期
     */
    public boolean isExpire() {
        if (expireTime > 0) {
            return createTime + expireTime * 1000 < System.currentTimeMillis();
        }
        return false;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public Integer getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Integer expireTime) {
        this.expireTime = expireTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(long lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }
}