package com.fungo.community.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.fungo.community.entity.CmmCircle;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CmmCircleMapper  extends BaseMapper<CmmCircle> {
    int deleteByPrimaryKey(String id);

    List<CmmCircle> selectPageByKeyword(Pagination page, @Param("keyword")  String keyword);

    int insertSelective(CmmCircle record);

    int updateByPrimaryKeySelective(CmmCircle record);

    int updateByPrimaryKey(CmmCircle record);

    String selectCircleByGameId(@Param("gameId") String gameId);
}