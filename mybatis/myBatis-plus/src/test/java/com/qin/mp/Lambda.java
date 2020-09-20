package com.qin.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Lambda {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectLambda() {

        /**
         * where name like '%雨%' and age < 40
         * 主要区别防止数据库字段名写错
         */
        // 等价Wrappers.query();
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getName,"雨")
                .lt(User::getAge,40);
        List<User> users = userMapper.selectList(lambdaQuery);
        users.forEach(System.out::println);
    }

    @Test
    public void selectLambda2() {

        /**
         * where name age < 40
         */
        // 等价Wrappers.query();
        LambdaQueryChainWrapper<User> lambdaQuery = new LambdaQueryChainWrapper(userMapper);
        List<User> list = lambdaQuery.like(User::getName, "雨")
                .lt(User::getAge, 40).list();

        list.forEach(System.out::println);
    }

}
