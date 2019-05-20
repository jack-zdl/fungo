package com.game.common.util;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;


public class UniqueIdCkeckUtil {

    /**
     * 唯一id存储在redis中的过期时间  默认
     */
    private static final long EXPIRE_DAY = 3;


    /**
     *  判断请求标识目前是否已经存在,不存在保存
     * @param uniqueId 保证唯一的请求标识
     * @return true 当前redis不存在标识，存入成功   false 已存在标识，存入失败
     */
    public static boolean checkUniqueIdAndSave(String uniqueId){
        if(StringUtil.isNull(uniqueId)){
            return false;
        }
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        //校验任务是否已经执行
        ValueOperations<String, String> forValue = redisTemplate.opsForValue();
        return forValue.setIfAbsent(uniqueId, System.currentTimeMillis()+"", EXPIRE_DAY, TimeUnit.DAYS);
    }


    public static void deleteUniqueId(String uniqueId){
        StringRedisTemplate redisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        redisTemplate.delete(uniqueId);
    }

}
