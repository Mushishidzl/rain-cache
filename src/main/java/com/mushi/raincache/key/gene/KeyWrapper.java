package com.mushi.raincache.key.gene;

import com.mushi.raincache.to.BaseTO;

/**
 * 〈key封装类〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class KeyWrapper extends BaseTO {

    private static final long serialVersionUID = -2010928285681837563L;


    /**
     * 目标雷鸣
     */
    private String targetName;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private Object params[];


    public KeyWrapper() {
    }


    public KeyWrapper(String targetName, String methodName, Object[] params) {
        this.targetName = targetName;
        this.methodName = methodName;
        this.params = params;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }
}