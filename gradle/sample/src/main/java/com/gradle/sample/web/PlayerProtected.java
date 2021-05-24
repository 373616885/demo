package com.gradle.sample.web;

import com.gradle.sample.mybatis.client.PlayerMapper;
import com.gradle.sample.mybatis.model.Player;
import com.gradle.sample.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author qinjp
 * @date 2019-05-21
 **/
@Component
public class PlayerProtected {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PlayerMapper playerMapper;

    @Async
    //@Transactional(rollbackFor = Exception.class)
    void deletePlayer(Player player) {
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        System.out.println("protected deletePlayer start");
        playerMapper.deleteByPrimaryKey(player.getUid());
        applicationContext.getBean(PlayerService.class).insertPlayer(player);
        System.out.println("protected deletePlayer end");
    }
}
