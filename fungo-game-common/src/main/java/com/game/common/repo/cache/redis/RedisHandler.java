package com.game.common.repo.cache.redis;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.query.SortQuery;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:redis集群操作
 *
 * @Date: Create in 2019/3/12
 */
@Component
public class RedisHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(RedisHandler.class);

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;


    @Autowired
    private StringRedisTemplate stringRedisTemplate;



    /**
     * 设置value
     *
     * @param key
     * @param value
     */
    public void set(final String key, final String value) {

        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.set(
                        redisTemplate.getStringSerializer().serialize(key),
                        redisTemplate.getStringSerializer().serialize(value));
                return null;
            }
        });
    }


    /**
     * 设置value
     *
     * @param key
     * @param value
     */
    public void set(final String key, final Object value) {

        if (StringUtils.isBlank(key) || null == value) {
            return;
        }
        redisTemplate.opsForValue().set(key, value);

    }

    /**
     * 设置value
     *
     * @param key
     * @param value
     */
    public void setString(final String key, final String value) {

        if (StringUtils.isBlank(key) || null == value) {
            return;
        }
        stringRedisTemplate.opsForValue().set(key, value);

    }


    /**
     * 设置value
     *
     * @param key
     * @param value
     * @param seconds 有效期，单位：秒
     * @return
     */
    public void set(final String key, final String value, final Integer seconds) {

        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }

        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                connection.setEx(
                        redisTemplate.getStringSerializer().serialize(key),
                        seconds,
                        redisTemplate.getStringSerializer().serialize(value)
                );
                return null;
            }
        });
    }


    /**
     * 设置value
     *
     * @param key
     * @param value
     * @param seconds 有效期，单位：秒
     * @return
     */
    public void set(final String key, final Object value, final Integer seconds) {

        if (StringUtils.isBlank(key) || null == value) {
            return;
        }

        redisTemplate.opsForValue().set(key, value, seconds,TimeUnit.SECONDS);
    }

    /**
     * 获取数据
     * @param key
     * @return
     */
    public String get(final String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        }
        return redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection)
                    throws DataAccessException {
                byte[] byteKye = redisTemplate.getStringSerializer().serialize(
                        key);
                if (connection.exists(byteKye)) {
                    byte[] byteValue = connection.get(byteKye);
                    String value = redisTemplate.getStringSerializer()
                            .deserialize(byteValue);
                    return value;
                }
                return null;
            }
        });
    }


    /**
     * 获取对象数据
     * @param key
     * @return
     */
    public Object getEntity(final String key) {

        if (StringUtils.isBlank(key)) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }


    /**
     * 删除数据
     *
     * @param key
     */
    public void delete(final String key) {

        if (StringUtils.isBlank(key)) {
            return;
        }
        redisTemplate.execute(new RedisCallback<Object>() {
            public Object doInRedis(RedisConnection connection) {
                connection.del(redisTemplate.getStringSerializer().serialize(
                        key));
                return null;
            }
        });
    }


    /**
     * 批量删除数据
     *
     * @param keyPrefix
     */
    public void batchDelete(final String keyPrefix) {

        if (StringUtils.isBlank(keyPrefix)) {
            return;
        }
        redisTemplate.execute(new RedisCallback<String>() {
            public String doInRedis(RedisConnection connection) {
                redisTemplate.delete(redisTemplate.keys(keyPrefix + "*"));
                return null;
            }
        });
    }


    /**
     * 模糊查询keys
     *
     * @param keyPrefix
     * @return
     */
    public Set<Object> findKeys(String keyPrefix) {
        if (StringUtils.isBlank(keyPrefix)) {
            return null;
        }

        return redisTemplate.keys(keyPrefix + "*");

    }


    /**
     * 获取 redisTemplate
     * @return
     */
    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }

    /**
     * 事务外执行redis操作( 在连接池环境中使用同一connect )
     * @param sessionCallback
     * @param <T>
     * @return
     */
    public <T> T execute(SessionCallback<T> sessionCallback) {

        return (T) getRedisTemplate().execute(sessionCallback);
    }

    /**
     * 事务内执行redis操作
     * @param sessionCallback
     * @param <T>
     * @return
     */
    public <T> T executeInTransactional(final SessionCallback<T> sessionCallback) {

        SessionCallback<T> transactionalSessionCallback = new SessionCallback<T>() {

            @Override
            public <K, V> T execute(RedisOperations<K, V> operations) throws DataAccessException {
                operations.multi();
                T result = sessionCallback.execute(operations);
                operations.exec();
                return result;
            }
        };
        return execute(transactionalSessionCallback);
    }

    /**
     * 获取指定 key 的剩余到期时间
     * @param key
     * @return
     */
    public Long getExpire(Object key) {
        return getRedisTemplate().getExpire(key);
    }

    /**
     * 获取指定 key 的剩余到期时间
     * @param key
     * @param timeUnit  时间的单位
     * @return
     */
    public Long getExpire(Object key, TimeUnit timeUnit) {
        return getRedisTemplate().getExpire(key, timeUnit);
    }

    /**
     * 设置指定key 指定时间后期到期
     * @param key
     * @param timeout   多久时间后到期
     * @param unit      时间单位
     * @return
     */
    public boolean expire(Object key, long timeout, final TimeUnit unit) {
        return getRedisTemplate().expire(key, timeout, unit);
    }

    /**
     * 设置指定 key 在指定时间到期
     * @param key
     * @param date  指定的时间
     * @return
     */
    public boolean expireAt(Object key, java.util.Date date) {
        return getRedisTemplate().expireAt(key, date);
    }

    /**
     * 是否存在某个 key
     * @param key
     * @return
     */
    public boolean hasKey(Object key) {

        return getRedisTemplate().hasKey(key);
    }

    public boolean persist(Object key) {

        return getRedisTemplate().persist(key);
    }


    /**
     * 重命名指定的 key
     * @param oldKey 原始key
     * @param newKey 新key
     * @return
     */
    public boolean renameIfAbsent(Object oldKey, Object newKey) {
        if (oldKey == null || newKey == null) {
            return false;
        }
        if (!oldKey.getClass().equals(newKey.getClass())) {
            return false;
        }
        return getRedisTemplate().renameIfAbsent(oldKey, newKey);
    }

    /**
     * 重命名指定的 key
     * @param oldKey 原始key
     * @param newKey 新key
     * @return
     */
    public void rename(Object oldKey, Object newKey) {
        if (oldKey == null || newKey == null) {
            return;
        }
        if (!oldKey.getClass().equals(newKey.getClass())) {
            return;
        }
        getRedisTemplate().rename(oldKey, newKey);
    }

    /**
     * pubsub functionality on the template
     * @param channel
     * @param message
     */
    public void convertAndSend(String channel, Object message) {

        getRedisTemplate().convertAndSend(channel, message);
    }


    /**
     * 获取 指定表达式的 keys
     * @param pattern 表达式
     * @return
     */
    public Set keys(Object pattern) {

        return getRedisTemplate().keys(pattern);
    }

    /**
     * 删除指定 key
     * @param key
     */
    public void delete(Object key) {

        getRedisTemplate().delete(key);
    }

    /**
     * 删除指定集合的 key
     * @param key
     */
    public void delete(Collection key) {

        getRedisTemplate().delete(key);
    }

    /**
     * 生成随机 key
     * @return
     */
    public Object randomKey() {

        return getRedisTemplate().randomKey();
    }

    /**
     * 把指定可以 移动到指定的 db
     * @param key       指定的key
     * @param dbIndex   db的索引
     * @return
     */
    public Boolean move(Object key, int dbIndex) {
        return getRedisTemplate().move(key, dbIndex);
    }


    /**
     * 绑定一个 key 并对他进行hash类的操作(  操作 map )
     * @param key
     * @return
     */
    public <K> BoundHashOperations boundHashOps(K key) {
        return getRedisTemplate().boundHashOps(key);
    }

    /**
     * 绑定一个 key 并对他进行set类的操作(  操作 set )
     * @param key
     * @return
     */
    public BoundZSetOperations boundZSetOps(Object key) {
        return getRedisTemplate().boundZSetOps(key);
    }

    /**
     * 排序后的集合
     * @param query
     * @param bulkMapper
     * @param <T>
     * @return
     */
    public <T> List<T> sort(SortQuery query, BulkMapper bulkMapper) {
        return getRedisTemplate().sort(query, bulkMapper);
    }

    /**
     *  执行redis 操作
     * @param action 操作
     * @param exposeConnection 是否使用原生的redis方法
     * @return
     */
    public <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
        return (T) getRedisTemplate().execute(action, exposeConnection);
    }


    /**
     * 获取操作句柄
     * @return
     */
    public ValueOperations opsForValue() {
        return getRedisTemplate().opsForValue();
    }

    /**
     * 绑定指定 key 并获取其set操作的句柄
     * @param key
     * @return
     */
    public BoundSetOperations boundSetOps(Object key) {
        return getRedisTemplate().boundSetOps(key);
    }

    /**
     * 获取指定 key 的数据存储类型
     * @param key
     * @return
     */
    public DataType type(Object key) {
        return getRedisTemplate().type(key);
    }

    /**
     * 排序
     * @param query
     * @param storeKey
     * @return
     */
    public Long sort(SortQuery query, Object storeKey) {
        return getRedisTemplate().sort(query, storeKey);
    }

    /**
     * 执行 redis 操作
     * @param action
     * @return
     */
    public List executePipelined(RedisCallback action) {
        return getRedisTemplate().executePipelined(action);
    }

    /**
     * 获取 hash 操作的句柄
     * @return
     */
    public HashOperations opsForHash() {
        return getRedisTemplate().opsForHash();
    }

    /**
     * 排序后的集合
     * @param query
     * @return
     */
    public List sort(SortQuery query) {
        return getRedisTemplate().sort(query);
    }

    /**
     * zset 操作的句柄
     * @return
     */
    public ZSetOperations opsForZSet() {
        return getRedisTemplate().opsForZSet();
    }

    /**
     * 获取 set 操作的句柄
     * @return
     */
    public SetOperations opsForSet() {
        return getRedisTemplate().opsForSet();
    }

    /**
     * 获取 list 操作
     * @return
     */
    public ListOperations opsForList() {
        return getRedisTemplate().opsForList();
    }

    /**
     * 获取指定 key 的操作句柄
     * @param key
     * @return
     */
    public <K> BoundValueOperations boundValueOps(K key) {
        return getRedisTemplate().boundValueOps(key);
    }

    /**
     * 获取指定 key 的list操作句柄
     * @param key
     * @return
     */
    public <K> BoundListOperations boundListOps(K key) {
        return getRedisTemplate().boundListOps(key);
    }

    /**
     * 执行 redis 操作
     * @param session
     * @return
     */
    public List<Object> executePipelined(SessionCallback<?> session) {
        return getRedisTemplate().executePipelined(session);
    }


}
