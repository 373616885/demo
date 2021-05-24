package com.qin.observer.demo;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者必须实现Observer接口update方法
 */
public class One implements Observer {

    /**
     * 被观察者通知观察者执行的方法
     * Observable o 被观察者本身
     * Object arg 传递的参数
     */
    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) {
            // 被观察者身上获取以改变的数据
            receive(((Publish) o).getData());
            return;
        }
        receive(arg);
    }

    public void receive(Object arg) {
        System.out.println("One receive：" + arg.toString());
    }
}
