package com.game.common.repo.cache.facade;


import com.game.common.repo.cache.redis.RedisHandler;
import com.game.common.util.SecurityMD5;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.StringRedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:fungo 用户文章模块缓存
 *   缓存key规则：
 *       1.与用户无关
 *             接口_入参
 *       2.与用户有关
 *           接口+用户id_入参
 * @Date: Create in 2019/3/12
 */
@Component
public class FungoCacheArticle {

    private final Logger LOGGER = LoggerFactory.getLogger(FungoCacheArticle.class);


    /**
     * 帖子(文章)详情Redis key
     */
    public static final String FUNGO_CORE_API_POST_CONTENT_DETAIL_WATCHNUM = "cmmPostWatchNum_cloud";



    /**
     * Redis 默认缓存时间: 秒
     */
    public static final Integer REDIS_EXPIRE_DEFAULT = 60 * 5;


    @Autowired
    private RedisHandler redisHandler;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


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
     *  获取缓存字符串数据
     * @param keyPrefix 缓存key的前缀 （对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀 （对象是json字符串，非对象是字符串）
     * @return
     */
    public String getIndexCacheWithStr(String keyPrefix, String keySuffix) {
        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_" ;
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        return redisHandler.get(redisKey);
    }

    public void setStringCache( String keyPrefix, String keySuffix,String value){
        //从redis获取
        String redisKey = SecurityMD5.encrypt16(keyPrefix) + "_join_" ;
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        redisHandler.setString(redisKey,value);
    }


    /**
     * 添加缓存和删除缓存,<b>此缓存是不加密的</b>
     * <p>前缀不加密,方便后续删除一系列的redis的key</p>
     * @param isCache true 缓存，false清除缓存
     * @param keyPrefix 缓存key的前缀（对象是json字符串，非对象是字符串）
     * @param keySuffix 缓存key的后缀（对象是json字符串，非对象是字符串）
     * @param expire 缓存过期时间
     * @param value
     */
    public void excIndexDecodeCache(boolean isCache, String keyPrefix, String keySuffix, Object value,Integer expire) {

        //从redis获取
        String redisKey = keyPrefix + "_join_";
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
    public Object getIndexDecodeCache(String keyPrefix, String keySuffix) {
        //从redis获取
        String redisKey = keyPrefix + "_join_";
        if (StringUtils.isNotBlank(keySuffix)){
            redisKey += SecurityMD5.encrypt16(keySuffix);
        }
        return redisHandler.getEntity(redisKey);
    }


    public void removePostRedisCache(String keyPrefix){

//        RedisFuture<String> future = redisAsyncCommands.get( "2019112718360917911" );
//        future.thenAccept( new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                System.out.println("异步删除"+s);
//            }
//        } );
        stringRedisTemplate.executePipelined( new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisConnection stringRedisConn = (StringRedisConnection)connection;
                for(int i=0; i< 10; i++) {
                    stringRedisConn.del(keyPrefix);
                    System.out.println(stringRedisConn.rPop("myqueue"));
                }
                return null;
            }
        });
        System.out.println("同步删除");
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


    //-----------
}
