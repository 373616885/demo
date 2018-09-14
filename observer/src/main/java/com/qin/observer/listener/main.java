package com.qin.observer.listener;

import java.util.EventObject;

public class main {
    public static void main(String[] args) {
        Sources sources = new Sources();

        sources.addListener(new Listener() {
            @Override
            public void handleEvent(EventObject event) {
                if (event.getSource().equals("开")){
                    System.out.println("开启  Listener");
                }
            }
        });
        sources.addListener(new Listener() {
            @Override
            public void handleEvent(EventObject event) {
                if (event.getSource().equals("关")){
                    System.out.println("关闭  Listener");
                }
            }
        });

        sources.notifyListenerEvents(new EventObject("关"));

        sources.notifyListenerEvents(new EventObject("开"));

    }
}
