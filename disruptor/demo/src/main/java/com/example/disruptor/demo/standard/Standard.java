package com.example.disruptor.demo.standard;

import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2019-07-06
 **/
@Slf4j
public class Standard {

    private EventHandler<StandardEvent> eventHandler ;


    public boolean publish(Long data){
        RingBuffer<StandardEvent> ringBuffer = disruptor.getRingBuffer();
        //Disruptor 提供另外一种形式的调用来简化以上操作
        ringBuffer.publishEvent(TRANSLATOR, data);
        return true;
    }

    @Setter
    @Getter
    @ToString
    private static class StandardEvent {
        private Long value;
    }

    /**
     * RingBuffer生产工厂,初始化RingBuffer的时候使用
     */
    private static final EventFactory<StandardEvent> FACTORY = new EventFactory<StandardEvent>() {
        @Override
        public StandardEvent newInstance() {
            return new StandardEvent();
        }
    };

    private static final ThreadFactory THREADFACTORY = new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("disruptor-");
            return t;
        }
    };

    private static final class  Translator implements EventTranslatorOneArg<StandardEvent, Long> {
        @Override
        public void translateTo(StandardEvent event, long sequence, Long data) {
            // 值转换成事件
            event.setValue(data);
        }
    }

    private static final Translator TRANSLATOR = new Translator();

    /**
     * 异常处理类
     */
    private static final class MessageExceptionHandler implements ExceptionHandler<StandardEvent> {

        @Override
        public void handleEventException(Throwable ex, long sequence, StandardEvent event) {
            ex.printStackTrace();
        }

        @Override
        public void handleOnStartException(Throwable ex) {
            ex.printStackTrace();

        }

        @Override
        public void handleOnShutdownException(Throwable ex) {
            ex.printStackTrace();

        }
    }

    WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();

    private int ringBufferSize;

    private volatile Disruptor<StandardEvent> disruptor;

    public synchronized void start() {
        ringBufferSize = 1024;
        disruptor = new Disruptor<StandardEvent>(FACTORY,
                ringBufferSize, THREADFACTORY, ProducerType.SINGLE,
                SLEEPING_WAIT);

        // 单处理器
        disruptor.handleEventsWith(eventHandler);
        // 异常处理
        disruptor.setDefaultExceptionHandler(new MessageExceptionHandler());
        // 开始
        disruptor.start();
    }




    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;


    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        final Disruptor<?> temp = disruptor;
        if (temp == null) {
            log.info("disruptor already shut down.");
            // disruptor was already shut down by another thread
            return true;
        }

        for (int i = 0; hasRingBuffer(temp) && i < MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN; i++) {
            try {
                // give up the CPU for a while
                Thread.sleep(SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS);
            } catch (InterruptedException e) {
                // ignored
            }
        }
        try {
            // busy-spins until all events currently in the disruptor have been processed, or timeout
            temp.shutdown(timeout, timeUnit);
        } catch (final TimeoutException e) {
            log.warn("shutdown timed out after {} {}", timeout, timeUnit);
            // give up events, if any
            temp.halt();
        }
        return true;
    }


    private static boolean hasRingBuffer(final Disruptor<?> disruptor) {
        final RingBuffer<?> ringBuffer = disruptor.getRingBuffer();
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }

}
