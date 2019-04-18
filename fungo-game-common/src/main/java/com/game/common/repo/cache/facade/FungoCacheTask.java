package com.game.common.repo.cache.facade;

import com.game.common.repo.cache.redis.RedisHandler;
import com.fungo.tools.SecurityMD5;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:fungo 用户任务模块缓存
 *   缓存key规则：
 *       1.与用户无关
 *             接口_入参
 *       2.与用户有关
 *           接口+用户id_入参
 * @Date: Create in 2019/3/12
 */
@Component
public class FungoCacheTask {

    private final Logger logger = LoggerFactory.getLogger(FungoCacheTask.class);


    /**
     * Redis 默认缓存时间: 秒
     */
    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 10;

    /**
     * Redis 缓存时间: 秒
     * 缓存12小时
     */
    public static final Integer REDIS_EXPIRE_12_HOUR = 60 * 60 * 12;

    /**
     * Redis 缓存时间: 秒
     * 缓存7天
     */
    public static final Integer REDIS_EXPIRE_7_DAYS = 60 * 60 * 24 * 7;

    /**
     * Redis 缓存时间: 秒
     * 缓存时间：30天
     */
    public static final Integer REDIS_EXPIRE_24_DAYS = 60 * 60 * 24 * 30;


    /**
     * Redis 缓存时间: 秒
     * 签到任务组和签到规则数据缓存时间：3个月
     */
    public static final Integer REDIS_EXPIRE_3_MONTHS = 60 * 60 * 24 * 30 * 3;

    /**
     * 用户任务-用户签到数据缓存key
     */
    public static String TASK_CACHE_KEY_MEMBER_CHECKIN = "MemberCheckIn";


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
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_";
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
     * @param value
     */
    public void excIndexCache(boolean isCache, String keyPrefix, String keySuffix, Object value, Integer expire) {

        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_";
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
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_";
        if (StringUtils.isNotBlank(keySuffix)) {
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        return redisHandler.getEntity(redisKey);
    }
}
