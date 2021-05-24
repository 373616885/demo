package com.redis.mq.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 **/
@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * spring boot 1.5版本 redisTemplate 扩展
     */
    public Boolean setIfAbsent15(final String key, final Serializable value, final long exptime) {
        return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer valueSerializer = stringRedisTemplate.getValueSerializer();
                RedisSerializer keySerializer = stringRedisTemplate.getKeySerializer();
                Object obj = connection.execute("set", keySerializer.serialize(key),
                        valueSerializer.serialize(value),
                        "NX".getBytes(StandardCharsets.UTF_8),
                        "EX".getBytes(StandardCharsets.UTF_8),
                        String.valueOf(exptime).getBytes(StandardCharsets.UTF_8));
                return obj != null;
            }
        });
    }

    private Boolean setIfAbsent(final String key, final Serializable value, final long exptime) {
        return stringRedisTemplate.execute((RedisConnection connection) -> {
            RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
            RedisSerializer keySerializer = redisTemplate.getKeySerializer();
            Object obj = connection.execute("set", keySerializer.serialize(key),
                    valueSerializer.serialize(value),
                    "NX".getBytes(StandardCharsets.UTF_8),
                    "EX".getBytes(StandardCharsets.UTF_8),
                    String.valueOf(exptime).getBytes(StandardCharsets.UTF_8));
            return obj != null;
        });
    }

    /**
     * spring boot 2 版本 redisTemplate.opsForValue().setIfAbsent 可以设置时间
     */
    public Boolean setIfAbsent20(String key, String value, long expireTime) {

        Boolean result = stringRedisTemplate.opsForValue().setIfAbsent(key,value,expireTime, TimeUnit.SECONDS);

        return result;

    }


    private static final String LOCK_SUCCESS = "OK";
    /**
     * 表示 NX 模式
     */
    private static final String SET_IF_NOT_EXIST = "NX";
    /**
     * 单位 毫秒
     **/
    private static final String SET_WITH_EXPIRE_TIME_PX = "PX";
    /**
     * 单位 秒
     **/
    private static final String SET_WITH_EXPIRE_TIME_EX = "EX";


    // jedis 客户端的
    public boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {

        jedis = new Jedis("localhost");

        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME_EX, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }



}
