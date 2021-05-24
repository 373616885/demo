package com.qin.boot.rabbit.servie;

import com.qin.boot.rabbit.config.RabbitMQConfig;
import com.qin.boot.rabbit.config.RabbitMQExchangeConfig;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Sender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void send() {
        System.out.println("发送消息...");
//        MessageProperties a = new MessageProperties();
//        a.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);//持久化
//        Message message = new Message("你好， qinjp!".getBytes(), new MessageProperties());
        //rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME,"你好， qinjp!");
        rabbitTemplate.convertAndSend(RabbitMQExchangeConfig.QUEUE_NAME, "你好， qinjp!", msg -> {
            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            //每条消息的存活时间--与 x-message-ttl 类似，区别这只对单条消息有效
            //而x-message-ttl是整个队列的消息存活时间
            //这个时间是在投递的时候判断的，与实际存活的时间不一定一致
            //而x-message-ttl一旦过期，就会从队列中抹去
            msg.getMessageProperties().setExpiration("60000"); //毫秒   1000 = 1秒 这里是60 秒
            return msg;
        });

    }
}
