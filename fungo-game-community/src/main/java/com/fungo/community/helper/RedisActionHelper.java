package com.fungo.community.helper;

import com.fungo.community.function.cache.EhcacheActionFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;
import java.util.*;


/**
 * <p>redis相关辅助操作</p>
 * @Date: 2019/11/28
 */
@Component
public class RedisActionHelper {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    @Autowired
    private EhcacheActionFunction ehcacheActionFunction;

    /**
     * 功能描述: redis的pipeLine管道清除redis缓存
     * @date: 2019/11/28 13:21
     */
    public void removePostRedisCache(List<String> keyPrefixs){
        redisTemplate.executePipelined( new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                for (String redisKey : keyPrefixs){
                    redisTemplate.delete(redisTemplate.keys(redisKey + "*"));
                }
                return null;
            }
        });

    }

    public void removeEhcacheKey(String ehcacheKey){
        try {
            Map<String,String> ehcacheMap = new HashMap<>(2);
            if("ALL".equals( ehcacheKey )){
                ehcacheMap.put( "FG_eh_post","");
                ehcacheMap.put( "FG_eh_community","");
            }else if("CIRCLE".equals( ehcacheKey )){
                ehcacheMap.put( "FG_eh_community","");
            }else if("POST".equals( ehcacheKey )){
                ehcacheMap.put( "FG_eh_post","");
            }
            ehcacheActionFunction.deletePostEhcacheHandler(ehcacheMap);
        }catch (Exception e){

        }
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
