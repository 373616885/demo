package com.qin.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;

/**
 * @Author qinjp
 **/
@Slf4j
@Component
public class AsyncExecutorConfig implements AsyncConfigurer {


    @Resource(name = ExecutorConfig.EXECUTOR_NAME)
    AsyncTaskExecutor asyncTaskExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return asyncTaskExecutor;
    }

    /**
     * 覆盖ThreadGroup的UncaughtExceptionHandler
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncUncaughtExceptionHandler(){
            @Override
            public void handleUncaughtException(Throwable ex, Method method, Object... params) {
                log.error("qinjp exception occurs in async method", ex);
            }
        };
    }



}
