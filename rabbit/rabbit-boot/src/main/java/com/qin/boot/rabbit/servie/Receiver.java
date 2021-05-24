package com.qin.boot.rabbit.servie;

import com.qin.boot.rabbit.config.RabbitMQConfig;
import com.qin.boot.rabbit.config.RabbitMQExchangeConfig;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class Receiver {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 局部处理器
     */
    @RabbitListener(queues = RabbitMQExchangeConfig.QUEUE_NAME)
    public void receiveMessage(@Payload String message,
                               @Headers Map<String,Object> map,
                              @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                               Channel channel) {
//        long deliveryTag = Long.valueOf(map.get("amqp_deliveryTag").toString());
        map.forEach((k,v) -> {
            System.out.println("key : " + k + "   value :" + v);
        });
        System.out.println(" deliveryTagId : " + deliveryTag);
        System.out.println("Received <" + message + ">");
        // AmqpRejectAndDontRequeueException
        //  这个异常不会重新发送回来
        //  如果有dead-letter queue被设置的话该消息就会被置入, 否则被丢弃
        try {

           channel.basicAck(deliveryTag,false);
            //channel.basicNack(deliveryTag,false,false);
            channel.basicReject(deliveryTag,false); //与basicNack差异缺少multiple参数
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
