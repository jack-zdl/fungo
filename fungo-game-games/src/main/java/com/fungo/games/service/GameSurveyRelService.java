package com.fungo.games.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.games.entity.GameSurveyRel;
import com.game.common.dto.game.GameSurveyRelDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 游戏测试会员关联表 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-06-08
 */
public interface GameSurveyRelService extends IService<GameSurveyRel> {

    List<GameSurveyRelDto> getMemberNoticeByGame();


}
