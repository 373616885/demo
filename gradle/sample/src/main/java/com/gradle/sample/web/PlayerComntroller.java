package com.gradle.sample.web;

import com.gradle.sample.mybatis.model.Player;
import com.gradle.sample.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author qinjp
 **/
@Slf4j
@RestController
public class PlayerComntroller {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PlayerService playerService;

    @Autowired
    private PlayerProtected playerProtected;

    private static final String SUCCESS = "SUCCESS";

    @RequestMapping("/insert")
    public String insertPlayer(){
        Player player = new Player();
        player.setUid("4");
        player.setName("qin");
        playerService.insertPlayer(player);
        return SUCCESS;
    }

    @RequestMapping("/protected")
    public String protectedPlayer(){
        Player player = new Player();
        player.setUid("5");
        player.setName("jie");
        playerProtected.deletePlayer(player);
        return "protected";
    }
}
