package com.redis.mq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class Receiver {
	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
	
	// 发布订阅--消费者
	public void receiveMessage(String message) {
		System.out.println("mq-service: Received <" + message + ">");
		LOGGER.warn("mq-service: Received <" + message + ">");
	}
}