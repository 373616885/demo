package com.qin.thread;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThreadApplicationTests {

    private AtomicInteger integer = new AtomicInteger(1);

    @Autowired
    private ExeService executorService;

    @Resource(name = ExecutorConfig.EXECUTOR_NAME)
    private AsyncTaskExecutor executor;

    @Test
    public void contextLoads() throws Exception {
        Runnable runnable =  new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(Thread.currentThread().getName());;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int a = integer.incrementAndGet();
                System.out.println(a);
                if (a%2==0) {
                    throw new RuntimeException("出现异常");
                }
            }
        };
        for(int i=0;i<50;i++){
            // 满了扔进去就拋异常
            executor.submit(runnable);

        }
        System.in.read();

    }

    @Test
    public void TestAsync() throws Exception {
        for(int i=0;i<5;i++){
            executorService.service();
        }

        System.out.println(1);
        System.in.read();
    }

}
