package com.redis.mq.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisMessageConfig {

	@Resource(name = "chatlistenerAdapter")
	private MessageListenerAdapter chatlistenerAdapter;

	// 初始化监听器
	@Bean
	RedisMessageListenerContainer chatContainer(RedisConnectionFactory connectionFactory) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		// 添加对应的监听器
		container.addMessageListener(chatlistenerAdapter, new PatternTopic("chat"));
		return container;
	}

	// 使用默认的工厂初始化redis操作模板
	@Bean
	StringRedisTemplate template(RedisConnectionFactory connectionFactory) {
		return new StringRedisTemplate(connectionFactory);
	}
}
