package com.fungo.community.dao.mapper;


import com.fungo.community.entity.CmmPost;
import com.fungo.community.entity.CmmPostCircle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmmPostCircleMapper {
    int deleteByPrimaryKey(String id);

    int insert(CmmPostCircle record);

    int insertSelective(CmmPostCircle record);

    CmmPostCircle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmPostCircle record);

    int updateByPrimaryKey(CmmPostCircle record);

    String getCmmCircleByPostId(String postId);


}