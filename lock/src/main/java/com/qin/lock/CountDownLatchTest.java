package com.qin.lock;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;

/**
 * @Author qinjp
 **/
public class CountDownLatchTest {

    @SneakyThrows
    public static void main(String[] args) {
        int N = 1000;
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i) {// create and start threads
            new Thread(new CountDownLatchTest().new Worker(startSignal, doneSignal)).start();
        }


        //doSomethingElse();            // don't let run yet
        startSignal.countDown();      // let all threads proceed
        //doSomethingElse();
        doneSignal.await();           // wait for all to finish
    }


    void doWork() {
        System.out.println("dowork");
    }

    class Worker implements Runnable {
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        @Override
        public void run() {
            try {
                startSignal.await();
                doWork();
                doneSignal.countDown();
            } catch (InterruptedException ex) {
            } // return;
        }

    }

}



