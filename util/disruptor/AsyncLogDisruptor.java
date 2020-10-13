package com.zzsim.gz.airport.web.log.config;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zzsim.gz.airport.web.log.domain.LogEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 参考：AsyncLoggerDisruptor
 *
 * @author qinjp
 * @date 2020/10/13
 */
@Component
@SuppressWarnings("all")
public class AsyncLogDisruptor {

    private Disruptor<LogEvent> disruptor;

    @Autowired
    private EventHandler eventHandler;

    @PostConstruct
    public synchronized void start() {
        disruptor = new Disruptor<>(FACTORY,
                RINGBUFFER_DEFAULT_SIZE,
                DEFAULT_THREAD_FACTORY,
                ProducerType.SINGLE,
                TIMEOUT_BLOCKING_WAIT_STRATEGY);

        disruptor.handleEventsWith(eventHandler);
        disruptor.start();
    }

    public void publish(LogEvent log) {
        disruptor.publishEvent((event, sequence) -> {
            event.setEquence(sequence);
            event.setToken(log.getToken());
            event.setUrl(log.getUrl());
            event.setParam(log.getParam());
            event.setResult(log.getResult());
        });
    }

    private static final int RINGBUFFER_DEFAULT_SIZE = 256 * 1024;

    private static final ThreadFactory DEFAULT_THREAD_FACTORY = r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("async-Log-disruptor");
        return t;
    };

    private static final TimeoutBlockingWaitStrategy TIMEOUT_BLOCKING_WAIT_STRATEGY = new TimeoutBlockingWaitStrategy(10L, TimeUnit.MILLISECONDS);

    private static final EventFactory<LogEvent> FACTORY = LogEvent::new;

}
