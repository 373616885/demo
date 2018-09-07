package com.qin.stream.client.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class SinkReceiver {

    private static Logger logger = LoggerFactory.getLogger(SinkReceiver.class);

    /**
     * 接收RabbitMQ发送来的消息
     */
    @StreamListener(Sink.INPUT)
    public void receive(String playLoad) {
        logger.info("Received:" + playLoad);
    }

}
