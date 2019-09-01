package com.qin.redis.web;

import com.qin.redis.mq.MessageConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping("/send/redis/{msg}")
    public String sendRedisMessage(@PathVariable String msg) {
    	// 发布订阅--生产者
        stringRedisTemplate.convertAndSend(MessageConfig.CHANNEL_PATTERN, msg);
        return "success";
    }
}
