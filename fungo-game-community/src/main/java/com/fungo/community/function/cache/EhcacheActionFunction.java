package com.fungo.community.function.cache;

import com.game.common.consts.FunGoGameConsts;
import com.game.common.util.CommonUtil;
import com.game.common.util.FunGoEHCacheUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.regex.Pattern;

/**
 * <p>清除Ehcache缓存</p>
 * @Date: 2019/12/5
 */
@Component
public class EhcacheActionFunction {

    private static final Logger logger = LoggerFactory.getLogger( EhcacheActionFunction.class);

    @Autowired
    private CacheManager cacheManager;
    // String ehcacheName, String ehcacheKey
    public boolean deletePostEhcacheHandler(Map<String,String> ehcacheMap){
        try {
            for(Map.Entry<String, String> ehcache : ehcacheMap.entrySet()){
                String ehcacheName = ehcache.getKey();
                String ehcacheKey = ehcache.getValue();
                if(cacheManager == null){
                    System.out.println("---------------");
                }
                List cacheKeys = cacheKeys(ehcacheName);
                if(CommonUtil.isNull(ehcacheKey)){
                        removeAll(ehcacheName);
                }else {
                    Pattern pattern = Pattern.compile(ehcacheKey);
                    for (Object cacheKey : cacheKeys) {
                        String cacheKeyStr = String.valueOf(cacheKey);
                        if (pattern.matcher(cacheKeyStr).find()) {
                            remove(ehcacheName, cacheKeyStr);
                        }
                    }
                }
            }
        }catch (Exception e){
            logger.error( "EhcacheActionFunction.deletePostEhcacheHandler异常",e);
        }
        return true;
    }


    public  List cacheKeys(String cacheName){
        return getCache(cacheName).getKeys();
    }

    public void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    public void removeAll(String cacheName) {
        getCache(cacheName).removeAll();
    }

    /**
     * 获得一个Cache，没有则创建一个。
     * @param cacheName
     * @return
     */
    private Cache getCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }
}
