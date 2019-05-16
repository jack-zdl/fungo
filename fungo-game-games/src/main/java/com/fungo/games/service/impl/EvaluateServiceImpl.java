package com.fungo.games.service.impl;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.entity.GameTag;
import com.fungo.games.helper.MQProduct;
import com.fungo.games.proxy.IEvaluateProxyService;
import com.fungo.games.service.*;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.ReplyBean;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.evaluation.*;
import com.game.common.dto.game.BasTagDto;
import com.game.common.dto.game.MermberSearchInput;
import com.game.common.dto.game.ReplyDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
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
    /*@Autowired
    private SystemFeignClient systemFeignClient;*/

    /**
     * 系统feignClient的hystrix代理
     */
    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;

    /**
     * MQ
     */
    @Autowired
    private MQProduct mqProduct;

    @Autowired
    private GameTagService gameTagService;

    @Autowired
    private GameDao gameDao;

    @Autowired
    private GameEvaluationService evaluationService;

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
            Map<String, Object> resMapCoin = iEvaluateProxyService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());

            //2 经验值
//            Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
//            迁移微服务后 SystemFeignClient调用 用户成长经验值
//            2019-05-10
//            lyc
            Map<String, Object> resMapExp = iEvaluateProxyService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
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
        t.setAuthor(iEvaluateProxyService.getAuthor(memberId));

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

    @Override
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
//        迁移 微服务后 变动 根据用户id获取authorbean
//        2019-05-11
//        lyc
//        bean.setAuthor(this.userService.getAuthor(evaluation.getMemberId()));
        bean.setAuthor(iEvaluateProxyService.getAuthor(evaluation.getMemberId()));
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
//            迁移微服务 根据条件判断查询总数(有notIn) feign客户端
//            2019-05-11
//            lyc
//            int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", evaluation.getId()).eq("member_id", memberId));
            BasActionDto basActionDto = new BasActionDto();
            basActionDto.setType(0);
            basActionDto.setState(0);
            basActionDto.setTargetId(evaluation.getId());
            basActionDto.setMemberId(memberId);
            int liked = iEvaluateProxyService.getBasActionSelectCount(basActionDto);
            bean.setIs_liked(liked > 0 ? true : false);
        }
        re.setData(bean);
        return re;
    }

    @Override
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
//            迁移微服务 根据用户id获取authorbean对象 feignclient
//            ctem.setAuthor(this.userService.getAuthor(cmmComment.getMemberId()));
//            2019-05-11
//            lyc
            ctem.setAuthor(iEvaluateProxyService.getAuthor(cmmComment.getMemberId()));
            //2.4 评分
            if (cmmComment.getRating() != null) {
                ctem.setRating(cmmComment.getRating());
            }

            //回复
//            迁移微服务 根据条件判断获取ReplyDtoList集合
//            2019-05-11
//            lyc
//            Page<ReplyDto> replyList = replyService.selectPage(new Page<>(1, 3), new EntityWrapper<Reply>().eq("target_id", cmmComment.getId()).eq("state", 0).orderBy("created_at", true));
            ReplyInputPageDto replyInputPageDto = new ReplyInputPageDto();
            replyInputPageDto.setPage(1);
            replyInputPageDto.setLimit(3);
            replyInputPageDto.setTarget_id(cmmComment.getId());
            replyInputPageDto.setState(0);
            Page<CmmCmtReplyDto> replyList = iEvaluateProxyService.getReplyDtoBysSelectPageOrderByCreatedAt(replyInputPageDto);
            int i = 0;
            for (CmmCmtReplyDto reply : replyList.getRecords()) {
                i = i + 1;
                if (i == 3) {
                    ctem.setReply_more(true);
                    break;
                }
                ReplyBean replybean = new ReplyBean();
                replybean.setAuthor(iEvaluateProxyService.getAuthor(reply.getMemberId()));
                replybean.setContent(CommonUtils.filterWord(reply.getContent()));
                replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
                replybean.setObjectId(reply.getId());
                replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
                replybean.setLike_num(reply.getLikeNum());
                replybean.setReplyToId(reply.getReplayToId());
//                微服务 根据条件判断获取memberDto对象
//                2019-05-11
//                lyc
//                Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
                MemberDto md = new MemberDto();
                md.setId(reply.getReplayToId());
                MemberDto m = iEvaluateProxyService.getMemberDtoBySelectOne(md);
                if (m != null) {
                    replybean.setReplyToName(m.getUserName());
                }
                ctem.getReplys().add(replybean);
            }

            //是否点赞
            if ("".equals(memberId) || memberId == null) {
                ctem.setIs_liked(false);
            } else {
//                int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", cmmComment.getId()).eq("member_id", memberId));
                BasActionDto basActionDto = new BasActionDto();
                basActionDto.setType(0);
                basActionDto.setState(0);
                basActionDto.setTargetId(cmmComment.getId());
                basActionDto.setMemberId(memberId);
                int liked = iEvaluateProxyService.getBasActionSelectCount(basActionDto);
                ctem.setIs_liked(liked > 0 ? true : false);


            }
            relist.add(ctem);
        }
        PageTools.pageToResultDto(re, page);
        re.setData(relist);

        //redis cache
        fungoCacheGame.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }



    @Override
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
    }

    /**
     * 根据后台标签id集合，分类标签，游戏id
     * @param tags
     * @param categoryId
     * @param gameId
     * @return
     */
    @SuppressWarnings("all")
    @Override
    @Transactional
    public boolean feignAddGameTagInsert(List<String> tags, String categoryId, String gameId) {
        if (tags.size() > 3) {
            return false;
        }
        // 获取游戏的官方标签(分类) 后台标签 type = 1 type = 2
        List<GameTag> gameTagList = gameTagService
                .selectList(Condition.create().setSqlSelect("id,tag_id").eq("game_id", gameId).eq("type", 2));//1
        GameTag cTag = gameTagService
                .selectOne(Condition.create().setSqlSelect("id,tag_id").eq("game_id", gameId).eq("type", 1));//2
        List<String> preTagIdList = new ArrayList<String>();//已有后台标签
        String preCategory = null;
        if(gameTagList != null && gameTagList.size()>0) {
            for(GameTag gameTag:gameTagList) {
                preTagIdList.add(gameTag.getTagId());
            }
        }
        if(cTag != null) {//是否存在官方标签
            preCategory = cTag.getTagId();
        }
        // 对比，增删
        List<String> newTagIdList = tags;
        newTagIdList = new ArrayList<String>(newTagIdList);
        List<String> tempIdList = new ArrayList<String>();
        preTagIdList.forEach(s -> tempIdList.add(s));

        // 删除id
        preTagIdList.removeAll(newTagIdList);
        // 添加id
        newTagIdList.removeAll(tempIdList);

        Game game = gameService.selectById(gameId);

        String tagsFormGame = game.getTags();
        //官方标签处理
        if(!CommonUtil.isNull(preCategory)) i:{//官方标签存在
            if(CommonUtil.isNull(categoryId)) {
                break i;
            }
            if(!preCategory.equals(categoryId)) {//如果不同
                //删旧(type=0)
                GameTag gameTag = gameTagService
                        .selectOne(new EntityWrapper<GameTag>().eq("tag_id", preCategory).eq("game_id", gameId));
                gameTag.setType(-1);
                gameTag.updateById();
                //添新
                GameTag ofTag = gameTagService
                        .selectOne(new EntityWrapper<GameTag>().eq("tag_id", categoryId).eq("game_id", gameId));
                if(ofTag != null) {
                    ofTag.setType(1);
                    gameTagService.updateById(ofTag);
                }else {
                    GameTag newGameTag = new GameTag();
                    newGameTag.setTagId(categoryId);
                    Date date = new Date();
                    newGameTag.setType(1);
                    newGameTag.setGameId(gameId);
                    newGameTag.setCreatedAt(date);
                    newGameTag.setUpdatedAt(date);
                    gameTagService.insert(newGameTag);

                }
//                迁移微服务 根据bastagid获取basTag对象
//                2019-05-14
//                lyc
//                BasTag tag = tagService.selectById(categoryId);
                BasTagDto basTagDto = new BasTagDto();
                basTagDto.setId(categoryId);
                BasTagDto tag = iEvaluateProxyService.getBasTagBySelectById(basTagDto);
                tagsFormGame = tag.getName();
            }
        }else {//官方标签不存在(1)
            //添新
            GameTag ofTag = gameTagService //有,改成官方
                    .selectOne(new EntityWrapper<GameTag>().eq("tag_id", categoryId).eq("game_id", gameId));
            if(ofTag != null) {
                ofTag.setType(1);
                gameTagService.updateById(ofTag);
            }else {
                GameTag newGameTag = new GameTag();
                newGameTag.setTagId(categoryId);
                Date date = new Date();
                newGameTag.setType(1);
                newGameTag.setGameId(gameId);
                newGameTag.setCreatedAt(date);
                newGameTag.setUpdatedAt(date);
                gameTagService.insert(newGameTag);

            }
//                迁移微服务 根据bastagid获取basTag对象
//                2019-05-14
//                lyc
//            BasTag tag = tagService.selectById(categoryId);
            BasTagDto basTagDto = new BasTagDto();
            basTagDto.setId(categoryId);
            BasTagDto tag = iEvaluateProxyService.getBasTagBySelectById(basTagDto);
            tagsFormGame = tag.getName();
        }

        //后台标签处理
        for (String delTagId : preTagIdList) {// 删除后台标签(type=0)
            GameTag gameTag = gameTagService
                    .selectOne(new EntityWrapper<GameTag>().eq("tag_id", delTagId).eq("game_id", gameId));

            if(gameTag != null ) {
                gameTag.setType(0);
                gameTagService.updateById(gameTag);
            }
        }
        for (String updateId : newTagIdList) {// 更新
            GameTag gameTag = gameTagService
                    .selectOne(new EntityWrapper<GameTag>().eq("tag_id", updateId).eq("game_id", gameId));
            if(gameTag == null) {
                GameTag newGameTag = new GameTag();
                newGameTag.setTagId(updateId);
                Date date = new Date();
                newGameTag.setGameId(gameId);
                newGameTag.setCreatedAt(date);
                newGameTag.setUpdatedAt(date);
                newGameTag.setType(2);
                gameTagService.insert(newGameTag);
            }else if(gameTag.getType() == 0) {
                gameTag.setType(2);
                gameTagService.updateById(gameTag);
            }

        }

        gameDao.updateTags(gameId,tagsFormGame);
        return true;
    }

    /**
     * 我的游戏评测(2.4.3)
     * @param loginId
     * @param input
     * @return
     */
    @Override
    public FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(String loginId, InputPageDto input) throws IOException {
        FungoPageResultDto<MyEvaluationBean> re = null;

        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_EVALUATIONLIST;
        String keySuffix = loginId + JSON.toJSONString(input);
        re = (FungoPageResultDto<MyEvaluationBean>) fungoCacheGame.getIndexCache(keyPrefix, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        re = new FungoPageResultDto<MyEvaluationBean>();
        Page<GameEvaluation> p = evaluationService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<GameEvaluation>()
                .eq("member_id", loginId).eq("state", 0).orderBy("updated_at", false));
        List<GameEvaluation> elist = p.getRecords();
        List<MyEvaluationBean> olist = new ArrayList<>();

//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//		Date today = format.parse(format.format(new Date()));
//		Long time = (long) (24 * 60 * 60 * 1000); // 一天
//		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        ObjectMapper mapper = new ObjectMapper();
        for (GameEvaluation eva : elist) {
            MyEvaluationBean bean = new MyEvaluationBean();
            bean.setContent(eva.getContent());
//			bean.setGameName(eva.getGameName());
            bean.setRating(eva.getRating());
            bean.setUpdatedAt(DateTools.fmtDate(eva.getUpdatedAt()));
            bean.setGameId(eva.getGameId());
            bean.setEvaId(eva.getId());
            if (eva.getImages() != null) {
                ArrayList<String> images = mapper.readValue(eva.getImages(), ArrayList.class);
                bean.setImages(images);
            }
            Game game = gameService.selectOne(Condition.create().setSqlSelect("id,icon,name").eq("id", eva.getGameId()));
            if (game != null) {
                bean.setIcon(game.getIcon());
                bean.setGameName(game.getName());
            }

            olist.add(bean);
        }
        PageTools.pageToResultDto(re, p);
        re.setData(olist);

        //redis cache
        fungoCacheGame.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;
    }


}
