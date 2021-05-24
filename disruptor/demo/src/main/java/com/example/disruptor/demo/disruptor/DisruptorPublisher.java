package com.example.disruptor.demo.disruptor;

import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
public class DisruptorPublisher {

    public static class TestHandler {
        private LongAdder longAdder = new LongAdder();

        public void process(LongEvent event) throws InterruptedException {
            Thread.sleep(1000);
            longAdder.increment();
            event.setValue(longAdder.longValue());
            System.out.println("LongEvent one: " + Thread.currentThread().getName()
                    + " value : " + event.getValue()
                    + " data : " + event.getData());

        }

    }


    private class TestEventHandler implements EventHandler<LongEvent> {

        private TestHandler handler;

        public TestEventHandler(TestHandler handler) {
            this.handler = handler;
        }

        @Override
        public void onEvent(LongEvent event, long sequence, boolean endOfBatch)
                throws Exception {
            // 请求之后处理的数据
            handler.process(event);
        }

    }

    private static final WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();

    private Disruptor<LongEvent> disruptor;
    private TestEventHandler handler;
    private RingBuffer<LongEvent> ringbuffer;
    private ThreadFactory threadFactory;

    private EventFactory<LongEvent> eventFactory = () -> new LongEvent();

    public DisruptorPublisher(int bufferSize, TestHandler handler) {
        this.handler = new TestEventHandler(handler);
        threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("disruptor-");
                return t;
            }
        };
        disruptor = new Disruptor<LongEvent>(eventFactory, bufferSize,
                threadFactory, ProducerType.SINGLE,
                YIELDING_WAIT);
    }

    @SuppressWarnings("unchecked")
    public void start() {
        disruptor.handleEventsWith(handler);
        disruptor.start();
        ringbuffer = disruptor.getRingBuffer();
    }

    public void publish(int data) {
        long seq = ringbuffer.next();
        try {
            LongEvent evt = ringbuffer.get(seq);
            // 请求之前的数据
            evt.setValue(0L);
            evt.setData(data);
        } finally {
            ringbuffer.publish(seq);
        }
    }

    public static void main(String[] args) {
        DisruptorPublisher disruptorPublisher = new DisruptorPublisher(1024, new TestHandler());
        disruptorPublisher.start();
        for (int i = 0; i < 100; i++) {
            disruptorPublisher.publish(i);
        }
        System.out.println("==== start ====");
        disruptorPublisher.disruptor.shutdown();
    }
}
