package com.example.disruptor.demo.spring;

import com.lmax.disruptor.EventFactory;

/**
 * 创建消息工厂用于生产消息
 */
public class NotifyEventFactory implements EventFactory<NotifyEvent> {

    @Override
    public NotifyEvent newInstance() {
        return new NotifyEvent();
    }
}
