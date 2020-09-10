package com.zzsim.gz.airport.common.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 前端返回错误信息
 *
 * @author qinjp
 * @date 2020/9/10
 */
public class MsgSource {

    private MsgSource() { }

    private static final Map<String, String> messages = new HashMap<>();

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

    static {
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            messages.put(key, resourceBundle.getString(key));
        }
    }

    public static String getMsg(String key) {
        return messages.get(key);
    }

}
