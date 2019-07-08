package com.example.disruptor.demo.standard;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2019-07-08
 **/
public abstract class AsyncDisruptor<T> {

    /**
     * 定义事件工厂
     */
    abstract EventFactory<T> newInstance();

    /**
     * 定义事件处理的具体实现
     */
    abstract EventHandler<T>[] onEvent();

    /**
     * event 事件与 data 转换
     */
    abstract void translateTo(T event, T data);

    public void publish(T data) {
        RingBuffer<T> ringBuffer = disruptor.getRingBuffer();
        //请求下一个事件序号；
        long sequence = ringBuffer.next();
        try {
            // 获取该序号对应的事件对象；
            T event = ringBuffer.get(sequence);
            // 获取要通过事件传递的业务数据
            translateTo(event, data);
        } finally {
            // 最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用
            ringBuffer.publish(sequence);
        }
    }

    private volatile Disruptor<T> disruptor;

    public synchronized void start() {
        start("SLEEP");
    }

    private synchronized void start(final String strategyUp) {
        if (disruptor != null) {
            return;
        }
        final int ringBufferSize = DisruptorConfig.calculateRingBufferSize(null);

        final ThreadFactory defaultThreadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("AsyncDisruptor-");
                return t;
            }
        };

        final WaitStrategy waitStrategy = DisruptorConfig.createWaitStrategy(strategyUp);

        // newInstance() 实现的定义事件工厂接口
        disruptor = new Disruptor<T>(this.newInstance(),
                ringBufferSize,
                defaultThreadFactory,
                ProducerType.SINGLE,
                waitStrategy);

        // 处理器 onEvent() 实现的定义事件处理的具体实现接口
        disruptor.handleEventsWith(this.onEvent());

        // 异常处理
        disruptor.setDefaultExceptionHandler(new DefaultExceptionHandler<T>());
        // 开始
        disruptor.start();

    }

    private static final int MAX_DRAIN_ATTEMPTS_BEFORE_SHUTDOWN = 200;
    private static final int SLEEP_MILLIS_BETWEEN_DRAIN_ATTEMPTS = 50;

    public void stop() {
        disruptor.shutdown();
    }

    public boolean stop(final long timeout, final TimeUnit timeUnit) {
        final Disruptor<T> temp = disruptor;
        if (temp == null) {
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
            // give up events, if any
            temp.halt();
        }
        return true;
    }


    private static boolean hasRingBuffer(final Disruptor<?> disruptor) {
        final RingBuffer<?> ringBuffer = disruptor.getRingBuffer();
        return !ringBuffer.hasAvailableCapacity(ringBuffer.getBufferSize());
    }

    /**
     * 异常处理类
     */
    private final class DefaultExceptionHandler<T> implements ExceptionHandler<T> {

        @Override
        public void handleEventException(Throwable ex, long sequence, T event) {
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
}
