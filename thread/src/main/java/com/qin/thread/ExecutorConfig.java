package com.qin.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @Author qinjp
 **/
@Slf4j
@Component
public class ExecutorConfig {

    public final static String EXECUTOR_NAME = "executor-async";

    private final static String NAME_FORMAT = "task-%d";

    private final int CORE_POOL_SIZE = 5;
    private final int MAXIMUM_POOL_SIZE = 8;
    private final int KEEP_ALIVE_TIME = 30;
    private final static int QUEUE_CAPACITY = 4;

    @Bean(name = ExecutorConfig.EXECUTOR_NAME)
    public AsyncTaskExecutor async() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(CORE_POOL_SIZE);
        executor.setMaxPoolSize(MAXIMUM_POOL_SIZE);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setKeepAliveSeconds(KEEP_ALIVE_TIME);
        executor.setThreadNamePrefix(ExecutorConfig.EXECUTOR_NAME + "-");
        executor.setDaemon(false);
        executor.setThreadFactory(factory);
        executor.setRejectedExecutionHandler(new RejectedHandler());
        executor.initialize();
        return executor;
    }


    private final ThreadFactory factory = new ThreadFactoryBuilder()
            .setNameFormat(NAME_FORMAT)
            .setDaemon(false)
            .setPriority(Thread.NORM_PRIORITY)
            .setUncaughtExceptionHandler(new ExceptionHandler())
            .build();


    private class RejectedHandler implements RejectedExecutionHandler {

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("task" + r.toString() +
                    " rejected from " + e.toString() +
                    " 线程池已满");
        }
    }

    /**
     *
     */
    private class ExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error(t.getName() + " 无返回值得异常处理", e.getMessage());
        }
    }

}
