package com.qin.redis.web;

import com.qin.redis.pc.Producer;
import com.qin.redis.utils.RedisLock;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ProducerController {

    private final StringRedisTemplate stringRedisTemplate;

    private final Producer producer;

    @GetMapping("/producer")
    public String producer(String msg) {
        RedisLock.lock(stringRedisTemplate,"373616885",20);
        producer.producer(msg);
        RedisLock.unLock(stringRedisTemplate,"373616885");
        return "success";
    }

}
