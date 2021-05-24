package com.qin.mp.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping(value = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> select() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
        return userList;
    }

    @GetMapping(value = "/page", produces = MediaType.APPLICATION_JSON_VALUE)
    public IPage<User> page() {
        System.out.println(("----- selectAll method test ------"));
        Page<User> page = new Page<>(2, 2);
        IPage<User> pages= userMapper.selectPageVo(page,"373616885");
        System.out.println("总页数：" + pages.getPages());
        System.out.println("总记录数：" + pages.getTotal());
        pages.getRecords().forEach(System.out::println);
        return pages;
    }

    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> xml() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lt("age",40);
        List<User> userList = userMapper.selectAllXml(queryWrapper);
        userList.forEach(System.out::println);
        return userList;
    }
}
