package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.GameSurveyRel;
import com.game.common.dto.game.GameSurveyRelDto;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 游戏测试会员关联表 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-08
 */
@Repository
public interface GameSurveyRelDao extends BaseMapper<GameSurveyRel> {

    /**
     * 功能描述:
     * 1 查询进入测试阶段的游戏
     * 2 根据游戏id查询预约过的用户
     * 3 发送信息给用户
     * @auther: dl.zhang
     * @date: 2019/7/29 18:23
     */
    List<GameSurveyRelDto> getMemberNoticeByGame();

    Integer updateMemberNoticeBatch(@Param("ids") List<String> ids);
}