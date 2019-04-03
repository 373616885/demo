package com.qin.topic;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author qinjp
 **/
@Service
public class Receive {

    @RabbitListener(queues = "topic_queue_a")    //监听器监听指定的Queue
    public void processA(String str) {
        System.out.println("Received A:" + str);
    }

    @RabbitListener(queues = "topic_queue_b")    //监听器监听指定的Queue
    public void processB(@Payload String message,
                         @Headers Map<String, Object> map,
                         @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                         Channel channel) {
//        map.forEach((k,v) -> {
//            System.out.println("key : " + k + "   value :" + v);
//        });
        //System.out.println(" deliveryTagId : " + deliveryTag);
        System.out.println("Received B:" + message);
        // AmqpRejectAndDontRequeueException
        //  这个异常不会重新发送回来
        //  如果有dead-letter queue被设置的话该消息就会被置入, 否则被丢弃
//        try {
//             // pub message是没有ack的 只有消费端才有
//            channel.basicAck(deliveryTag,false);
//            //channel.basicNack(deliveryTag,false,false);
//            channel.basicReject(deliveryTag,false); //与basicNack差异缺少multiple参数
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


}
