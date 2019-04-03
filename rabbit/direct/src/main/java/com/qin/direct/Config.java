package com.qin.direct;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.SerializerMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

/**
 * @Author qinjp
 **/
@Configuration
public class Config {


    @Autowired
    private PublisherConfirmCallBack publisherConfirmCallBack;

    @Autowired
    private PublisherReturnCallback publisherReturnCallback;

    //@PostConstruct
    //public void Init () {
        //一个 RabbitTemplate 只能设置一个 publisherConfirmCallBack
        //所有的回调都调同一个方法 ，
        //不然报 Only one ConfirmCallback is supported by each RabbitTemplate
        //如果想回调不同的方法就必须是多例每次bean都是新的
        //RabbitTemplate @Scope("prototype")
        //然后就可以每个send方法都有自己的回调
        //rabbitTemplate.setConfirmCallback(publisherConfirmCallBack);
       // rabbitTemplate.setReturnCallback(publisherReturnCallback);
    //}

    /**
     * 一个 RabbitTemplate 只能设置一个 publisherConfirmCallBack
     * 所有的回调都调同一个方法
     * 不然报 Only one ConfirmCallback is supported by each RabbitTemplate
     * 如果想回调不同的方法就必须是多例每次bean都是新的
     * RabbitTemplate @Scope("prototype")
     * 然后就可以每个send方法都有自己的回调
     */
    @Bean
    @Scope("prototype")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        // 强制标志 如果有回调一定会触发
        //template.setMandatory(true);
        return template;
    }

    /**
     * 自定义一个Queue
     */
    @Bean
    public Queue queue() {
        // 是否持久化
        boolean durable = true;
        // 仅创建者可以使用的私有队列，断开后自动删除
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;

        //Map<String, Object> args = new HashMap<String, Object>();
        //args.put("x-message-ttl", 60000);// 定义消息在队列中存活时间--这是全局设置， -- 毫秒   1000 = 1秒 这里是60 秒 不能设置为 0
        // 队列在没有消费者空闲30分钟后将被自动删除
        //args.put("x-expires", 1800000);// queue 上没有任何 consumer -- 以毫秒为单位 不能设置为 0 这里是30分钟
        // 绑定死信交换机
        //args.put("x-dead-letter-exchange", "dead_letter_exchange");//设置死信交换机
        // 绑定死信交换机
        //args.put("x-dead-letter-routing-key", "dead_letter_routing-key");//设置死信routingKey

        return new Queue("direct-queue", durable, exclusive, autoDelete);
    }

    @Bean
    public DirectExchange directExchange(){
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new DirectExchange("direct_exchange", durable, autoDelete);
    }

    // 业务绑定
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange())
                .with("bind");
    }

}
