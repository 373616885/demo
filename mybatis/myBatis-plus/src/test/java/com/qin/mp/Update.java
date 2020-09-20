package com.qin.mp;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.qin.mp.domain.User;
import com.qin.mp.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class Update {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateById() {
        User user = new User();
        user.setEmail("373616885@qq.com");
        user.setAge(40);
        user.setUserId(2L);
        int rows = userMapper.updateById(user);
        System.out.println("影响行数：" + rows);
    }


    @Test
    public void updateWrapper() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "李艺伟")
                .eq("age", 28);

        /**
         * 默认不为null的出现在set中
         */
        User user = new User();
        user.setEmail("373616885@qq.com");
        user.setAge(30);
        // 这设置ID 是没有用的
        //user.setUserId(2L);

        int rows = userMapper.update(user, updateWrapper);


        System.out.println("影响行数：" + rows);
    }


    @Test
    public void updateWrapper2() {
        // 这里不为null的出现在where中
        User whereUser = new User();
        whereUser.setAge(28);

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>(whereUser);

        /**
         * 默认不为null的出现在set中
         */
        User user = new User();
        user.setEmail("373616885@qq.com");
        user.setAge(30);
        // 这设置ID 是没有用的
        //user.setUserId(2L);
        int rows = userMapper.update(user, updateWrapper);
        System.out.println("影响行数：" + rows);
    }


    @Test
    public void updateWrapper3() {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("age",30)
                .eq("name","李艺伟")
                .set("age",31);
        int rows = userMapper.update(null, updateWrapper);
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void updateLambda() {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getAge,31)
                .eq(User::getName,"李艺伟")
                .set(User::getAge,32);
        int rows = userMapper.update(null, updateWrapper);
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void updateLambdaChain() {
        LambdaUpdateChainWrapper<User> updateWrapper = new LambdaUpdateChainWrapper<>(userMapper);
        boolean result = updateWrapper.eq(User::getAge, 32)
                .eq(User::getName, "李艺伟")
                .set(User::getAge, 33).update();
        System.out.println("影响行数：" + result);
    }

}
