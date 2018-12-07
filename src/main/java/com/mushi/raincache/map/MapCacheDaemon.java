package com.mushi.raincache.map;

import com.mushi.raincache.config.CacheConfig;
import com.mushi.raincache.serializer.HessianSerializer;
import com.mushi.raincache.serializer.Serialzer;
import com.mushi.raincache.to.CacheWrapper;
import com.mushi.raincache.util.LogUtils;
import com.mushi.raincache.util.OSUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈MapCache的守护线程，负责持久化〉
 *
 * @author mushi
 * @create 2018/12/7
 * @since 1.0.0
 */
public class MapCacheDaemon implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MapCacheDaemon.class);

    private final CacheConfig cacheConfig;

    private final MapCache mapCache;

    private final Serialzer<Object> serialzer;


    private boolean isRun = true;

    private final String fileName = "map.cache";


    public MapCacheDaemon(MapCache mapCache, CacheConfig cacheConfig) {
        LogUtils.info(logger, "MapCacheDaemon init...");
        this.mapCache = mapCache;
        this.cacheConfig = cacheConfig;
        this.serialzer = new HessianSerializer<Object>();
        LogUtils.info(logger, "MapCacheDaemon init success!");
    }


    @Override
    public void run() {
        while (isRun) {
            // 清理过期缓存
            clearCache();
            try {
                Thread.sleep(cacheConfig.getTimeBetweenPersist() * 1000);
            } catch (InterruptedException e) {
                logger.error("失败", e);
            }
            // 持久化缓存
            persistCache();
        }
    }

    /**
     * 清理过期缓存
     */
    public void clearCache() {
        LogUtils.info(logger, "开始清理缓存。。。");
        int cnt = 0;
        ConcurrentHashMap<String, Object> cache = mapCache.getCache();
        Iterator<Map.Entry<String, Object>> iterator = cache.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            Object obj = entry.getValue();
            if (obj instanceof CacheWrapper) {
                CacheWrapper cacheWrapper = (CacheWrapper) obj;
                if (cacheWrapper.isExpire()) {
                    iterator.remove();
                    ++cnt;
                }
            }
        }
        LogUtils.info(logger, "过期缓存清理完毕，清理数量：{0}", cnt);

    }


    /**
     * 持久化缓存
     */
    public void persistCache() {
        if (!cacheConfig.isPersist()) {
            return;
        }
        LogUtils.info(logger, "开始持久化缓存");
        File cacheFile = createCacheFile();
        OutputStream out = null;
        BufferedOutputStream bos = null;
        try {
            out = new FileOutputStream(cacheFile);
            bos = new BufferedOutputStream(out);
            bos.write(serialzer.serialze(mapCache.getCache()));

        } catch (Exception e) {
            logger.error("持久化缓存失败", e);
        } finally {
            try {
                bos.close();
                out.close();

            } catch (IOException e) {
                logger.error("关闭流失败", e);

            }
        }
        LogUtils.info(logger, "持久化缓存结束");
    }


    /**
     * 读取文件的缓存
     */
    public void readCacheFromDisk() {
        LogUtils.info(logger, "开始从磁盘中读取缓存的文件");
        String filePath = getSavePath() + fileName;
        File file = new File(filePath);
        if (file.exists() && file.length() > 0) {
            InputStream in = null;
            BufferedInputStream bis = null;
            ByteArrayOutputStream baos = null;
            try {
                in = new FileInputStream(file);
                bis = new BufferedInputStream(in);
                baos = new ByteArrayOutputStream();

                byte[] bytes = new byte[1024];
                while (bis.read(bytes) != -1) {
                    baos.write(bytes);
                }

                Object obj = serialzer.deserialize(baos.toByteArray());
                if (obj instanceof ConcurrentHashMap) {
                    ConcurrentHashMap<String, Object> map = (ConcurrentHashMap<String, Object>) obj;
                    mapCache.getCache().putAll(map);
                    LogUtils.info(logger, "缓存读取了{0}个对象："+ map.size());
                }
            } catch (Exception e) {
                logger.error("读取文件失败！", e);
            } finally {
                try {
                    bis.close();
                    in.close();
                    baos.close();
                } catch (IOException e) {
                    logger.error("关闭失败！", e);
                }
            }
        }
        LogUtils.info(logger, "缓存读取完毕！");

    }


    /**
     * 创建缓存的文件
     *
     * @return
     */
    public File createCacheFile() {
        String savePath = getSavePath();
        File file = new File(savePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = savePath + fileName;
        File folder = new File(filePath);
        if (!folder.exists()) {
            try {
                folder.createNewFile();
            } catch (IOException e) {
                logger.error("创建文件失败", e);
            }
        }
        return folder;
    }


    /**
     * 获取缓存存储路径
     *
     * @return
     */
    public String getSavePath() {
        String persistecePath = cacheConfig.getPersistecePath();
        String savePath = "";
        if (OSUtil.isLinux()) {
            savePath = persistecePath + File.separatorChar;
        } else if (OSUtil.isWindows()) {
            savePath = "F:" + persistecePath + File.separatorChar;
        }

        return savePath;
    }


    public boolean isRun() {
        return isRun;
    }

    public void setRun(boolean run) {
        isRun = run;
    }
}