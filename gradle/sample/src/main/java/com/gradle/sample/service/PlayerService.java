package com.gradle.sample.service;

import com.gradle.sample.mybatis.client.PlayerMapper;
import com.gradle.sample.mybatis.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author qinjp
 **/
@Service
public class PlayerService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PlayerMapper playerMapper;

    @Transactional(rollbackFor = Exception.class)
    void insertPlayer(Player player){
        playerMapper.deleteByPrimaryKey(player.getUid());
        playerMapper.insert(player);
    }

    @Transactional
    public void println() {
        System.out.println("1");
    }

    public void insert(Player player) {
        applicationContext.getBean(PlayerService.class).insertPlayer(player);
    }

}
