package com.qin.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//@Configuration
public class DeadLetterExchangeConfig {

    @Bean
    public Queue deadQueue(){
        Queue queue = new Queue("dead_letter_queue", true);
        return queue;
    }

    @Bean
    public DirectExchange deadExchange() {
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new DirectExchange("dead_letter_exchange", durable, autoDelete);
    }

    // 业务绑定
    @Bean
    public Binding deadDinding() {
        return BindingBuilder.bind(deadQueue()).to(deadExchange())
                .with("dead_letter_routing-key");
    }

}
