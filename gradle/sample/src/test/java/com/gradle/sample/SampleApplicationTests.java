package com.gradle.sample;

import com.gradle.sample.mybatis.model.Player;
import com.gradle.sample.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SampleApplicationTests {

    @Autowired
    PlayerService playerService ;

    @Test
    public void contextLoads() {
        Player player = new Player();
        player.setUid("4");
        player.setName("qin");
        playerService.insertPlayer(player);
    }

}
