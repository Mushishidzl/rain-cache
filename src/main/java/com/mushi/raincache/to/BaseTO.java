package com.mushi.raincache.to;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 〈基础TO〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 *
 */
public class BaseTO implements Serializable {

    private static final long serialVersionUID = -5822688508979824409L;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}