package com.qin.mybatis.demo.mybatis.client;

import com.qin.mybatis.demo.mybatis.entity.Player;

public interface PlayerMapper {

    int deleteByPrimaryKey(String uid);

    int insert(Player record);

    int insertSelective(Player record);

    Player selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(Player record);

    int updateByPrimaryKey(Player record);
}