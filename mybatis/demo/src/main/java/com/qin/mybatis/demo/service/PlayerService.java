package com.qin.mybatis.demo.service;

import com.qin.mybatis.demo.mybatis.client.PlayerMapper;
import com.qin.mybatis.demo.mybatis.client.RechargeMapper;
import com.qin.mybatis.demo.mybatis.entity.Player;
import com.qin.mybatis.demo.mybatis.entity.Recharge;
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

    @Autowired
    RechargeMapper rechargeMapper;

    //@Transactional(rollbackFor = Exception.class)
    void insertPlayer(Player player){
        Recharge recharge = rechargeMapper.selectByPrimaryKey(1);
        System.out.println(recharge.toString());
        playerMapper.deleteByPrimaryKey(player.getUid());
        playerMapper.insert(player);
    }


}
