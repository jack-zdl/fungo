package com.game.common.util;

import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;

public class UniqueIdCkeckUtil {

    /**
     * 任务唯一标识所存储的在的set集合key名称
     */
    public static  final String REDIS_SET_TASK_REQUEST_ID = "redis_set_task_request_id";

    /**
     *  判断请求标识目前是否已经存在
     * @param setName redis的set集合的key
     * @param uniqueId 保证唯一的请求标识
     * @return true 已存在   false 不存在
     */
    public static boolean checkUniqueIdExist(String setName,String uniqueId){
        StringRedisTemplate stringRedisTemplate = SpringUtil.getBean(StringRedisTemplate.class);
        SetOperations<String, String> opsForSet = stringRedisTemplate.opsForSet();
        return opsForSet.isMember(setName,uniqueId);
    }


}
