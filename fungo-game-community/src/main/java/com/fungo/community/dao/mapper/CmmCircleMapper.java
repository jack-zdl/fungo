package com.fungo.community.dao.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.fungo.community.dto.PostCircleDto;
import com.fungo.community.entity.CmmCircle;
import com.game.common.dto.ResultDto;
import com.game.common.vo.CmmCircleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface CmmCircleMapper  extends BaseMapper<CmmCircle> {
    int deleteByPrimaryKey(String id);

    List<CmmCircle> selectPageByKeyword(Pagination page,  CmmCircleVo cmmCircleVo);

    List<CmmCircle> selectPageByIds(Pagination page,@Param("sortType") String sortType ,@Param("ids")  List<String> ids);

    
    int insertSelective(CmmCircle record);

    int updateByPrimaryKeySelective(CmmCircle record);

    int updateByPrimaryKey(CmmCircle record);

    String selectCircleByGameId(@Param("gameId") String gameId);

    List<CmmCircle> selectCircleByGame(Pagination page,@Param("gameId") String gameId);


    List<String> listCircleNameByPost(@Param("postId")String postId);

    List<CmmCircle> selectCircleByPostId(@Param("postId") String postId);

    PostCircleDto getPostCircleDtoById(@Param("id")String id);

}