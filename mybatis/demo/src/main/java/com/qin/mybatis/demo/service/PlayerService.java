package com.qin.mybatis.demo.service;

import com.qin.mybatis.demo.mybatis.client.PlayerMapper;
import com.qin.mybatis.demo.mybatis.client.RechargeMapper;
import com.qin.mybatis.demo.mybatis.entity.Player;
import com.qin.mybatis.demo.mybatis.entity.Recharge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @Author qinjp
 **/
@Service
//@Transactional(rollbackFor = Exception.class)
public class PlayerService {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    PlayerMapper playerMapper;

    @Autowired
    RechargeMapper rechargeMapper;

    public void insertPlayer(Player player){
        Player recharge = playerMapper.selectByUidAndName(player.getUid(),player.getName());
        if (Objects.nonNull(recharge)) {
            System.out.println(recharge.toString());
        }
        playerMapper.deleteByPrimaryKey(player.getUid());
        playerMapper.insert(player);
    }


}
