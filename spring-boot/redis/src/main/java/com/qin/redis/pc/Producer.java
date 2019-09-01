package com.qin.redis.pc;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
public class Producer{

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 生产者
     */
    public void producer(String msg) {
        // 对应 操作 redis 的 list
        stringRedisTemplate.opsForList().leftPush(Constant.PRODUCER_CONSUMER, msg);
    }

}
