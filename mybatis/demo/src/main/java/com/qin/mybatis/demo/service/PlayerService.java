package com.qin.mybatis.demo.service;

import com.qin.mybatis.demo.mybatis.client.PlayerMapper;
import com.qin.mybatis.demo.mybatis.entity.Player;
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
    public void insertPlayer(Player player){
        playerMapper.deleteByPrimaryKey(player.getUid());
        playerMapper.insert(player);
    }


}
