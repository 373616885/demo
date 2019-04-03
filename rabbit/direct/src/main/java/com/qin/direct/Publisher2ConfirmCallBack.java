package com.qin.direct;

import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author qinjp
 **/
@Component
public class Publisher2ConfirmCallBack implements RabbitTemplate.ConfirmCallback {


    /**
     * 发布确认 实现ConfirmCallBack接口 消息发送到交换器Exchange后触发回调
     * spring.rabbitmq.publisher-confirms=true
     *
     * @param correlationData 消息唯一标识
     * @param ack             确认结果
     * @param cause           失败原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("消息唯一标识2：" + correlationData);
        System.out.println("确认结果2：" + ack);
        System.out.println("失败原因2：" + cause);
    }
}
