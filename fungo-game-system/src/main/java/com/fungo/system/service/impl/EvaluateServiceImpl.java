package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.api.FungoPageResultDto;
import com.fungo.api.ResultDto;
import com.fungo.emoji.EmojiDealUtil;
import com.fungo.emoji.FilterEmojiUtil;
import com.fungo.framework.Setting;
import com.fungo.framework.dao.PageTools;
import com.fungo.system.service.IEvaluateService;
import com.fungo.tools.DateTools;
import com.fungo.validate.utils.CommonUtil;
import org.apache.commons.lang3.StringUtils;
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
//
//    @Autowired
//    private CmmCommentService commentService;
//    @Autowired
//    private CmmCommunityService communityService;
//    @Autowired
//    private CmmPostService postService;
//    @Autowired
//    private MemberService memberService;
//    @Autowired
//    private ICounterService counterService;
//    @Autowired
//    private MooMoodService moodService;
//    @Autowired
//    private MooMessageService messageServive;
//    @Autowired
//    BasActionService actionService;
//    @Autowired
//    private GameEvaluationService gameEvaluationService;
//    @Autowired
//    private GameService gameService;
//    @Autowired
//    private IPushService pushService;
//
//    @Autowired
//    private ReplyService replyService;
//
//    @Autowired
//    private IUserService userService;
//    @Autowired
//    private IGameProxy gameProxy;
//
//    @Autowired
//    private FungoCacheGame fungoCacheGame;
//
//    @Autowired
//    private FungoCacheArticle fungoCacheArticle;
//
//    @Autowired
//    private FungoCacheMood fungoCacheMood;
//
//    @Autowired
//    private FungoCacheComment fungoCacheComment;
//
//    //用户成长业务
//    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
//    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;
//
//    @Override
//    public ResultDto<CommentOut> addComment(String memberId, CommentInput commentInput, String appVersion) throws Exception {
//        ResultDto<CommentOut> re = new ResultDto<CommentOut>();
//        CommentOut t = new CommentOut();
//        String id = "";
//        int floor = 0;
//        String targetMemberId = "";
//        if (CommonUtil.isNull(commentInput.getContent())) {
//            return ResultDto.error("-1", "内容不能为空!");
//        }
//        if (1 == commentInput.getTarget_type()) {//文章评论
//            CmmComment comment = new CmmComment();
//            floor = commentService.selectCount(new EntityWrapper<CmmComment>().eq("post_id", commentInput.getTarget_id()));
//
//            //表情编码
//            String content = commentInput.getContent();
//            if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(content))) {
//                content = FilterEmojiUtil.encodeEmoji(content);
//            }
//            comment.setContent(content);
//
//            comment.setMemberId(memberId);
//            comment.setFloor(floor + 1);
//            comment.setLikeNum(0);
//            comment.setPostId(commentInput.getTarget_id());
//            comment.setCreatedAt(new Date());
//            comment.setUpdatedAt(new Date());
//            comment.setState(0);
//            CmmPost post = postService.selectById(commentInput.getTarget_id());
//            targetMemberId = post.getMemberId();
//            if (post.getMemberId().equals(memberId)) {
//                t.setIs_host(true);
//            } else {
//                t.setIs_host(false);
//            }
//            //最后回复时间
//            post.setLastReplyAt(new Date());
//            post.updateById();
//
//            commentService.insert(comment);
//
//            id = comment.getId();
//            counterService.addCounter("t_cmm_post", "comment_num", commentInput.getTarget_id());//增加评论数
//            //推送通知
//            gameProxy.addNotice(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_COMMENT, commentInput.getContent(), appVersion, "");
//        } else if (2 == commentInput.getTarget_type()) {//心情评论
//
//            MooMessage comment = new MooMessage();
//            //编码
//            String content = commentInput.getContent();
//            if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(content))) {
//                content = FilterEmojiUtil.encodeEmoji(content);
//            }
//            comment.setContent(content);
//
//
//            comment.setMemberId(memberId);
//            comment.setLikeNum(0);
//            comment.setMoodId(commentInput.getTarget_id());
//            comment.setCreatedAt(new Date());
//            comment.setUpdatedAt(new Date());
//            comment.setState(0);
//
//            messageServive.insert(comment);
//
//            MooMood mood = this.moodService.selectById(commentInput.getTarget_id());
//            targetMemberId = mood.getMemberId();
//            if (mood.getMemberId().equals(memberId)) {
//                t.setIs_host(true);
//            } else {
//                t.setIs_host(false);
//            }
//            id = comment.getId();
//            counterService.addCounter("t_moo_mood", "comment_num", commentInput.getTarget_id());//增加评论数
//            gameProxy.addNotice(Setting.MSG_TYPE_MOOD, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_COMMENT, commentInput.getContent(), appVersion, "");
//
//        }
//
//        t.setAuthor(userService.getAuthor(memberId));
//        t.setContent(CommonUtils.filterWord(commentInput.getContent()));
//        t.setFloor(floor + 1);
//        t.setObjectId(id);
//        t.setCreatedAt(DateTools.fmtDate(new Date()));
//        t.setReply_count(0);
//        t.setReply_more(false);
//        t.setReplys(new ArrayList<String>());
//        t.setUpdatedAt(DateTools.fmtDate(new Date()));
//        re.setData(t);
////		gameProxy.addScore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), commentInput.getTarget_type());
//
//        //完成任务 V2.4.6版本任务之前任务废弃
//        // int addTaskCore = gameProxy.addTaskCore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), commentInput.getTarget_type());
//
//        //V2.4.6版本任务
//        String tips = "";
//        //每日任务
//        //1 fungo币
//        Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());
//
//        //2 经验值
//        Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
//
//        if (null != resMapCoin && !resMapCoin.isEmpty()) {
//            if (null != resMapExp && !resMapExp.isEmpty()) {
//                boolean coinsSuccess = (boolean) resMapCoin.get("success");
//                boolean expSuccess = (boolean) resMapExp.get("success");
//                if (coinsSuccess && expSuccess) {
//                    tips = (String) resMapCoin.get("msg");
//                    tips += (String) resMapExp.get("msg");
//                } else {
//                    tips = (String) resMapCoin.get("msg");
//                }
//            }
//        }
//        //end
//
//        if (StringUtils.isNotBlank(tips)) {
//            re.show(tips);
//        } else {
//            re.show("发布成功");
//        }
//        //clear redis cache
//        //帖子/心情评论列表
//        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
//        // 帖子列表
//        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST, "", null);
//        //获取心情动态列表(v2.4)
//        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
//        //获取心情内容
//        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOOD_CONTENT_GET, "", null);
//        return re;
//    }
//
//    @Override
//    public ResultDto<CommentBeanOut> getCommentDetail(String memberId, String commentId) {
//
//        ResultDto<CommentBeanOut> re = new ResultDto<CommentBeanOut>();
//        CommentBeanOut bean = null;
//
//        //from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_POST_COMMENT_DETAIL + commentId;
//        String keySuffix = memberId;
//
//        bean = (CommentBeanOut) fungoCacheComment.getIndexCache(keyPrefix, keySuffix);
//        if (null != bean) {
//            re.setData(bean);
//            return re;
//        }
//
//        bean = new CommentBeanOut();
//
//        CmmComment comment = commentService.selectById(commentId);
//
//        if (comment == null) {
//            return ResultDto.error("-1", "该评论详情不存在");
//        }
//        bean.setObjectId(comment.getId());
//        if (!CommonUtil.isNull(comment.getContent())) {
//            //表情解码
//            String content = comment.getContent();
//            if (StringUtils.isNotBlank(content)) {
//                content = FilterEmojiUtil.decodeEmoji(content);
//            }
//            bean.setContent(CommonUtils.filterWord(content) );
//        }
//        bean.setCreatedAt(DateTools.fmtDate(comment.getCreatedAt()));
//        bean.setLike_num(comment.getLikeNum());
//        bean.setReply_num(comment.getReplyNum());
//        bean.setState(comment.getState());
//        bean.setUpdatedAt(DateTools.fmtDate(comment.getUpdatedAt()));
//        bean.setAuthor(this.userService.getAuthor(comment.getMemberId()));
//        bean.setFloor(comment.getFloor());
//        if ("".equals(memberId) || memberId == null) {
//            bean.setIs_liked(false);
//        } else {
//            int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", comment.getId()).eq("member_id", memberId));
//            bean.setIs_liked(liked > 0 ? true : false);
//        }
//        re.setData(bean);
//
//        //redis cahce
//        fungoCacheComment.excIndexCache(true, keyPrefix, keySuffix, bean);
//        return re;
//    }
//
//
//    public ResultDto<CommentBeanOut> getMoodMessageDetail(String memberId, String messageId) {
//
//        ResultDto<CommentBeanOut> re = new ResultDto<CommentBeanOut>();
//        CommentBeanOut bean = null;
//
//        //from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MOOD_COMMENT_DETAIL + messageId;
//        String keySuffix = memberId;
//
//        bean = (CommentBeanOut) fungoCacheComment.getIndexCache(keyPrefix, keySuffix);
//        if (null != bean) {
//            re.setData(bean);
//            return re;
//        }
//
//        bean = new CommentBeanOut();
//        MooMessage comment = messageServive.selectById(messageId);
//        if (comment == null) {
//            return ResultDto.error("-1", "该评论详情不存在");
//        }
//        bean.setObjectId(comment.getId());
//        if (!CommonUtil.isNull(comment.getContent())) {
//            bean.setContent(CommonUtils.filterWord(comment.getContent()));
//        }
//        bean.setCreatedAt(DateTools.fmtDate(comment.getCreatedAt()));
//        bean.setLike_num(comment.getLikeNum());
//        bean.setReply_num(comment.getReplyNum());
//        bean.setState(comment.getState());
//        bean.setUpdatedAt(DateTools.fmtDate(comment.getUpdatedAt()));
//        bean.setAuthor(this.userService.getAuthor(comment.getMemberId()));
//        //是否点赞
//        if ("".equals(memberId) || memberId == null) {
//            bean.setIs_liked(false);
//        } else {
//            int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", comment.getId()).eq("member_id", memberId));
//            bean.setIs_liked(liked > 0 ? true : false);
//        }
//        re.setData(bean);
//
//        //redis cache
//        fungoCacheComment.excIndexCache(true, keyPrefix, keySuffix, bean);
//
//        return re;
//    }
//
//
//    @Override
//    public FungoPageResultDto<CommentOutPageDto> getCommentList(String memberId, CommentInputPageDto commentpage) {
//
//        FungoPageResultDto<CommentOutPageDto> re = null;
//
//        String id = "";
//        if (StringUtils.isNotBlank(commentpage.getMood_id())) {
//            id = commentpage.getMood_id();
//        } else if (StringUtils.isNotBlank(commentpage.getPost_id())) {
//            id = commentpage.getPost_id();
//        }
//
//        //from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS;
//        String keySuffix = id + JSON.toJSONString(commentpage);
//        re = (FungoPageResultDto<CommentOutPageDto>) fungoCacheArticle.getIndexCache(keyPrefix, keySuffix);
//
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<CommentOutPageDto>();
//        List<CommentOutPageDto> relist = new ArrayList<CommentOutPageDto>();
//        re.setData(relist);
//
//        if (commentpage.getMood_id() != null && !"".equals(commentpage.getMood_id())) {
//
//            MooMood mood = moodService.selectById(commentpage.getMood_id());
//
//            if (mood == null) {
//                return FungoPageResultDto.error("-1", "心情不存在");
//            }
//
//            Wrapper<MooMessage> entityWrapper = new EntityWrapper<MooMessage>();
//            entityWrapper.eq("mood_id", commentpage.getMood_id());
//            entityWrapper.and("state != {0}", -1);
//
//            if ("host".equals(commentpage.getFilter())) {//社区主
//                entityWrapper.eq("member_id", mood.getMemberId());
//            }
//
//            if (commentpage.getSort() == 0 || commentpage.getSort() == 1) {//排序
//                entityWrapper.orderBy("created_at", true);
//            } else if (commentpage.getSort() == 2) {
//                entityWrapper.orderBy("created_at", false);
//            } else if (commentpage.getSort() == 3) {
//                entityWrapper.orderBy("like_num", true);
//            } else if (commentpage.getSort() == 4) {
//                entityWrapper.orderBy("like_num", false);
//            }
//
//
//            Page p = new Page<>(commentpage.getPage(), commentpage.getLimit());
//            Page<MooMessage> page = messageServive.selectPage(p, entityWrapper);
//            List<MooMessage> list = page.getRecords();
//
//            for (MooMessage mooMessage : list) {
//                CommentOutPageDto ctem = new CommentOutPageDto();
////				if(!CommonUtil.isNull(mooMessage.getContent())) {
////
////				}
//
//                String content = mooMessage.getContent();
//                //表情解码
//                if (StringUtils.isNotBlank(content)) {
//                    content = FilterEmojiUtil.decodeEmoji(content);
//                }
//
//                ctem.setContent(CommonUtils.filterWord(content));
//                ctem.setCreatedAt(DateTools.fmtDate(mooMessage.getCreatedAt()));
//                ctem.setFloor(0);
//                ctem.setObjectId(mooMessage.getId());
//                ctem.setReply_count(mooMessage.getReplyNum());
//                ctem.setUpdatedAt(DateTools.fmtDate(mooMessage.getUpdatedAt()));
//                if (mooMessage.getMemberId().equals(mood.getMemberId())) {
//                    ctem.setIs_host(true);
//                }
//                ctem.setLike_num(mooMessage.getLikeNum());
//
//                AuthorBean author = userService.getAuthor(mooMessage.getMemberId());
//                if (null != author) {
//                    ctem.setAuthor(author);
//                }
//
//                //回复
//                Page<Reply> replyList = replyService.selectPage(new Page<>(1, 3), new EntityWrapper<Reply>().eq("target_id", mooMessage.getId()).ne("state", -1).orderBy("created_at", true));
//                int i = 0;
//                for (Reply reply : replyList.getRecords()) {
//                    i = i + 1;
//                    if (i == 3) {
//                        ctem.setReply_more(true);
//                        break;
//                    }
//                    ReplyBean replybean = new ReplyBean();
//
//                    AuthorBean replyAuthor = userService.getAuthor(reply.getMemberId());
//                    if (null != replyAuthor) {
//                        replybean.setAuthor(replyAuthor);
//                    }
//
//                    //表情解码
//                    String coententReply = reply.getContent();
//                    if (StringUtils.isNotBlank(coententReply)) {
//                        coententReply = FilterEmojiUtil.decodeEmoji(coententReply);
//                    }
//                    replybean.setContent(CommonUtils.filterWord(coententReply));
//                    replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
//                    replybean.setObjectId(reply.getId());
//                    replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
//                    replybean.setReplyToId(reply.getReplayToId());
//                    Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
//                    if (m != null) {
//                        replybean.setReplyToName(m.getUserName());
//                    }
//                    ctem.getReplys().add(replybean);
//                }
//                //是否点赞
//                if ("".equals(memberId) || memberId == null) {
//                    ctem.setIs_liked(false);
//                } else {
//                    int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", mooMessage.getId()).eq("member_id", memberId));
//                    ctem.setIs_liked(liked > 0 ? true : false);
//                }
//                relist.add(ctem);
//            }
//            re.setData(relist);
//            PageTools.pageToResultDto(re, page);
//
//        } else if (commentpage.getPost_id() != null && !"".equals(commentpage.getPost_id())) {
//            CmmPost post = postService.selectById(commentpage.getPost_id());
//            if (post == null) {
//                return FungoPageResultDto.error("-1", "帖子不存在");
//            }
//            Wrapper<CmmComment> commentWrapper = new EntityWrapper<CmmComment>();
//            commentWrapper.eq("post_id", commentpage.getPost_id());
//            commentWrapper.and("state != {0}", -1);
//            if ("host".equals(commentpage.getFilter())) {//社区主
//                commentWrapper.eq("member_id", post.getMemberId());
//            }
//            if (commentpage.getSort() == 0 || commentpage.getSort() == 1) {//排序
//                commentWrapper.orderBy("created_at", true); //时间正序
//            } else if (commentpage.getSort() == 2) {
//                commentWrapper.orderBy("created_at", false); //时间倒序
//            } else if (commentpage.getSort() == 3) {
//                commentWrapper.orderBy("like_num", true); //点赞数 正序
//            } else if (commentpage.getSort() == 4) {
//                commentWrapper.orderBy("like_num", false); //点赞数 倒序
//            }
//
//            Page<CmmComment> page = this.commentService.selectPage(new Page<>(commentpage.getPage(), commentpage.getLimit()), commentWrapper);
//            List<CmmComment> list = page.getRecords();
//
//            for (CmmComment cmmComment : list) {
//                CommentOutPageDto ctem = new CommentOutPageDto();
//
//                //表情解码
//                String coententReply = cmmComment.getContent();
//                if (StringUtils.isNotBlank(coententReply)) {
//                    coententReply = FilterEmojiUtil.decodeEmoji(coententReply);
//                }
//                ctem.setContent(CommonUtils.filterWord(coententReply));
//                ctem.setCreatedAt(DateTools.fmtDate(cmmComment.getCreatedAt()));
//                ctem.setFloor(cmmComment.getFloor());
//                ctem.setObjectId(cmmComment.getId());
//                ctem.setReply_count(cmmComment.getReplyNum());
//                ctem.setUpdatedAt(DateTools.fmtDate(cmmComment.getUpdatedAt()));
//                if (post.getMemberId().equals(cmmComment.getMemberId())) {
//                    ctem.setIs_host(true);
//                }
//                ctem.setLike_num(cmmComment.getLikeNum());
//                AuthorBean author = userService.getAuthor(cmmComment.getMemberId());
//                if (null != author) {
//                    ctem.setAuthor(author);
//                }
//                Page<Reply> replyList = replyService.selectPage(new Page<>(1, 3), new EntityWrapper<Reply>().eq("target_id", cmmComment.getId()).ne("state", -1).orderBy("created_at", true));
//                int i = 0;
//                //回复
//                for (Reply reply : replyList.getRecords()) {
//                    i = i + 1;
//                    if (i == 3) {
//                        ctem.setReply_more(true);
//                        break;
//                    }
//                    ReplyBean replybean = new ReplyBean();
//
//                    AuthorBean authorReply = userService.getAuthor(reply.getMemberId());
//                    if (null != authorReply) {
//                        replybean.setAuthor(authorReply);
//                    }
//
//                    //表情解码
//                    String coententReply2 = reply.getContent();
//                    if (StringUtils.isNotBlank(coententReply2)) {
//                        coententReply2 = FilterEmojiUtil.decodeEmoji(coententReply2);
//                    }
//                    replybean.setContent(CommonUtils.filterWord(coententReply2));
//                    replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
//                    replybean.setObjectId(reply.getId());
//                    replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
//                    replybean.setLike_num(reply.getLikeNum());
//                    replybean.setReplyToId(reply.getReplayToId());
//                    Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
//                    if (m != null) {
//                        replybean.setReplyToName(m.getUserName());
//                    }
//                    ctem.getReplys().add(replybean);
//
//                }
//                //是否点赞
//                if ("".equals(memberId) || memberId == null) {
//                    ctem.setIs_liked(false);
//                } else {
//                    int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", cmmComment.getId()).eq("member_id", memberId));
//                    ctem.setIs_liked(liked > 0 ? true : false);
//                }
//                relist.add(ctem);
//            }
//            re.setData(relist);
//            PageTools.pageToResultDto(re, page);
//        }
//
//        //redis cache
//        fungoCacheArticle.excIndexCache(true, keyPrefix, keySuffix, re);
//
//        return re;
//    }
//
//    @Override
//    @Transactional
//    public ResultDto<EvaluationBean> addGameEvaluation(String memberId, EvaluationInput commentInput) throws Exception {
////		if(CommonUtil.isNull(commentInput.getContent())) {
////			return ResultDto.error("-1","内容不能为空!");
////		}
//        ResultDto<EvaluationBean> re = new ResultDto<EvaluationBean>();
//        EvaluationBean t = new EvaluationBean();
//        GameEvaluation evaluation = gameEvaluationService.selectOne(new EntityWrapper<GameEvaluation>().eq("member_id", memberId).eq("game_id", commentInput.getTarget_id()).eq("state", 0));
//
//        //int times = -1;
//        String tips = "";
//
//        if (commentInput.getRating() == null) {
//            return ResultDto.error("-1", "请对该游戏进行评分");
//        }
//        if (commentInput.getRating() < 1 || commentInput.getRating() > 10) {
//            return ResultDto.error("-1", "评分只能在1-10之间");
//        }
//
//        if (commentInput.getTrait1() < 0 || commentInput.getTrait1() > 10) {
//            return null;
//        }
//        if (commentInput.getTrait2() < 0 || commentInput.getTrait2() > 10) {
//            return null;
//        }
//        if (commentInput.getTrait3() < 0 || commentInput.getTrait3() > 10) {
//            return null;
//        }
//        if (commentInput.getTrait4() < 0 || commentInput.getTrait4() > 10) {
//            return null;
//        }
//        if (commentInput.getTrait5() < 0 || commentInput.getTrait5() > 10) {
//            return null;
//        }
//
//
//        if (evaluation == null) {//用户未对该游戏发表过评论，新增
//            evaluation = new GameEvaluation();
//            //2.4
//            //分数限制
//            evaluation.setRating(commentInput.getRating());
//
//            evaluation.setTrait1(commentInput.getTrait1() == 0 ? null : commentInput.getTrait1());
//            evaluation.setTrait2(commentInput.getTrait2() == 0 ? null : commentInput.getTrait2());
//            evaluation.setTrait3(commentInput.getTrait3() == 0 ? null : commentInput.getTrait3());
//            evaluation.setTrait4(commentInput.getTrait4() == 0 ? null : commentInput.getTrait4());
//            evaluation.setTrait5(commentInput.getTrait5() == 0 ? null : commentInput.getTrait5());
//
//            evaluation.setContent(commentInput.getContent());
//            evaluation.setCreatedAt(new Date());
//            evaluation.setGameId(commentInput.getTarget_id());
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                if (commentInput.getImages() != null) {
//                    evaluation.setImages(objectMapper.writeValueAsString(commentInput.getImages()));
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            evaluation.setIsRecommend("1");
//            evaluation.setLikeNum(0);
//            evaluation.setMemberId(memberId);
//            evaluation.setPhoneModel(commentInput.getPhone_model());
//            evaluation.setState(0);
//            evaluation.setUpdatedAt(new Date());
//            //游戏名
//            Game game = gameService.selectOne(Condition.create().setSqlSelect("id,name").eq("id", commentInput.getTarget_id()));
//            if (game != null) {
//                evaluation.setGameName(game.getName());
//            }
//
//            gameEvaluationService.insert(evaluation);
////			if(commentInput.isIs_recommend()) {
//            counterService.addCounter("t_game", "comment_num", commentInput.getTarget_id());//增加评论数
////			}else {
////				counterService.addCounter("t_game", "unrecommend_num", commentInput.getTarget_id());//增加评论数
////			}
//
////			gameProxy.addScore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_GAME);
//
//            //V2.4.6版本任务之前任务 废弃
//            //times = gameProxy.addTaskCore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_GAME);
//
//            //V2.4.6版本任务
//            //1 fungo币
//            Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());
//
//            //2 经验值
//            Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
//
//            if (null != resMapCoin && !resMapCoin.isEmpty()) {
//                if (null != resMapExp && !resMapExp.isEmpty()) {
//                    boolean coinsSuccess = (boolean) resMapCoin.get("success");
//                    boolean expSuccess = (boolean) resMapExp.get("success");
//                    if (coinsSuccess && expSuccess) {
//                        tips = (String) resMapCoin.get("msg");
//                        tips += (String) resMapExp.get("msg");
//                    } else {
//                        tips = (String) resMapCoin.get("msg");
//                    }
//                }
//            }
//            //end
//
//
//            //产生一个action
//            BasAction action = new BasAction();
//            action.setCreatedAt(new Date());
//            action.setTargetId(evaluation.getId());
//            action.setTargetType(6);
//            action.setType(8);
//            action.setMemberId(memberId);
//            action.setState(0);
//            action.setUpdatedAt(new Date());
//            this.actionService.insertAllColumn(action);
//        } else {//用户对该游戏发表过评论，修改
//            //2.4
//            evaluation.setRating(commentInput.getRating());
//
//            evaluation.setTrait1(commentInput.getTrait1() == 0 ? null : commentInput.getTrait1());
//            evaluation.setTrait2(commentInput.getTrait2() == 0 ? null : commentInput.getTrait2());
//            evaluation.setTrait3(commentInput.getTrait3() == 0 ? null : commentInput.getTrait3());
//            evaluation.setTrait4(commentInput.getTrait4() == 0 ? null : commentInput.getTrait4());
//            evaluation.setTrait5(commentInput.getTrait5() == 0 ? null : commentInput.getTrait5());
//
//            evaluation.setContent(commentInput.getContent());
//            Date date = new Date();
//            evaluation.setCreatedAt(date);
//            evaluation.setGameId(commentInput.getTarget_id());
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                if (commentInput.getImages() != null) {
//                    evaluation.setImages(objectMapper.writeValueAsString(commentInput.getImages()));
//                }
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
////			String r = commentInput.isIs_recommend()?"1":"0";
////			String d = evaluation.getIsRecommend();
////			if(!r.equals(d)) {
////				if("1".equals(d)) {
////					counterService.subCounter("t_game", "recommend_num", commentInput.getTarget_id());//增加评论数
////					counterService.addCounter("t_game", "unrecommend_num", commentInput.getTarget_id());
////				}else {
////					counterService.addCounter("t_game", "recommend_num", commentInput.getTarget_id());
////					counterService.subCounter("t_game", "unrecommend_num", commentInput.getTarget_id());
////				}
////				evaluation.setIsRecommend(r);
////			}
////			evaluation.setLikeNum(0);
//            evaluation.setMemberId(memberId);
//            evaluation.setPhoneModel(commentInput.getPhone_model());
//            evaluation.setState(0);
//            evaluation.setUpdatedAt(date);
//            evaluation.updateById();
//            BasAction action = actionService.selectOne(new EntityWrapper<BasAction>()
//                    .eq("member_id", memberId)
//                    .eq("target_id", evaluation.getId())
//                    .eq("target_type", 6)
//                    .eq("type", 8)
//                    .eq("state", 0));
//            if (action != null) {
//                action.setUpdatedAt(date);
//                actionService.updateAllColumnById(action);
//            }
//
//
//        }
//
//        //返回评论内容
//        t.setAuthor(userService.getAuthor(memberId));
//        t.setContent(CommonUtils.filterWord(commentInput.getContent()));
//        t.setObjectId(evaluation.getId());
//        t.setCreatedAt(DateTools.fmtDate(new Date()));
//        t.setReply_count(0);
//        t.setRating(commentInput.getRating());
//        t.setReply_more(false);
//        t.setImages(commentInput.getImages());
//        t.setReplys(new ArrayList<String>());
//        t.setUpdatedAt(DateTools.fmtDate(new Date()));
//        t.setPhone_model(commentInput.getPhone_model());
//        t.setIs_recommend(commentInput.isIs_recommend());
//        re.setData(t);
//
//        if (StringUtils.isNotBlank(tips)) {
//            re.show(tips);
//        } else {
//            re.show("发布成功");
//        }
//
//        //清除该用户的评论游戏redis cache
//        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + memberId, "", null);
//        //清除 我的游戏评测(2.4.3)
//        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_EVALUATIONLIST , memberId, null);
//        //游戏详情
//        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + commentInput.getTarget_id(), "", null);
//        // 获取最近评论的游戏
//        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + memberId, "", null);
//
//        //游戏评价列表
//        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS, "", null);
//        return re;
//    }
//
//    @Override
//    public ResultDto<EvaluationOutBean> getEvaluationDetail(String memberId, String commentId) {
//        ResultDto<EvaluationOutBean> re = new ResultDto<EvaluationOutBean>();
//        EvaluationOutBean bean = new EvaluationOutBean();
//        GameEvaluation evaluation = gameEvaluationService.selectById(commentId);
//        if (evaluation == null) {
//            return ResultDto.error("-1", "该评论详情不存在");
//        }
//        //2.4
//        if (evaluation.getRating() != null) {
//            bean.setRating(evaluation.getRating());
//        }
//        bean.setSort(evaluation.getSort());
//
//        bean.setAuthor(this.userService.getAuthor(evaluation.getMemberId()));
//        bean.setContent(CommonUtils.filterWord(evaluation.getContent()));
//        bean.setCreatedAt(DateTools.fmtDate(evaluation.getCreatedAt()));
//        ObjectMapper objectMapper = new ObjectMapper();
//        ArrayList<String> imgs = null;
//        try {
//            if (evaluation.getImages() != null) {
//                imgs = (ArrayList<String>) objectMapper.readValue(evaluation.getImages(), ArrayList.class);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (imgs == null) {
//            imgs = new ArrayList<String>();
//        }
//        bean.setGameId(evaluation.getGameId());
//        bean.setImages(imgs);
//        bean.setIs_recommend("1".equals(evaluation.getIsRecommend()) ? true : false);
//        bean.setLike_num(evaluation.getLikeNum());
//        bean.setPhone_model(evaluation.getPhoneModel());
//        bean.setReply_count(evaluation.getReplyNum());
//        bean.setState(evaluation.getState());
//        bean.setUpdatedAt(DateTools.fmtDate(evaluation.getUpdatedAt()));
//        bean.setObjectId(evaluation.getId());
//        //是否点赞
//        if ("".equals(memberId) || memberId == null) {
//            bean.setIs_liked(false);
//        } else {
//            int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", evaluation.getId()).eq("member_id", memberId));
//            bean.setIs_liked(liked > 0 ? true : false);
//        }
//        re.setData(bean);
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<EvaluationOutPageDto> getEvaluationList(String memberId, EvaluationInputPageDto pageDto) {
//
//        FungoPageResultDto<EvaluationOutPageDto> re = null;
//
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS;
//        String keySuffix = pageDto.getGame_id() + JSON.toJSONString(pageDto);
//
//        re = (FungoPageResultDto<EvaluationOutPageDto>) fungoCacheGame.getIndexCache(keyPrefix, keySuffix);
//        if (null != re) {
//            return re;
//        }
//        re = new FungoPageResultDto<EvaluationOutPageDto>();
//        List<EvaluationOutPageDto> relist = new ArrayList<EvaluationOutPageDto>();
//        Game game = gameService.selectById(pageDto.getGame_id());
//        if (game == null) {
//            return FungoPageResultDto.error("-1", "游戏不存在");
//        }
//        Wrapper<GameEvaluation> commentWrapper = new EntityWrapper<GameEvaluation>();
//        commentWrapper.eq("game_id", pageDto.getGame_id());
//        commentWrapper.and("state !={0}", -1);
//        if ("mine".equals(pageDto.getFilter())) {//社区主
//            commentWrapper.eq("member_id", memberId);
//        }
//        //pageDto.getSort()==0||
//        if (pageDto.getSort() == 1) {//排序
//            commentWrapper.orderBy("created_at", true);
//        } else if (pageDto.getSort() == 0 || pageDto.getSort() == 2) {
//            commentWrapper.orderBy("created_at", false);
//        } else if (pageDto.getSort() == 3) {
//            commentWrapper.groupBy("id").orderBy("sum(like_num+reply_num)", true);//按照点赞数和回复数排序
//        } else if (pageDto.getSort() == 4) {
//            commentWrapper.groupBy("id").orderBy("sum(like_num+reply_num)", false);
//        }
//
//        Page<GameEvaluation> page = this.gameEvaluationService.selectPage(new Page<>(pageDto.getPage(), pageDto.getLimit()), commentWrapper);
//        List<GameEvaluation> list = page.getRecords();
//
//        for (GameEvaluation cmmComment : list) {
//            EvaluationOutPageDto ctem = new EvaluationOutPageDto();
//            ctem.setContent(CommonUtils.filterWord(cmmComment.getContent()));
//            ctem.setCreatedAt(DateTools.fmtDate(cmmComment.getCreatedAt()));
//            ObjectMapper objectMapper = new ObjectMapper();
//            try {
//                ctem.setImages(objectMapper.readValue(cmmComment.getImages(), ArrayList.class));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            ctem.setIs_recommend("1".equals(cmmComment.getIsRecommend()) ? true : false);
//            ctem.setLike_num(cmmComment.getLikeNum());
//            ctem.setObjectId(cmmComment.getId());
//            ctem.setPhone_model(cmmComment.getPhoneModel());
//            ctem.setReply_count(cmmComment.getReplyNum());
//            ctem.setUpdatedAt(DateTools.fmtDate(cmmComment.getUpdatedAt()));
//            ctem.setAuthor(this.userService.getAuthor(cmmComment.getMemberId()));
//            //2.4 评分
//            if (cmmComment.getRating() != null) {
//                ctem.setRating(cmmComment.getRating());
//            }
//            //回复
//            Page<Reply> replyList = replyService.selectPage(new Page<>(1, 3), new EntityWrapper<Reply>().eq("target_id", cmmComment.getId()).eq("state", 0).orderBy("created_at", true));
//            int i = 0;
//            for (Reply reply : replyList.getRecords()) {
//                i = i + 1;
//                if (i == 3) {
//                    ctem.setReply_more(true);
//                    break;
//                }
//                ReplyBean replybean = new ReplyBean();
//                replybean.setAuthor(this.userService.getAuthor(reply.getMemberId()));
//                replybean.setContent(CommonUtils.filterWord(reply.getContent()));
//                replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
//                replybean.setObjectId(reply.getId());
//                replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
//                replybean.setLike_num(reply.getLikeNum());
//                replybean.setReplyToId(reply.getReplayToId());
//                Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
//                if (m != null) {
//                    replybean.setReplyToName(m.getUserName());
//                }
//                ctem.getReplys().add(replybean);
//            }
//
//            //是否点赞
//            if ("".equals(memberId) || memberId == null) {
//                ctem.setIs_liked(false);
//            } else {
//                int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", cmmComment.getId()).eq("member_id", memberId));
//                ctem.setIs_liked(liked > 0 ? true : false);
//            }
//            relist.add(ctem);
//        }
//        PageTools.pageToResultDto(re, page);
//        re.setData(relist);
//
//        //redis cache
//        fungoCacheGame.excIndexCache(true, keyPrefix, keySuffix, re);
//
//        return re;
//    }
//
//    @Override
//    @Transactional
//    public ResultDto<ReplyOutBean> addReply(String memberId, ReplyInputBean replyInput, String appVersion) throws Exception {
//        if (CommonUtil.isNull(replyInput.getContent())) {
//            return ResultDto.error("-1", "内容不能为空!");
//        }
//        ResultDto<ReplyOutBean> re = new ResultDto<ReplyOutBean>();
//        ReplyOutBean t = new ReplyOutBean();
//        Reply reply = new Reply();
//        reply.setContent(replyInput.getContent());
//        reply.setCreatedAt(new Date());
//        reply.setLikeNum(0);
//        reply.setMemberId(memberId);
//        reply.setReplayToId(replyInput.getReply_to());
//        reply.setTargetType(replyInput.getTarget_type());
//        reply.setTargetId(replyInput.getTarget_id());
//        reply.setUpdatedAt(new Date());
//        reply.setReplyToContentId(replyInput.getReply_to_content_id());
//        Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
//        if (m != null) {
//            reply.setReplyName(m.getUserName());
//        }
//        replyService.insert(reply);
//
//        if (replyInput.getTarget_type() == 5) {//回复文章评论
//            counterService.addCounter("t_cmm_comment", "reply_num", replyInput.getTarget_id());//增加评论数
//            //最后回复时间
//            CmmComment comment = commentService.selectById(replyInput.getTarget_id());
//            if (comment != null) {
//                CmmPost post = postService.selectById(comment.getPostId());
//                if (post != null) {
//                    post.setLastReplyAt(new Date());
//                    post.updateById();
//                }
//            }
//            try {//发送通知
//                if(CommonUtil.isNull(replyInput.getReply_to_content_id())){ //只有没有三级评论时才会发送消息
//                    this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_GAME, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_COMMENT, replyInput.getContent(), "", "");
//                }
//                if (!CommonUtil.isNull(replyInput.getReply_to_content_id())) {
//                    this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_RE, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_COMMENT, replyInput.getContent(), appVersion, replyInput.getReply_to());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (replyInput.getTarget_type() == 6) {//回复游戏评论
//            counterService.addCounter("t_game_evaluation", "reply_num", replyInput.getTarget_id());//增加评论数
//            try {
//                if(CommonUtil.isNull(replyInput.getReply_to_content_id())) { //只有没有三级评论时才会发送消息
//                    this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_GAME, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_EVALUATION, replyInput.getContent(), "", "");
//                }
//                if (!CommonUtil.isNull(replyInput.getReply_to_content_id())) {
//                    this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_RE, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_EVALUATION, replyInput.getContent(), appVersion, replyInput.getReply_to());
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else if (replyInput.getTarget_type() == 8) {//回复心情评论
//            counterService.addCounter("t_moo_message", "reply_num", replyInput.getTarget_id());//增加评论数
//            if(CommonUtil.isNull(replyInput.getReply_to_content_id())) { //只有没有三级评论时才会发送消息
//                this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_GAME, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_MESSAGE, replyInput.getContent(), appVersion, "");
//            }
//            if (!CommonUtil.isNull(replyInput.getReply_to_content_id())) {
//                this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_RE, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_MESSAGE, replyInput.getContent(), appVersion, replyInput.getReply_to());
//            }
//        }
//
////		gameProxy.addScore(Setting.ACTION_TYPE_REPLY, memberId, replyInput.getTarget_id(), replyInput.getTarget_type());
//        //完成任务 获取积分 V2.4.6版本任务之前任务废弃
//        //int count = gameProxy.addTaskCore(Setting.ACTION_TYPE_REPLY, memberId, replyInput.getTarget_id(), replyInput.getTarget_type());
//
//        //V2.4.6版本任务
//        String tips = "";
//
//        //1 fungo币
//        Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());
//
//        //2 经验值
//        Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
//                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
//
//        if (null != resMapCoin && !resMapCoin.isEmpty()) {
//            if (null != resMapExp && !resMapExp.isEmpty()) {
//                boolean coinsSuccess = (boolean) resMapCoin.get("success");
//                boolean expSuccess = (boolean) resMapExp.get("success");
//                if (coinsSuccess && expSuccess) {
//                    tips = (String) resMapCoin.get("msg");
//                    tips += (String) resMapExp.get("msg");
//                } else {
//                    tips = (String) resMapCoin.get("msg");
//                }
//            }
//        }
//        //end
//
//
//        t.setAuthor(this.userService.getAuthor(memberId));
//        t.setContent(CommonUtils.filterWord(replyInput.getContent()));
//        t.setCreatedAt(DateTools.fmtDate(new Date()));
//        t.setObjectId(reply.getId());
//        t.setTarget_id(replyInput.getTarget_id());
//        t.setTarget_type(replyInput.getTarget_type());
//        if (replyInput.getReply_to() != null) {
//            t.setReply_to(this.userService.getAuthor(replyInput.getReply_to()));
//        }
//        t.setUpdatedAt(DateTools.fmtDate(new Date()));
//        re.setData(t);
//
//        if (StringUtils.isNotBlank(tips)) {
//            re.show(tips);
//        } else {
//            re.show("发布成功");
//        }
//        //clear redis cache
//        //帖子/心情评论列表
//        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
//        //游戏评价列表
//        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS, "", null);
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<ReplyOutPageDto> getReplyList(String memberId, ReplyInputPageDto pageDto) {
//        FungoPageResultDto<ReplyOutPageDto> re = new FungoPageResultDto<ReplyOutPageDto>();
//        List<ReplyOutPageDto> relist = new ArrayList<ReplyOutPageDto>();
//        re.setData(relist);
//
//        Wrapper<Reply> commentWrapper = new EntityWrapper<Reply>();
//        commentWrapper.eq("target_id", pageDto.getTarget_id()).eq("state", 0);
//
//        if (pageDto.getSort() == 0 || pageDto.getSort() == 1) {//排序
//            commentWrapper.orderBy("created_at", true);
//        } else if (pageDto.getSort() == 2) {
//            commentWrapper.orderBy("created_at", false);
//        } else if (pageDto.getSort() == 3) {
//            commentWrapper.orderBy("like_num", true);
//        } else if (pageDto.getSort() == 4) {
//            commentWrapper.orderBy("like_num", false);
//        }
//        Page<Reply> page = this.replyService.selectPage(new Page<Reply>(pageDto.getPage(), pageDto.getLimit()), commentWrapper);
//        List<Reply> rlist = page.getRecords();
//
//        for (Reply reply : rlist) {
//
//            ReplyOutPageDto bean = new ReplyOutPageDto();
//
//            bean.setAuthor(this.userService.getAuthor(reply.getMemberId()));
//
//            //表情解码
//            String content = reply.getContent();
//            if (StringUtils.isNotBlank(content)) {
//                content = FilterEmojiUtil.decodeEmoji(content);
//            }
//            bean.setContent(CommonUtils.filterWord(content));
//
//            bean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
//            bean.setLike_num(reply.getLikeNum());
//            bean.setObjectId(reply.getId());
//            if (reply.getReplayToId() != null) {
//                bean.setReply_to(this.userService.getAuthor(reply.getReplayToId()));
//            } else {
////				AuthorBean bean1 =new AuthorBean();
////				bean.setReply_to(bean1);
//            }
//            if ("".equals(memberId) || memberId == null) {
//                bean.setIs_liked(false);
//            } else {
//                int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", reply.getId()).eq("member_id", memberId));
//                bean.setIs_liked(liked > 0 ? true : false);
//            }
//            bean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
//            relist.add(bean);
//        }
//        PageTools.pageToResultDto(re, page);
//        return re;
//    }
//
//    @Override
//    public ResultDto<EvaluationOutBean> anliEvaluationDetail(String memberId, String evaId) {
//        //根据sort和时间来排行
//        ResultDto<EvaluationOutBean> result = this.getEvaluationDetail(memberId, evaId);
//        EvaluationOutBean eva = result.getData();
//        Game game = gameService.selectById(eva.getGameId());
//        if (game != null) {
//            eva.setGameIcon(game.getIcon());
//            eva.setGameIntro(game.getIntro());
//            eva.setGameName(game.getName());
//        }
//        int sort = eva.getSort();
//        String id = eva.getObjectId();
//        //上一评论
//        GameEvaluation pre = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).gt("created_at", eva.getCreatedAt()).ne("id", id).orderBy("concat(sort,created_at)").last("limit 1"));
////		GameEvaluation pree = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}",-1).le("sort",sort).ne("id", id).orderBy("concat(sort,created_at)",false).last("limit 1"));
//        //下一评论
//        GameEvaluation next = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).le("created_at", eva.getCreatedAt()).ne("id", id).orderBy("concat(sort,created_at)", false).last("limit 1"));
//        if (pre != null) {
//            eva.setPreEvaId(pre.getId());
//        }//
//        if (next != null) {
//            eva.setNextEvaId(next.getId());
//        }
//
//        return result;
//    }


    @Override
    public Set<String> getGameEvaluationHotAndAnliCount(String mb_id, String startDate, String endDate) {
//        Set<String> gameEvaSet = null;
////        try {
////            EntityWrapper<GameEvaluation> evaluationEntityWrapper = new EntityWrapper<>();
////            evaluationEntityWrapper.eq("member_id", mb_id);
////            evaluationEntityWrapper.between("updated_at", startDate, endDate);
////            evaluationEntityWrapper.eq("state", 0);
////            //type  0:普通 1:热门 2:精华
////            evaluationEntityWrapper.in("type", new Integer[]{1, 2});
////
////            List<GameEvaluation> gameEvaluationsList = gameEvaluationService.selectList(evaluationEntityWrapper);
////            if (null != gameEvaluationsList && !gameEvaluationsList.isEmpty()) {
////
////                gameEvaSet = new HashSet<String>();
////
////                for (GameEvaluation gameEvaluation : gameEvaluationsList) {
////                    gameEvaSet.add(gameEvaluation.getId());
////                }
////            }
////
////        } catch (Exception ex) {
////            ex.printStackTrace();
////            logger.error("查看用户在指定时间段内游戏评论上热门和安利墙的文章数量出现异常", ex);
////        }
////        logger.info("查看用户在指定时间段内游戏评论上热门和安利墙的文章数量-gameEvaSet:{}", gameEvaSet);
////        return gameEvaSet;
        return null;
    }


    //获取回复信息
//    private ReplyBean getReplyBean(Reply reply) {
//        ReplyBean replybean = new ReplyBean();
//        replybean.setAuthor(this.userService.getAuthor(reply.getMemberId()));
//        replybean.setContent(CommonUtils.filterWord(reply.getContent()));
//        replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
//        replybean.setObjectId(reply.getId());
//        replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
//        replybean.setLike_num(reply.getLikeNum());
//        replybean.setReplyToId(reply.getReplayToId());
//        Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
//        if (m != null) {
//            replybean.setReplyToName(m.getUserName());
//        }
//        return replybean;
//    }

}
