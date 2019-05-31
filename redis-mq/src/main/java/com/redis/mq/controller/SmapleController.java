package com.redis.mq.controller;

import com.redis.mq.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.soap.Addressing;

/**
 * @author qinjp
 * @date 2019-05-31
 **/
@RestController
public class SmapleController {

    @Autowired
    private RedisLock redisLock;

    @GetMapping("/sample/{message}")
    public String sendRedisMessage(@PathVariable String message) {
        redisLock.setIfAbsent20("373616885",message,10000);
        return "success";
    }
}
