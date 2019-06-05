package com.qin.mybatis.demo.web;

import com.qin.mybatis.demo.mybatis.entity.Player;
import com.qin.mybatis.demo.service.PlayerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2019-06-05
 **/
@RestController
public class SampleController {

    private PlayerService playerService;

    @RequestMapping("/sample")
    public String sample(){
        Player player = new Player();
        player.setUid("5");
        player.setName("jie");
        playerService.insertPlayer(player);
        return "success";
    }
}
