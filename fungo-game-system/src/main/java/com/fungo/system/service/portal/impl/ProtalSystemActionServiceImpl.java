package com.fungo.system.service.portal.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.constts.CommonlyConst;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.entity.BasAction;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.entity.MemberFollower;
import com.fungo.system.facede.IDeveloperProxyService;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.helper.mq.MQProduct;
import com.fungo.system.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.consts.Setting;
import com.game.common.dto.AbstractEventDto;
import com.game.common.dto.AbstractTaskEventDto;
import com.game.common.dto.ActionInput;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.BannerBean;
import com.game.common.dto.mall.MallBannersInput;
import com.game.common.dto.mall.MallGoodsInput;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProtalSystemActionServiceImpl implements IActionService {
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

    @Autowired
    private IDeveloperProxyService iDeveloperProxyService;
    @Autowired
    private MQProduct mqProduct;
    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private IActionService iActionService;


    @Override
    public ResultDto<String> like(String memberId, ActionInput inputDto, String appVersion) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> unLike(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> share(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> collect(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> unCollect(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    //关注
    @Transactional
    public ResultDto<String> follow(String memberId, ActionInput inputDto) throws Exception {

        ResultDto<String> resultDto = ResultDto.ResultDtoFactory.buildSuccess( AbstractResultEnum.CODE_SYSTEM_FOUR.getKey(),AbstractResultEnum.CODE_SYSTEM_FOUR.getSuccessValue() );;
        if ( memberId.equals(inputDto.getTarget_id())) {
            return ResultDto.error("-1", "不能关注你自己");
        }

        BasAction action = this.getAction(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);

        //对象是否是用户
        boolean isMember = false;
        if (action == null) {
            action = this.buildAction(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);
            this.actionService.insert(action);
            iActionService.addCounter(memberId, Setting.ACTION_TYPE_FOLLOW, inputDto);//粉丝数 +1

            if ("t_member".equals(getTableName(inputDto.getTarget_type()))) {//用户 关注数+1
                isMember = true;
                counterService.addCounter("t_member", "follower_num", memberId);

                //V2.4.6版本之前任务
                //gameProxy.addTaskCore(Setting.ACTION_TYPE_FOLLOW, memberId, inputDto.getTarget_id(), inputDto.getTarget_type());
                AbstractTaskEventDto abstractEventDto = new AbstractTaskEventDto(this);
                if( 0 == inputDto.getTarget_type()){
                    abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_USER.getKey());
                }
//            else if(11 == inputDto.getTarget_type()){
//                abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_CIRCLE.getKey());
//            }
                abstractEventDto.setFollowType(inputDto.getTarget_type());
                abstractEventDto.setUserId(memberId);
                abstractEventDto.setObjectId(inputDto.getTarget_id());
                applicationEventPublisher.publishEvent(abstractEventDto);
            }
        }else {
            AbstractTaskEventDto abstractEventDto = new AbstractTaskEventDto(this);
            if( 0 == inputDto.getTarget_type()){
                abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_USER.getKey());
            }else if(11 == inputDto.getTarget_type()){
                abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.FOLLOW_ONE_OFFICIAL_CIRCLE.getKey());
            }
            abstractEventDto.setFollowType(inputDto.getTarget_type());
            abstractEventDto.setUserId(memberId);
            abstractEventDto.setObjectId(inputDto.getTarget_id());
            applicationEventPublisher.publishEvent(abstractEventDto);
            action.setState( 0 );
            action.setUpdatedAt( new Date( ) );
            action.updateById();
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
                if( 2 == f1.getState()){
                    resultDto = ResultDto.ResultDtoFactory.buildSuccess( AbstractResultEnum.CODE_SYSTEM_THREE.getKey(),AbstractResultEnum.CODE_SYSTEM_THREE.getSuccessValue() );
                }
            }
            // c查看是否相互关注
//            BasAction myAction = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", memberId).eq("target_id", inputDto.getTarget_id()).ne("state", "-1"));
//            BasAction otherAction = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id",inputDto.getTarget_id()).eq("target_id", memberId).ne("state", "-1"));



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

        AbstractEventDto abstractEventDto = new AbstractEventDto(this);
        abstractEventDto.setEventType( AbstractEventDto.AbstractEventEnum.USER_FOLLOW.getKey());
        abstractEventDto.setFollowType(inputDto.getTarget_type());
        abstractEventDto.setUserId(memberId);
        abstractEventDto.setObjectId(inputDto.getTarget_id());
        applicationEventPublisher.publishEvent(abstractEventDto);

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
        // 删除圈子相关的信息
        fungoCacheArticle.removeIndexDecodeCache(false, FungoCoreApiConstant.PUB_CIRCLE);
        // 删除文章相关的信息
        fungoCacheArticle.removeIndexDecodeCache(false, FungoCoreApiConstant.PUB_POST);
        return resultDto;
    }

    @Override
    public ResultDto<String> unFollow(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> report(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> collectionLike(String memberId, ActionInput inputDto, String appVersion) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> unCollectionLike(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<BannerBean> queryCollectionLike(MallBannersInput mallBannersInput) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> downLoad(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> ignore(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> browse(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> whetherIsDone(String memberId, ActionInput inputDto) throws Exception {
        return null;
    }

    @Override
    public ResultDto<String> addAction(String memberId, int targetType, int type, String targetId, String information) {
        return null;
    }

    @Override
    public boolean addCounter(String memberId, int type, ActionInput inputDto) {
        return false;
    }

    @Override
    public boolean subCounter(String memberId, int type, ActionInput inputDto) {
        return false;
    }

    @Override
    public ResultDto<String> useSomeTimeFast(String memberId) throws Exception {
        return null;
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




}
