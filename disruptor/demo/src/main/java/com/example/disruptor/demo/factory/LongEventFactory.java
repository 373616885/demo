package com.example.disruptor.demo.factory;

import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.EventFactory;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
public class LongEventFactory implements EventFactory<LongEvent> {

    @Override
    public LongEvent newInstance() {
        return new LongEvent();
    }
}