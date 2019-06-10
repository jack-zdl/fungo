package com.game.common.repo.cache.facade;

import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.game.common.repo.cache.redis.RedisHandler;
import com.game.common.util.SecurityMD5;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:fungo 广告模块缓存
 *   缓存key规则：
 *       1.与用户无关
 *             接口_入参
 *       2.与用户有关
 *           接口+用户id_入参
 * @Date: Create in 2019/3/12
 */
@Component
public class FungoCacheAdvert {

    private final Logger LOGGER = LoggerFactory.getLogger(FungoCacheAdvert.class);


    /**
     * Redis 默认缓存时间: 秒
     */
    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 10;

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
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_" ;
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        if (isCache) {
            redisHandler.set(redisKey, value,REDIS_EXPIRE_DEFAULT);
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
}
