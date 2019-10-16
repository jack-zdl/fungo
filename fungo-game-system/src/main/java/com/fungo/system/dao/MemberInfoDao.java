package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.system.entity.Member;
import com.fungo.system.entity.MemberInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberInfoDao extends BaseMapper<MemberInfo> {

    /**
     * 功能描述: 根据邀请人id查询集合
     * @auther: dl.zhang
     * @date: 2019/10/15 19:45
     */
    List<MemberInfo> selectListByRecommendId();
}