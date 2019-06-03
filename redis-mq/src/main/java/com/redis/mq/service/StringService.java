package com.redis.mq.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author qinjp
 * @date 2019-06-03
 **/
@Service
public class StringService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set() {
        stringRedisTemplate.opsForValue().set("name", "tom");
        String name = stringRedisTemplate.opsForValue().get("name");
        System.out.println(name);
        stringRedisTemplate.opsForValue().set("name", "tom", 10, TimeUnit.SECONDS);
        // 由于设置的是10秒失效，十秒之内查询有结果，十秒之后返回为null
        name = stringRedisTemplate.opsForValue().get("name");
        System.out.println(name);
        stringRedisTemplate.opsForValue().set("key", "hello world");
        stringRedisTemplate.opsForValue().set("key", "redis", 6);
        name = stringRedisTemplate.opsForValue().get("key");
        System.out.println(name);

    }


    public void setIfAbsent() {
        //false  multi1之前已经存在
        stringRedisTemplate.opsForValue().setIfAbsent("multi1", "multi1");
        //true  multi111之前不存在
        stringRedisTemplate.opsForValue().setIfAbsent("multi111", "multi111");

    }

    public void multiSet() {
        Map<String, String> maps = new HashMap<String, String>();
        maps.put("multi1", "multi1");
        maps.put("multi2", "multi2");
        maps.put("multi3", "multi3");
        stringRedisTemplate.opsForValue().multiSet(maps);
        List<String> keys = new ArrayList<String>();
        keys.add("multi1");
        keys.add("multi2");
        keys.add("multi3");
        System.out.println(stringRedisTemplate.opsForValue().multiGet(keys));
    }

    public void multiSetIfAbsent() {
        Map<String,String> maps = new HashMap<String, String>();
        maps.put("multi11","multi11");
        maps.put("multi22","multi22");
        maps.put("multi33","multi33");
        Map<String,String> maps2 = new HashMap<String, String>();
        maps2.put("multi1","multi1");
        maps2.put("multi2","multi2");
        maps2.put("multi3","multi3");
        System.out.println(stringRedisTemplate.opsForValue().multiSetIfAbsent(maps));
        System.out.println(stringRedisTemplate.opsForValue().multiSetIfAbsent(maps2));
    }

    public void increment() {
        stringRedisTemplate.opsForValue().increment("increlong",1);
        System.out.println(stringRedisTemplate.opsForValue().get("increlong"));
        stringRedisTemplate.opsForValue().increment("increlong",2);
        System.out.println(stringRedisTemplate.opsForValue().get("increlong"));
        stringRedisTemplate.opsForValue().increment("increlong",3);
        System.out.println(stringRedisTemplate.opsForValue().get("increlong"));
    }

    public void size() {
        // 返回key所对应的value值得长度
        Long valueLength = stringRedisTemplate.opsForValue().size("key");
        System.out.println(valueLength);
    }

    public void setBit() {
        stringRedisTemplate.opsForValue().set("bitTest","a");
        // 'a' 的ASCII码是 97。转换为二进制是：01100001
        // 'b' 的ASCII码是 98  转换为二进制是：01100010
        // 'c' 的ASCII码是 99  转换为二进制是：01100011
        //因为二进制只有0和1，在setbit中true为1，false为0，因此我要变为'b'的话第六位设置为1，第七位设置为0
        stringRedisTemplate.opsForValue().setBit("bitTest",6, true);
        stringRedisTemplate.opsForValue().setBit("bitTest",7, false);
        System.out.println(stringRedisTemplate.opsForValue().get("bitTest"));


        System.out.println(stringRedisTemplate.opsForValue().getBit("bitTest",7));
    }

}
