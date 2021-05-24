package com.qin.mq.rabbitrecv.config;

import com.qin.mq.rabbitrecv.service.MailMessageListenerAdapter;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * rabbit 配置类
 */
@Configuration
@PropertySource(value = {"classpath:application.properties"})
public class RabbitMQConfig {

	@Autowired
	private Environment env;

	@Bean
	public ConnectionFactory connectionFactory() throws Exception {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(env.getProperty("mq.host").trim());
		connectionFactory.setPort(Integer.parseInt(env.getProperty("mq.port").trim()));
		connectionFactory.setVirtualHost(env.getProperty("mq.vhost").trim());
		connectionFactory.setUsername(env.getProperty("mq.username").trim());
		connectionFactory.setPassword(env.getProperty("mq.password").trim());
		return connectionFactory;
	}
	
	@Bean
	public CachingConnectionFactory cachingConnectionFactory() throws Exception {
		CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(connectionFactory());
		return cachingConnectionFactory;
	}
	
	@Bean
	public RabbitTemplate rabbitTemplate() throws Exception {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory());
		rabbitTemplate.setChannelTransacted(true);
		return rabbitTemplate;
	}
	
	@Bean
	public AmqpAdmin amqpAdmin() throws Exception {
		return new RabbitAdmin(cachingConnectionFactory());
	}

	@Bean
	public SimpleMessageListenerContainer listenerContainer(
			@Qualifier("mailMessageListenerAdapter") MailMessageListenerAdapter mailMessageListenerAdapter) throws Exception {

		String queueName = env.getProperty("mq.queue").trim();

		SimpleMessageListenerContainer simpleMessageListenerContainer =
				new SimpleMessageListenerContainer(cachingConnectionFactory());
		simpleMessageListenerContainer.setQueueNames(queueName);
		simpleMessageListenerContainer.setMessageListener(mailMessageListenerAdapter);

		/***
		 * AcknowledgeMode.MANUAL 手动确认
		 * AcknowledgeMode.NONE 不确认
		 * AcknowledgeMode.AUTO
		 * 	没有抛出异常则自动确认
		 * 	当抛出 AmqpRejectAndDontRequeueException 异常的时候且 requeue = false（不重新入队列），则消息会被拒绝
		 * 	当抛出 ImmediateAcknowledgeAmqpException 异常，则消费者会被确认
		 * 	其他的异常，且 requeue = true 则消息会被拒绝
		 */
		// 不确认
		simpleMessageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
		// 发送异常是否不重新入队列--有发生死循环的风险
		simpleMessageListenerContainer.setDefaultRequeueRejected(false);
		return simpleMessageListenerContainer;
	}
}
