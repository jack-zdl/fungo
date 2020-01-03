package com.game.common.consts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Setting {
	
	/**
	 * 用户行为类型
	 */
	public static final int ACTION_TYPE_LIKE = 0;// 点赞
	public static final int ACTION_TYPE_REPLY = 1;// 回复
	public static final int ACTION_TYPE_COMMENT = 2;// 评论
	public static final int ACTION_TYPE_SHARE = 3;//分享
	public static final int ACTION_TYPE_COLLECT = 4;//收藏
	public static final int ACTION_TYPE_FOLLOW = 5;//关注
	public static final int ACTION_TYPE_REPORT = 6;//
	public static final int ACTION_TYPE_DOWNLOAD = 7;//下载
	public static final int ACTION_TYPE_PUSH = 8;//
	public static final int ACTION_TYPE_UNPUSH = 9;//
	public static final int ACTION_TYPE_IGNORE = 10;//
	public static final int ACTION_TYPE_POST = 11;// 发帖
	public static final int ACTION_TYPE_BROWSE = 12;//浏览
	public static final int ACTION_TYPE_LOGIN = 13;
	public static final int ACTION_TYPE_FEEDBACK = 16;//反馈
//	public static final int ACTION_TYPE_FOLLOWER = 15;
	public static final int ACTION_TYPE_MOOD = 14;//发布心情
	public static final int ACTION_TYPE_BOOK = 17;//预约
	public static final int ACTION_TYPE_BIND_APPLE = 18;//绑定appleId
	public static final int ACTION_TYPE_BIND_THIRDPARTY = 19;//绑定第三方
	public static final int ACTION_TYPE_COLLECTION = 20;  //合集点赞
	
	public static final int ACTION_TYPE_AVATAR = 41;//修改用户头像
	public static final int ACTION_TYPE_NICKNAME = 42;//修改用户昵称
	public static final int ACTION_TYPE_INTRO = 43;//修改用户简介

	/**
	 * 资源类型
	 */
	public static final int RES_TYPE_MEMBER = 0;//用户
	public static final int RES_TYPE_POST = 1;//帖子
	public static final int RES_TYPE_MOOD = 2;//心情
	public static final int RES_TYPE_GAME = 3;//游戏
	public static final int RES_TYPE_COMMUNITY = 4;//社区
	public static final int RES_TYPE_COMMENT = 5;//文章评论
	public static final int RES_TYPE_EVALUATION = 6;//游戏评论
	public static final int RES_TYPE_REPLY = 7;//回复
	public static final int RES_TYPE_MESSAGE = 8;//心情评论
	public static final int RES_TYPE_FEEDBACK = 9;//反馈
	public static final int RES_TYPE_LINGKAFAST = 16;//零卡加速器

	
	/**
	 * 消息通知类型类型
	 */
	public static final int MSG_TYPE_LIKE_POST =0 ;
	public static final int MSG_TYPE_LIKE_EV =1 ;
	public static final int MSG_TYPE_LIKE_GMEV =2 ;
	public static final int MSG_TYPE_LIKE_MOOD =7 ;
	public static final int MSG_TYPE_COMM =3 ;
	public static final int MSG_TYPE_REPLAY_P =4 ;
	public static final int MSG_TYPE_REPLAY_GAME =5 ;
	public static final int MSG_TYPE_SYSTEM =6 ;//系统消息
	public static final int MSG_TYPE_MOOD =8 ;//评价我的心情
	public static final int MSG_TYPE_REPLAY_M =9 ;//回复心情评论
	public static final int MSG_TYPE_LIKE_MOOD_MESSAGE =11 ;//点赞心情评论
	public static final int MSG_TYPE_REPLAY_RE =12 ;//回复二级回复

	/**
	 * 定义系统消息为6  的 data 字段 actionType类型
	 * 商城秒杀类型
	 */
	public static  final int MSG_TYPE_SYSTEM_ACTION_TYPE_MALL_SECKILL = 9;


//	赞了我的文章 | 0
//	赞了我的评论 | 1
//	赞了我的游戏评价 | 2
//	评论了我的文章 | 3
//	回复我的评论 | 4
//	回复我的游戏评价 | 5
//	系统消息 | 6
	
	public static final String TASK_TYPE_POST_SHARE = "POST_SHARE";
	public static final String DEFAULT_SHARE_URL_PRO = "https://www.fungoweb.com";//http://static.fungoweb.com 官方外链（pro外链）
	public static final String DEFAULT_SHARE_URL_DEV = "https://pc.mingbonetwork.com";//http://static.fungoweb.com dev环境外链
	public static final String officeTagId = "";

	
	public static final String FUM_IMAGE = "http://output-mingbo.oss-cn-beijing.aliyuncs.com/member/imgs/honors/styleNorm/Fungo%E8%BA%AB%E4%BB%BD%E8%AF%81%403x.png";
	public static final String LEVEL_ONE_IMAGE = "http://output-mingbo.oss-cn-beijing.aliyuncs.com/member/imgs/level/%E7%AD%89%E7%BA%A7%201%2020px%403x.png";
	
	public static String RUN_ENVIRONMENT;//当前运行环境

	@Value("${spring.profiles.active}")
	private void setRUN_ENVIRONMENT(String rUN_ENVIRONMENT) {
		RUN_ENVIRONMENT = rUN_ENVIRONMENT;
	}
	
	

	// 用户操作记录枚举
	// ACTION_TYPE: {
	// 0: '点赞',like
	// 1: '回复',
	// 2: '评论',
	// 3: '分享',share
	// 4: '收藏',collect
	// 5: '关注',follow
	// 6: '举报',report
	// 7: '下载',downLoad
	// 8: '推荐',
	// 9: '不推荐',
	// 10: '忽略',ignore
	// 11: '发帖',
	// 12: '浏览',browse
	// 13: '登录',
	// 14: '用户反馈',
	// 15: '反对',
	// },

	//

	// //资源类型对照
	// RESOURCE_TYPE: {
	// 0: '_User',
	// 1: 'Post',
	// 2: 'Mood',
	// 3: 'Game',
	// 4: 'Community',
	// 5: 'Comment',
	// 6: 'Evaluation',
	// 7: 'Reply',
	// 8: 'Message', // 心情评论
	// 9: 'Feedback'
	// },

	
	

}
