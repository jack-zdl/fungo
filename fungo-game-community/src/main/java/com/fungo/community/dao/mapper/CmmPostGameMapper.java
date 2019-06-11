package com.fungo.community.dao.mapper;

import com.fungo.community.entity.CmmPostGame;


public interface CmmPostGameMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmmPostGame record);

    int insertSelective(CmmPostGame record);

    CmmPostGame selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmPostGame record);

    int updateByPrimaryKey(CmmPostGame record);
}