package com.fungo.community.dao.mapper;


import com.fungo.community.entity.CmmPostCircle;

public interface CmmPostCircleMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmmPostCircle record);

    int insertSelective(CmmPostCircle record);

    CmmPostCircle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmPostCircle record);

    int updateByPrimaryKey(CmmPostCircle record);

    String getCmmCircleByPostId(String id);
}