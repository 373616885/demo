package com.zzsim.gz.airport.web.log.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2020/10/12
 */
public class LogSingleThread {

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 1;
    private static final long KEEP_ALIVE_TIME = 0L;
    private static final int CAPACITY = 8192;
    private static final String NAME_FORMAT = "log-single-thread-%d";


    private static final ThreadFactory FACTORY = new ThreadFactoryBuilder()
            .setNameFormat(NAME_FORMAT)
            .setDaemon(false)
            .setPriority(Thread.NORM_PRIORITY)
            .build();

    private static final ThreadPoolExecutor LOG_SINGLE_THREAD = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(CAPACITY), FACTORY);

    public static void execute(Runnable command) {
        // 这里异常被吞掉
        LOG_SINGLE_THREAD.execute(command);
    }

}
