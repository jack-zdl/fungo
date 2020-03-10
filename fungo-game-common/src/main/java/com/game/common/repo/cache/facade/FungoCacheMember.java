package com.game.common.repo.cache.facade;


import com.game.common.repo.cache.redis.RedisHandler;
import com.game.common.util.SecurityMD5;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:fungo 用户模块缓存
 *   缓存key规则：
 *       1.与用户无关
 *             接口_入参
 *       2.与用户有关
 *           接口+用户id_入参
 * @Date: Create in 2019/3/12
 */
@Component
public class FungoCacheMember {

    private final Logger LOGGER = LoggerFactory.getLogger(FungoCacheMember.class);

    /**
     * Redis 默认缓存时间: 秒
     */
//    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 3;
    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 2;


    @Autowired
    private RedisHandler redisHandler;


    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 添加缓存和删除
     * @param isCache true 缓存，false清除缓存
     * @param keyPrefix 缓存key的前缀（对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀（对象是json字符串，非对象是字符串）
     * @param value
     */
    public void excIndexCache(boolean isCache, String keyPrefix, String keySuffix, Object value) {

        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_" ;
        if (StringUtils.isNotBlank(keySuffix)) {
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }

        if (isCache) {
            redisHandler.set(redisKey, value,REDIS_EXPIRE_DEFAULT);
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
    public void excIndexCache(boolean isCache, String keyPrefix, String keySuffix, Object value,Integer expire) {

        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_";
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        if (isCache) {
            redisHandler.set(redisKey, value,expire);
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
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        return redisHandler.getEntity(redisKey);
    }


    /**
     *  获取缓存数据
     * @param keyPrefix 缓存key的前缀 （对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀 （对象是json字符串，非对象是字符串）
     * @return
     */
    public void setRedisSetCache(String keyPrefix, String... keySuffix) {
        redisTemplate.opsForSet().add(keyPrefix,keySuffix);
        //设置过期时间

    }

    public Set<String> getRedisSet(String set1, List<String> set2){
        long result=  redisTemplate.opsForSet().intersectAndStore(set1, set2,set1+"+join");
        return redisTemplate.opsForSet().members(set1+"+join");
    }

    public void setExpireTime(String keyPrefix){
        redisTemplate.expire(keyPrefix,5000 , TimeUnit.HOURS);
    }

    //-----------
}
