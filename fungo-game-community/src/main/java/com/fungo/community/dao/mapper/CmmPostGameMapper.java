package com.fungo.community.dao.mapper;

import com.fungo.community.entity.CmmPostGame;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CmmPostGameMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmmPostGame record);

    int insertSelective(CmmPostGame record);

    CmmPostGame selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmPostGame record);

    int updateByPrimaryKey(CmmPostGame record);

    String getCommunityIdByPostId(@Param("postId") String id);

    List<String> listGameIds(@Param("postId") String postId);
}