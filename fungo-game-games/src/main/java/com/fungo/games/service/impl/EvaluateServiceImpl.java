package com.fungo.games.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.helper.MQProduct;
import com.fungo.games.service.GameEvaluationService;
import com.fungo.games.service.GameService;
import com.fungo.games.service.ICounterService;
import com.fungo.games.service.IEvaluateService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.evaluation.*;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

@Service
public class EvaluateServiceImpl implements IEvaluateService {


    private static final Logger logger = LoggerFactory.getLogger(EvaluateServiceImpl.class);

   /* @Autowired
    private CmmCommentService commentService;
    @Autowired
    private CmmCommunityService communityService;
    @Autowired
    private CmmPostService postService;
    @Autowired
    private MemberService memberService;*/
    @Autowired
    private ICounterService counterService;
    /*@Autowired
    private MooMoodService moodService;
    @Autowired
    private MooMessageService messageServive;
    @Autowired
    BasActionService actionService;*/
    @Autowired
    private GameEvaluationService gameEvaluationService;
    @Autowired
    private GameService gameService;
    /*@Autowired
    private IPushService pushService;

    @Autowired
    private ReplyService replyService;

    @Autowired
    private IUserService userService;
    @Autowired
    private IGameProxy gameProxy;*/

    @Autowired
    private FungoCacheGame fungoCacheGame;

    @Autowired
    private FungoCacheArticle fungoCacheArticle;

    /*@Autowired
    private FungoCacheMood fungoCacheMood;

    @Autowired
    private FungoCacheComment fungoCacheComment;*/

    //用户成长业务
    /*@Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;*/

    /**
     * 系统feignClient
     */
    @Autowired
    private SystemFeignClient systemFeignClient;

    /**
     * MQ
     */
    @Autowired
    private MQProduct mqProduct;

    @Override
    @Transactional
    public ResultDto<EvaluationBean> addGameEvaluation(String memberId, EvaluationInput commentInput) throws Exception {
//		if(CommonUtil.isNull(commentInput.getContent())) {
//			return ResultDto.error("-1","内容不能为空!");
//		}
        ResultDto<EvaluationBean> re = new ResultDto<EvaluationBean>();
        EvaluationBean t = new EvaluationBean();
        GameEvaluation evaluation = gameEvaluationService.selectOne(new EntityWrapper<GameEvaluation>().eq("member_id", memberId).eq("game_id", commentInput.getTarget_id()).eq("state", 0));

        //int times = -1;
        String tips = "";

        if (commentInput.getRating() == null) {
            return ResultDto.error("-1", "请对该游戏进行评分");
        }
        if (commentInput.getRating() < 1 || commentInput.getRating() > 10) {
            return ResultDto.error("-1", "评分只能在1-10之间");
        }

        if (commentInput.getTrait1() < 0 || commentInput.getTrait1() > 10) {
            return null;
        }
        if (commentInput.getTrait2() < 0 || commentInput.getTrait2() > 10) {
            return null;
        }
        if (commentInput.getTrait3() < 0 || commentInput.getTrait3() > 10) {
            return null;
        }
        if (commentInput.getTrait4() < 0 || commentInput.getTrait4() > 10) {
            return null;
        }
        if (commentInput.getTrait5() < 0 || commentInput.getTrait5() > 10) {
            return null;
        }


        if (evaluation == null) {//用户未对该游戏发表过评论，新增
            evaluation = new GameEvaluation();
            //2.4
            //分数限制
            evaluation.setRating(commentInput.getRating());

            evaluation.setTrait1(commentInput.getTrait1() == 0 ? null : commentInput.getTrait1());
            evaluation.setTrait2(commentInput.getTrait2() == 0 ? null : commentInput.getTrait2());
            evaluation.setTrait3(commentInput.getTrait3() == 0 ? null : commentInput.getTrait3());
            evaluation.setTrait4(commentInput.getTrait4() == 0 ? null : commentInput.getTrait4());
            evaluation.setTrait5(commentInput.getTrait5() == 0 ? null : commentInput.getTrait5());

            evaluation.setContent(commentInput.getContent());
            evaluation.setCreatedAt(new Date());
            evaluation.setGameId(commentInput.getTarget_id());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if (commentInput.getImages() != null) {
                    evaluation.setImages(objectMapper.writeValueAsString(commentInput.getImages()));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            evaluation.setIsRecommend("1");
            evaluation.setLikeNum(0);
            evaluation.setMemberId(memberId);
            evaluation.setPhoneModel(commentInput.getPhone_model());
            evaluation.setState(0);
            evaluation.setUpdatedAt(new Date());
            //游戏名
            Game game = gameService.selectOne(Condition.create().setSqlSelect("id,name").eq("id", commentInput.getTarget_id()));
            if (game != null) {
                evaluation.setGameName(game.getName());
            }

            gameEvaluationService.insert(evaluation);
//			if(commentInput.isIs_recommend()) {
            counterService.addCounter("t_game", "comment_num", commentInput.getTarget_id());//增加评论数
//			}else {
//				counterService.addCounter("t_game", "unrecommend_num", commentInput.getTarget_id());//增加评论数
//			}

//			gameProxy.addScore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_GAME);

            //V2.4.6版本任务之前任务 废弃
            //times = gameProxy.addTaskCore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_GAME);

            //V2.4.6版本任务
            //1 fungo币
//            Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());
//            迁移微服务后 SystemFeignClient调用 用户成长fungo币
//            2019-05-10
//            lyc
            Map<String, Object> resMapCoin = systemFeignClient.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());

            //2 经验值
//            Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
//            迁移微服务后 SystemFeignClient调用 用户成长经验值
//            2019-05-10
//            lyc
            Map<String, Object> resMapExp = systemFeignClient.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());

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


            //产生一个action
            /*BasAction action = new BasAction();
            action.setCreatedAt(new Date());
            action.setTargetId(evaluation.getId());
            action.setTargetType(6);
            action.setType(8);
            action.setMemberId(memberId);
            action.setState(0);
            action.setUpdatedAt(new Date());
            this.actionService.insertAllColumn(action);*/
//            迁移微服务 MQ执行
//            2019-05-10
//            lyc
            BasActionDto basActionDto = new BasActionDto();
            basActionDto.setCreatedAt(new Date());
            basActionDto.setTargetId(evaluation.getId());
            basActionDto.setTargetType(6);
            basActionDto.setType(8);
            basActionDto.setMemberId(memberId);
            basActionDto.setState(0);
            basActionDto.setUpdatedAt(new Date());
            mqProduct.basActionInsertAllColumn(basActionDto);
        } else {//用户对该游戏发表过评论，修改
            //2.4
            evaluation.setRating(commentInput.getRating());

            evaluation.setTrait1(commentInput.getTrait1() == 0 ? null : commentInput.getTrait1());
            evaluation.setTrait2(commentInput.getTrait2() == 0 ? null : commentInput.getTrait2());
            evaluation.setTrait3(commentInput.getTrait3() == 0 ? null : commentInput.getTrait3());
            evaluation.setTrait4(commentInput.getTrait4() == 0 ? null : commentInput.getTrait4());
            evaluation.setTrait5(commentInput.getTrait5() == 0 ? null : commentInput.getTrait5());

            evaluation.setContent(commentInput.getContent());
            Date date = new Date();
            evaluation.setCreatedAt(date);
            evaluation.setGameId(commentInput.getTarget_id());
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                if (commentInput.getImages() != null) {
                    evaluation.setImages(objectMapper.writeValueAsString(commentInput.getImages()));
                }
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//			String r = commentInput.isIs_recommend()?"1":"0";
//			String d = evaluation.getIsRecommend();
//			if(!r.equals(d)) {
//				if("1".equals(d)) {
//					counterService.subCounter("t_game", "recommend_num", commentInput.getTarget_id());//增加评论数
//					counterService.addCounter("t_game", "unrecommend_num", commentInput.getTarget_id());
//				}else {
//					counterService.addCounter("t_game", "recommend_num", commentInput.getTarget_id());
//					counterService.subCounter("t_game", "unrecommend_num", commentInput.getTarget_id());
//				}
//				evaluation.setIsRecommend(r);
//			}
//			evaluation.setLikeNum(0);
            evaluation.setMemberId(memberId);
            evaluation.setPhoneModel(commentInput.getPhone_model());
            evaluation.setState(0);
            evaluation.setUpdatedAt(date);
            evaluation.updateById();
//            迁移 微服务 逻辑块变动前
//            2019-05-10
//            lyc
            /*BasAction action = actionService.selectOne(new EntityWrapper<BasAction>()
                    .eq("member_id", memberId)
                    .eq("target_id", evaluation.getId())
                    .eq("target_type", 6)
                    .eq("type", 8)
                    .eq("state", 0));
            if (action != null) {
                action.setUpdatedAt(date);
                actionService.updateAllColumnById(action);
            }*/
//            迁移 微服务 逻辑块变动后
//            2019-05-10
//            lyc
            mqProduct.selectOneAndUpdateAllColumnById(memberId,evaluation.getId(),6,8,0);


        }

        //返回评论内容
//        t.setAuthor(userService.getAuthor(memberId));
//        迁移微服务 feignClient调用 根据用户id获取authorBean
//        2019-05-10
//        lyc
        t.setAuthor(systemFeignClient.getAuthor(memberId));

        t.setContent(CommonUtils.filterWord(commentInput.getContent()));
        t.setObjectId(evaluation.getId());
        t.setCreatedAt(DateTools.fmtDate(new Date()));
        t.setReply_count(0);
        t.setRating(commentInput.getRating());
        t.setReply_more(false);
        t.setImages(commentInput.getImages());
        t.setReplys(new ArrayList<String>());
        t.setUpdatedAt(DateTools.fmtDate(new Date()));
        t.setPhone_model(commentInput.getPhone_model());
        t.setIs_recommend(commentInput.isIs_recommend());
        re.setData(t);

        if (StringUtils.isNotBlank(tips)) {
            re.show(tips);
        } else {
            re.show("发布成功");
        }

        //清除该用户的评论游戏redis cache
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + memberId, "", null);
        //清除 我的游戏评测(2.4.3)
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_EVALUATIONLIST , memberId, null);
        //游戏详情
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + commentInput.getTarget_id(), "", null);
        // 获取最近评论的游戏
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + memberId, "", null);

        //游戏评价列表
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS, "", null);
        return re;
    }

    /*@Override
    public ResultDto<EvaluationOutBean> getEvaluationDetail(String memberId, String commentId) {
        ResultDto<EvaluationOutBean> re = new ResultDto<EvaluationOutBean>();
        EvaluationOutBean bean = new EvaluationOutBean();
        GameEvaluation evaluation = gameEvaluationService.selectById(commentId);
        if (evaluation == null) {
            return ResultDto.error("-1", "该评论详情不存在");
        }
        //2.4
        if (evaluation.getRating() != null) {
            bean.setRating(evaluation.getRating());
        }
        bean.setSort(evaluation.getSort());

        bean.setAuthor(this.userService.getAuthor(evaluation.getMemberId()));
        bean.setContent(CommonUtils.filterWord(evaluation.getContent()));
        bean.setCreatedAt(DateTools.fmtDate(evaluation.getCreatedAt()));
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<String> imgs = null;
        try {
            if (evaluation.getImages() != null) {
                imgs = (ArrayList<String>) objectMapper.readValue(evaluation.getImages(), ArrayList.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (imgs == null) {
            imgs = new ArrayList<String>();
        }
        bean.setGameId(evaluation.getGameId());
        bean.setImages(imgs);
        bean.setIs_recommend("1".equals(evaluation.getIsRecommend()) ? true : false);
        bean.setLike_num(evaluation.getLikeNum());
        bean.setPhone_model(evaluation.getPhoneModel());
        bean.setReply_count(evaluation.getReplyNum());
        bean.setState(evaluation.getState());
        bean.setUpdatedAt(DateTools.fmtDate(evaluation.getUpdatedAt()));
        bean.setObjectId(evaluation.getId());
        //是否点赞
        if ("".equals(memberId) || memberId == null) {
            bean.setIs_liked(false);
        } else {
            int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", evaluation.getId()).eq("member_id", memberId));
            bean.setIs_liked(liked > 0 ? true : false);
        }
        re.setData(bean);
        return re;
    }*/

    /*@Override
    public FungoPageResultDto<EvaluationOutPageDto> getEvaluationList(String memberId, EvaluationInputPageDto pageDto) {

        FungoPageResultDto<EvaluationOutPageDto> re = null;

        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS;
        String keySuffix = pageDto.getGame_id() + JSON.toJSONString(pageDto);

        re = (FungoPageResultDto<EvaluationOutPageDto>) fungoCacheGame.getIndexCache(keyPrefix, keySuffix);
        if (null != re) {
            return re;
        }
        re = new FungoPageResultDto<EvaluationOutPageDto>();
        List<EvaluationOutPageDto> relist = new ArrayList<EvaluationOutPageDto>();
        Game game = gameService.selectById(pageDto.getGame_id());
        if (game == null) {
            return FungoPageResultDto.error("-1", "游戏不存在");
        }
        Wrapper<GameEvaluation> commentWrapper = new EntityWrapper<GameEvaluation>();
        commentWrapper.eq("game_id", pageDto.getGame_id());
        commentWrapper.and("state !={0}", -1);
        if ("mine".equals(pageDto.getFilter())) {//社区主
            commentWrapper.eq("member_id", memberId);
        }
        //pageDto.getSort()==0||
        if (pageDto.getSort() == 1) {//排序
            commentWrapper.orderBy("created_at", true);
        } else if (pageDto.getSort() == 0 || pageDto.getSort() == 2) {
            commentWrapper.orderBy("created_at", false);
        } else if (pageDto.getSort() == 3) {
            commentWrapper.groupBy("id").orderBy("sum(like_num+reply_num)", true);//按照点赞数和回复数排序
        } else if (pageDto.getSort() == 4) {
            commentWrapper.groupBy("id").orderBy("sum(like_num+reply_num)", false);
        }

        Page<GameEvaluation> page = this.gameEvaluationService.selectPage(new Page<>(pageDto.getPage(), pageDto.getLimit()), commentWrapper);
        List<GameEvaluation> list = page.getRecords();

        for (GameEvaluation cmmComment : list) {
            EvaluationOutPageDto ctem = new EvaluationOutPageDto();
            ctem.setContent(CommonUtils.filterWord(cmmComment.getContent()));
            ctem.setCreatedAt(DateTools.fmtDate(cmmComment.getCreatedAt()));
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                ctem.setImages(objectMapper.readValue(cmmComment.getImages(), ArrayList.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            ctem.setIs_recommend("1".equals(cmmComment.getIsRecommend()) ? true : false);
            ctem.setLike_num(cmmComment.getLikeNum());
            ctem.setObjectId(cmmComment.getId());
            ctem.setPhone_model(cmmComment.getPhoneModel());
            ctem.setReply_count(cmmComment.getReplyNum());
            ctem.setUpdatedAt(DateTools.fmtDate(cmmComment.getUpdatedAt()));
            ctem.setAuthor(this.userService.getAuthor(cmmComment.getMemberId()));
            //2.4 评分
            if (cmmComment.getRating() != null) {
                ctem.setRating(cmmComment.getRating());
            }
            //回复
            Page<Reply> replyList = replyService.selectPage(new Page<>(1, 3), new EntityWrapper<Reply>().eq("target_id", cmmComment.getId()).eq("state", 0).orderBy("created_at", true));
            int i = 0;
            for (Reply reply : replyList.getRecords()) {
                i = i + 1;
                if (i == 3) {
                    ctem.setReply_more(true);
                    break;
                }
                ReplyBean replybean = new ReplyBean();
                replybean.setAuthor(this.userService.getAuthor(reply.getMemberId()));
                replybean.setContent(CommonUtils.filterWord(reply.getContent()));
                replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
                replybean.setObjectId(reply.getId());
                replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
                replybean.setLike_num(reply.getLikeNum());
                replybean.setReplyToId(reply.getReplayToId());
                Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
                if (m != null) {
                    replybean.setReplyToName(m.getUserName());
                }
                ctem.getReplys().add(replybean);
            }

            //是否点赞
            if ("".equals(memberId) || memberId == null) {
                ctem.setIs_liked(false);
            } else {
                int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", cmmComment.getId()).eq("member_id", memberId));
                ctem.setIs_liked(liked > 0 ? true : false);
            }
            relist.add(ctem);
        }
        PageTools.pageToResultDto(re, page);
        re.setData(relist);

        //redis cache
        fungoCacheGame.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }*/



    /*@Override
    public ResultDto<EvaluationOutBean> anliEvaluationDetail(String memberId, String evaId) {
        //根据sort和时间来排行
        ResultDto<EvaluationOutBean> result = this.getEvaluationDetail(memberId, evaId);
        EvaluationOutBean eva = result.getData();
        Game game = gameService.selectById(eva.getGameId());
        if (game != null) {
            eva.setGameIcon(game.getIcon());
            eva.setGameIntro(game.getIntro());
            eva.setGameName(game.getName());
        }
        int sort = eva.getSort();
        String id = eva.getObjectId();
        //上一评论
        GameEvaluation pre = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).gt("created_at", eva.getCreatedAt()).ne("id", id).orderBy("concat(sort,created_at)").last("limit 1"));
//		GameEvaluation pree = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}",-1).le("sort",sort).ne("id", id).orderBy("concat(sort,created_at)",false).last("limit 1"));
        //下一评论
        GameEvaluation next = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).le("created_at", eva.getCreatedAt()).ne("id", id).orderBy("concat(sort,created_at)", false).last("limit 1"));
        if (pre != null) {
            eva.setPreEvaId(pre.getId());
        }//
        if (next != null) {
            eva.setNextEvaId(next.getId());
        }

        return result;
    }*/




}
