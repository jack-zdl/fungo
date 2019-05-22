package com.fungo.games.service;


import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.FollowUserOutBean;
import com.game.common.dto.mark.BindingAppleInputBean;
import com.game.common.dto.mark.InviteInput;
import com.game.common.dto.mark.MakeCheckOut;
import com.game.common.dto.mark.MakeInputPageDto;

public interface IMakeAndInviteGameService {
	
	/**
	 * 预约游戏
	 * @param memberId
	 * @param gameId
	 * @param phoneModel
	 * @return
	 * @throws Exception
	 */
	public ResultDto<String> makGame(String memberId, String gameId, String phoneModel) throws Exception;

	/**
	 * 取消预约
	 * @param memberId
	 * @param gameId
	 * @param phoneModel
	 * @return
	 */
	public ResultDto<String> unmakeGame(String memberId, String gameId, String phoneModel);

	/**
	 * 预约游戏绑定appleId
	 * @param memberId
	 * @param makeInput
	 * @return
	 */
	public ResultDto<String> bindingAppleID(String memberId, BindingAppleInputBean makeInput);


	/**
	 * 获取用该用对游戏的预约状态
	 * @param memberId 用户id
	 * @param gameId 游戏id
	 * @param phoneModel 手机类型 iOS Android
	 * @return
	 */
	public ResultDto<MakeCheckOut> getgameCheck(String memberId, String gameId, String phoneModel);

	/**
	 * 同意条款
	 * @param memberId
	 * @param gameId
	 * @param phoneModel
	 * @return
	 */
	public ResultDto<String> getgameAgree(String memberId, String gameId, String phoneModel);

	/**
	 *
	 * @param memberId
	 * @param inputPageDto
	 * @return
	 * @throws Exception
	 */
	public FungoPageResultDto<FollowUserOutBean> getInviteUserList (String memberId, MakeInputPageDto inputPageDto) throws Exception;



}
