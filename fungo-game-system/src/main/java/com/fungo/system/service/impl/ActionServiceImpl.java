package com.fungo.system.service.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.constts.CommonlyConst;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.entity.BasAction;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.entity.MemberFollower;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.helper.mq.MQProduct;
import com.fungo.system.facede.IDeveloperProxyService;
import com.fungo.system.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.consts.Setting;
import com.game.common.dto.ActionInput;
import com.game.common.dto.ResultDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.*;
import freemarker.template.utility.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ActionServiceImpl implements IActionService {
    @Autowired
    private BasActionService actionService;
    @Autowired
    private BasActionDao actionDao;
    @Autowired
    private IGameProxy gameProxy;
    @Autowired
    private ICounterService counterService;
    @Autowired
    private MemberFollowerService followService;
    @Autowired
    private ScoreLogService scoreLogService;
    @Autowired
    private BasNoticeService noticeService;

    @Autowired
    private FungoCacheMember fungoCacheMember;

    @Autowired
    private FungoCacheArticle fungoCacheArticle;

    @Autowired
    private FungoCacheMood fungoCacheMood;

    @Autowired
    private FungoCacheGame fungoCacheGame;

    @Autowired
    private FungoCacheCommunity fungoCacheCommunity;

    @Autowired
    private CommunityFeignClient communityFeignClient;

    /*@Autowired
    private GamesFeignClient gamesFeignClient;*/

    @Autowired
    private IDeveloperProxyService iDeveloperProxyService;

    @Autowired
    private MQProduct mqProduct;


    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;


    //点赞
    @Transactional
    public ResultDto<String> like(String memberId, ActionInput inputDto, String appVersion) throws Exception {
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_LIKE, inputDto);

        if (action == null) {//点赞记录不存在
            action = this.buildAction(memberId, Setting.ACTION_TYPE_LIKE, inputDto);
            actionService.insert(action);
            this.addCounter(memberId, Setting.ACTION_TYPE_LIKE, inputDto);

            //点赞他人内容 点赞他人游戏评价 被点赞
//			gameProxy.addScore(Setting.ACTION_TYPE_LIKE, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());
            gameProxy.addNotice(Setting.ACTION_TYPE_LIKE, memberId, inputDto.getTarget_id(), inputDto.getTarget_type(), inputDto.getInformation(), appVersion, "");

            //完成任务
            if (Setting.RES_TYPE_EVALUATION == inputDto.getTarget_type()) {//点赞游戏评价

                //V2.4.6版本之前任务 废弃
                //scoreLogService.expTask(memberId, 35, "", inputDto.getTarget_id(), inputDto.getTarget_type());

            } else {//点赞其它内容

                //V2.4.6版本之前任务 废弃
                //scoreLogService.expTask(memberId, 22, "", inputDto.getTarget_id(), inputDto.getTarget_type());

                if (Setting.RES_TYPE_POST == inputDto.getTarget_type()) {

                    //帖子详情
                    fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, inputDto.getTarget_id(), null);
                    //帖子/心情评论列表 + inputDto.getTarget_id()
                    fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);

                }
            }

            //被点赞用户的id
            String targetMemberId = gameProxy.getMemberID(inputDto.getTarget_id(), inputDto.getTarget_type());


            //V2.4.6版本之前任务 废弃
            //被点赞玩家获得奖励
            //ResultDto<Integer> expTask = scoreLogService.expTask(targetMemberId, 41, "", inputDto.getTarget_id(), inputDto.getTarget_type());

            //获得勋章判断
            //if (expTask.isSuccess()) {
            //}

            //用户被点赞总次数
            int likeCount = noticeService.selectCount(new EntityWrapper<BasNotice>().eq("member_id", targetMemberId).in("type", new Integer[]{0, 1, 2, 7, 11}));
//			int likeCount = scoreLogService.selectCount(new EntityWrapper<ScoreLog>().eq("member_id", targetMemberId).eq("code_idt", 41));

            if (likeCount == 50) {//expTask.getData()
                scoreLogService.updateRanked(targetMemberId, new ObjectMapper(), 31);
            } else if (likeCount == 100) {
                scoreLogService.updateRanked(targetMemberId, new ObjectMapper(), 32);
            } else if (likeCount == 300) {
                scoreLogService.updateRanked(targetMemberId, new ObjectMapper(), 33);
            }

        } else {//点赞记录存在

            if (-1 == action.getState()) {
                action.setState(0);
                action.setUpdatedAt(new Date());
                this.addCounter(memberId, Setting.ACTION_TYPE_LIKE, inputDto);
                this.actionService.updateById(action);
            }
        }


        //V2.4.6版本任务
        // 每日任务
        //1 经验值
        iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_LIKE_EXP.code());

        //2 fungo币
        iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_LIKE_COIN.code());


        //首页文章帖子列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
        //心情评论列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        //获取心情动态列表(v2.4)
        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOOD_CONTENT_GET, "", null);
        return ResultDto.success("点赞成功");
    }

    //取消点赞
    @Transactional
    public ResultDto<String> unLike(String memberId, ActionInput inputDto) {
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_LIKE, inputDto);
        if (action != null && 0 == action.getState()) {
            action.setState(-1);
            action.updateById();
            this.subCounter(memberId, Setting.ACTION_TYPE_LIKE, inputDto);
        }


        //首页文章帖子列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);

        //获取心情动态列表(v2.4)
        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOOD_CONTENT_GET, "", null);
        //帖子详情
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, inputDto.getTarget_id(), null);
        return ResultDto.success("取消成功");
    }

    //分享
    public ResultDto<String> share(String memberId, ActionInput inputDto) throws Exception {
        ResultDto<String> re = new ResultDto<String>();
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_SHARE, inputDto);

        //int times = -1;
        String tips = "";

        if (action == null) {
            action = this.buildAction(memberId, Setting.ACTION_TYPE_SHARE, inputDto);
            this.actionService.insert(action);
//			gameProxy.addScore(Setting.ACTION_TYPE_SHARE, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());
            //V2.4.6版本之前的任务
            //times = gameProxy.addTaskCore(Setting.ACTION_TYPE_SHARE, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());

            //V2.4.6版本任务
            //分享文章
            if (inputDto.getTarget_type() == Setting.RES_TYPE_POST) {
                //每日任务
                //1 fungo币
                Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_COIN.code());

                //2 经验值
                Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_EXP.code());


                if (null != resMapCoin && !resMapCoin.isEmpty()) {
                    if (null != resMapExp && !resMapExp.isEmpty()) {
                        boolean coinsSuccess = (boolean) resMapCoin.get("success");
                        boolean expSuccess = (boolean) resMapExp.get("success");
                        if (coinsSuccess && expSuccess) {
                            tips = (String) resMapCoin.get("msg");
                            tips += (String) resMapExp.get("msg");
                        } else {
                            tips = (String) resMapCoin.get("msg");
                        }
                    }
                }

                //分享游戏
            } else if (inputDto.getTarget_type() == Setting.RES_TYPE_GAME) {

                //1 fungo币
                Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                        MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_COIN.code());

                //2 经验值
                Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                        MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_SHARE_1_GAME_EXP.code());

                if (null != resMapCoin && !resMapCoin.isEmpty()) {
                    if (null != resMapExp && !resMapExp.isEmpty()) {
                        boolean coinsSuccess = (boolean) resMapCoin.get("success");
                        boolean expSuccess = (boolean) resMapExp.get("success");
                        if (coinsSuccess && expSuccess) {
                            tips = (String) resMapCoin.get("msg");
                            tips += (String) resMapExp.get("msg");
                        } else {
                            tips = (String) resMapCoin.get("msg");
                        }
                    }
                }
            }

        }

       /* if (times > 0) {
            re.show("分享成功，经验+5！Fun币+2！");
            re.setData("分享成功，经验+5！Fun币+2！");
        } else {
            re.show("分享成功");
        }*/
        if (StringUtils.isNotBlank(tips)) {
            re.setData(tips);
            re.show(tips);
        } else {
            re.show("分享成功");
        }

        return re;
    }

    //收藏
    public ResultDto<String> collect(String memberId, ActionInput inputDto) throws Exception {
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_COLLECT, inputDto);
        if (action == null) {
            action = this.buildAction(memberId, Setting.ACTION_TYPE_COLLECT, inputDto);
            this.actionService.insert(action);
            //收藏数+1
            this.addCounter(memberId, Setting.ACTION_TYPE_COLLECT, inputDto);
//			gameProxy.addScore(Setting.ACTION_TYPE_COLLECT, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());
            if (Setting.RES_TYPE_POST == inputDto.getTarget_type()) {

                //V2.4.6版本之前任务废弃
                //文章被收藏用户获得奖励
                //gameProxy.addTaskCore(Setting.ACTION_TYPE_COLLECT, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());
            }
        }

        //更新用户我的收藏Redis cache 2019-05-06 抽取冗余代码
        updateMyCollectionRedisCache(memberId, inputDto);
        return ResultDto.success("收藏成功");
    }

    //取消收藏
    @Transactional
    public ResultDto<String> unCollect(String memberId, ActionInput inputDto) {
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_COLLECT, inputDto);
        if (action != null) {
            action.setState(-1);
            action.updateById();
            //收藏数-1
            this.subCounter(memberId, Setting.ACTION_TYPE_COLLECT, inputDto);
        }
        //更新用户我的收藏Redis cache 2019-05-06 抽取冗余代码
        updateMyCollectionRedisCache(memberId, inputDto);

        return ResultDto.success("取消成功");
    }

    /**
     * 更新用户我的收藏Redis cache 2019-05-06 抽取冗余代码
     *
     * @param memberId
     * @param inputDto
     */
    private void updateMyCollectionRedisCache(String memberId, ActionInput inputDto) {
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_COLLECTION + memberId;
        fungoCacheMember.excIndexCache(false, keyPrefix, "", null);

        if (Setting.RES_TYPE_POST == inputDto.getTarget_type()) {
            //帖子详情
            fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, inputDto.getTarget_id(), null);
            //帖子/心情评论列表+ inputDto.getTarget_id()
            fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        }
        //首页文章帖子列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
    }

    //关注
    @Transactional
    public ResultDto<String> follow(String memberId, ActionInput inputDto) throws Exception {

        if (memberId.equals(inputDto.getUser_id()) || memberId.equals(inputDto.getTarget_id())) {
            return ResultDto.error("-1", "不能关注你自己");
        }

        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);

        //对象是否是用户
        boolean isMember = false;
        if (action == null) {
            action = this.buildAction(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);
            this.actionService.insert(action);
            this.addCounter(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);//粉丝数 +1

            if ("t_member".equals(getTableName(inputDto.getTarget_type()))) {//用户 关注数+1
                isMember = true;
                counterService.addCounter("t_member", "follower_num", memberId);

                //V2.4.6版本之前任务
                //gameProxy.addTaskCore(Setting.ACTION_TYPE_FOLLOW, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());

            }
        }
        if (isMember) {
            String userId = memberId;
            String targetId = inputDto.getTarget_id();
            //有没有关注
            //有没有互相关注

            //用户对目标用户的关注记录
            MemberFollower f1 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", userId).eq("follower_id", targetId));
            if (f1 == null) {//没有关注记录

                //生成双方关注记录
                MemberFollower fo = new MemberFollower();
                Date date = new Date();
                fo.setCreatedAt(date);
                fo.setUpdatedAt(date);
                fo.setFollowedAt(date);
                fo.setMemberId(userId);
                fo.setFollowerId(targetId);
                fo.setState(1);
                followService.insert(fo);

                MemberFollower ft = new MemberFollower();
                ft.setCreatedAt(date);
                ft.setUpdatedAt(date);
                ft.setFollowedAt(date);
                ft.setMemberId(targetId);
                ft.setFollowerId(userId);
                ft.setState(4);
                followService.insert(ft);
            } else {
                //目标用户对用户的关注记录
                MemberFollower f2 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", targetId).eq("follower_id", userId));
                Integer state = f2.getState();
                Date date = new Date();
                if (1 == state) {//用户被目标用户关注 设置为互相关注
                    f1.setState(2);
                    f2.setState(2);
                    f1.setUpdatedAt(date);
                    f2.setUpdatedAt(date);
                    followService.updateById(f1);
                    followService.updateById(f2);
                } else if (3 == state) {//双方取消关注 设置状态
                    f1.setState(1);
                    f2.setState(4);
                    f1.setUpdatedAt(date);
                    f2.setUpdatedAt(date);
                    followService.updateById(f1);
                    followService.updateById(f2);
                }
            }


            //V2.4.6版本任务
            //新手任务
            //1fungo币
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_WATCH_3_USER_COIN.code());

            //2 经验值
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_WATCH_3_USER_EXP.code());

            //clear redis
            //清除 获取我的粉丝
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FANS + targetId, "", null);
            //清除 会员信息（web端）
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + memberId, "", null);
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + targetId, "", null);
            //其他会员信息
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + memberId, "", null);
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + targetId, "", null);

            // 个人资料
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", null);
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + targetId, "", null);
            //清除个人获取用户任务完成进度数据的缓存
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_TASK_USER_TASK_PROGRESS + "-" + memberId, "", null);
        }

        //clear redis
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FOLLW + memberId;
        fungoCacheMember.excIndexCache(false, keyPrefix, "", null);
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        //社区
        fungoCacheCommunity.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_LIST, "", null);
        //清除单独查询社区的redis缓存
        fungoCacheCommunity.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_DETAIL + inputDto.getTarget_id(), "", null);
        //帖子详情
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, "", null);
        //首页-文章帖子列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
        //获取心情动态列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
        return ResultDto.success("关注成功");
    }

    //取消关注
    @Transactional
    public ResultDto<String> unFollow(String memberId, ActionInput inputDto) {
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);
        boolean isMember = false;
        if (action != null) {
            action.setState(-1);
            action.updateById();
            this.subCounter(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);//粉丝数 -1
            if ("t_member".equals(getTableName(inputDto.getTarget_type()))) {//用户 关注数-1
                isMember = true;
                counterService.subCounter("t_member", "follower_num", memberId);
            }
        }

        if (isMember) {
            String userId = memberId;
            String targetId = inputDto.getTarget_id();
            MemberFollower f1 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", userId).eq("follower_id", targetId));
            if (f1 != null) {
                MemberFollower f2 = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", targetId).eq("follower_id", userId));
                Integer state = f2.getState();
                Date date = new Date();
                if (2 == state) {//互相关注
                    f1.setState(4);
                    f2.setState(1);
                    f1.setUpdatedAt(date);
                    f2.setUpdatedAt(date);
                    followService.updateById(f1);
                    followService.updateById(f2);
                } else if (4 == state) {
                    f1.setState(3);
                    f2.setState(3);
                    f1.setUpdatedAt(date);
                    f2.setUpdatedAt(date);
                    followService.updateById(f1);
                    followService.updateById(f2);
                }
            }

            //clear redis
            //清除 获取我的粉丝
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FANS + targetId, "", null);
            //清除 会员信息（web端）
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + memberId, "", null);
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + targetId, "", null);
            //其他会员信息
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + memberId, "", null);
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_CARD + targetId, "", null);

            // 个人资料
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + memberId, "", null);
            fungoCacheMember.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_INFO + targetId, "", null);

        }

        //clear redis
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FOLLW + memberId;
        fungoCacheMember.excIndexCache(false, keyPrefix, "", null);
        //帖子/心情评论列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        //社区
        fungoCacheCommunity.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_LIST, "", null);
        //单个社区
        fungoCacheCommunity.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_DETAIL + inputDto.getTarget_id(), "", null);
        //帖子详情
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, "", null);
        //首页-文章帖子列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
        //获取心情动态列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
        return ResultDto.success("取消成功");
    }

    //举报
    @Transactional
    public ResultDto<String> report(String memberId, ActionInput inputDto) throws Exception {
        ResultDto<String> re = new ResultDto<String>();
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_REPORT, inputDto);
        int times = -1;
        if (action == null) {
            action = this.buildAction(memberId, Setting.ACTION_TYPE_REPORT, inputDto);
            this.actionService.insert(action);
//			gameProxy.addScore(Setting.ACTION_TYPE_REPORT, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());
            //V2.4.6版本中取消举报获取exp和cion
            // times = gameProxy.addTaskCore(Setting.ACTION_TYPE_REPORT, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());
        } else {
            return ResultDto.error("16", "您已经举报过了");
        }

        if (times > 0) {
            re.show("举报成功，经验+2，感谢反馈！");
            re.setData("举报成功，经验+2，感谢反馈！");
        } else {
            re.show("举报成功，感谢反馈！");
            re.setData("举报成功，感谢反馈！");
        }
        return re;
    }

    //下载
    @Transactional
    public ResultDto<String> downLoad(String memberId, ActionInput inputDto) throws Exception {

        inputDto.setTarget_type(3);

        //int times = -1;

        String tips = "";

        if (!"".equals(memberId)) {

            BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_DOWNLOAD, inputDto);

            if (action == null) {

                action = this.buildAction(memberId, Setting.ACTION_TYPE_DOWNLOAD, inputDto);

                this.actionService.insert(action);

                //fix : 游戏下载量 随机增加[by mxf 2019-05-07]
                //增加下载数
                //counterService.addCounter("t_game", "download_num", inputDto.getTarget_id());
                //end
            }

            //登录用户下载游戏后  clear redis cache
            String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_GAMELIST + memberId;
            fungoCacheMember.excIndexCache(false, keyPrefix, "", null);

        }

        //fix : 游戏下载量 随机增加[by mxf 2019-05-07]
        /*
        else {
            //增加下载数
            counterService.addCounter("t_game", "download_num", inputDto.getTarget_id());
        }*/
        //end
//        微服务迁移 游戏下载量变化
//        2019-05-30
//        lyc
        /*
        String game_id = inputDto.getTarget_id();
        if (StringUtils.isNotBlank(game_id)) {
            EntityWrapper<Game> gameEntityWrapper = new EntityWrapper<Game>();
            gameEntityWrapper.setSqlSelect("id,boom_download_num,download_num");
            gameEntityWrapper.eq("id", game_id);
            Game gameDB = gameService.selectOne(gameEntityWrapper);
            if (null != gameDB) {
                Long downNumNew = 0L;
                if (null == gameDB.getBoomDownloadNum() || 0 == gameDB.getBoomDownloadNum()) {
                    downNumNew = gameDB.getDownloadNum().longValue();
                } else {
                    downNumNew = gameDB.getBoomDownloadNum();
                }
                downNumNew = FungoLivelyCalculateUtils.calcViewAndDownloadCount(downNumNew);
                gameDB.setBoomDownloadNum(downNumNew);
                gameDB.setDownloadNum(gameDB.getDownloadNum() + 1);
                gameDB.updateById();
            }
        }*/
//        游戏下载量变化
        Map<String, String> ssmap = new HashMap<>();
        ssmap.put("gameId", inputDto.getTarget_id());
        mqProduct.updateGameDownNumAndBoomDownloadNum(ssmap);


        //V2.4.6之前版本任务
        // times = gameProxy.addTaskCore(Setting.ACTION_TYPE_DOWNLOAD, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());

        //V2.4.6版本任务
        //登录用户执行下载游戏任务
        if (StringUtils.isNoneBlank(memberId)) {
            //新手任务
            //1 经验值
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_FIRST_DOWN_EXP.code());
            //2 fungo币
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_FIRST_DOWN_COIN.code());
            //end

            // 每周任务
            //1 fungo币
            Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY_FIRST_DOWN_GAME_COIN.code());

            //2 经验值
            Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_WEEKLY_FIRST_DOWN_GAME_EXP.code());

            if (null != resMapCoin && !resMapCoin.isEmpty()) {
                if (null != resMapExp && !resMapExp.isEmpty()) {
                    boolean coinsSuccess = (boolean) resMapCoin.get("success");
                    boolean expSuccess = (boolean) resMapExp.get("success");
                    if (coinsSuccess && expSuccess) {
                        tips = (String) resMapCoin.get("msg");
                        tips += (String) resMapExp.get("msg");
                    } else {
                        tips = (String) resMapCoin.get("msg");
                    }
                }
            }
            //end
        }

        ResultDto<String> re = new ResultDto<String>();
        if (StringUtils.isNotBlank(tips)) {
            re.show(tips);
            re.setData(tips);
        } else {
            re.show("下载成功");
            re.setData("下载成功");
        }

        //清除Redis cache
        //清除游戏详情数据
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + inputDto.getTarget_id(), "", null);

        return re;
    }

    //  切换feign客户端 调用游戏服务
    //@todo  mq  调用游戏服务
    private void gameFeignClientUpdateCounterByDownLoad(ActionInput inputDto) {
        Map<String, String> map = new HashMap<>();
        map.put("tableName", "t_game");
        map.put("fieldName", "download_num");
        map.put("id", inputDto.getTarget_id());
        map.put("type", "add");
        mqProduct.updateCounter(map);
    }

    //忽略
    @Transactional
    public ResultDto<String> ignore(String memberId, ActionInput inputDto) {
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_IGNORE, inputDto);
        if (action == null) {
            action = this.buildAction(memberId, Setting.ACTION_TYPE_IGNORE, inputDto);
            this.actionService.insert(action);
        }
        return ResultDto.success();
    }

    //浏览
    @Transactional
    public ResultDto<String> browse(String memberId, ActionInput inputDto) {
        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_BROWSE, inputDto);
        if (action == null) {
            action = this.buildAction(memberId, Setting.ACTION_TYPE_BROWSE, inputDto);
            this.actionService.insert(action);
        }
        return ResultDto.success("浏览成功");
    }

    //查询用户是否有操作记录
    public ResultDto<String> whetherIsDone(String memberId, ActionInput inputDto) {
        return null;
    }

    //插入用户行为记录
    private BasAction buildAction(String memberId, int type, ActionInput inputDto) {
        BasAction action = new BasAction();
        action.setCreatedAt(new Date());
        action.setInformation(inputDto.getInformation());
        action.setMemberId(memberId);
        action.setState(0);
        action.setTargetId(inputDto.getTarget_id());
        action.setTargetType(inputDto.getTarget_type());
        action.setType(type);
        action.setUpdatedAt(new Date());
        return action;
    }

    //获取用户行为记录
    public BasAction getAction(String memberId, int type, ActionInput inputDto) {
        if (0 == type) {
            return this.actionService.selectOne(new EntityWrapper<BasAction>()
                    .eq("member_id", memberId)
                    .eq("target_id", inputDto.getTarget_id())
                    .eq("target_type", inputDto.getTarget_type())
                    .eq("type", type));
        }

        return this.actionService.selectOne(new EntityWrapper<BasAction>()
                .eq("member_id", memberId)
                .eq("target_id", inputDto.getTarget_id())
                .eq("target_type", inputDto.getTarget_type())
                .eq("type", type)
                .eq("state", 0));
    }

    //表字段 增数
    public boolean addCounter(String memberId, int type, ActionInput inputDto) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tableName", getTableName(inputDto.getTarget_type()));
        map.put("fieldName", getFieldName(type));
        map.put("id", inputDto.getTarget_id());
        map.put("type", "add");
        return getCounterBoolean(inputDto, map);
    }

    private boolean getCounterBoolean(ActionInput inputDto, Map<String, String> map) {
        if (CommonlyConst.getCommunityList().contains(inputDto.getTarget_type())) {
//            社区服务空缺 19-05-07
            //@todo mq 消息
            mqProduct.communityUpdate(map); //communityFeignClient.updateCounter(map);
            return true;
        }
        if (CommonlyConst.getGameList().contains(inputDto.getTarget_type())) {
//            feign客户端调用游戏服务
            //@todo mq 消息
            mqProduct.updateCounter(map);//iDeveloperProxyService.updateCounter(map);
            return true;
        }
        if (CommonlyConst.getSystemList().contains(inputDto.getTarget_type())) {
            return actionDao.updateCountor(map);
        }
        return false;
    }

    //表字段 减数
    public boolean subCounter(String memberId, int type, ActionInput inputDto) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tableName", getTableName(inputDto.getTarget_type()));
        map.put("fieldName", getFieldName(type));
        map.put("id", inputDto.getTarget_id());
        map.put("type", "sub");
        return getCounterBoolean(inputDto, map);
    }

    //根据资源类型获取表名
    private String getTableName(int type) {
        if (type == 0) {
            return "t_member";
        } else if (type == 1) {
            return "t_cmm_post";
        } else if (type == 2) {
            return "t_moo_mood";
        } else if (type == 3) {
            return "t_game";
        } else if (type == 4) {
            return "t_cmm_community";
        } else if (type == 5) {
            return "t_cmm_comment";
        } else if (type == 6) {
            return "t_game_evaluation";
        } else if (type == 7) {
            return "t_reply";
        } else if (type == 8) {
            return "t_moo_message";
        } else if (type == 9) {
            return "t_bas_feedback";
        } else if(type == 11){
            return "t_cmm_circle";
        }
        return null;
    }

    //获取表字段
    private String getFieldName(int type) {
        if (type == 0) {
            return "like_num";
        } else if (type == 4) {
            return "collect_num";
        } else if (type == 5) {
            return "followee_num";
        } else if (type == 12) {
            return "watch_num";
        } else if (type == 15) {
            return "follower_num";
        } else if (type == 7) {
            return "post_num";
        } else if(type == 11 ){
            return "member_num";
        }
        return null;
    }

    @Override
    public ResultDto<String> addAction(String memberId, int targetType, int type, String targetId, String information) {
        BasAction action = new BasAction();
        action.setCreatedAt(new Date());
        action.setInformation(information);
        action.setMemberId(memberId);
        action.setState(0);
        action.setTargetId(targetId);
        action.setTargetType(targetType);
        action.setType(type);
        action.setUpdatedAt(new Date());
        action.insert();
        return ResultDto.success("");
    }
}
