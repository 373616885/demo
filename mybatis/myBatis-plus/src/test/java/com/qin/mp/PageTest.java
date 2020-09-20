package com.qin.mp;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class PageTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void SelectPage() {
        Page<User> page = new Page<>(1, 5);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id", "name")
                .like("name", "雨").lt("age", 40);
        IPage<User> IPages = userMapper.selectPage(page, queryWrapper);
        System.out.println("总页数：" + IPages.getPages());
        System.out.println("总记录数：" + IPages.getTotal());
        IPages.getRecords().forEach(System.out::println);
    }


    @Test
    public void selectMapsPage() {
        Page<Map<String, Object>> page = new Page<>(2, 2);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id", "name").lt("age", 40);
        IPage<Map<String, Object>> mapIPage = userMapper.selectMapsPage(page, queryWrapper);
        System.out.println("总页数：" + mapIPage.getPages());
        System.out.println("总记录数：" + mapIPage.getTotal());
        mapIPage.getRecords().forEach(System.out::println);
    }

    @Test
    public void SelectPageNoConut() {
        Page<User> page = new Page<>(2, 5, false);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id", "name").lt("age", 40);
        IPage<User> IPages = userMapper.selectPage(page, queryWrapper);
        System.out.println("总页数：" + IPages.getPages());
        System.out.println("总记录数：" + IPages.getTotal());
        IPages.getRecords().forEach(System.out::println);
    }


    @Test
    public void SelectPageCustomize() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> page = new Page<>(1, 3);
        queryWrapper.lt("age", 40);
        IPage<User> IPages = userMapper.selectPageVo(page,"373616885");
    }

}
