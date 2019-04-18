package com.fungo.system.service;

import com.fungo.api.FungoPageResultDto;
import com.fungo.api.InputPageDto;
import com.fungo.api.ResultDto;
import com.fungo.system.dao.FollowInptPageDao;
import com.fungo.system.dto.*;
import com.fungo.system.entity.AuthorBean;

import java.util.List;
import java.util.Map;

public interface IMemberService {
	//获取我的收藏
	public FungoPageResultDto<CollectionOutBean> getCollection(String memberId, InputPageDto inputPage);

	/**
	 * 获取用户关注
	 * @param memberId 当前登录用户id
	 * @param memberId2 目标用户id
	 * @param inputPage
	 * @return
	 * @throws Exception
	 */
	public FungoPageResultDto<Map<String,Object>> getFollower(String memberId, String memberId2, FollowInptPageDao inputPage) throws Exception;
	/**
	 * 获取用户粉丝
	 * @param memberId 当前登录用户id
	 * @param memberId2 目标用户id
	 * @param inputPage
	 * @return
	 * @throws Exception
	 */
	public FungoPageResultDto<Map<String,Object>> getFollowee(String memberId, String memberId2, InputPageDto inputPage) throws Exception;
	//getHistory
	public FungoPageResultDto<Map<String,Object>> getHistory(String memberId, InputPageDto inputPage);

	//获取点赞我的
	public FungoPageResultDto<Map<String,Object>> getLikeNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception;
	//获取评论我的
	public  FungoPageResultDto<Map<String,Object>> getCommentNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception;


	//获取我的未读消息
	public  Map<String,Object>  getUnReadNotice(String memberId, String appVersion);

	//获取系统消息
	public  FungoPageResultDto<SysNoticeBean> getSystemNotice(String memberId, InputPageDto inputPage);

	//获取我的时间线
	public FungoPageResultDto<Map<String,Object>> getTimeLine(String memberId);

	//获取用户动态
	public ResultDto<String> getFeed(String memberId);
	//获取用户资料
	public ResultDto<String> getUserCard(String memberId, String cardId);

	//获取用户测试游戏
	public FungoPageResultDto<MyGameBean> getGameList(String memberId, MyGameInputPageDto inputPage, String os);
	
	//用户信息
	ResultDto<AuthorBean> getUserInfo(String memberId);
	//用户信息 (pc)
	public ResultDto<AuthorBean> getUserInfoPc(String memberId);
	//用户发布统计
	public ResultDto<Map<String,Integer>> getPublishCount(String loginId);
	//用户游戏评论
	public FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(String loginId, InputPageDto input) throws Exception;
//	//用户帖子
//	public FungoPageResultDto<MyPublishBean> getMyPosts(String loginId, InputPageDto input) throws Exception;
//	//用户心情
//	public FungoPageResultDto<MyPublishBean> getMyMoods(String loginId, InputPageDto input) throws Exception;
//	//用户等级信息
//	public ResultDto<MemberLevelBean> getMemberLevel(String loginId);
//	//用户回复列表
//	public FungoPageResultDto<MyCommentBean> getMyComments(String loginId, InputPageDto input);
//	public void initRank() throws Exception;
//	//获取用户等级图标
//	String getLevelRankUrl(int level, List<IncentRuleRank> levelRankList);

	


}
