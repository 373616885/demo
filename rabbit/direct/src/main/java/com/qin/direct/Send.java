package com.qin.direct;

import org.springframework.amqp.core.MessageDeliveryMode;
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

    @Autowired
    private PublisherConfirmCallBack publisherConfirmCallBack;

    @Autowired
    private PublisherReturnCallback publisherReturnCallback;

    @Autowired
    private Publisher2ConfirmCallBack publisher2ConfirmCallBack;

    @Autowired
    private Publisher2ReturnCallback publisher2ReturnCallback;


    /**
     * rabbitTemplate.send(message);   //发消息，参数类型为org.springframework.amqp.core.Message
     * rabbitTemplate.convertAndSend(object); //转换并发送消息。 将参数对象转换为org.springframework.amqp.core.Message后发送
     * rabbitTemplate.convertSendAndReceive(message) //转换并发送消息,且等待消息者返回响应消息。
     * @param msg
     */
    public void send(String msg) {

        //若使用confirm-callback或return-callback，必须要配置publisherConfirms或publisherReturns为true
        //每个rabbitTemplate只能有一个confirm-callback和return-callback
        //要能想每个rabbitTemplate能有不用confirm-callback就必须多例

        //使用return-callback时必须设置mandatory为true，或者在配置中设置mandatory-expression的值为true，
        //可针对每次请求的消息去确定’mandatory’的boolean值，只能在提供’return -callback’时使用，与mandatory互斥。

        // pub message是没有ack的 只有消费端才有
        // 生产端发布 message是没有ack的 只有消费端才有

        // 如果有回调一定会触发
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(publisherConfirmCallBack);
        rabbitTemplate.setReturnCallback(publisherReturnCallback);

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        System.out.println("发送消息:" + msg);
//        MessageProperties a = new MessageProperties();
//        a.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);//持久化
//        Message message = new Message("你好， qinjp!".getBytes(), new MessageProperties());
        //rabbitTemplate.convertAndSend("direct_exchange","bind",true,correlationData);
        rabbitTemplate.convertAndSend("direct-queue-qin", "你好", mpp -> {
            mpp.getMessageProperties().setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);
            //每条消息的存活时间--与 x-message-ttl 类似，区别这只对单条消息有效
            //而x-message-ttl是整个队列的消息存活时间
            //这个时间是在投递的时候判断的，与实际存活的时间不一定一致
            //而x-message-ttl一旦过期，就会从队列中抹去
            mpp.getMessageProperties().setExpiration("60000"); //毫秒   1000 = 1秒 这里是60 秒
            return mpp;
        });

    }

    public void send2(String msg) {

        rabbitTemplate.setMandatory(true);
        rabbitTemplate.setConfirmCallback(publisher2ConfirmCallBack);
        rabbitTemplate.setReturnCallback(publisher2ReturnCallback);

        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        System.out.println("发送消息:" + msg);
//        MessageProperties a = new MessageProperties();
//        a.setDeliveryMode(MessageDeliveryMode.NON_PERSISTENT);//持久化
//        Message message = new Message("你好， qinjp!".getBytes(), new MessageProperties());
        rabbitTemplate.convertAndSend("direct_exchange","bind",msg,correlationData);
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
