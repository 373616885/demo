package com.example.disruptor.demo.spring;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotifyEventHandlerException implements ExceptionHandler<NotifyEvent> {

    @Override
    public void handleEventException(Throwable throwable, long sequence, NotifyEvent event) {
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
}
