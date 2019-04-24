package com.qin.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Author qinjp
 **/
public class CyclicBarrierTest {

    public static void main(String[] args) {
        //1.得到一个CyclicBarrier实例
        CyclicBarrier cb = new CyclicBarrier(4);
        new Thread(new Fishing(cb),"1").start();
        new Thread(new Fishing(cb),"2").start();
        new Thread(new Fishing(cb),"3").start();
        new Thread(new Fishing(cb),"4").start();
    }


    static class Fishing implements Runnable{
        CyclicBarrier cb;
        public Fishing(CyclicBarrier cb) {
            this.cb = cb;
        }

        @Override
        public void run() {
            try {
                cb.await();
                System.out.println("第（" + Thread.currentThread().getName() + "）个人开始钓鱼");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
