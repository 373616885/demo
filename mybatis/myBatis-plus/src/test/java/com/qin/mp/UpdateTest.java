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
public class UpdateTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void updateById() {
        User user = new User();
        user.setEmail("373616885@qq.com");
        user.setAge(38);
        user.setUserId("1L");
        int rows = userMapper.updateById(user);
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void updateWrapper() {
        // updateWrapper 出现在 where 条件中
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("name", "刘明强")
                .eq("age", 31);
        // user 出现在 set 中
        User user = new User();
        user.setEmail("78802581@qq.com");
        user.setAge(40);
        userMapper.update(user,updateWrapper);
    }


    @Test
    public void updateWrapper2() {
        // 这里不为null的出现在where中
        User whereUser = new User();
        whereUser.setAge(29);
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>(whereUser);
        // 这里不为null的出现在 set 中
        User user = new User();
        user.setAge(40);
        userMapper.update(user,updateWrapper);
    }

    @Test
    public void updateWrapper3() {
        // 这里.set 方法就不需要创建对象（少量字段可以用）
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("age",30)
                .eq("name","李艺伟")
                .set("age",31);
        // 有 set 可以为 null
        int rows = userMapper.update(null, updateWrapper);
        System.out.println("影响行数：" + rows);
    }


    @Test
    public void updateLambda() {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getAge,40)
                .eq(User::getName,"刘明强")
                .set(User::getAge,32);
        int rows = userMapper.update(null, updateWrapper);
        System.out.println("影响行数：" + rows);
    }

    @Test
    public void updateLambdaChain() {
        // 链式写法
        LambdaUpdateChainWrapper<User> updateWrapper = new LambdaUpdateChainWrapper<>(userMapper);
        // 返回是否成功
        boolean result = updateWrapper.eq(User::getAge, 32)
                .eq(User::getName, "李艺伟")
                .set(User::getAge, 33).update();
        System.out.println("是否成功：" + result);
    }

}
