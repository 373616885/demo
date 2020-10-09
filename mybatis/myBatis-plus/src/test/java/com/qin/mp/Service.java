package com.qin.mp;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.qin.mp.domain.User;
import com.qin.mp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Service {

    @Autowired
    UserService userService;

    @Test
    public void getOne() {
        User user = userService.getOne(Wrappers.<User>lambdaQuery().eq(User::getAge, 30),false);
        System.out.println(user);
    }


    @Test
    public void chain() {
        // 链式调用
        List<User> users = userService.lambdaQuery().eq(User::getAge, 30).like(User::getName,"南").list();
        System.out.println(users);
    }

    @Test
    public void chain1() {
        // 链式调用
        Boolean result = userService.lambdaUpdate().ge(User::getAge, 50).set(User::getEmail,"78802581@qq.com").update();
        System.out.println(result);
    }
}
