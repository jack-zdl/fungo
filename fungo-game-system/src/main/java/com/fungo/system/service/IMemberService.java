package com.fungo.system.service;

import com.fungo.system.dto.FollowInptPageDao;
import com.fungo.system.dto.*;
import com.fungo.system.entity.IncentRuleRank;
import com.game.common.api.InputPageDto;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MyCommentBean;
import com.game.common.dto.community.MyPublishBean;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IMemberService {
	//获取我的收藏
	FungoPageResultDto<CollectionOutBean> getCollection(String memberId, InputPageDto inputPage);

	/**
	 * 获取用户关注
	 * @param memberId 当前登录用户id
	 * @param memberId2 目标用户id
	 * @param inputPage
	 * @return
	 * @throws Exception
	 */
	FungoPageResultDto<Map<String,Object>> getFollower(String memberId, String memberId2, FollowInptPageDao inputPage) throws Exception;
	/**
	 * 获取用户粉丝
	 * @param memberId 当前登录用户id
	 * @param memberId2 目标用户id
	 * @param inputPage
	 * @return
	 * @throws Exception
	 */
	FungoPageResultDto<Map<String,Object>> getFollowee(String memberId, String memberId2, InputPageDto inputPage) throws Exception;
	//getHistory
	FungoPageResultDto<Map<String,Object>> getHistory(String memberId, InputPageDto inputPage);

	//获取点赞我的
	FungoPageResultDto<Map<String,Object>> getLikeNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception;
	//获取评论我的
	FungoPageResultDto<Map<String,Object>> getCommentNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception;


	//获取我的未读消息
	Map<String,Object>  getUnReadNotice(String memberId, String appVersion);

	//获取我的未读消息 Pc2.0
	Map<String,Object>  getNewUnReadNotice(String memberId,String os, String appVersion);

	//获取系统消息
	FungoPageResultDto<SysNoticeBean> getSystemNotice(String os ,String memberId, InputPageDto inputPage);

	//获取我的时间线
	FungoPageResultDto<Map<String,Object>> getTimeLine(String memberId);

	//获取用户动态
	ResultDto<String> getFeed(String memberId);
	//获取用户资料
	ResultDto<String> getUserCard(String memberId, String cardId);

	//获取用户测试游戏
	FungoPageResultDto<MyGameBean> getGameList(String memberId, MyGameInputPageDto inputPage, String os);

	//用户信息
	ResultDto<AuthorBean> getUserInfo(String memberId);
	//用户信息 (pc)
	ResultDto<AuthorBean> getUserInfoPc(String memberId);
	//用户发布统计
	ResultDto<Map<String,Integer>> getPublishCount(String loginId);
	//用户游戏评论
	FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(String loginId, InputPageDto input) throws Exception;
	//用户帖子
	FungoPageResultDto<MyPublishBean> getMyPosts(String loginId,String memberId, InputPageDto input) throws Exception;
	//用户心情
	FungoPageResultDto<MyPublishBean> getMyMoods(String loginId, InputPageDto input) throws Exception;
	//用户等级信息
	ResultDto<MemberLevelBean> getMemberLevel(String loginId);
	//用户回复列表
	FungoPageResultDto<MyCommentBean> getMyComments(String loginId, String memberId, InputPageDto input);
	void initRank() throws Exception;
	//获取用户等级图标
	String getLevelRankUrl(int level, List<IncentRuleRank> levelRankList);
	//查询当前用户的中秋抽奖权限
	ResultDto<String> getLotteryPermission(String memberId);

	ResultDto<String> checkAndUpdateUserRecommend();

	void checkTwoUserRecommend();

	ResultDto<InviteInfoVO> getInviteInfo(String memberId);

	boolean addNotice(String memberId,String content);

	boolean addActionTypeNotice(String memberId,String content,String actionType);

	boolean getNewMember(Date registerDate, Integer level);

	boolean getActiveMemeber(String memberId);

	void checkAllUserName();

}
