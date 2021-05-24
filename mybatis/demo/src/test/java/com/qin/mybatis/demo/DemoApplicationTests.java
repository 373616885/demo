package com.qin.mybatis.demo;

import com.qin.mybatis.demo.mybatis.entity.Player;
import com.qin.mybatis.demo.service.PlayerService;
import com.qin.mybatis.demo.service.SampleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    PlayerService playerService;

    @Autowired
    SampleService sampleService;

    @Test
    public void contextLoads() {
        Player player = new Player();
        player.setUid("4");
        player.setName("qin");
        sampleService.sample();
    }

}
