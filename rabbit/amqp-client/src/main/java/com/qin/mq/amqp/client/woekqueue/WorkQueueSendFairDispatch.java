package com.qin.mq.amqp.client.woekqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkQueueSendFairDispatch {
    private final static String QUEUE_NAME = "hello_WorkQueueSendFairDispatch";

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
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 公平转发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        // 发送消息
        for (int i = 10; i >0; i--) {
            String message = "qinjp:" + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [FairDispatch] Sent '" + message + "'");
        }
        // 关闭频道和连接
        channel.close();
        connection.close();
    }

}