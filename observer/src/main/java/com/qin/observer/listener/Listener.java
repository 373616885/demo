package com.qin.observer.listener;

import java.util.EventListener;
import java.util.EventObject;

public interface Listener extends EventListener {

    void handleEvent(EventObject event);

}
