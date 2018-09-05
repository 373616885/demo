package com.qin.mq.amqp.client.Exchanges;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsDirect {

    private static final String EXCHANGE_NAME = "logs-direct";
    private static final String[] LOG_LEVEL_ARR = {"debug", "info", "error"};

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接
        ConnectionFactory factory = new ConnectionFactory();
        // 设置 RabbitMQ 的主机名
        factory.setHost("localhost");
        // 创建一个连接
        Connection connection = factory.newConnection();
        // 创建一个通道
        Channel channel = connection.createChannel();
        // 指定一个交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        // 创建一个非持久的、唯一的、自动删除的队列
        String queueName = channel.queueDeclare().getQueue();

        // 设置日志级别
        String severity = LOG_LEVEL_ARR[2];
        // 绑定交换器和队列
        // queueBind(String queue, String exchange, String routingKey)
        // 参数1 queue ：队列名
        // 参数2 exchange ：交换器名
        // 参数3 routingKey ：路由键名
        channel.queueBind(queueName, EXCHANGE_NAME, severity);//订阅（Subscribing）
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {
                System.out.println("这是一个特殊的处理");
                String message = new String(body, "UTF-8");
                System.out.println(" [direct] Received '" + message + "'");
            }
        };

        channel.basicConsume(queueName, true, consumer);


    }


}
