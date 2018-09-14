package com.qin.observer.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class Observables {

    /**
     *
     1）在发射完 3 之后， 调用 e.onComplete() 方法，结束 发射数据。4 没有发射出来。

     2) 另外一个值得注意的点是，在RxJava 2.x中，可以看到发射事件方法相比1.x多了一个throws Excetion，意味着我们做一些特定操作再也不用try-catch了。

     3) 并且2.x 中有一个Disposable概念，这个东西可以直接调用切断，可以看到，当它的isDisposed()返回为false的时候，接收器能正常接收事件，但当其为true的时候，接收器停止了接收。所以可以通过此参数动态控制接收事件了。
     *
     */
    public static void main(String[] args) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(0);
                e.onNext(1);
                e.onNext(2);
                e.onComplete(); //结束
                e.onNext(3);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                System.out.println("qinjp onSubscribe: " + d.isDisposed());
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                System.out.println("qinjp onNext: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("qinjp onError: ");
            }

            @Override
            public void onComplete() {
                System.out.println("qinjp onComplete: ");
            }
        });

    }
}
