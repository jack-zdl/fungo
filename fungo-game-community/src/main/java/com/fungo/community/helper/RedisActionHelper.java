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

import static freemarker.template.utility.Collections12.singletonList;

//import static freemarker.template.utility.Collections12.singletonList;

/**
 * <p>redis相关辅助操作</p>
 * @Date: 2019/11/28
 */
@Component
public class RedisActionHelper {

    @Autowired
    private FungoCacheArticle fungoCacheArticle;
//
//    @Autowired
//    private RedisTemplate<Object, Object> redisTemplate;
//
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

//    @Autowired
//    RedisScript<Boolean> script;

//    public boolean checkAndSet(String expectedValue, String newValue) {
//        return stringRedisTemplate.execute(script, singletonList("key"), expectedValue, newValue);
//    }

    /**
     * 功能描述: 清除文章相关的redis 缓存
     * @date: 2019/11/28 13:21
     */
    public void removePostRedisCache(List<String> keyPrefixs){

//        RedisFuture<String> future = redisAsyncCommands.get( "2019112718360917911" );
//        future.thenAccept( new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                System.out.println("异步删除"+s);
//            }
//        } );
//        stringRedisTemplate.executePipelined( new RedisCallback<Object>() {
//            public Object doInRedis(RedisConnection connection) throws DataAccessException {
//                StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
//                for(int i=0; i< keyPrefixs.size(); i++) {
//                    System.out.println(keyPrefixs.get(i)+"_join_" );
////                    stringRedisConn.del(keyPrefixs.get(i)+"*");
//                    redisTemplate.delete(redisTemplate.keys(keyPrefixs.get(i) + "*"));
////                    stringRedisTemplate.delete(keyPrefixs.get(i)+"*" );
//                }
//                return null;
//            }
//        });
        Long start2 = System.currentTimeMillis();
        for(int i=0; i< 1000; i++) {
//            redisTemplate.delete(redisTemplate.keys(keyPrefixs.get(i) + "*"));
            redisTemplate.opsForValue().set("key2"+i, "value2"+i);
        }
        Long end2  = System.currentTimeMillis();
        System.out.println("同步方法=="+(end2-start2));
        Long star1 = System.currentTimeMillis();
        stringRedisTemplate.executePipelined( new SessionCallback<Object>() {

            @Override
            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
                for(int i=0; i< 1000; i++) {
//                    redisTemplate.delete(redisTemplate.keys(keyPrefixs.get(i) + "*"));
                    redisTemplate.opsForValue().set("key1"+i, "value1"+i);
//                    stringRedisTemplate.delete(keyPrefixs.get(i)+"*" );
                }
                return null;
            }
        });
        Long end1 = System.currentTimeMillis();
        System.out.println("22222222222222222="+(end1 - star1));


        System.out.println("同步删除");

//        redisTemplate.delete(redisTemplate.keys(keyPrefix + "*"));
//        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_ALL_POST_TOPIC,"", null);

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

    public static void main(String[] args) {

//        RedisActionHelper redisActionHelper = new RedisActionHelper();
//        redisActionHelper.removePostRedisCache();
//        System.out.println(RedisActionHelper.getRandomRedisCacheTime());
    }
}
