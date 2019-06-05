package com.qin.mybatis.demo.web;

import com.qin.mybatis.demo.mybatis.entity.Player;
import com.qin.mybatis.demo.service.PlayerService;
import com.qin.mybatis.demo.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qinjp
 * @date 2019-06-05
 **/
@RestController
public class SampleController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    SampleService sampleService;


    @RequestMapping("/sample")
    public String sample(){
        Player player = new Player();
        player.setUid("5");
        player.setName("jie");
        sampleService.sample();
        return "success";
    }
}
