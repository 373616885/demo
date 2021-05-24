package com.example.disruptor.demo.disruptor;

import com.example.disruptor.demo.consumer.Consumer;
import com.example.disruptor.demo.model.NotifyEvent;
import com.example.disruptor.demo.producer.EventProducer;
import com.example.disruptor.demo.producer.Producer;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinjp
 * @date 2019-07-05
 **/
public class Dis {
    public static void main(String[] args) {
        AtomicInteger threadNumber = new AtomicInteger(1);
        Producer producer = new Producer();
        //2的N次方
        int ringBufferSize = 1024;
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("disruptor-" + threadNumber.getAndIncrement());
                return t;
            }
        };

        //创建disruptor
        Disruptor<NotifyEvent> disruptor = new Disruptor<>(producer, ringBufferSize, threadFactory);

        //连接消费事件方法
        disruptor.handleEventsWith(new Consumer());
        disruptor.start();
        RingBuffer<NotifyEvent> ringBuffer = disruptor.getRingBuffer();
        EventProducer ep = new EventProducer(ringBuffer);
        for(long i = 0; i<10; i++){
            ep.publish("qinjp"+i);
        }

        disruptor.shutdown();
    }
}
