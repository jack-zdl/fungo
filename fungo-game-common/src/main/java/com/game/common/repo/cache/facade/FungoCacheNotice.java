package com.game.common.repo.cache.facade;


import com.game.common.repo.cache.redis.RedisHandler;
import com.game.common.util.SecurityMD5;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:fungo 用户通知缓存
 *   缓存key规则：
 *       "memberNotice" + mb_id +  消息类型 + 消息id
 * @Date: Create in 2019/3/12
 */
@Component
public class FungoCacheNotice {

    private final Logger LOGGER = LoggerFactory.getLogger(FungoCacheNotice.class);


    /**
     * Redis 默认缓存时间: 秒
     * 3周
     */
    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 60 * 24 * 7 * 3;

    @Autowired
    private RedisHandler redisHandler;


    /**
     * Redis 缓存用户详情数据 前缀
     */
    public static final String REDIS_MEMBER_NOTICE = "memberNotice";


    /**
     * 添加缓存和删除
     * @param isCache true 缓存，false清除缓存
     * @param keyPrefix 缓存key的前缀（对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀（对象是json字符串，非对象是字符串）
     * @param value
     */
    public void excIndexCache(boolean isCache, String keyPrefix, String keySuffix, Object value) {

        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_";
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        if (isCache) {
            redisHandler.set(redisKey, value, REDIS_EXPIRE_DEFAULT);
        } else {
            redisHandler.batchDelete(redisKey);
        }
    }


    /**
     * 添加缓存和删除
     * @param isCache true 缓存，false清除缓存
     * @param keyPrefix 缓存key的前缀（对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀（对象是json字符串，非对象是字符串）
     * @param expire 缓存过期时间
     * @param value
     */
    public void excIndexCache(boolean isCache, String keyPrefix, String keySuffix, Object value, Integer expire) {

        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_";
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        if (isCache) {
            redisHandler.set(redisKey, value, expire);
        } else {
            redisHandler.batchDelete(redisKey);
        }
    }


    /**
     *  获取缓存数据
     * @param keyPrefix 缓存key的前缀 （对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀 （对象是json字符串，非对象是字符串）
     * @return
     */
    public String getIndexCache(String keyPrefix, String keySuffix) {
        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_";
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        return redisHandler.get(redisKey);
    }

    /**
     *  获取缓存数据
     * @param key 缓存key
     * @return
     */
    public Object getIndexCache(String key) {
        //从redis获取
        return redisHandler.getEntity(key);
    }





    /**
     * 添加缓存和删除
     * @param isCache true 缓存，false清除缓存
     * @param keyPrefix 缓存key的前缀（对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀（对象是json字符串，非对象是字符串）
     * @param value
     */
    public void excIndexCacheWithOutSecurity(boolean isCache, String keyPrefix, String keySuffix, Object value) {

        //从redis获取
        String redisKey =  keyPrefix + "_";
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += keySuffix;
        }
        if (isCache) {
            redisHandler.set(redisKey, value, REDIS_EXPIRE_DEFAULT);
        } else {
            redisHandler.batchDelete(redisKey);
        }
    }


    /**
     * 添加缓存和删除
     * @param isCache true 缓存，false清除缓存
     * @param keyPrefix 缓存key的前缀（对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀（对象是json字符串，非对象是字符串）
     * @param expire 缓存过期时间
     * @param value
     */
    public void excIndexCacheWithOutSecurity(boolean isCache, String keyPrefix, String keySuffix, Object value, Integer expire) {

        //从redis获取
        String redisKey =  keyPrefix + "_";
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += keySuffix;
        }
        if (isCache) {
            redisHandler.set(redisKey, value, expire);
        } else {
            redisHandler.batchDelete(redisKey);
        }
    }



    /**
     *  获取缓存数据的keys
     * @param keyPrefix 缓存key的前缀 （对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀 （对象是json字符串，非对象是字符串）
     * @return
     */
    public Set<Object> findKeys(String keyPrefix, String keySuffix) {
        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_";
        if (StringUtils.isNotBlank(keySuffix)) {
            redisKey = redisKey + keySuffix;
        }
        return redisHandler.findKeys(redisKey);
    }


    /**
     *  获取缓存数据的keys
     * @param keyPrefix 缓存key的前缀 （对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀 （对象是json字符串，非对象是字符串）
     * @return
     */
    public Set<Object> findKeysWithOutSecurity(String keyPrefix, String keySuffix) {
        //从redis获取
        String redisKey = keyPrefix + "_";
        if (StringUtils.isNotBlank(keySuffix)) {
            redisKey = redisKey + keySuffix;
        }
        return redisHandler.findKeys(redisKey);
    }


}
