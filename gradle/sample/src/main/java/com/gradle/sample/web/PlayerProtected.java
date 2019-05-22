package com.gradle.sample.web;

import com.gradle.sample.mybatis.client.PlayerMapper;
import com.gradle.sample.mybatis.model.Player;
import com.gradle.sample.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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


    @Transactional(rollbackFor = Exception.class)
    protected void deletePlayer(Player player) {
        System.out.println("protected deletePlayer start");
        playerMapper.deleteByPrimaryKey(player.getUid());
        applicationContext.getBean(PlayerService.class).insertPlayer(player);
        System.out.println("protected deletePlayer end");
    }
}
