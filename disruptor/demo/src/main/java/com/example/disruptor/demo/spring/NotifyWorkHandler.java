package com.example.disruptor.demo.spring;

import com.lmax.disruptor.WorkHandler;

public class NotifyWorkHandler implements WorkHandler<NotifyEvent> {
    @Override
    public void onEvent(NotifyEvent event) throws Exception {
        System.out.println("NotifyWorkHandler : " + event);
    }
}
