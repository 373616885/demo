package com.example.disruptor.demo.handler;

import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.EventHandler;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
public class LongEventHandler implements EventHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        event.setValue(sequence);
        System.out.println("LongEvent one: " + Thread.currentThread().getName() + ": " + event.getValue() + ": " + sequence);
        System.out.println("LongEvent one: " + event);

    }

}
