package com.qin.servlet.embedded.listenter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("====应用启动 contextInitialized====");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("====应用销毁 contextDestroyed====");

    }
}
