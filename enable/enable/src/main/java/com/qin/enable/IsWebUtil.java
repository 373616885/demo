package com.qin.enable;

import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.util.ClassUtils;

public class IsWebUtil {

    public static final boolean IS_WEB;

    private static final String SERVLET_WEB_APPLICATION_CLASS = "org.springframework.web.context.support.GenericWebApplicationContext";

    static {
        ClassLoader classLoader = CachingConfigurationSelector.class.getClassLoader();
        IS_WEB = ClassUtils.isPresent(SERVLET_WEB_APPLICATION_CLASS, classLoader);
    }

    public static boolean IsServlet() {
        ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        try {
            forName(SERVLET_WEB_APPLICATION_CLASS, classLoader);
            return true;
        } catch (Throwable ex) {
            return false;
        }
    }

    private static Class<?> forName(String className, ClassLoader classLoader) throws ClassNotFoundException {
        if (classLoader != null) {
            return classLoader.loadClass(className);
        }
        return Class.forName(className);
    }

}
