package com.fungo.community.dao.mapper;


import com.fungo.community.entity.CmmCircle;
import org.apache.ibatis.annotations.Param;

public interface CmmCircleMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmmCircle record);

    int insertSelective(CmmCircle record);

    CmmCircle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmCircle record);

    int updateByPrimaryKey(CmmCircle record);

    String selectCircleByGameId(@Param("gameId") String gameId);
}