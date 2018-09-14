package com.qin.observer.listener;

import java.util.EventObject;
import java.util.Vector;

public class Sources {

    //监听器列表，监听器的注册则加入此列表
    private Vector<Listener> ListenerList = new Vector<>();

    //注册监听器
    public void addListener(Listener eventListener) {
        ListenerList.add(eventListener);
    }
    //撤销注册
    public void removeListener(Listener eventListener) {
        ListenerList.remove(eventListener);
    }

    //接受外部事件
    public void notifyListenerEvents(EventObject event) {
        for (Listener eventListener : ListenerList) {
            eventListener.handleEvent(event);
        }
    }



}
