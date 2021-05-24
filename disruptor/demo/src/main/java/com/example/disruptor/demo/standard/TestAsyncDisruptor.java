package com.example.disruptor.demo.standard;

import com.example.disruptor.demo.handler.LongEventHandler;
import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;

import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2019-07-08
 **/
public class TestAsyncDisruptor extends AsyncDisruptor<LongEvent> {


    @Override
    EventFactory<LongEvent> newInstance() {
        return () -> new LongEvent();
    }

    @Override
    EventHandler<LongEvent>[] onEvent() {
        return new EventHandler[]{
            new LongEventHandler()
        };
    }

    @Override
    void translateTo(LongEvent event, LongEvent data) {
        event.setData(data.getData());
        event.setMsg(data.getMsg());
    }

    public static void main(String[] args) {
        TestAsyncDisruptor t  = new TestAsyncDisruptor();

        t.start();
        for (int i = 0; i < 1000; i++) {
            LongEvent a = new LongEvent();
            a.setData(i);
            a.setMsg("之前值是:"+i);
            t.publish(a);
        }
        System.out.println("end");
        t.stop(1, TimeUnit.DAYS);

    }
}
