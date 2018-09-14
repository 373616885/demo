package com.qin.observer.rxjava;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.schedulers.ScheduledRunnable;
import io.reactivex.schedulers.Schedulers;


public class Maps {
    public static void main(String[] args) {

        Thread t = Thread.currentThread();
        //System.out.println(t.getName());
        Observable.create(new ObservableOnSubscribe<Integer>() {
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        }).map(new Function<Integer, String>() {
            public String apply(Integer integer) throws Exception {
                System.out.println("qin apply: " + integer + "  线程：" + Thread.currentThread().getName());
                return "This is result " + integer;
            }
        })
        //.subscribeOn(Schedulers.io()) //在子线程发射
        //.observeOn(Schedulers.newThread())  //在主线程接收
        .subscribe(new Consumer<String>() {
            public void accept(@NonNull String s) throws Exception {
                System.out.println("qin accept: " + s + "  线程：" + Thread.currentThread().getName());
            }
        });
    }
}
