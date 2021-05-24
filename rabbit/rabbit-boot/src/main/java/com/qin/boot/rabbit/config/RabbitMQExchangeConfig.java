package com.qin.boot.rabbit.config;

import com.qin.boot.rabbit.servie.Receiver;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQExchangeConfig {

    public static final String QUEUE_NAME = "spring-boot";
    public static final String QUEUE_EXCHANGE_NAME = "spring-boot-exchange";

    @Bean
    public Queue queue() {
        // 是否持久化
        // the queue will survive a broker restart
        boolean durable = false;
        // 仅创建者可以使用的私有队列，断开后自动删除
        // used by only one connection and the queue will be deleted when that connection closes
        boolean exclusive = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        // queue that has had at least one consumer is deleted when last consumer unsubscribes
        boolean autoDelete = false;


        Map<String, Object> args = new HashMap<String, Object>();
        args.put("x-message-ttl", 60000);// 定义消息在队列中存活时间--这是全局设置， -- 毫秒   1000 = 1秒 这里是60 秒 不能设置为 0
        // 队列在没有消费者空闲30分钟后将被自动删除
        args.put("x-expires", 1800000);// queue 上没有任何 consumer -- 以毫秒为单位 不能设置为 0 这里是30分钟
        // 绑定死信交换机
        args.put("x-dead-letter-exchange", "dead_letter_exchange");//设置死信交换机
        // 绑定死信交换机
        args.put("x-dead-letter-routing-key", "dead_letter_routing-key");//设置死信routingKey

        return new Queue(QUEUE_NAME, durable, exclusive, autoDelete ,args);
        //return new Queue(QUEUE_NAME, durable, exclusive, autoDelete);
    }

    @Bean
    public TopicExchange exchange() {
        // 是否持久化
        boolean durable = false;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new TopicExchange(QUEUE_EXCHANGE_NAME, durable, autoDelete);
    }

    // 业务绑定
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(exchange())
                .with(QUEUE_NAME);
    }

    /**
     * 全局处理器
     */
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(QUEUE_NAME);
//        container.setMessageListener((MessageListener) message -> {         //消息监听处理
//            System.out.println("====接收到消息=====");
//            System.out.println(new String(message.getBody()));
//            //相当于自己的一些消费逻辑抛错误
//            //throw new NullPointerException("consumer fail");
//        });
//
//        container.setDefaultRequeueRejected(false);//拒绝是否回到队列
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);//是否手工确认
//        return container;
//    }

//    @Bean
//    public RabbitListenerContainerFactory<?> rabbitListenerContainerFactory(ConnectionFactory connectionFactory){
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setAcknowledgeMode(AcknowledgeMode.NONE);             //开启手动 ack
//        return factory;
//    }


//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        // 这个receiver对象反射执行receiveMessage这个方法
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }

}
