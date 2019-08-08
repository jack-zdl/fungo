package com.fungo.games.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameEvaluation;
import com.fungo.games.facede.IEvaluateProxyService;
import com.fungo.games.service.*;
import com.fungo.games.service.portal.ProtalGameIEvaluateService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCmtReplyDto;
import com.game.common.dto.community.ReplyBean;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.evaluation.*;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProtalGamesEvaluateServiceImpl implements ProtalGameIEvaluateService {

    private static final Logger logger = LoggerFactory.getLogger( ProtalGamesEvaluateServiceImpl.class);

    @Autowired
    private GameEvaluationService gameEvaluationService;
    @Autowired
    private GameService gameService;
    @Autowired
    private FungoCacheGame fungoCacheGame;
    /**
     * 系统feignClient的hystrix代理
     */
    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;
    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;

    public ResultDto<EvaluationOutBean> getEvaluationDetail(String memberId, String commentId) {
        ResultDto<EvaluationOutBean> re = new ResultDto<>();
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

    public FungoPageResultDto<EvaluationOutPageDto> getEvaluationList(String memberId, EvaluationInputPageDto pageDto) {

        FungoPageResultDto<EvaluationOutPageDto> re = null;

        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS;
        String gameId = pageDto.getGame_id();
        String keySuffix = gameId + JSON.toJSONString(pageDto);
        try {
            re = (FungoPageResultDto<EvaluationOutPageDto>) fungoCacheGame.getIndexCache(keyPrefix, keySuffix);
            if (null != re) {
                return re;
            }
            re = new FungoPageResultDto<>();
            List<EvaluationOutPageDto> relist = new ArrayList<EvaluationOutPageDto>();
            Game game = gameService.selectById(gameId);
            if (game == null) {
                return FungoPageResultDto.FungoPageResultDtoFactory.buildWarning(AbstractResultEnum.CODE_GAME_THREE.getKey(), AbstractResultEnum.CODE_GAME_THREE.getFailevalue());
            }
            Wrapper<GameEvaluation> commentWrapper = new EntityWrapper<>();
            commentWrapper.eq("game_id", gameId);
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
                    logger.error("objectMapper.readValue出现异常,cmmComment.getImages()="+cmmComment.getImages(),e);
                }
                ctem.setIs_recommend("1".equals(cmmComment.getIsRecommend()) ? true : false);
                ctem.setLike_num(cmmComment.getLikeNum() == null ? 0 : cmmComment.getLikeNum());
                ctem.setObjectId(cmmComment.getId());
                ctem.setPhone_model(cmmComment.getPhoneModel());
                ctem.setReply_count(cmmComment.getReplyNum()== null ? 0 : cmmComment.getReplyNum() );
                ctem.setUpdatedAt(DateTools.fmtDate(cmmComment.getUpdatedAt()));
                ctem.setTrait1( cmmComment.getTrait1()== null ? 0 : cmmComment.getTrait1() );
                ctem.setTrait2( cmmComment.getTrait2()== null ? 0 : cmmComment.getTrait2() );
                ctem.setTrait3( cmmComment.getTrait3()== null ? 0 : cmmComment.getTrait3() );
                ctem.setTrait4( cmmComment.getTrait4()== null ? 0 : cmmComment.getTrait4() );
                ctem.setTrait5( cmmComment.getTrait5() == null ? 0 : cmmComment.getTrait5());
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
                FungoPageResultDto<CmmCmtReplyDto> replyList = iEvaluateProxyService.getReplyDtoBysSelectPageOrderByCreatedAt(replyInputPageDto);
                int i = 0;
                if (replyList.getData() != null){
                    for (CmmCmtReplyDto reply : replyList.getData()) {
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
                        if (reply.getReplayToId()!= null){
                            MemberDto md = new MemberDto();
                            md.setId(reply.getReplayToId());
                            MemberDto m = iEvaluateProxyService.getMemberDtoBySelectOne(md);
                            if (m != null) {
                                replybean.setReplyToName(m.getUserName());
                            }
                        }
                        ctem.getReplys().add(replybean);
                    }
                    ctem.setReply_count(replyList.getCount());
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
            re.setData(relist);
            PageTools.pageToResultDto(re, page);
            //redis cache
            fungoCacheGame.excIndexCache(true, keyPrefix, keySuffix, re);
        }catch (Exception e){
            logger.error("获取游戏评价列表,游戏id="+gameId,e);
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildError("获取游戏评价列表异常");
        }
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

}
