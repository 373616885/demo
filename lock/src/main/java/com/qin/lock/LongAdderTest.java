package com.qin.lock;

import lombok.SneakyThrows;

import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author qinjp
 **/
public class LongAdderTest {

    @SneakyThrows
    public static void main(String[] args) {
        int N = 100;
        CountDownLatch end = new CountDownLatch(N);
        CyclicBarrier cb = new CyclicBarrier(N);

        // Creates a new adder with initial sum of zero.
        LongAdder longAdder = new LongAdder();
        System.out.println(longAdder.longValue());
        ExecutorService service = Executors.newFixedThreadPool(N);
        for (int i = 0; i < N; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("add one start");
                        cb.await();// 并发等待
                        System.out.println("add one end");
                        // 加一
                        longAdder.increment();
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // 这个放到最后
                        // longAdder.increment(); 还是比较耗时的
                        end.countDown();
                    }
                }
            });

        }

        end.await();// 等待完成

        System.out.println("end :" + longAdder.sum());

    }
}
