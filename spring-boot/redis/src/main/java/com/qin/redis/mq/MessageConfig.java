package com.qin.redis.mq;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class MessageConfig {


    public static final String CHANNEL_PATTERN = "topic-mq";

    /**
     * 建立适配器
     */
    @Bean
    public MessageListenerAdapter adapter(Receiver receiver) {
        // onMessage 如果RedisMessage 中 没有实现接口，这个参数必须跟RedisMessage中的读取信息的方法名称一样
        return new MessageListenerAdapter(receiver, "receiveMessage");
    }

    /**
     * 建立监听器
     */
    @Bean
    RedisMessageListenerContainer chatContainer(RedisConnectionFactory connectionFactory, MessageListenerAdapter adapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        // 添加对应的监听器
        container.addMessageListener(adapter, new PatternTopic(CHANNEL_PATTERN));
        return container;
    }


}
