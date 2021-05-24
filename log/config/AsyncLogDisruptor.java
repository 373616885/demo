package com.zzsim.gz.airport.web.log.config;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.TimeoutBlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.zzsim.gz.airport.web.log.domain.LogEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 异常处理队列
 * 参考：AsyncLoggerDisruptor
 * 该队列最大 256 * 1024
 * 超过会阻塞--阻塞策略 TimeoutBlockingWaitStrategy
 *
 * @author qinjp
 * @date 2020/10/13
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AsyncLogDisruptor {

    private Disruptor<LogEvent> disruptor;

    @NonNull
    private final EventHandler<LogEvent> eventHandler;

    @PostConstruct
    public void init() {
        // 环形队列大小
        int ringbufferDefaultSize = 256 * 1024;

        disruptor = new Disruptor<>(factory,
                ringbufferDefaultSize,
                threadFactory,
                ProducerType.SINGLE,
                timeoutBlockingWaitStrategy);
        // 事件处理
        disruptor.handleEventsWith(eventHandler);
        // 异常处理
        disruptor.setDefaultExceptionHandler(defaultExceptionHandler);
        // 开始
        disruptor.start();
    }

    void publish(LogEvent log) {
        disruptor.publishEvent((event, sequence) -> {
            event.setEquence(sequence);
            event.setToken(log.getToken());
            event.setHost(log.getHost());
            event.setUrl(log.getUrl());
            event.setParam(log.getParam());
            event.setResult(log.getResult());
        });
    }

    private final ThreadFactory threadFactory = r -> {
        Thread t = new Thread(r);
        t.setDaemon(true);
        t.setName("async-Log-disruptor");
        return t;
    };

    private final TimeoutBlockingWaitStrategy timeoutBlockingWaitStrategy = new TimeoutBlockingWaitStrategy(10L, TimeUnit.MILLISECONDS);

    private final EventFactory<LogEvent> factory = LogEvent::new;

    private final ExceptionHandler<LogEvent> defaultExceptionHandler = new ExceptionHandler<LogEvent>() {

        @Override
        public void handleEventException(Throwable throwable, long sequence, LogEvent event) {
            throwable.fillInStackTrace();
            log.error("process data error sequence ==[{}] event==[{}] ,ex ==[{}]", sequence, event, throwable.getMessage());
        }

        @Override
        public void handleOnStartException(Throwable throwable) {
            log.error("start disruptor error ==[{}]!", throwable.getMessage());
        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {
            log.error("shutdown disruptor error ==[{}]!", throwable.getMessage());
        }
    };

}
