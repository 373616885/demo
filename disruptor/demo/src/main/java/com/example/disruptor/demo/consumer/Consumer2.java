package com.example.disruptor.demo.consumer;

import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.WorkHandler;


/**
 * @author qinjp
 * @date 2019-07-06
 **/
//@Slf4j
public class Consumer2 implements WorkHandler<LongEvent> {

    @Override
    public void onEvent(LongEvent event) throws Exception {
        Thread.sleep(110);
        System.out.println("Consumer2 :" + Thread.currentThread().getName() + " -接收到消息:" + event);
    }
}
