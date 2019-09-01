package com.qin.redis.utils;


import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2019-05-31
 **/
public class RedisLock {
    /**
     * RedisLock的正确姿势 ，来找redis作者的总结
     * 加锁：
     * 通过setnx 向特定的key写入一个随机数，并设置失效时间，写入成功即加锁成功
     * 注意点：
     *  必须给锁设置一个失效时间            ----->    避免死锁
     *  加锁时，每个节点产生一个随机字符串    ----->    避免锁误删
     *  写入随机数与设置失效时间必须是同时    ----->    保证加锁的原子性
     *  使用：
     *      SET key value NX PX 3000
     *
     * 解锁：
     *  匹配随机数，删除redis上的特定的key数据，
     *  要保证获取数据，判断一致以及删除数据三个操作是原子性
     *  执行如下lua脚本：
     *      if redis.call('get', KEYS[1]) == ARGV[1] then
     *          return redis.call('del', KEYS[1])
     *      else
     *          return 0
     *      end
     *
     */

    /**
     * KEY 的个数
     **/
    private static final int NUM_KEYS = 1;

    private static final Long RELEASE_LOCK_SUCCESS = 1L;

    /**
     * lua脚本
     **/
    private static final String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then " +
            "return redis.call('del', KEYS[1]) " +
            "else " +
            "return 0 " +
            "end";
    /**
     * 存储随机数
     **/
    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    /**
     * 加锁
     */
    public static boolean lock(StringRedisTemplate stringRedisTemplate, String key, int expireTime) {
        // 产生随机数
        String uuid = UUID.randomUUID().toString();
        // 设置 存在 key 就不设置
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(key, uuid, expireTime, TimeUnit.SECONDS);
        // 成功加锁
        if (BooleanUtils.isTrue(lock)) {
            // 随机数绑定线程
            LOCAL.set(uuid);

            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     */
    public static boolean unLock(StringRedisTemplate stringRedisTemplate, String key) {

        String uuid = LOCAL.get();

        //当前线程没有绑定uuid
        //直接返回
        if (StringUtils.isBlank(uuid)) {
            return false;
        }

        Object result = stringRedisTemplate.execute((RedisConnection connection) -> connection.eval(
                SCRIPT.getBytes(StandardCharsets.UTF_8),
                ReturnType.INTEGER,
                NUM_KEYS,
                key.getBytes(StandardCharsets.UTF_8),
                uuid.getBytes(StandardCharsets.UTF_8)));

        if (RELEASE_LOCK_SUCCESS.equals(result)) {
            // 解除绑定线程的随机数
            LOCAL.remove();
            return true;
        }
        return false;
    }


    public static void main(String[] args) {

    }

}
