package com.qin.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class Customize {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void SelectAll() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age",40);
        List<User> userList = userMapper.selectAll(queryWrapper);
        userList.forEach(System.out::println);
    }


    @Test
    public void SelectAllXml() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age",40);
        List<User> userList = userMapper.selectAllXml(queryWrapper);
        userList.forEach(System.out::println);
    }

}
