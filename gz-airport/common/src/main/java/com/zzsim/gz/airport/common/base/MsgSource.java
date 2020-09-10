package com.zzsim.gz.airport.common.base;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 前端返回错误信息
 * @author qinjp
 * @date 2020/9/10
 */
public class MsgSource {

    private static final Map<String,String> messages = new HashMap<>();

    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");

    static{
        Enumeration<String> keys = resourceBundle.getKeys();
        while (keys.hasMoreElements()) {
            messages.put(keys.nextElement() , resourceBundle.getString(keys.nextElement()));
        }
    }

    public static String getMsg(String key){
        return messages.get(key);
    }


    public static void main(String[] args) {
//        ResourceBundle resourceBundle = ResourceBundle.getBundle("messages");
//
//        static{
//            Enumeration<String> keys = resourceBundle.getKeys();
//            while (keys.hasMoreElements()) {
//                messages.put(keys.nextElement() , resourceBundle.getString(keys.nextElement()));
//            }
//        }
    }
}
