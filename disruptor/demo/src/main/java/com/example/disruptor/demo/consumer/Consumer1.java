package com.example.disruptor.demo.consumer;

import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
@Slf4j
public class Consumer1 implements WorkHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event) throws Exception {
        Thread.sleep(100);
        System.out.println( "Consumer1 :"+ Thread.currentThread().getName() + " -接收到消息:" + event);
    }
}
