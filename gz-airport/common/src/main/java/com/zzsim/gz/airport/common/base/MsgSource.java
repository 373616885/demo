package com.zzsim.gz.airport.common.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 获取配置文件 messages.properties信息
 *
 * @author qinjp
 * @date 2020/9/10
 */
public final class MsgSource {

    private MsgSource() {
    }

    private static final Map<String, String> MESSAGES = new HashMap<>();

    private static final ResourceBundle RESOURCEBUNDLE = ResourceBundle.getBundle("messages");

    static {
        Enumeration<String> keys = RESOURCEBUNDLE.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            MESSAGES.put(key, RESOURCEBUNDLE.getString(key));
        }
    }

    public static String getMsg(String key) {
        return MESSAGES.get(key);
    }

}
