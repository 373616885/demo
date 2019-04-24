package com.qin.lock;

import lombok.SneakyThrows;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static javafx.scene.input.KeyCode.R;

/**
 * @Author qinjp
 **/
public class LockTest {


    /**
     * lock()：获取锁，如果锁被暂用则一直等待
     * <p>
     * unlock():释放锁
     * <p>
     * tryLock(): 注意返回类型是boolean，如果获取锁的时候锁被占用就返回false，否则返回true
     * <p>
     * tryLock(long time, TimeUnit unit)：比起tryLock()就是给了一个时间期限，保证等待参数时间
     * <p>
     * lockInterruptibly()：用该锁的获得方式，如果线程在获取锁的阶段进入了等待，那么可以中断此线程，先去做别的事
     */
    private Lock lock = new ReentrantLock();// 默认非公平锁

    //需要参与同步的方法
    private void method(Thread thread) {
        System.out.println("线程名" + thread.getName() + "Start");

        lock.lock();
        try {
            System.out.println("线程名" + thread.getName() + "获得了锁");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("线程名" + thread.getName() + "释放了锁");
            lock.unlock();
        }
    }

    @SneakyThrows
    public static void main(String[] args) {

        final CyclicBarrier cb = new CyclicBarrier(2);

        LockTest lockTest = new LockTest();

        //线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                lockTest.method(Thread.currentThread());
            }
        }, "t1").start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lockTest.method(Thread.currentThread());
            }
        }, "t2").start();

        Thread.sleep(5000);
    }


}


