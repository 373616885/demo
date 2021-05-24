package com.redis.mq.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @PostMapping("/sendRedisMsg")
    public String sendRedisMessage(String message) {
    	// 发布订阅--生产者
        stringRedisTemplate.convertAndSend("chat", message);
        return "success";
    }
}
