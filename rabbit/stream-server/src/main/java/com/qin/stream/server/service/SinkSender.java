package com.qin.stream.server.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@EnableBinding(Source.class)
public class SinkSender {

    private static Logger logger = LoggerFactory.getLogger(SinkSender.class);

    @Autowired
    Source source;

    public void send(String message) {
        source.output().send((MessageBuilder.withPayload(message).build()));
        logger.info("send : {}", message);
    }

}
