package com.example.disruptor.demo.spring;

import com.lmax.disruptor.EventHandler;

public class NotifyEventHandler implements EventHandler<NotifyEvent> {

    @Override
    public void onEvent(NotifyEvent notifyEvent, long l, boolean b) throws Exception {
        System.out.println("接收到消息：" + notifyEvent);
    }

}