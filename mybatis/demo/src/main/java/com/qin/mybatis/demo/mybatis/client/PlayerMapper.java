package com.qin.mybatis.demo.mybatis.client;

import com.gradle.sample.mybatis.model.Player;

public interface PlayerMapper {
    int deleteByPrimaryKey(String uid);

    int insert(Player record);

    int insertSelective(Player record);

    Player selectByPrimaryKey(String uid);

    int updateByPrimaryKeySelective(Player record);

    int updateByPrimaryKey(Player record);
}