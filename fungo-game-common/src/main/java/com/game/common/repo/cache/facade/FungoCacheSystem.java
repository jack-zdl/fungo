package com.game.common.repo.cache.facade;


import com.game.common.repo.cache.redis.RedisHandler;
import com.game.common.util.SecurityMD5;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:fungo 系统数据缓存，如游戏工具
 *   缓存key规则：
 *
 * @Date: Create in 2019/3/12
 */
@Component
public class FungoCacheSystem {

    private final Logger LOGGER = LoggerFactory.getLogger(FungoCacheSystem.class);


    /**
     * Redis 默认缓存时间: 秒
     * 30 分钟
     */
//    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 30;
    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 2;

    @Autowired
    private RedisHandler redisHandler;


    /**
     * 添加缓存和删除
     * @param isCache true 缓存，false清除缓存
     * @param keyPrefix 缓存key的前缀（对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀（对象是json字符串，非对象是字符串）
     * @param value
     */
    public void excIndexCache(boolean isCache, String keyPrefix, String keySuffix, Object value) {

        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_";
        if (StringUtils.isNotBlank(keySuffix)) {
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
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_";
        if (StringUtils.isNotBlank(keySuffix)) {
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
    public Object getIndexCache(String keyPrefix, String keySuffix) {
        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_";
        if (StringUtils.isNotBlank(keySuffix)) {
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        return redisHandler.getEntity(redisKey);
    }

}
