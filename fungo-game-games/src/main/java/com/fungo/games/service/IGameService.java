package com.fungo.games.service;


import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.*;

import java.util.List;

public interface IGameService {

	/**
	 * 游戏详情
	 * @param gameId 游戏id
	 * @param memberId 用户id
	 * @param ptype 平台类型 iOS Android
	 * @return
	 * @throws Exception
	 */
	public ResultDto<GameOut> getGameDetail(String gameId, String memberId, String ptype) throws Exception;
	
	/**
	 * 游戏列表
	 * @param gameInputDto
	 * @param memberId
	 * @param os 平台类型 iOS Android
	 * @return
	 */
	public FungoPageResultDto<GameOutPage> getGameList(GameInputPageDto gameInputDto, String memberId, String os);
	
	/**
	 * 官方分类标签
	 * @return
	 */
	public ResultDto<List<TagOutPage>> getGameTags();

	/**
	 * 用户最近评论的游戏
	 * @param userId
	 * @param input
	 * @return
	 */
	public FungoPageResultDto<GameOutPage> recentEvaluatedGamesByMember(String userId, InputPageDto input);

	/**
	 * 游戏合集下的游戏内容
	 * @param memberId
	 * @param input
	 * @param os
	 * @return
	 */
	public FungoPageResultDto<GameItem> getGameItems(String memberId, GameItemInput input, String os);

	/**
	 * 游戏综合评分
	 * @param gameId
	 * @return
	 */
	double getGameRating(String gameId);
}
