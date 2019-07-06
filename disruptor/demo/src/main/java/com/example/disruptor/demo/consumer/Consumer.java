package com.example.disruptor.demo.consumer;

import com.example.disruptor.demo.model.NotifyEvent;
import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinjp
 * @date 2019-07-05
 **/
@Slf4j
public class Consumer implements EventHandler<NotifyEvent> {
    @Override
    public void onEvent(NotifyEvent event, long sequence, boolean endOfBatch) throws Exception {
        log.info(Thread.currentThread().getName() + " -接收到消息:"+ event);
    }
}
