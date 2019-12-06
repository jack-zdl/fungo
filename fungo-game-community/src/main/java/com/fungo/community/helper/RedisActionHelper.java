package com.fungo.community.helper;
//
//import com.game.common.consts.FungoCoreApiConstant;
//import com.game.common.repo.cache.facade.FungoCacheArticle;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.connection.StringRedisConnection;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.core.script.RedisScript;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.util.SecurityMD5;
import com.game.common.util.SpringUtil;
import io.lettuce.core.RedisFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static freemarker.template.utility.Collections12.singletonList;

//import static freemarker.template.utility.Collections12.singletonList;

/**
 * <p>redis相关辅助操作</p>
 * @Date: 2019/11/28
 */
@Component
public class RedisActionHelper {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 功能描述: redis的pipeLine管道清除redis缓存
     * @date: 2019/11/28 13:21
     */
    public void removePostRedisCache(List<String> keyPrefixs){
        redisTemplate.executePipelined( new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (String redisKey : keyPrefixs){
                    redisTemplate.delete(redisTemplate.keys("key_" + "*"));
                }
                return null;
            }
        });

    }


    /**
     * 功能描述: 随机一个240-360数字,设置成缓存时间，防止缓存击穿
     * @date: 2019/11/28 13:50
     */
    public static int getRandomRedisCacheTime(){
        Random random = new Random();
        int cacheTime = random.nextInt(120)+240;
        return cacheTime;
    }


}
