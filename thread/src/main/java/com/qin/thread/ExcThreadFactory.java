package com.qin.thread;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @Author qinjp
 **/
@Slf4j
public final class ExcThreadFactory {

    private final static int CORE_POOL_SIZE = 10;
    private final static int MAXIMUM_POOL_SIZE = 10;
    private final static long KEEP_ALIVE_TIME = 0L;
    // 缓存个数
    private final static int CAPACITY = 1024;
    private final static String NAME_FORMAT = "xx-task-%d";

    public final static ExecutorService ORDER_THREAD = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAXIMUM_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(CAPACITY),
            ExcThreadFactory.ORDER_THREAD_FACTORY,
            new RejectedHandler());

    private final static ThreadFactory ORDER_THREAD_FACTORY = new ThreadFactoryBuilder()
            .setNameFormat(NAME_FORMAT)
            // false 主线程结束后，还继续运行
            // true  主线程结束后，便马上结束
            .setDaemon(false)
            // 值必须在 MIN_PRIORITY(1)到 MAX_PRIORITY(10)范围内
            // 要返回一个线程为默认的优先级，指定 NORM_PRIORITY(5)
            .setPriority(Thread.NORM_PRIORITY)
            // 无返回值发送异常后处理
            .setUncaughtExceptionHandler(new AsyncExceptionHandler())
            .build();

    private static class RejectedHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            throw new RejectedExecutionException("task" + r.toString() +
                    " rejected from " + e.toString() +
                    " because of too many tasks");
        }
    }

    /**
     * 如果这个单个线程是ThreadGroup中的一个Thread，
     * 那么这个线程将使用ThreadGroup的UncaughtExceptionHandler。
     * ThreadGroup自身已经实现了Thread.UncaughtExceptionHandler接口
     * @Async 通过实现 AsyncConfigurer 的getAsyncUncaughtExceptionHandler方法就可以覆盖
     */
    private static class AsyncExceptionHandler implements Thread.UncaughtExceptionHandler {

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            log.error(t.getName() + " throw Exception occurs in async method", e.getMessage());
        }
    }

}

