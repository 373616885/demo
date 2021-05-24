package com.example.disruptor.demo.producer;

import com.example.disruptor.demo.model.NotifyEvent;
import com.lmax.disruptor.EventFactory;

/**
 * @author qinjp
 * @date 2019-07-05
 **/
public class Producer implements EventFactory<NotifyEvent> {

    @Override
    public NotifyEvent newInstance() {
        return new NotifyEvent();
    }
}
