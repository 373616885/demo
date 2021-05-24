package com.qin.mp;

import com.qin.mp.domain.ModeUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ARTest {

    @Test
    public void insert() {
        ModeUser modeUser = new ModeUser();
        modeUser.setAge(29);
        modeUser.setEmail("liuming@qq.com");
        modeUser.setName("刘明");
        modeUser.setManagerId(2L);
        modeUser.setPassword("373616885");
        boolean result = modeUser.insert();
        System.out.println(result);
    }

    @Test
    public void selectById() {
        ModeUser modeUser = new ModeUser();
        modeUser.setUserId(1L);
        ModeUser result = modeUser.selectById();
        System.out.println(result == modeUser);
        System.out.println(result);
    }

    @Test
    public void selectById2() {
        ModeUser modeUser = new ModeUser();
        ModeUser result = modeUser.selectById(1305143761016913921L);
        System.out.println(result == modeUser);
        System.out.println(result);
    }


    @Test
    public void updateById() {
        ModeUser modeUser = new ModeUser();
        modeUser.setName("qinjp");
        modeUser.setAge(30);
        modeUser.setManagerId(2L);
        modeUser.setUserId(1305151554415374338L);
        boolean result = modeUser.updateById();
        System.out.println(result);
    }

    @Test
    public void insertOrUpdate() {
        // 设置了ID就会去查询一次，判断是否已经存在数据
        // 不存在就是插入，存在就修改
        // 没设置就是直接插入
        ModeUser modeUser = new ModeUser();
        //modeUser.setUserId(1305151554415374343L);
        modeUser.setName("铁铲哥2");
        modeUser.setAge(32);
        modeUser.setManagerId(2L);
        boolean result = modeUser.insertOrUpdate();
        System.out.println(result);
    }


    @Test
    public void deleteById() {
        /**
         * 删除不存在的属于成功这需要注意
         */
        ModeUser modeUser = new ModeUser();
        modeUser.setUserId(1305151554415374340L);
        boolean result = modeUser.deleteById();
        System.out.println(result);
    }

}
