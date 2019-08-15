package s;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Singleton2 {

    private Singleton2(){

    }

    private static Singleton2 instance;

    public static Singleton2 getInstance(){
        if(instance == null) {//1：读取instance的值
            // 这里并发高的时候，会返回多个实例，不是单例了
            instance = new Singleton2();//2: 实例化instance
        }
        return instance;
    }

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(20);
        for (int i = 0; i< 20; i++) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+":"+Singleton2.getInstance());
                }
            });
        }
    }

}
