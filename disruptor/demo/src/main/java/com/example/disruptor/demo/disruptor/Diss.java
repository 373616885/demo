package com.example.disruptor.demo.disruptor;

import com.example.disruptor.demo.factory.LongEventFactory;
import com.example.disruptor.demo.handler.LongEventHandler;
import com.example.disruptor.demo.model.LongEvent;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author qinjp
 * @date 2019-07-05
 **/
public class Diss {

    public static AtomicInteger threadNumber = new AtomicInteger(1);

    public static void main(String[] args) {
        // 如何使用 Disruptor
        /**
         * 1.定义事件
         * LongEvent 事件(Event)就是通过 Disruptor 进行交换的数据类型
         * 2.定义事件工厂
         * LongEventFactory 需要实现接口 com.lmax.disruptor.EventFactory<T>
         * 3.定义事件处理的具体实现
         * LongEventHandler 通过实现接口 com.lmax.disruptor.EventHandler<T> 定义事件处理的具体实现
         * 4.定义用于事件处理的线程池
         * ThreadFactory 提供的线程来触发 Consumer 的事件处理
         * 5.指定等待策略
         */

        // Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer 如何等待新事件，这是策略模式的应用。
        // Disruptor 提供了多个 WaitStrategy 的实现，每种策略都具有不同性能和优缺点，根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升。
        // 例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
        // BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
        // SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
        // YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
        // 消费的时候需要的线程
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                t.setName("disruptor-" + threadNumber.getAndIncrement());
                return t;
            }
        };

        WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
        WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
        WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();

        //6.启动 Disruptor
        EventFactory<LongEvent> eventFactory = new LongEventFactory();


        int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；

        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory,
                ringBufferSize, threadFactory, ProducerType.SINGLE,
                BLOCKING_WAIT);

        EventHandler<LongEvent> eventHandler = new LongEventHandler();

        disruptor.handleEventsWith(eventHandler);
        disruptor.setDefaultExceptionHandler(new MessageExceptionHandler());
        // 多个就可以并行计算
        //disruptor.handleEventsWith(eventHandler,eventHandler);
        // 串行依次执行
        //disruptor.handleEventsWith(eventHandler).then(eventHandler);
        // 并行,串行结合--菱形方式执行
        //disruptor.handleEventsWith(eventHandler,eventHandler).then(eventHandler);
        // 链式并行计算
        //disruptor.handleEventsWith(new C11EventHandler()).then(new C12EventHandler());
        //disruptor.handleEventsWith(new C21EventHandler()).then(new C22EventHandler());

        disruptor.start();

        // 7.发布事件
        /**
         * 第一步：先从 RingBuffer 获取下一个可以写入的事件的序号；
         * 第二步：获取对应的事件对象，将数据写入事件对象；
         * 第三部：将事件提交到 RingBuffer;
         */
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        long sequence = ringBuffer.next();//请求下一个事件序号；

        try {
            // 获取该序号对应的事件对象；
            LongEvent event = ringBuffer.get(sequence);
            //获取要通过事件传递的业务数据；
            Long data = 1L;
            event.setValue(data);
        } finally{
            // 最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用
            ringBuffer.publish(sequence);
        }

        //==== 简化写法 ====//
        //获取要通过事件传递的业务数据；
        Long data = 1L;
        //Disruptor 提供另外一种形式的调用来简化以上操作
        ringBuffer.publishEvent(TRANSLATOR, data);
        //8.关闭 Disruptor
        disruptor.shutdown();

    }

    /**
     * 异常处理类
     */
    public static class MessageExceptionHandler implements ExceptionHandler<LongEvent>{
        @Override
        public void handleEventException(Throwable ex, long sequence, LongEvent event) {
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

    static class Translator implements EventTranslatorOneArg<LongEvent, Long>{
        @Override
        public void translateTo(LongEvent event, long sequence, Long data) {
            event.setValue(data);
        }
    }

    static Translator TRANSLATOR = new Translator();

}
