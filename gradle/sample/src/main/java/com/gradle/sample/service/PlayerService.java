package com.gradle.sample.service;

import com.gradle.sample.mybatis.client.PlayerMapper;
import com.gradle.sample.mybatis.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * @Author qinjp
 **/
@Service
public class PlayerService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PlayerMapper playerMapper;

    //@Transactional(rollbackFor = Exception.class)
    public void insertPlayer(Player player){
        playerMapper.insert(player);
    }



    //@Transactional(rollbackFor = Exception.class)
    public void deletePlayer(Player player) {
        System.out.println("deletePlayer start");
        playerMapper.deleteByPrimaryKey(player.getUid());
        applicationContext.getBean(PlayerService.class).insertPlayer(player);
        System.out.println("deletePlayer end");
    }
}
