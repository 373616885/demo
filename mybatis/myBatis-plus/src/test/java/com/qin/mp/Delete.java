package com.qin.mp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import com.sun.corba.se.impl.encoding.WrapperInputStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class Delete {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void deleteById() {
        int rows = userMapper.deleteById(1L);
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void deleteBatchIds() {
        int rows = userMapper.deleteBatchIds(Arrays.asList(1L,2L));
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void deleteByMap() {
        Map<String,Object> map = new HashMap<>();
        map.put("age",25);
        map.put("name","向西");
        int rows = userMapper.deleteByMap(map);
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void deleteByWrapper() {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.like(User::getName,"雨");
        int rows = userMapper.delete(lambdaQuery);
        System.out.println("影响行数：" + rows);
    }

}
