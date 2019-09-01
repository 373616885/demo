package com.qin.redis.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Receiver {

    // 发布订阅--消费者
    public void receiveMessage(String message) {
        log.warn("mq-redis: Received <" + message + ">");
    }
}
