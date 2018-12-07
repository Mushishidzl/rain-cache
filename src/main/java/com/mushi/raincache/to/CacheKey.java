package com.mushi.raincache.to;

import org.apache.commons.lang3.StringUtils;

/**
 * 〈缓存键〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class CacheKey extends BaseTO {

    private static final long serialVersionUID = 8415559268283507924L;


    private String nameSpace;

    private String key;

    public CacheKey(String key) {
        this(null, key);
    }

    public CacheKey(String nameSpace, String key) {
        this.nameSpace = nameSpace;
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isNotBlank(nameSpace)) {
            sb.append(nameSpace).append(".");
        }
        sb.append(key);
        this.key = sb.toString();
    }


    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}