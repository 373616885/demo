package com.gradle.sample.web;

import com.gradle.sample.mybatis.model.Player;
import com.gradle.sample.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author qinjp
 **/
@Slf4j
@RestController
public class PlayerComntroller {

    @Autowired
    PlayerService playerService;

    @RequestMapping("/insert")
    public String insertPlayer(){
        log.warn("==== statr ===");
        Player player = new Player();
        player.setUid("4");
        player.setName("qin");
        playerService.insertPlayer(player);
        return "success";
    }
}
