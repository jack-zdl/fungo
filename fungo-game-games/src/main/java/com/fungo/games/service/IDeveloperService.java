package com.fungo.games.service;



import com.game.common.dto.DeveloperGame.DeveloperGamePageInput;
import com.game.common.dto.DeveloperGame.DeveloperQueryIn;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.AddGameInputBean;
import com.game.common.dto.game.GameHistoryOut;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IDeveloperService {
	/**
	 * 开发者上传游戏
	 * @param memberUserPrefile
	 * @param input
	 * @return
	 */
//	public ResultDto<String> addGame(MemberUserProfile memberUserPrefile, AddGameInputBean input);

	/**
	 * 开发者更新游戏
	 * @param memberUserPrefile
	 * @param input
	 * @return
	 */
//	public ResultDto<String> updateGame(MemberUserProfile memberUserPrefile, AddGameInputBean input);

	/**
	 * 开发者游戏详情
	 * @param gameId
	 * @param userId
	 * @return
	 */
//	public ResultDto<DeveloperGameOut> gameDetail(String gameId, String userId);

	/**
	 * 开发者游戏列表
	 * @param input
	 * @param userId
	 * @return
	 */
//	public FungoPageResultDto<GameOutBean> gameList(DeveloperGamePageInput input, String userId);

	/**
	 * 游戏更新历史
	 * @param userId
	 * @param input
	 * @return
	 */
	public FungoPageResultDto<GameHistoryOut> gameHistory(String userId,
														  DeveloperGamePageInput input);


	/**
	 * 游戏统计数据
	 * @param input
	 * @return
	 */
	public ResultDto<List<Map<String, Object>>> gemeAnalyzeLog(DeveloperQueryIn input);

	/**
	 * 开发者消息(作废)
	 * @return
	 */
//	public ResultDto<List<Map<String, Object>>> messageList();

	/**
	 * 社区统计数据
	 * @param input
	 * @return
	 * @throws ParseException
	 */
//	public ResultDto<Map<String, Integer>> communityAnalyze(DeveloperQueryIn input) throws ParseException;

	/**
	 * 检查是否有权限
	 * @param memberId
	 * @return
	 */
//	public boolean checkDpPower(String memberId);
}
