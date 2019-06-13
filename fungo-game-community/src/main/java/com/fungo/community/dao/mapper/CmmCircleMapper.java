package com.fungo.community.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.fungo.community.entity.BasVideoJob;
import com.fungo.community.entity.CmmCircle;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmmCircleMapper  extends BaseMapper<CmmCircle> {
    int deleteByPrimaryKey(String id);

    List<CmmCircle> selectPageByKeyword(Pagination page,String keyword);

    int insertSelective(CmmCircle record);

    CmmCircle selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(CmmCircle record);

    int updateByPrimaryKey(CmmCircle record);
}