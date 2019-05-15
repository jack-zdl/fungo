package com.fungo.system.proxy;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameOutBean;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.GameReleaseLogDto;
import com.game.common.dto.game.GameSurveyRelDto;

import java.util.List;
import java.util.Map;

public interface IDeveloperProxyService {

	/**
	 * 开发者游戏列表
	 * @param collect
	 * @return
	 */
	FungoPageResultDto<GameOutBean> gameList(List<String> collect,int page, int limit );

	List<GameReleaseLogDto> selectGameReleaseLog(GameReleaseLogDto gameReleaseLog);

	ResultDto<String> addGameTag(List<String> tags, String categoryId, String gameId);

	boolean updateCounter(Map<String, String> map);

	boolean updatecommunityCounter(Map<String, String> map);


	GameDto selectGame(String gameId);

	int selectCount(  GameSurveyRelDto gameSurveyRel);

	int selectGameEvaluationCount(  GameEvaluationDto gameEvaluation);

	int selectPostCount(CmmPostDto cmmPostDto);

    String getMemberIdByTargetId(Map<String, String> map);
}

