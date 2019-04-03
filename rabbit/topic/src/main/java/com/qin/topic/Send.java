package com.qin.topic;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * @Author qinjp
 **/
@Service
public class Send {

    @Autowired
    private RabbitTemplate rabbitTemplate;


    /**
     * rabbitTemplate.send(message);   //发消息，参数类型为org.springframework.amqp.core.Message
     * rabbitTemplate.convertAndSend(object); //转换并发送消息。 将参数对象转换为org.springframework.amqp.core.Message后发送
     * rabbitTemplate.convertSendAndReceive(message) //转换并发送消息,且等待消息者返回响应消息。
     *
     * @param msg
     */
    public void send(String msg,String routingKey) {

        // pub message是没有ack的 只有消费端才有

        //rabbitTemplate.setMandatory(true);
        //rabbitTemplate.setConfirmCallback(publisherConfirmCallBack);
        //rabbitTemplate.setReturnCallback(publisherReturnCallback);

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        System.out.println("发送消息:" + msg);
//        MessageProperties a = new MessageProperties();
//        a.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);//持久化
//        Message message = new Message("你好， qinjp!".getBytes(), new MessageProperties());
        rabbitTemplate.convertAndSend("topic_exchange", routingKey, msg, correlationData);
//        rabbitTemplate.convertAndSend("direct-queue", "你好", msg -> {
//            msg.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
//            //每条消息的存活时间--与 x-message-ttl 类似，区别这只对单条消息有效
//            //而x-message-ttl是整个队列的消息存活时间
//            //这个时间是在投递的时候判断的，与实际存活的时间不一定一致
//            //而x-message-ttl一旦过期，就会从队列中抹去
//            msg.getMessageProperties().setExpiration("60000"); //毫秒   1000 = 1秒 这里是60 秒
//            return msg;
//        });

    }

}
