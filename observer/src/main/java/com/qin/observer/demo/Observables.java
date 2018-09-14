package com.qin.observer.demo;


public class Observables {

    public static void main(String[] args) {
        // 被观察者--注册中心
        Publish observable = new Publish();
        // 观察者--被通知的角色
        One one = new One();
        Two two = new Two();
        // 向注册中心注册
        observable.addObserver(one);
        observable.addObserver(two);

        observable.setData("data is change");
        observable.changeData("I am qin");

    }
}
