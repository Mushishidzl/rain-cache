package com.mushi.raincache.util;

/**
 * 〈操作类型的判断工具〉
 *
 * @author mushi
 * @create 2018/12/6
 * @since 1.0.0
 */
public class OSUtil {


    private static final String os = System.getProperty("os.name").toLowerCase();

    public static boolean isLinux() {
        return os.indexOf("linux") >= 0;
    }

    public static boolean isWindows() {
        return os.indexOf("windows") >= 0;
    }



}