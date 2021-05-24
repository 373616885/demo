package com.qin.redis.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author qinjp
 * @date 2019-08-14
 **/
@Component
public class RedisScan {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 注意Cursor一定不能关闭，在之前的版本中，这里Cursor需要手动关闭，
     * 但是从spring-data-redis-1.8.0.RELEASE开始，不能手动关闭！否则会报异常。
     * ScanOptions有两个参数，一个是match，另一个是count，分别对应scan命令的两个参数
     */
    @SuppressWarnings("unchecked")
    public Cursor<String> scan(String pattern, int limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        return (Cursor) redisTemplate.executeWithStickyConnection(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize);
            }
        });
    }

    public static void main(String[] args) {
        RedisScan helper = new RedisScan();
        Cursor<String> cursor = helper.scan("tpl-htmltpl:*",4);
        while (cursor.hasNext()) {
            cursor.next();
        }
    }

}
