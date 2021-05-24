package com.redis.mq.util;

import org.springframework.beans.factory.annotation.Autowired;
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
public class RedisLimit {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String LIMIT_TIMES = "limit_times";

    /**
     * KEY 的个数
     **/
    private static final int NUM_KEYS = 1;
    /**
     * 成功返回值
     **/
    private static final Long TRUE = 1L;

    /**
     * 默认一个小时
     **/
    private static final Integer DEFAULT_TIME = 3600;

    /**
     * 频率限制脚本
     **/
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
     * @param key   限制关键字
     * @param count 限制次数
     * @return true 允许访问 false 不允许访问
     */
    public Boolean limit(String key, Integer count) {
        return limit(getKey(key), DEFAULT_TIME, count);
    }

    private String getKey(String key) {
        return LIMIT_TIMES + ":" + key;
    }

    private Boolean limit(String key, Integer timout, Integer count) {
        Object result = stringRedisTemplate.execute((RedisConnection connection) -> connection.eval(
                SCRIPT.getBytes(StandardCharsets.UTF_8),
                ReturnType.INTEGER,
                NUM_KEYS,
                key.getBytes(StandardCharsets.UTF_8),
                String.valueOf(timout).getBytes(StandardCharsets.UTF_8),
                String.valueOf(count).getBytes(StandardCharsets.UTF_8)));
        return TRUE.equals(result);
    }

}
