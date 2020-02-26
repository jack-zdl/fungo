package com.game.common.util;

import com.game.common.framework.runtime.SpringUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import java.util.List;

/**
 * <p>
 *     ehcache模糊删除操作
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public class FunGoEHCacheUtils {

    private static CacheManager cacheManager = (CacheManager) SpringUtils.getObject(CacheManager.class);

    public static Object get(String cacheName, String key) {
        Element element = getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }

    public static void put(String cacheName, String key, Object value) {
        Element element = new Element(key, value);
        getCache(cacheName).put(element);
    }

    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    public static void removeAll(String cacheName) {
        getCache(cacheName).removeAll();
    }

    public static List cacheKeys(String cacheName){
        return getCache(cacheName).getKeys();
    }

    /**
     * 获得一个Cache，没有则创建一个。
     * @param cacheName
     * @return
     */
    private static Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }


}
