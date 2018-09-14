package com.qin.observer.demo;

import java.util.Observable;

/**
 * 被观察者
 */
public class Publish extends Observable {

    // data 发送的改变就通知观察者
    private Object data;

    public Object getData() {
        return data;
    }

    // data 发送的改变就通知观察者
    public void setData(Object data) {
        this.data = data;
        setChanged();
        // 不带附加信息
        notifyObservers();
    }

    // data 直接发送信息给观察者
    public void changeData(Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}
