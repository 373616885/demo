package com.gradle.sample.service;

import com.gradle.sample.mybatis.client.PlayerMapper;
import com.gradle.sample.mybatis.model.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author qinjp
 **/
@Service
public class PlayerService {

    @Autowired
    PlayerMapper playerMapper;

    public void insertPlayer(Player player){
        playerMapper.deleteByPrimaryKey(player.getUid());
        playerMapper.insert(player);
    }
}
