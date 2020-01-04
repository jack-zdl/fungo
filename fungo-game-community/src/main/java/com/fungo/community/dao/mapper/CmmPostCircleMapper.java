package com.fungo.community.dao.mapper;


import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dto.PostCircleDto;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmPostCircle;
import com.game.common.dto.circle.CircleMemberPulishDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
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

    CmmCircle getCircleEntityByPostId(String postId);


    List<PostCircleDto> getCircleEntityByPostIds(@Param("postIds") List<String> postIds);
    /**
     * 功能描述: 根据文章id查询当前默认关联的圈子
     * @auther: dl.zhang
     * @date: 2019/7/13 15:16
     */
    CmmCircle getCircleByPostId(String postId);

    /**
     * 功能描述:  根据非游戏圈子查询玩家榜
     * @param: [circleId] 圈子id
     * @return: java.util.List<com.game.common.dto.circle.CircleMemberPulishDto>
     * @auther: dl.zhang
     * @date: 2019/6/26 17:29
     */
    List<CircleMemberPulishDto> getCmmPostCircleByCircleId(Page page, @Param("circleId") String circleId);

    /**
     * 功能描述: 根据游戏圈子查询玩家榜
     * @param: [circleId]
     * @return: java.util.List<com.game.common.dto.circle.CircleMemberPulishDto>
     * @auther: dl.zhang
     * @date: 2019/6/26 17:32
     */
    List<CircleMemberPulishDto> getCmmPostCircleOfGameByCircleId(@Param("circleId") String circleId);

    /**
     * 功能描述:  根据非游戏圈子查询玩家榜
     * @param: [circleId] 圈子id
     * @return: java.util.List<com.game.common.dto.circle.CircleMemberPulishDto>
     * @auther: dl.zhang
     * @date: 2019/6/26 17:29
     */
    int getSumByCircleId(@Param("circleId") String circleId);

    /**
     * 功能描述: 根据传入的文章id 集合删除文章-圈子记录
     * @auther: dl.zhang
     * @date: 2019/7/9 17:32
     */
    int deletePostCircleByPostIds(@Param("ids")  List<String> ids);

    int countCircleIdPostId(@Param("postId") String postId,@Param("circleId") String circleId);

}