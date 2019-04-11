package com.redis.mq.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Protocol;
import redis.clients.util.SafeEncoder;

import java.io.Serializable;

/**
 * @Author qinjp
 **/
@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;


    public boolean setIfAbsent(final String key, final Serializable value, final long exptime) {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer valueSerializer = redisTemplate.getValueSerializer();
                RedisSerializer keySerializer = redisTemplate.getKeySerializer();
                Object obj = connection.execute("set", keySerializer.serialize(key),
                        valueSerializer.serialize(value),
                        SafeEncoder.encode("NX"),
                        SafeEncoder.encode("EX"),
                        Protocol.toByteArray(exptime));
                return obj != null;
            }
        });
    }
}
