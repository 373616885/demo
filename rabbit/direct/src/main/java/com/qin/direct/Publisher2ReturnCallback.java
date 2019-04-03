package com.qin.direct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author qinjp
 **/
@Component
public class Publisher2ReturnCallback implements RabbitTemplate.ReturnCallback {

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        System.out.println("消息主体 message 2: " + message);
        System.out.println("回复 replyCode 2: " + replyCode);
        System.out.println("描述 2 ：" + replyText);
        System.out.println("消息使用的交换器 exchange 2: " + exchange);
        System.out.println("消息使用的路由键 routing 2: " + routingKey);
    }
}
