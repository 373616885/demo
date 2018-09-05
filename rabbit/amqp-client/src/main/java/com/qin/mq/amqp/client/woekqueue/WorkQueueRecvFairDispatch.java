package com.qin.mq.amqp.client.woekqueue;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WorkQueueRecvFairDispatch {
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
        // 公平转发
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);
        // 指定一个队列
        boolean durable = false;// 持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        // 创建队列消费者
        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [FairDispatch] Received '" + message + "'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        // acknowledgment is covered below
        boolean autoAck = false;
        // true 服务器确认，false客户端发送确认请求--需要channel.basicAck回应
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        String[] taskArr = task.split(":");
        TimeUnit.SECONDS.sleep(5);
    }

}
