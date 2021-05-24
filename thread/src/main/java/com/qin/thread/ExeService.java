package com.qin.thread;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author qinjp
 **/
@Service
public class ExeService {

    private AtomicInteger integer = new AtomicInteger(1);

    /**
     *实现Thread.UncaughtExceptionHandler接口的void uncaughtException(Thread t, Throwable e)方法。
     * 如果不设置一个Handler，那么单个Thread的Handler是null。
     * 但是，如果这个单个线程是ThreadGroup中的一个Thread，
     * 那么这个线程将使用ThreadGroup的UncaughtExceptionHandler。
     * ThreadGroup自身已经实现了Thread.UncaughtExceptionHandler接口
     *
     * @Async 通过实现 AsyncConfigurer 的getAsyncUncaughtExceptionHandler方法就可以覆盖
     */
    @Async(value = ExecutorConfig.EXECUTOR_NAME)
    public void service() {
        try {
            System.out.println(Thread.currentThread().getName());
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int a = integer.incrementAndGet();
        System.out.println(a);
        if (a % 2 == 0) {
            throw new RuntimeException("出现异常");
        }
    }

}
