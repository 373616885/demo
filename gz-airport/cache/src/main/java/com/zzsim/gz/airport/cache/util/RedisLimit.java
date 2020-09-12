package com.zzsim.gz.airport.cache.util;

import com.zzsim.gz.airport.cache.base.CacheConstant;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author qinjp
 * @date 2019-06-14
 **/
@Component
@AllArgsConstructor
public class RedisLimit {

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 存 redis 的 key
     */
    private static final String LIMIT_TIMES = "limit:times";

    /**
     * KEY 的个数
     */
    private static final int NUM_KEYS = 1;

    /**
     * 成功返回值
     */
    private static final Long TRUE = 1L;

    /**
     * 频率限制脚本
     */
    private static final String SCRIPT = "local current = redis.call('incr',KEYS[1]) " +
            "if tonumber(current) == 1 then " +
            "    redis.call('expire',KEYS[1],ARGV[1]) " +
            "end " +
            "if current > tonumber(ARGV[2]) then " +
            "    return 0 " +
            "end " +
            "return 1";

    /**
     * redis 频率限制 默认一个小时
     *
     * @param key     限制关键字
     * @param timeout 限制时间，单位秒 （3600 一个小时）
     * @param count   限制次数
     * @return true   允许访问 false 不允许访问
     */
    public boolean limit(String key, Integer timeout, Integer count) {
        return execute(getKey(key), timeout, count);
    }

    private String getKey(String key) {
        return CacheConstant.KEY_PREFIX + ":" + LIMIT_TIMES + ":" + key;
    }

    private boolean execute(String key, Integer timeout, Integer count) {
        Object result = stringRedisTemplate.execute((RedisConnection connection) -> connection.eval(
                SCRIPT.getBytes(StandardCharsets.UTF_8),
                ReturnType.INTEGER,
                NUM_KEYS,
                key.getBytes(StandardCharsets.UTF_8),
                String.valueOf(timeout).getBytes(StandardCharsets.UTF_8),
                String.valueOf(count).getBytes(StandardCharsets.UTF_8)));
        return TRUE.equals(result);
    }

}
