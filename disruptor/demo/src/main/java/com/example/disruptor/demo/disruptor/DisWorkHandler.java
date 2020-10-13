package com.example.disruptor.demo.disruptor;

import com.example.disruptor.demo.consumer.Consumer1;
import com.example.disruptor.demo.consumer.Consumer2;
import com.example.disruptor.demo.factory.LongEventFactory;
import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
public class DisWorkHandler {


    public static void main(String[] args) {


        //启动 Disruptor
        EventFactory<LongEvent> eventFactory = new LongEventFactory();


        int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory,
                ringBufferSize, Executors.defaultThreadFactory(), ProducerType.SINGLE,
                new SleepingWaitStrategy());
        // 多消费者重复处理生产者的消息，则使用disruptor.handleEventsWith方法将消费者传入
        // 消费者不重复的处理生产者的消息，则使用disruptor.handleEventsWithWorkerPool方法将消费者传入
        disruptor.handleEventsWithWorkerPool(new Consumer1(),new Consumer2());
        disruptor.start();
        // 7.发布事件
        /**
         * 第一步：先从 RingBuffer 获取下一个可以写入的事件的序号；
         * 第二步：获取对应的事件对象，将数据写入事件对象；
         * 第三部：将事件提交到 RingBuffer;
         */
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        for (int i=0;i<10;i++) {
            //==== 简化写法 ====//
            //获取要通过事件传递的业务数据；
            Long data = Long.valueOf(i);
            //Disruptor 提供另外一种形式的调用来简化以上操作
            ringBuffer.publishEvent(WTranslator, data);
            //8.关闭 Disruptor
        }

        disruptor.shutdown();


    }

    static class WTranslator implements EventTranslatorOneArg<LongEvent, Long> {
        @Override
        public void translateTo(LongEvent event, long sequence, Long data) {
            event.setValue(data);
        }
    }
    static WTranslator WTranslator = new WTranslator();

}
