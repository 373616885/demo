package com.qin.mp;

import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class Retrieve {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void selectByid() {
        User user = userMapper.selectById(1L);
        System.out.println(user);
    }

    @Test
    public void selectBatchIds() {
        List<Long> ids = Arrays.asList(1L,2L,3L);
        List<User> users = userMapper.selectBatchIds(ids);
        System.out.println(users);
    }


    @Test
    public void selectByMap() {
        Map<String,Object>  columnMap = new HashMap<>();
        // where name = '向西' and age = 25
        // 这里放的是数据库的列
        columnMap.put("name","向南");
        columnMap.put("age","28");
        columnMap.put("manager_id","2");
        List<User> users = userMapper.selectByMap(columnMap);
        System.out.println(users);
    }

}
