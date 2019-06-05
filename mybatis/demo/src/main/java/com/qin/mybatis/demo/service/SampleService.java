package com.qin.mybatis.demo.service;

import com.qin.mybatis.demo.mybatis.entity.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author qinjp
 * @date 2019-06-05
 **/
@Service
public class SampleService {

    @Autowired
    private PlayerService playerService;


    public String sample(){
        Player player = new Player();
        player.setUid("5");
        player.setName("jie");
        playerService.insertPlayer(player);
        return "success";
    }
}
