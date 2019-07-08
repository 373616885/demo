package com.example.disruptor.demo.spring;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class NotifyServiceImpl implements NotifyService, InitializingBean , DisposableBean {

    private Disruptor<NotifyEvent> disruptor;
    private static final int RING_BUFFER_SIZE = 1024 * 1024;

    /**
     * 实现 InitializingBean 接口
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        disruptor = new Disruptor<NotifyEvent>(new NotifyEventFactory(), RING_BUFFER_SIZE, Executors.defaultThreadFactory(), ProducerType.SINGLE, new BlockingWaitStrategy());
        disruptor.setDefaultExceptionHandler(new NotifyEventHandlerException());
        disruptor.handleEventsWith(new NotifyEventHandler());
        disruptor.start();
    }

    @Override
    public void sendNotify(String message) {
        RingBuffer<NotifyEvent> ringBuffer = disruptor.getRingBuffer();
        //lambda式写法，如果是用jdk1.8以下版本使用以上注释的一段
        ringBuffer.publishEvent((event, sequence, data) -> event.setMessage(data), message);
    }


    @Override
    public void destroy() throws Exception {
        disruptor.shutdown();
    }
}
