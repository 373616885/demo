package com.example.demo;

import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.UUID;

/**
 * @author qinjp
 * @date 2019-05-31
 **/
public class RedisLockTwo {
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
     *  为什么要保证原子性：
     *      为了防止误删
     *      例如 A 超时了释放了锁 （判断uuid.equals(jedis.get(lockKey)之后 -- 超时，B加锁）
     *      B获得锁 ，这时 A 完成了释放了B获得的锁
     *      // 判断加锁与解锁是不是同一个客户端
     *     if (uuid.equals(jedis.get(lockKey))) {
     *         // A运行到这里，超时释放锁，B获得锁
     *         // 若在此时，这把锁突然不是这个客户端的，则会误解锁(这时 A 完成了释放了B获得的锁)
     *         jedis.del(lockKey);
     *     }
     *  执行如下lua脚本：
     *      if redis.call('get', KEYS[1]) == ARGV[1] then
     *          return redis.call('del', KEYS[1])
     *      else
     *          return 0
     *      end
     *
     */
    // 使用jedis 客户端的
    /**
     * SET key value NX PX 3000 成功返回值
     */
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
     * lua脚本
     **/
    private static final String SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
    /**
     * 存储随机数
     **/
    private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

    /**
     * 加锁
     */
    public static boolean lock(Jedis jedis, String key, int expireTime) {
        // 产生随机数
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        String result = jedis.set(key, uuid, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME_PX, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            // 随机数绑定线程
            LOCAL.set(uuid);
            return true;
        }
        return false;

    }

    /**
     * 释放分布式锁
     */
    public static boolean unLock(Jedis jedis, String key) {

        String uuid = LOCAL.get();

        //当前线程没有绑定uuid
        //直接返回
        if (uuid == null || "".equals(uuid)) {
            return false;
        }
		// 为了防止误删--例如 A 超时了释放了锁 ，B获得锁 ，这时 A 完成了释放了B获得的锁
        Object result = jedis.eval(SCRIPT, Collections.singletonList(key), Collections.singletonList(uuid));

        if (Long.valueOf(1).equals(result)) {
            // 解除绑定线程的随机数
            LOCAL.remove();
            return true;
        }
        return false;
    }


    public static void main(String[] args) {
        Jedis jedis = new Jedis("47.100.185.77", 6379);
        jedis.auth("373616885");
        jedis.select(0);
        final String LOCK_KEY = "LOCK_KEY";

        RedisLockTwo.lock(jedis, LOCK_KEY, 50000000);

        RedisLockTwo.unLock(jedis, LOCK_KEY);

    }

}
