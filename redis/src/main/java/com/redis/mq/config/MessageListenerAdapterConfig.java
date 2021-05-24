package com.redis.mq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import com.redis.mq.service.Receiver;

@Configuration
public class MessageListenerAdapterConfig {
	@Autowired
	private Receiver receiver;

	// 利用反射来创建监听到消息之后的执行方法
	@Bean(name="chatlistenerAdapter")
	MessageListenerAdapter chatlistenerAdapter() {
		return new MessageListenerAdapter(receiver, "receiveMessage");
	}
}
