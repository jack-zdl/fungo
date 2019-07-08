package com.fungo.community.dao.mapper;


import com.fungo.community.entity.CmmCircleGame;

public interface CmmCircleGameMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmmCircleGame record);

    int insertSelective(CmmCircleGame record);

    CmmCircleGame selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmCircleGame record);

    int updateByPrimaryKey(CmmCircleGame record);
}