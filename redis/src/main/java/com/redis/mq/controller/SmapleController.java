package com.redis.mq.controller;

import com.redis.mq.util.RedisHelper;
import com.redis.mq.util.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

/**
 * @author qinjp
 * @date 2019-05-31
 **/
@RestController
public class SmapleController {

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/sample/{message}")
    public String sendRedisMessage(@PathVariable String message) {
        redisLock.setIfAbsent20("373616885",message,10000);
        return "success";
    }

    @GetMapping("/sample/scon")
    public String scon() {
        Cursor<String> cursor = RedisHelper.scan(stringRedisTemplate,"tpl-htmltpl:*",4);
        while (cursor.hasNext()) {
            cursor.next();
        }

        return "success";
    }
}
