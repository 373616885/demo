package com.qin.demo.web;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.qin.demo.bean.Cat;
import com.qin.demo.bean.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("set/redis")
    public String set() {

        redisTemplate.opsForValue().set("qin","qinjiepeng");
        User user = new User();
        user.setAge(30);
        user.setName("qinjiepeng");
        Cat cat = new Cat();
        cat.setName("tom");
        cat.setAge(1);
        user.setCat(cat);
        user.setCats(Lists.newArrayList(cat));
        redisTemplate.opsForValue().set("jie",user);
        return "SUCCESS";
    }


    @GetMapping("get/redis")
    public User get() {
        Object jie = redisTemplate.opsForValue().get("jie");
        return (User) jie;
    }


}
