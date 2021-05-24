package com.qin.topic;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Author qinjp
 **/
@Configuration
public class Config {

    /**
     * 自定义一个Queue
     */
    @Bean
    public Queue queueA() {
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

        return new Queue("topic_queue_a", durable, exclusive, autoDelete);
    }

    @Bean
    public Queue queueB() {
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

        return new Queue("topic_queue_b", durable, exclusive, autoDelete);
    }

    @Bean
    public TopicExchange topicExchange(){
        // 是否持久化
        boolean durable = true;
        // 当所有消费客户端连接断开后，是否自动删除队列
        boolean autoDelete = false;
        return new TopicExchange("topic_exchange", durable, autoDelete);
    }

    /***
     * 业务绑定
     *  .（句号）: 分隔单词的分隔符
     *  *（星号）：可以（只能）匹配一个单词
     *  #（井号）：可以匹配多个单词（或者零个）
     */

    @Bean
    public Binding bindQueueA() {
        return BindingBuilder.bind(queueA()).to(topicExchange())
                .with("*.a.*");
    }

    @Bean
    public Binding bindQueueB() {
        return BindingBuilder.bind(queueB()).to(topicExchange())
                .with("*.b.*");
    }

    @Bean
    public Binding bindQueueC() {
        return BindingBuilder.bind(queueB()).to(topicExchange())
                .with("*.*.b");
    }

}
