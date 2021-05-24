package com.qin.mq.amqp.client.woekqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkQueueSendDurability {

    private final static String QUEUE_NAME = "work_queue_hello_Durability";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 RabbitMQ 的主机名
        factory.setHost("localhost");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个队列
        boolean durable = true;// 持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        // 发送消息
        for (int i = 0; i < 10; i++) {
            String message = "qinjp:" + i;
            // 通过设置 MessageProperties 值为 PERSISTENT_TEXT_PLAIN
            // 标识我们的信息为持久化的
            channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [Durability] Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}
