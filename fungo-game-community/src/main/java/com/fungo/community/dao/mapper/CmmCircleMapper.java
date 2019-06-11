package com.fungo.community.dao.mapper;


import com.fungo.community.entity.CmmCircle;

public interface CmmCircleMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmmCircle record);

    int insertSelective(CmmCircle record);

    CmmCircle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmCircle record);

    int updateByPrimaryKey(CmmCircle record);
}