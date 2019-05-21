package com.fungo.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.constts.CommonlyConst;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.entity.Member;
import com.fungo.system.proxy.IDeveloperProxyService;
import com.fungo.system.proxy.IGameProxyService;
import com.fungo.system.service.*;
import com.game.common.consts.Setting;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCommentDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMessageDto;
import com.game.common.dto.community.MooMoodDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.enums.FunGoTaskV243Enum;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.date.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class GameProxyImpl implements IGameProxy {
	@Autowired
	private BasNoticeService noticeService;
	@Autowired
	private ScoreLogService scoreLogService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private BasActionDao actionDao;
	@Autowired
	private IPushService pushService;
	@Autowired
	private IGameProxyService gameProxyServiceImpl;

	@Autowired
	private IDeveloperProxyService iDeveloperProxyService;

	@Autowired
	private IGameProxyService iGameProxyService;
	/**
	 * information --> content
	 * 用户通知接口
	 */
	public void addNotice(int eventType, String memberId, String target_id, int target_type, String information,String appVersion,String replyToId) throws Exception {
		String notiveMemberId="";//发送To用户ID
		int msgType=1;//消息类型
		Map<String,Object> date=new HashMap<String,Object>();
		getMemberInfo(memberId,date);
		boolean push = true;
		if(Setting.ACTION_TYPE_LIKE == eventType && Setting.RES_TYPE_POST==target_type) {// 点赞帖子
			// @todo 社区帖子的
			CmmPostDto cmmPostParam = new CmmPostDto();cmmPostParam.setId(target_id);
			CmmPostDto post =   iGameProxyService.selectCmmPostById(cmmPostParam);  //this.postService.selectById(target_id);
			notiveMemberId = post.getMemberId();
			date.put("post_title",post.getTitle() );
			date.put("post_content",reduceString(post.getContent()));
			date.put("post_id", target_id);
			date.put("type", 0);
			date.put("createdAt", DateTools.fmtDate(post.getCreatedAt()));
			msgType = 0;
		}else if(Setting.ACTION_TYPE_LIKE == eventType && Setting.RES_TYPE_COMMENT==target_type){// 点赞评论
			// @todo 社区一级评论
			CmmCommentDto comment= iGameProxyService.selectCmmCommentById(target_id); //this.commentService.selectById(target_id);
			notiveMemberId=comment.getMemberId();
			date.put("comment_content", reduceString(comment.getContent()));
			date.put("comment_id",target_id );
			date.put("type", 1);
			date.put("post_id", comment.getPostId());
			// @todo 社区帖子的
			CmmPostDto cmmPostParam = new CmmPostDto();cmmPostParam.setId(target_id);
			CmmPostDto post = iGameProxyService.selectCmmPostById(cmmPostParam);  //this.postService.selectById(comment.getPostId());
			date.put("post_title",post.getTitle() );
			date.put("post_content",reduceString(post.getContent()));
			msgType = 1;
		}else if(Setting.ACTION_TYPE_LIKE == eventType && Setting.RES_TYPE_EVALUATION==target_type){// 点赞游戏评价
			// @todo 游戏评价
			GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
			gameEvaluationDto.setId(target_id);
			GameEvaluationDto evaluation=  iGameProxyService.selectGameEvaluationById(gameEvaluationDto); //this.gameEvaluationService.selectById(target_id);
			notiveMemberId=evaluation.getMemberId();
			date.put("evaluation_content", evaluation.getContent());
			date.put("evaluation_id", target_id);
			date.put("type", 2);
			date.put("game_id",evaluation.getGameId() );
			// @todo 游戏
			GameDto param = new GameDto();	param.setId(evaluation.getGameId());
			GameDto game= iGameProxyService.selectGameById(param); //this.gameService.selectById(evaluation.getGameId());
			date.put("game_icon", game.getIcon());
			date.put("game_intro", game.getIntro());
			date.put("game_name", game.getName());
			msgType = 2;
		}else if(Setting.ACTION_TYPE_LIKE == eventType && Setting.RES_TYPE_MOOD==target_type){// 点赞心情
			// @todo 心情
			MooMoodDto mood = iGameProxyService.selectMooMoodById(target_id); //this.moodService.selectById(target_id);
			notiveMemberId=mood.getMemberId();
			date.put("mood_id", target_id);
			date.put("mood_content", mood.getContent());
			date.put("type", 7);
			date.put("createdAt", DateTools.fmtDate(new Date()));
			msgType=7;//消息类型
		}else if(Setting.ACTION_TYPE_LIKE == eventType && Setting.RES_TYPE_MESSAGE==target_type) {// 点赞心情评论
			// @todo 心情评论
			MooMessageDto mooMessage = iGameProxyService.selectMooMessageById(target_id); //this.messageServive.selectById(target_id);
			notiveMemberId=mooMessage.getMemberId();
			date.put("reply_content",information);
			date.put("type",11 );
			date.put("message_id",target_id );
			date.put("message_content",mooMessage.getContent());
			date.put("mood_id",mooMessage.getMoodId());
			// @todo 心情
			MooMoodDto mood = iGameProxyService.selectMooMoodById(target_id); // this.moodService.selectById(mooMessage.getMoodId());
			date.put("mood_content", mood.getContent());
			msgType=11;//消息类型
			push = CommonUtils.versionAdapte(appVersion, "2.4.4");
		}else if(Setting.ACTION_TYPE_COMMENT == eventType){// 评论帖子
			//// @todo 社区帖子的
			CmmPostDto cmmPostParam = new CmmPostDto();cmmPostParam.setId(target_id);
			CmmPostDto post = iGameProxyService.selectCmmPostById(cmmPostParam);// this.postService.selectById(target_id);
			notiveMemberId=post.getMemberId();
			date.put("post_id",target_id );
			date.put("post_title", post.getTitle());
			date.put("post_content",reduceString(post.getContent()));
			date.put("comment_content", information);
			date.put("type",3 );
			msgType=3;//消息类型
		}else if(Setting.MSG_TYPE_MOOD == eventType){// 评论心情
			// @todo 心情
			MooMoodDto mood = iGameProxyService.selectMooMoodById(target_id); //this.moodService.selectById(target_id);
			notiveMemberId=mood.getMemberId();
			date.put("mood_id",target_id );
			date.put("mood_content",mood.getContent() );
			date.put("message_content", information);
			date.put("type",8 );//7
			date.put("createdAt", DateTools.fmtDate(new Date()));
			msgType=8;//消息类型
		}else if(Setting.MSG_TYPE_REPLAY_GAME == eventType && Setting.RES_TYPE_COMMENT==target_type){//回复评论
			// // @todo 社区一级评论
			CmmCommentDto comment= iGameProxyService.selectCmmCommentById(target_id); //this.commentService.selectById(target_id);
			notiveMemberId=comment.getMemberId();
			date.put("reply_content",information );
			date.put("type",4 );
			date.put("comment_id", target_id);
			date.put("comment_content", comment.getContent());
			date.put("post_id", comment.getPostId());
			//@todo 社区帖子的
			CmmPostDto cmmPostDto = new CmmPostDto();
			cmmPostDto.setId(target_id);
			CmmPostDto post = iGameProxyService.selectCmmPostById(cmmPostDto);//this.postService.selectById(comment.getPostId());
			date.put("post_title",post.getTitle() );
			date.put("post_content",post.getContent() );
			msgType=4;//消息类型
		}else if(Setting.MSG_TYPE_REPLAY_GAME == eventType && Setting.RES_TYPE_EVALUATION==target_type){//回复游戏评论
			//@todo 游戏评价
			GameEvaluationDto param = new GameEvaluationDto();
			param.setId(target_id);
			GameEvaluationDto evaluation = iGameProxyService.selectGameEvaluationById(param); //this.gameEvaluationService.selectById(target_id);
			notiveMemberId=evaluation.getMemberId();
			date.put("reply_content",information);
			date.put("type",5 );
			date.put("evaluation_id",target_id );
			date.put("evaluation_content",evaluation.getContent());
			date.put("game_id",evaluation.getGameId());
			// @todo 游戏
			GameDto gameDto = new GameDto();
			gameDto.setId(evaluation.getGameId());
			GameDto game= iGameProxyService.selectGameById(gameDto);  //this.gameService.selectById(evaluation.getGameId());
			date.put("game_icon", game.getIcon());
			date.put("game_intro", game.getIntro());
			date.put("game_name", game.getName());
			msgType=5;//消息类型
		}else if(Setting.MSG_TYPE_REPLAY_GAME == eventType && Setting.RES_TYPE_MESSAGE==target_type){//回复心情评论
			//@todo 心情评论
			MooMessageDto mooMessage = iGameProxyService.selectMooMessageById(target_id);  // this.messageServive.selectById(target_id);
			notiveMemberId=mooMessage.getMemberId();
			date.put("reply_content",information);
			date.put("type",9 );
			date.put("message_id",target_id );
			date.put("message_content",mooMessage.getContent());
			date.put("mood_id",mooMessage.getMoodId());
			// @todo 心情
			MooMoodDto mood = iGameProxyService.selectMooMoodById(target_id);  // this.moodService.selectById(mooMessage.getMoodId());
			date.put("mood_content", mood.getContent());
			msgType=9;//消息类型
			push = CommonUtils.versionAdapte(appVersion, "2.4.4");
		}else if(Setting.MSG_TYPE_REPLAY_RE == eventType && Setting.RES_TYPE_COMMENT==target_type) {//回复二级回复-文章评论
			// @todo 二级回复-文章评论
			CmmCommentDto comment= iGameProxyService.selectCmmCommentById(target_id);   //this.commentService.selectById(target_id);
			notiveMemberId=replyToId;
			date.put("reply_content",information );
			date.put("type",12 );
			date.put("comment_id", target_id);
			date.put("comment_content", comment.getContent());
			date.put("post_id", comment.getPostId());
			// @todo 心情
			CmmPostDto cmmPostDto = new CmmPostDto();
			cmmPostDto.setId(target_id);
			CmmPostDto post = iGameProxyService.selectCmmPostById(cmmPostDto); //this.postService.selectById(comment.getPostId());
			date.put("post_title",post.getTitle() );
			date.put("post_content",post.getContent() );
			msgType=12;//消息类型
			push = CommonUtils.versionAdapte(appVersion, "2.4.4");
		}else if(Setting.MSG_TYPE_REPLAY_RE == eventType && Setting.RES_TYPE_EVALUATION==target_type) {//回复二级回复-游戏评论
			//@todo 回复二级回复-游戏评论
			GameEvaluationDto param = new GameEvaluationDto();
			param.setId(target_id);
			GameEvaluationDto evaluation = iGameProxyService.selectGameEvaluationById(param); //this.gameEvaluationService.selectById(target_id);
			notiveMemberId=replyToId;
			date.put("reply_content",information);
			date.put("type",12 );
			date.put("evaluation_id",target_id );
			date.put("evaluation_content",evaluation.getContent());
			date.put("game_id",evaluation.getGameId());
			//@todo 回复二级回复-游戏评论
			GameDto gameDto = new GameDto();
			gameDto.setId(evaluation.getGameId());
			GameDto game = iGameProxyService.selectGameById(gameDto); //this.gameService.selectById(evaluation.getGameId());
			date.put("game_icon", game.getIcon());
			date.put("game_intro", game.getIntro());
			date.put("game_name", game.getName());
			msgType=12;//消息类型
			push = CommonUtils.versionAdapte(appVersion, "2.4.4");
		}else if(Setting.MSG_TYPE_REPLAY_RE == eventType && Setting.RES_TYPE_MESSAGE==target_type) {//回复二级回复-心情评论
			//@todo 二级回复-心情评论
			MooMessageDto mooMessage = iGameProxyService.selectMooMessageById(target_id); //this.messageServive.selectById(target_id);
			notiveMemberId=replyToId;
			date.put("reply_content",information);
			date.put("type",12 );
			date.put("message_id",target_id );
			date.put("message_content",mooMessage.getContent());
			date.put("mood_id",mooMessage.getMoodId());
			// @todo 回复二级回复-心情评论
			MooMoodDto mood = iGameProxyService.selectMooMoodById(target_id); //this.moodService.selectById(mooMessage.getMoodId());
			date.put("mood_content", mood.getContent());
			msgType=12;//消息类型
			push = CommonUtils.versionAdapte(appVersion, "2.4.4");
		}else {
			return ;
		}
		ObjectMapper objectMapper = new ObjectMapper();

		BasNotice notice =new BasNotice();
		notice.setCreatedAt(new Date());
		notice.setData(objectMapper.writeValueAsString(date));
		notice.setIsRead(0);
		notice.setMemberId(notiveMemberId);
		notice.setType(msgType);
		notice.setUpdatedAt(new Date());
		noticeService.insert(notice);
		if(push) {
			pushService.push(notiveMemberId, target_type,appVersion);
		}
	}
	private void getMemberInfo(String memberId,Map<String,Object> map) {
		Member member=this.memberService.selectById(memberId);
		map.put("user_level", member.getLevel());
		map.put("user_avatar", member.getAvatar());
		map.put("user_id", member.getId());
		map.put("user_name", member.getUserName());
	}
	
	//旧版 完成任务加积分接口 已弃用
	public ResultDto<String> addScore(int eventType, String memberId, String target_id, int target_type) throws IOException, ParseException {
		if(Setting.ACTION_TYPE_LOGIN == eventType) {//登录
//			scoreLogService.finishTask(memberId, "LOGIN", "");
		}else if(Setting.ACTION_TYPE_SHARE == eventType && Setting.RES_TYPE_POST==target_type){//分享帖子
			scoreLogService.finishTask(memberId, "POST_SHARE", "");
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_0", "");
		}else if(Setting.ACTION_TYPE_SHARE == eventType && Setting.RES_TYPE_GAME==target_type){//分享游戏
			scoreLogService.finishTask(memberId, "GAME_SHARE", "");
		}else if(Setting.ACTION_TYPE_POST == eventType ){//发布文章
			return scoreLogService.finishTask(memberId, "POST_ADD", "");
		}else if(Setting.ACTION_TYPE_COMMENT == eventType && Setting.RES_TYPE_POST==target_type){//发布帖子
			scoreLogService.finishTask(memberId, "POST_COMMENT", "");
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_0", "");
		}else if(Setting.ACTION_TYPE_COMMENT == eventType && Setting.RES_TYPE_GAME==target_type){//评论游戏
			scoreLogService.finishTask(memberId, "EVALUATION_ADD", "");
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_0", "");
		}/*else if(Setting.ACTION_TYPE_COMMENT == eventType ){//评论

			scoreLogService.finishTask(memberId, "POST_COMMENT", "");
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_0", "");
		}*//*else if(Setting.ACTION_TYPE_COMMENT == eventType && Setting.RES_TYPE_EVALUATION ==target_type) {//游戏评论
			scoreLogService.finishTask(memberId, "EVALUATION_ADD", "");
		}*/else if(Setting.ACTION_TYPE_REPLY == eventType && Setting.RES_TYPE_COMMENT==target_type) {//帖子评论回复
			scoreLogService.finishTask(memberId, "POST_COMMENT", "");
		}else if(Setting.ACTION_TYPE_REPLY == eventType && Setting.RES_TYPE_EVALUATION==target_type) {//游戏评价回复
			scoreLogService.finishTask(memberId, "EVALUATION_REPLY", "");
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_3", "");
		}else if(Setting.ACTION_TYPE_LIKE == eventType && Setting.RES_TYPE_POST==target_type) {//点赞帖子
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_1", "");
		}else if(Setting.ACTION_TYPE_LIKE == eventType && Setting.RES_TYPE_EVALUATION==target_type) {//点赞游戏评价
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_2", "");
		}else if(Setting.ACTION_TYPE_COLLECT == eventType ){//收藏
			scoreLogService.finishTask(getMemberID(target_id,target_type), "HOT_0", "");
		}else if(Setting.ACTION_TYPE_REPORT == eventType ){//举报
			scoreLogService.finishTask(memberId, "REPORT", "");
		}else if(Setting.ACTION_TYPE_PUSH == eventType ){//推荐
			scoreLogService.finishTask(memberId, "EVALUATION_ADD", "");
		}else if(Setting.ACTION_TYPE_UNPUSH == eventType ){//NU推荐
			scoreLogService.finishTask(memberId, "EVALUATION_ADD", "");
		}else if(Setting.ACTION_TYPE_FEEDBACK == eventType ){//反馈
			scoreLogService.finishTask(memberId, "FEEDBACK", "");
		}else if(Setting.ACTION_TYPE_MOOD == eventType) {
			return scoreLogService.finishTask(memberId, "MOOD_ADD", "");
		}

		return null;
	}
	

	//2.4.3之后 完成任务加积分接口
	@Override
	public int addTaskCore(int eventType,String memberId, String target_id, int target_type) throws Exception {
		int count = -1;
		ResultDto<Integer> expTask = null;

		if(Setting.ACTION_TYPE_POST == eventType ) {//发布文章

			expTask = scoreLogService.expTask(memberId, FunGoTaskV243Enum.CM_SEND_ARTICLE_EXP.code(), "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_MOOD == eventType) {//发布心情

			expTask = scoreLogService.expTask(memberId, FunGoTaskV243Enum.CM_SEND_MOOD_EXP.code(), "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_LIKE == eventType) {//点赞他人内容(特殊任务,不走通用方法)

			if(target_type == Setting.RES_TYPE_EVALUATION) {//点赞他人游戏评价

			}else{

			}

		}else if(Setting.ACTION_TYPE_COMMENT == eventType || Setting.ACTION_TYPE_REPLY == eventType) {//评论 回复他人内容

			if(Setting.ACTION_TYPE_REPLY == eventType && Setting.RES_TYPE_EVALUATION == target_type) {//回复游戏评论

				expTask = scoreLogService.expTask(memberId, FunGoTaskV243Enum.GM_REPLY_EXP.code(), "", target_id, target_type);
				scoreLogService.expTask(getMemberID(target_id, target_type), FunGoTaskV243Enum.BTQ_BY_SAY_EXP.code(), "", target_id, target_type);//游戏评论被回复

			}else if(Setting.ACTION_TYPE_COMMENT == eventType && Setting.RES_TYPE_GAME == target_type) {//发表游戏评论

				expTask = scoreLogService.expTask(memberId, FunGoTaskV243Enum.GM_SEND_SAY_EXP.code(), "", target_id, target_type);

			}else if(Setting.ACTION_TYPE_COMMENT == eventType && Setting.RES_TYPE_POST == target_type){//文章被评论

				expTask = scoreLogService.expTask(memberId, FunGoTaskV243Enum.CM_VIEWS_OTHER_EXP.code(), "", target_id, target_type);
				//文章被评论
				scoreLogService.expTask(getMemberID(target_id, target_type), FunGoTaskV243Enum.BTQ_SAY_COLLECT_SHARD_EXP.code(), "", target_id, target_type);

			}else {

				expTask = scoreLogService.expTask(memberId, FunGoTaskV243Enum.CM_VIEWS_OTHER_EXP.code(), "", target_id, target_type);
			}

		}else if(Setting.ACTION_TYPE_SHARE == eventType) {//分享(文章[2] 游戏)

			if(Setting.RES_TYPE_GAME == target_type) {//分享游戏

				expTask = scoreLogService.expTask(memberId, 31, "", target_id, target_type);
				if(expTask.isSuccess())
				scoreLogService.funCoinTask(memberId, 310, "", target_id, target_type);

			}else if(Setting.RES_TYPE_POST == target_type) {//分享文章

				expTask = scoreLogService.expTask(memberId, 24, "", target_id, target_type);
				if(expTask.isSuccess()) {
					scoreLogService.funCoinTask(memberId, 240, "", target_id, target_type);
				}
				scoreLogService.expTask(getMemberID(target_id, target_type), 40, "", target_id, target_type);//被分享
			}

		}else if(Setting.ACTION_TYPE_DOWNLOAD == eventType) {//下载

			expTask = scoreLogService.expTask(memberId, 30, "", target_id, target_type);
			if(expTask.isSuccess())
			scoreLogService.funCoinTask(memberId, 300, "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_BOOK == eventType) { //预约

			expTask = scoreLogService.expTask(memberId, 32, "", target_id, target_type);
			if(expTask.isSuccess())
			scoreLogService.funCoinTask(memberId, 320, "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_FOLLOW == eventType) {//关注用户

			expTask = scoreLogService.expTask(memberId, 13, "", target_id, target_type);
//			scoreLogService.funCoinTask(memberId, icode, "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_COLLECT == eventType && Setting.RES_TYPE_POST == target_type) {//文章被收藏

			expTask = scoreLogService.expTask(getMemberID(target_id, target_type), 40, "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_BIND_APPLE == eventType) {//绑定appleId

			expTask = scoreLogService.expTask(memberId, 11, "", target_id, target_type);
			if(expTask.isSuccess())
			scoreLogService.funCoinTask(memberId, 110, "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_AVATAR == eventType) {//修改头像

			expTask = scoreLogService.expTask(memberId, 14, "", target_id, target_type);
			if(expTask.isSuccess())
			scoreLogService.funCoinTask(memberId, 140, "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_NICKNAME == eventType) {//修改昵称

			expTask = scoreLogService.expTask(memberId, 10, "", target_id, target_type);
			if(expTask.isSuccess()) {
				scoreLogService.funCoinTask(memberId, 100, "", target_id, target_type);
			}

		}else if(Setting.ACTION_TYPE_INTRO == eventType) {//修改简介

			expTask = scoreLogService.expTask(memberId, 15, "", target_id, target_type);
			if(expTask.isSuccess())
			scoreLogService.funCoinTask(memberId, 150, "", target_id, target_type);

		}else if(Setting.ACTION_TYPE_BIND_THIRDPARTY == eventType) {//绑定第三方账号

			expTask = scoreLogService.expTask(memberId, 12, "", target_id, target_type);
			if(expTask.isSuccess()) {
				scoreLogService.funCoinTask(memberId, 120, "", target_id, target_type);
			}

		}else if(Setting.ACTION_TYPE_REPORT == eventType) { //举报

			expTask = scoreLogService.expTask(memberId, FunGoTaskV243Enum.CM_REPORT_ILLEGAL_EXP.code(), "", target_id, target_type);

		}
		if(expTask != null) {
			if(expTask.isSuccess()) {
				return expTask.getData();
			}
		}
		
		return  1 ;//count;
	}

	/**
	 * 被点赞用户的id
	 * # 迁移变动
	 * @param target_id
	 * @param target_type
	 * @return
	 */
	@Override
	public String getMemberID(String target_id ,int target_type ) {
		Map<String,String> map =new HashMap<String,String>();
		map.put("tableName", getTableName(target_type));
		map.put("id",target_id);
		if (CommonlyConst.getCommunityList().contains(target_type)){
//            社区服务空缺 19-05-07
			if (false){
				// @todo 社区
//				被点赞用户的id
				return iDeveloperProxyService.getMemberIdByTargetId(map); //  communityFeignClient.getMemberIdByTargetId(map);
			}
//            return false;
		}
		if (CommonlyConst.getGameList().contains(target_type)){
//            feign客户端调用游戏服务 被点赞用户的id
			// @todo 社区
			return iDeveloperProxyService.getMemberIdByTargetId(map);
		}
		if (CommonlyConst.getSystemList().contains(target_type)){
			return this.actionDao.getMemberIdByTargetId(map);
		}
		return  "";
	}
	
	//根据资源类型获取表名
	private String getTableName(int type) {
		if(type==0) {
			return "t_member";
		}else if(type==1) {
			return "t_cmm_post";
		}else if(type==2) {
			return "t_moo_mood";
		}else if(type==3) {
			return "t_game";
		}else if(type==4) {
			return "t_cmm_community";
		}else if(type==5) {
			return "t_cmm_comment";
		}else if(type==6) {
			return "t_game_evaluation";
		}else if(type==7) {
			return "t_reply";
		}else if(type==8) {
			return "t_moo_message";
		}else if(type==9) {
			return "t_bas_feedback";
		}
		return null;
	}
	
	public String reduceString(String content) {
		
		if(CommonUtil.isNull(content)) {
			return "";
		}
		return content.length()>200?content.substring(0, 200)+"......[省略]":content;
		
	}
	
	//完成任务后返回的消息
	public String taskReturnString(int code,String notice) {
		if(code > 0) {
			return notice;
		}else {
			return notice;
		}
	}
	
	

}
