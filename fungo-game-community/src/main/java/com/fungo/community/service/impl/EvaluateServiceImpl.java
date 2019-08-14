package com.fungo.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.service.*;
import com.fungo.community.entity.*;
import com.fungo.community.facede.GameFacedeService;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.facede.TSMQFacedeService;
import com.fungo.community.service.ICounterService;
import com.fungo.community.service.IEvaluateService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.consts.Setting;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.*;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheComment;
import com.game.common.repo.cache.facade.FungoCacheGame;
import com.game.common.repo.cache.facade.FungoCacheMood;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.UUIDUtils;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.EmojiDealUtil;
import com.game.common.util.emoji.FilterEmojiUtil;
import com.game.common.vo.DelObjectListVO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class EvaluateServiceImpl implements IEvaluateService {

    private static final Logger logger = LoggerFactory.getLogger(EvaluateServiceImpl.class);

    @Autowired
    private CmmCommentDaoService commentService;
    @Autowired
    private CmmPostDaoService postService;
    @Autowired
    private MooMoodDaoService moodService;
    @Autowired
    private MooMessageDaoService messageServive;
    @Autowired
    private ReplyDaoService replyService;
    @Autowired
    private FungoCacheGame fungoCacheGame;
    @Autowired
    private FungoCacheArticle fungoCacheArticle;
    @Autowired
    private FungoCacheMood fungoCacheMood;
    @Autowired
    private FungoCacheComment fungoCacheComment;
    @Autowired
    private ICounterService counterService;
    //依赖系统和用户微服务
    @Autowired
    private SystemFacedeService systemFacedeService;
    //依赖游戏微服务
    @Autowired
    private GameFacedeService gameFacedeService;
    @Autowired
    private TSMQFacedeService tSMQFacedeService;

    @Override
    public ResultDto<CommentOut> addComment(String memberId, CommentInput commentInput, String appVersion) throws Exception {
        ResultDto<CommentOut> re = new ResultDto<CommentOut>();
        CommentOut t = new CommentOut();
        String id = "";
        int floor = 0;
        String targetMemberId = "";
        if (CommonUtil.isNull(commentInput.getContent())) {
            return ResultDto.error("-1", "内容不能为空!");
        }
        Map<String, Object> noticeMap = null;
        if (1 == commentInput.getTarget_type()) {//文章评论
            CmmComment comment = new CmmComment();
            floor = commentService.selectCount(new EntityWrapper<CmmComment>().eq("post_id", commentInput.getTarget_id()));
            //表情编码
            String content = commentInput.getContent();
            if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(content))) {
                content = FilterEmojiUtil.encodeEmoji(content);
            }
            comment.setContent(content);

            comment.setMemberId(memberId);
            comment.setFloor(floor + 1);
            comment.setLikeNum(0);
            comment.setPostId(commentInput.getTarget_id());
            comment.setCreatedAt(new Date());
            comment.setUpdatedAt(new Date());
            comment.setState(0);

            CmmPost post = postService.selectById(commentInput.getTarget_id());

            targetMemberId = post.getMemberId();

            if (post.getMemberId().equals(memberId)) {
                t.setIs_host(true);
            } else {
                t.setIs_host(false);
            }

            //设置评论数
            post.setCommentNum(comment.getFloor());

            //最后回复时间
            post.setLastReplyAt(new Date());
            post.updateById();
            commentService.insert(comment);
            id = comment.getId();
            //counterService.addCounter("t_cmm_post", "comment_num", commentInput.getTarget_id());//增加评论数
            //!fixme 推送通知
            //        addNotice(int eventType, String memberId, String target_id, int target_type, String information, String appVersion, String replyToId)
            //gameProxy.addNotice(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_COMMENT, commentInput.getContent(), appVersion, "");
            noticeMap = new HashMap<>();
            noticeMap.put("eventType", Setting.ACTION_TYPE_COMMENT);
            noticeMap.put("memberId", memberId);
            noticeMap.put("target_id", commentInput.getTarget_id());
            noticeMap.put("target_type", Setting.RES_TYPE_COMMENT);
            noticeMap.put("information", commentInput.getContent());
            noticeMap.put("appVersion", appVersion);
            noticeMap.put("replyToId", "");
            noticeMap.put( "commentId",comment.getId());
        } else if (2 == commentInput.getTarget_type()) {//心情评论
            MooMessage comment = new MooMessage();
            //编码
            String content = commentInput.getContent();
            if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(content))) {
                content = FilterEmojiUtil.encodeEmoji(content);
            }
            comment.setContent(content);
            comment.setMemberId(memberId);
            comment.setLikeNum(0);
            comment.setMoodId(commentInput.getTarget_id());
            comment.setCreatedAt(new Date());
            comment.setUpdatedAt(new Date());
            comment.setState(0);
            messageServive.insert(comment);

            MooMood mood = this.moodService.selectById(commentInput.getTarget_id());
            targetMemberId = mood.getMemberId();
            if (mood.getMemberId().equals(memberId)) {
                t.setIs_host(true);
            } else {
                t.setIs_host(false);
            }
            id = comment.getId();
            counterService.addCounter("t_moo_mood", "comment_num", commentInput.getTarget_id());//增加评论数

            //!fixme 通知
            //gameProxy.addNotice(Setting.MSG_TYPE_MOOD, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_COMMENT, commentInput.getContent(), appVersion, "");

            noticeMap = new HashMap<>();
            noticeMap.put("eventType", Setting.MSG_TYPE_MOOD);
            noticeMap.put("memberId", memberId);
            noticeMap.put("target_id", commentInput.getTarget_id());
            noticeMap.put("target_type", Setting.RES_TYPE_COMMENT);
            noticeMap.put("information", commentInput.getContent());
            noticeMap.put("appVersion", appVersion);
            noticeMap.put("replyToId", "");
            noticeMap.put( "commentId",comment.getId());
        }
        if (null != noticeMap) {
            this.sendNoticeToSystemServcie(noticeMap);
        }
        //!fixme 查询用户数据
        //t.setAuthor(userService.getAuthor(memberId));
        AuthorBean author = null;
        try {
            ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(memberId);
            if (null != beanResultDto) {
                author = beanResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        t.setAuthor(author);
        t.setContent(CommonUtils.filterWord(commentInput.getContent()));
        t.setFloor(floor + 1);
        t.setObjectId(id);
        t.setCreatedAt(DateTools.fmtDate(new Date()));
        t.setReply_count(0);
        t.setReply_more(false);
        t.setReplys(new ArrayList<String>());
        t.setUpdatedAt(DateTools.fmtDate(new Date()));
        re.setData(t);
//		gameProxy.addScore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), commentInput.getTarget_type());
        //完成任务 V2.4.6版本任务之前任务废弃
        // int addTaskCore = gameProxy.addTaskCore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), commentInput.getTarget_type());
        //V2.4.6版本任务
        String tips = "";
        //每日任务
        /*
        //1 fungo币
        Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());
        //2 经验值
        Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
        */
        //任务：
        // 1.调用微服务接口
        // 2.执行失败，发送MQ消息
        Map<String, Object> resMapCoin = null;
        Map<String, Object> resMapExp = null;
        String uuidCoin = UUIDUtils.getUUID();
        String uuidExp = UUIDUtils.getUUID();
        //coin task
        TaskDto taskDtoCoin = new TaskDto();
        taskDtoCoin.setRequestId(uuidCoin);
        taskDtoCoin.setMbId(memberId);
        taskDtoCoin.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoCoin.setTaskType(MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT);
        taskDtoCoin.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());
        //exp task
        TaskDto taskDtoExp = new TaskDto();
        taskDtoExp.setRequestId(uuidExp);
        taskDtoExp.setMbId(memberId);
        taskDtoExp.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoExp.setTaskType(MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT);
        taskDtoExp.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
        try {
            //coin task
            ResultDto<Map<String, Object>> coinTaskResultDto = systemFacedeService.exTask(taskDtoCoin);
            if (null != coinTaskResultDto) {
                resMapCoin = coinTaskResultDto.getData();
            }
            //exp task
            ResultDto<Map<String, Object>> expTaskResultDto = systemFacedeService.exTask(taskDtoExp);
            if (null != expTaskResultDto) {
                resMapExp = expTaskResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            //出现异常mq发送
            //coin task
            doExcTaskSendMQ(taskDtoCoin);
            //exp task
            doExcTaskSendMQ(taskDtoExp);
        }
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
        if (StringUtils.isNotBlank(tips)) {
            re.show(tips);
        } else {
            re.show("发布成功");
        }
        //clear redis cache
        //帖子/心情评论列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        // 帖子列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST, "", null);
        //获取心情动态列表(v2.4)
        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
        //获取心情内容
        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOOD_CONTENT_GET, "", null);
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, commentInput.getTarget_id(), null);
        return re;
    }

    //发送消息通知给系统微服务
    private void sendNoticeToSystemServcie(Map<String, Object> noticeMap) {

        //MQ发布到系统微服务
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_COMMUNITY);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        routinKey.deleteCharAt(routinKey.length() - 1);
        routinKey.append("add_notice");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_ADD_NOTICE.getCode());

        mqResultDto.setBody(noticeMap);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        //执行MQ发送
        ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);
        logger.info("--添加评论-执行发送消息--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
        //-----start
    }

    //社区心情--用户执行任务
    private void doExcTaskSendMQ(TaskDto taskDto) {
        //-----start
        //MQ 业务数据发送给系统用户业务处理
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_MOOD);
        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        routinKey.deleteCharAt(routinKey.length() - 1);
        routinKey.append("cmtPostMQDoTask");
        transactionMessageDto.setRoutingKey(routinKey.toString());
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_DO_TASK.getCode());

        mqResultDto.setBody(taskDto);
        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        //执行MQ发送
        ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);
        logger.info("--社区文章用户发布评论执行任务--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
        //-----start
    }


    @Override
    public ResultDto<CommentBeanOut> getCommentDetail(String memberId, String commentId) {

        ResultDto<CommentBeanOut> re = new ResultDto<CommentBeanOut>();
        CommentBeanOut bean = null;
        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_POST_COMMENT_DETAIL + commentId;
        String keySuffix = memberId;

        bean = (CommentBeanOut) fungoCacheComment.getIndexCache(keyPrefix, keySuffix);
        if (null != bean) {
            re.setData(bean);
            return re;
        }
        bean = new CommentBeanOut();
        CmmComment comment = commentService.selectById(commentId);
        if (comment == null) {
            return ResultDto.error("-1", "该评论详情不存在");
        }
        bean.setObjectId(comment.getId());
        if (!CommonUtil.isNull(comment.getContent())) {
            //表情解码
            String content = comment.getContent();
            if (StringUtils.isNotBlank(content)) {
                content = FilterEmojiUtil.decodeEmoji(content);
            }
            bean.setContent(CommonUtils.filterWord(content));
        }
        bean.setCreatedAt(DateTools.fmtDate(comment.getCreatedAt()));
        bean.setLike_num(comment.getLikeNum());
        bean.setReply_num(comment.getReplyNum());
        bean.setState(comment.getState());
        bean.setUpdatedAt(DateTools.fmtDate(comment.getUpdatedAt()));

        //!fixme 获取用户数据
        //bean.setAuthor(this.userService.getAuthor(comment.getMemberId()));

        AuthorBean authorBean = new AuthorBean();
        try {
            ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(comment.getMemberId());
            if (null != beanResultDto) {
                authorBean = beanResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bean.setAuthor(authorBean);
        bean.setFloor(comment.getFloor());
        if ("".equals(memberId) || memberId == null) {
            bean.setIs_liked(false);
        } else {
            //!fixme 获取点赞数
            //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", comment.getId()).eq("member_id", memberId));
            BasActionDto basActionDto = new BasActionDto();
            basActionDto.setMemberId(memberId);
            basActionDto.setType(0);
            basActionDto.setState(0);
            basActionDto.setTargetId(comment.getId());
            int liked = 0;
            try {
                ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);
                if (null != resultDto) {
                    liked = resultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            bean.setIs_liked(liked > 0 ? true : false);
        }
        re.setData(bean);
        //redis cahce
        fungoCacheComment.excIndexCache(true, keyPrefix, keySuffix, bean);
        return re;
    }

    public ResultDto<CommentBeanOut> getMoodMessageDetail(String memberId, String messageId) {
        ResultDto<CommentBeanOut> re = new ResultDto<CommentBeanOut>();
        CommentBeanOut bean = null;
        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MOOD_COMMENT_DETAIL + messageId;
        String keySuffix = memberId;
        bean = (CommentBeanOut) fungoCacheComment.getIndexCache(keyPrefix, keySuffix);
        if (null != bean) {
            re.setData(bean);
            return re;
        }
        bean = new CommentBeanOut();
        MooMessage comment = messageServive.selectById(messageId);
        if (comment == null) {
            return ResultDto.error("-1", "该评论详情不存在");
        }
        bean.setObjectId(comment.getId());
        if (StringUtils.isNotBlank(comment.getContent())) {
            String interactContent = FilterEmojiUtil.decodeEmoji(comment.getContent());
            comment.setContent(interactContent);
        }
        if (!CommonUtil.isNull(comment.getContent())) {
            bean.setContent(CommonUtils.filterWord(comment.getContent()));
        }
        bean.setCreatedAt(DateTools.fmtDate(comment.getCreatedAt()));
        bean.setLike_num(comment.getLikeNum());
        bean.setReply_num(comment.getReplyNum());
        bean.setState(comment.getState());
        bean.setUpdatedAt(DateTools.fmtDate(comment.getUpdatedAt()));

        //!fixme 获取用户数据
        //bean.setAuthor(this.userService.getAuthor(comment.getMemberId()));
        AuthorBean authorBean = new AuthorBean();
        try {
            ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(comment.getMemberId());
            if (null != beanResultDto) {
                authorBean = beanResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bean.setAuthor(authorBean);
        //是否点赞
        if ("".equals(memberId) || memberId == null) {
            bean.setIs_liked(false);
        } else {

            //!fixme 获取点赞数
            //行为类型
            //点赞 | 0
            //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", comment.getId()).eq("member_id", memberId));
            BasActionDto basActionDto = new BasActionDto();
            basActionDto.setMemberId(memberId);
            basActionDto.setType(0);
            basActionDto.setState(0);
            basActionDto.setTargetId(comment.getId());

            int liked = 0;

            try {
                ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                if (null != resultDto) {
                    liked = resultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            bean.setIs_liked(liked > 0 ? true : false);
        }
        re.setData(bean);

        //redis cache
        fungoCacheComment.excIndexCache(true, keyPrefix, keySuffix, bean);
        return re;
    }


    @Override
    public FungoPageResultDto<CommentOutPageDto> getCommentList(String memberId, CommentInputPageDto commentpage) {

        FungoPageResultDto<CommentOutPageDto> re = null;

        String id = "";
        if (StringUtils.isNotBlank(commentpage.getMood_id())) {
            id = commentpage.getMood_id();
        } else if (StringUtils.isNotBlank(commentpage.getPost_id())) {
            id = commentpage.getPost_id();
        }
        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS;
        String keySuffix = id + JSON.toJSONString(commentpage);
        re = (FungoPageResultDto<CommentOutPageDto>) fungoCacheArticle.getIndexCache(keyPrefix, keySuffix);

        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }
        re = new FungoPageResultDto<CommentOutPageDto>();
        List<CommentOutPageDto> relist = new ArrayList<CommentOutPageDto>();
        re.setData(relist);

        if (commentpage.getMood_id() != null && !"".equals(commentpage.getMood_id())) {

            MooMood mood = moodService.selectById(commentpage.getMood_id());

            if (mood == null) {
                return FungoPageResultDto.error("-1", "心情不存在");
            }

            Wrapper<MooMessage> entityWrapper = new EntityWrapper<MooMessage>();
            entityWrapper.eq("mood_id", commentpage.getMood_id());
            entityWrapper.and("state != {0}", -1);

            if ("host".equals(commentpage.getFilter())) {//社区主
                entityWrapper.eq("member_id", mood.getMemberId());
            }

            if (commentpage.getSort() == 0 || commentpage.getSort() == 1) {//排序
                entityWrapper.orderBy("created_at", true);
            } else if (commentpage.getSort() == 2) {
                entityWrapper.orderBy("created_at", false);
            } else if (commentpage.getSort() == 3) {
                entityWrapper.orderBy("like_num", true);
            } else if (commentpage.getSort() == 4) {
                entityWrapper.orderBy("like_num", false);
            }


            Page p = new Page<>(commentpage.getPage(), commentpage.getLimit());
            Page<MooMessage> page = messageServive.selectPage(p, entityWrapper);
            List<MooMessage> list = page.getRecords();

            for (MooMessage mooMessage : list) {
                CommentOutPageDto ctem = new CommentOutPageDto();
//				if(!CommonUtil.isNull(mooMessage.getContent())) {
//					
//				}

                String content = mooMessage.getContent();
                //表情解码
                if (StringUtils.isNotBlank(content)) {
                    content = FilterEmojiUtil.decodeEmoji(content);
                }

                ctem.setContent(CommonUtils.filterWord(content));
                ctem.setCreatedAt(DateTools.fmtDate(mooMessage.getCreatedAt()));
                ctem.setFloor(0);
                ctem.setObjectId(mooMessage.getId());
                ctem.setReply_count(mooMessage.getReplyNum());
                ctem.setUpdatedAt(DateTools.fmtDate(mooMessage.getUpdatedAt()));
                if (mooMessage.getMemberId().equals(mood.getMemberId())) {
                    ctem.setIs_host(true);
                }
                ctem.setLike_num(mooMessage.getLikeNum());

                //!fixme 获取用户数据
                //AuthorBean author = userService.getAuthor(mooMessage.getMemberId());

                AuthorBean author = null;
                try {
                    ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(mooMessage.getMemberId());
                    if (null != beanResultDto) {
                        author = beanResultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                if (null != author) {
                    ctem.setAuthor(author);
                }

                //回复
                Page<Reply> replyList = replyService.selectPage(new Page<>(1, 3), new EntityWrapper<Reply>().eq("target_id", mooMessage.getId()).ne("state", -1).orderBy("created_at", true));
                int i = 0;
                for (Reply reply : replyList.getRecords()) {
                    i = i + 1;
                    if (i == 3) {
                        ctem.setReply_more(true);
                        break;
                    }
                    ReplyBean replybean = new ReplyBean();

                    //!fixme 获取用户数据
                    //AuthorBean replyAuthor = userService.getAuthor(reply.getMemberId());

                    AuthorBean replyAuthor = null;
                    try {
                        ResultDto<AuthorBean> beanResultDtoReply = systemFacedeService.getAuthor(reply.getMemberId());
                        if (null != beanResultDtoReply) {
                            replyAuthor = beanResultDtoReply.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (null != replyAuthor) {
                        replybean.setAuthor(replyAuthor);
                    }

                    //表情解码
                    String coententReply = reply.getContent();
                    if (StringUtils.isNotBlank(coententReply)) {
                        coententReply = FilterEmojiUtil.decodeEmoji(coententReply);
                    }
                    replybean.setContent(CommonUtils.filterWord(coententReply));
                    replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
                    replybean.setObjectId(reply.getId());
                    replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
                    replybean.setReplyToId(reply.getReplayToId());

                    //!fixme 查询用户数据
                    //Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));

                    List<String> idsList = new ArrayList<String>();
                    idsList.add(reply.getReplayToId());

                    ResultDto<List<MemberDto>> listMembersByids = null;
                    try {
                        listMembersByids = systemFacedeService.listMembersByids(idsList, null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    MemberDto memberDto = null;
                    if (null != listMembersByids) {
                        List<MemberDto> memberDtoList = listMembersByids.getData();
                        if (null != memberDtoList && !memberDtoList.isEmpty()) {
                            memberDto = memberDtoList.get(0);
                        }
                    }

                    if (memberDto != null) {
                        replybean.setReplyToName(memberDto.getUserName());
                    }
                    ctem.getReplys().add(replybean);
                }
                //是否点赞
                if ("".equals(memberId) || memberId == null) {
                    ctem.setIs_liked(false);
                } else {

                    //!fixme 获取点赞数
                    //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", mooMessage.getId()).eq("member_id", memberId));

                    BasActionDto basActionDto = new BasActionDto();

                    basActionDto.setMemberId(memberId);
                    basActionDto.setType(0);
                    basActionDto.setState(0);
                    basActionDto.setTargetId(mooMessage.getId());

                    int liked = 0;
                    try {
                        ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                        if (null != resultDto) {
                            liked = resultDto.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    ctem.setIs_liked(liked > 0 ? true : false);
                }
                relist.add(ctem);
            }
            re.setData(relist);
            PageTools.pageToResultDto(re, page);

        } else if (commentpage.getPost_id() != null && !"".equals(commentpage.getPost_id())) {
            CmmPost post = postService.selectById(commentpage.getPost_id());
            if (post == null) {
                return FungoPageResultDto.error("-1", "帖子不存在");
            }
            Wrapper<CmmComment> commentWrapper = new EntityWrapper<CmmComment>();
            commentWrapper.eq("post_id", commentpage.getPost_id());
            commentWrapper.and("state != {0}", -1);
            if ("host".equals(commentpage.getFilter())) {//社区主
                commentWrapper.eq("member_id", post.getMemberId());
            }
            if (commentpage.getSort() == 0 || commentpage.getSort() == 1) {//排序
                commentWrapper.orderBy("created_at", true); //时间正序
            } else if (commentpage.getSort() == 2) {
                commentWrapper.orderBy("created_at", false); //时间倒序
            } else if (commentpage.getSort() == 3) {
                commentWrapper.orderBy("like_num", true); //点赞数 正序
            } else if (commentpage.getSort() == 4) {
                commentWrapper.orderBy("like_num", false); //点赞数 倒序
            }

            Page<CmmComment> page = this.commentService.selectPage(new Page<>(commentpage.getPage(), commentpage.getLimit()), commentWrapper);
            List<CmmComment> list = page.getRecords();

            for (CmmComment cmmComment : list) {
                CommentOutPageDto ctem = new CommentOutPageDto();

                //表情解码
                String coententReply = cmmComment.getContent();
                if (StringUtils.isNotBlank(coententReply)) {
                    coententReply = FilterEmojiUtil.decodeEmoji(coententReply);
                }
                ctem.setContent(CommonUtils.filterWord(coententReply));
                ctem.setCreatedAt(DateTools.fmtDate(cmmComment.getCreatedAt()));
                ctem.setFloor(cmmComment.getFloor());
                ctem.setObjectId(cmmComment.getId());
                ctem.setReply_count(cmmComment.getReplyNum());
                ctem.setUpdatedAt(DateTools.fmtDate(cmmComment.getUpdatedAt()));
                if (post.getMemberId().equals(cmmComment.getMemberId())) {
                    ctem.setIs_host(true);
                }
                ctem.setLike_num(cmmComment.getLikeNum());

                //!fixme 获取用户数据
                //AuthorBean author = userService.getAuthor(cmmComment.getMemberId());

                AuthorBean author = null;
                try {
                    ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(cmmComment.getMemberId());
                    if (null != beanResultDto) {
                        author = beanResultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (null != author) {
                    ctem.setAuthor(author);
                }
                Page<Reply> replyList = replyService.selectPage(new Page<>(1, 3), new EntityWrapper<Reply>().eq("target_id", cmmComment.getId()).ne("state", -1).orderBy("created_at", true));
                int i = 0;
                //回复
                for (Reply reply : replyList.getRecords()) {
                    i = i + 1;
                    if (i == 3) {
                        ctem.setReply_more(true);
                        break;
                    }
                    ReplyBean replybean = new ReplyBean();

                    //!fixme 获取用户数据
                    //AuthorBean authorReply = userService.getAuthor(reply.getMemberId());

                    AuthorBean authorReply = null;
                    try {
                        ResultDto<AuthorBean> beanResultDtoReply = systemFacedeService.getAuthor(reply.getMemberId());
                        if (null != beanResultDtoReply) {
                            authorReply = beanResultDtoReply.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    if (null != authorReply) {
                        replybean.setAuthor(authorReply);
                    }

                    //表情解码
                    String coententReply2 = reply.getContent();
                    if (StringUtils.isNotBlank(coententReply2)) {
                        coententReply2 = FilterEmojiUtil.decodeEmoji(coententReply2);
                    }
                    replybean.setContent(CommonUtils.filterWord(coententReply2));
                    replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
                    replybean.setObjectId(reply.getId());
                    replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
                    replybean.setLike_num(reply.getLikeNum());
                    replybean.setReplyToId(reply.getReplayToId());


                 /*
                    Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));


                    if (m != null) {
                        replybean.setReplyToName(m.getUserName());
                    }
                    */

                    List<String> idsList = new ArrayList<String>();
                    idsList.add(reply.getReplayToId());

                    ResultDto<List<MemberDto>> listMembersByids = null;

                    try {
                        listMembersByids = systemFacedeService.listMembersByids(idsList, null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    MemberDto memberDto = null;
                    if (null != listMembersByids) {
                        List<MemberDto> memberDtoList = listMembersByids.getData();
                        if (null != memberDtoList && !memberDtoList.isEmpty()) {
                            memberDto = memberDtoList.get(0);
                        }
                    }


                    if (memberDto != null) {
                        replybean.setReplyToName(memberDto.getUserName());
                    }

                    ctem.getReplys().add(replybean);

                }
                //是否点赞
                if ("".equals(memberId) || memberId == null) {
                    ctem.setIs_liked(false);
                } else {

                    //!fixme 查询点赞数
                    //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", cmmComment.getId()).eq("member_id", memberId));

                    BasActionDto basActionDto = new BasActionDto();

                    basActionDto.setMemberId(memberId);
                    basActionDto.setType(0);
                    basActionDto.setState(0);
                    basActionDto.setTargetId(cmmComment.getId());
                    int liked = 0;

                    try {

                        ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                        if (null != resultDto) {
                            liked = resultDto.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    ctem.setIs_liked(liked > 0 ? true : false);
                }
                relist.add(ctem);
            }
            re.setData(relist);
            PageTools.pageToResultDto(re, page);
        }

        //redis cache
        fungoCacheArticle.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }

    @Override
    @Transactional
    public ResultDto<EvaluationBean> addGameEvaluation(String memberId, EvaluationInput commentInput) throws Exception {
//		if(CommonUtil.isNull(commentInput.getContent())) {
//			return ResultDto.error("-1","内容不能为空!");
//		}
        ResultDto<EvaluationBean> re = new ResultDto<EvaluationBean>();
        EvaluationBean t = new EvaluationBean();
        //!fixme 获取游戏评论
        //GameEvaluation evaluation = gameEvaluationService.selectOne(new EntityWrapper<GameEvaluation>().eq("member_id", memberId).eq("game_id", commentInput.getTarget_id()).eq("state", 0));

        GameEvaluationDto gameEvaluationDto = null;
        try {
            ResultDto<GameEvaluationDto> evaluationDtoResultDto = gameFacedeService.getGameEvaluationSelectOne(memberId, commentInput.getTarget_id());
            if (null != evaluationDtoResultDto) {
                gameEvaluationDto = evaluationDtoResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

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

        //用户未对该游戏发表过评论，新增
        if (gameEvaluationDto == null) {
            //调用MQ发送
            String gameEvaluationId = this.addGameEvaluationSenderMQ(memberId, commentInput);


//			if(commentInput.isIs_recommend()) {
            counterService.addCounter("t_game", "comment_num", commentInput.getTarget_id());//增加评论数


//			}else {
//				counterService.addCounter("t_game", "unrecommend_num", commentInput.getTarget_id());//增加评论数
//			}

//			gameProxy.addScore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_GAME);

            //V2.4.6版本任务之前任务 废弃
            //times = gameProxy.addTaskCore(Setting.ACTION_TYPE_COMMENT, memberId, commentInput.getTarget_id(), Setting.RES_TYPE_GAME);

            //V2.4.6版本任务
            /*
            //1 fungo币
            Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());

            //2 经验值
            Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
            */


            //任务：
            // 1.调用微服务接口
            // 2.执行失败，发送MQ消息
            Map<String, Object> resMapCoin = null;
            Map<String, Object> resMapExp = null;

            String uuidCoin = UUIDUtils.getUUID();
            String uuidExp = UUIDUtils.getUUID();


            //coin task
            TaskDto taskDtoCoin = new TaskDto();
            taskDtoCoin.setRequestId(uuidCoin);
            taskDtoCoin.setMbId(memberId);
            taskDtoCoin.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
            taskDtoCoin.setTaskType(MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT);
            taskDtoCoin.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());


            //exp task
            TaskDto taskDtoExp = new TaskDto();
            taskDtoExp.setRequestId(uuidExp);
            taskDtoExp.setMbId(memberId);
            taskDtoExp.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
            taskDtoExp.setTaskType(MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT);
            taskDtoExp.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());

            try {

                //coin task
                ResultDto<Map<String, Object>> coinTaskResultDto = systemFacedeService.exTask(taskDtoCoin);
                if (null != coinTaskResultDto) {
                    resMapCoin = coinTaskResultDto.getData();
                }

                //exp task
                ResultDto<Map<String, Object>> expTaskResultDto = systemFacedeService.exTask(taskDtoExp);
                if (null != expTaskResultDto) {
                    resMapExp = expTaskResultDto.getData();
                }

            } catch (Exception ex) {
                ex.printStackTrace();

                //出现异常mq发送
                //coin task
                doExcTaskSendMQ(taskDtoCoin);

                //exp task
                doExcTaskSendMQ(taskDtoExp);

            }


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
            /*
            BasAction action = new BasAction();
            action.setCreatedAt(new Date());
            action.setTargetId(evaluation.getId());
            action.setTargetType(6);
            action.setType(8);
            action.setMemberId(memberId);
            action.setState(0);
            action.setUpdatedAt(new Date());
            this.actionService.insertAllColumn(action);
            */

            //MQ 业务数据发送给系统用户业务处理
            BasActionDto basActionDtoAdd = new BasActionDto();
            basActionDtoAdd.setCreatedAt(new Date());
            basActionDtoAdd.setTargetId(gameEvaluationId);
            basActionDtoAdd.setTargetType(6);
            basActionDtoAdd.setType(8);
            basActionDtoAdd.setMemberId(memberId);
            basActionDtoAdd.setState(0);
            basActionDtoAdd.setUpdatedAt(new Date());

            this.sendActionWithMQ(basActionDtoAdd);
            //-----start

            //用户对该游戏发表过评论，修改
        } else {

            //2.4

            //!fixme 执行MQ发送修改游戏评论
            //evaluation.updateById();

            this.updateGameEvaluationSenderMQ(memberId, commentInput, gameEvaluationDto);

            /*
            //修改用户行为的时间
            BasAction action = actionService.selectOne(new EntityWrapper<BasAction>()
                    .eq("member_id", memberId)
                    .eq("target_id", evaluation.getId())
                    .eq("target_type", 6)
                    .eq("type", 8)
                    .eq("state", 0));
            if (action != null) {
                action.setUpdatedAt(date);
                actionService.updateAllColumnById(action);
            }*/

        }

        //!fixme 返回评论内容
        //t.setAuthor(userService.getAuthor(memberId));

        AuthorBean author = new AuthorBean();
        try {
            ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(memberId);
            if (null != beanResultDto) {
                author = beanResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        t.setAuthor(author);

        t.setContent(CommonUtils.filterWord(commentInput.getContent()));

        t.setObjectId(gameEvaluationDto.getId());

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
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_EVALUATIONLIST, memberId, null);
        //游戏详情
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_DETAIL + commentInput.getTarget_id(), "", null);
        // 获取最近评论的游戏
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_RECENTEVA + memberId, "", null);

        //游戏评价列表
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS, "", null);
        return re;
    }


    //修改游戏评论发送MQ
    private void updateGameEvaluationSenderMQ(String memberId, EvaluationInput commentInput, GameEvaluationDto gameEvaluationDto) {

        gameEvaluationDto.setRating(commentInput.getRating());

        gameEvaluationDto.setTrait1(commentInput.getTrait1() == 0 ? null : commentInput.getTrait1());
        gameEvaluationDto.setTrait2(commentInput.getTrait2() == 0 ? null : commentInput.getTrait2());
        gameEvaluationDto.setTrait3(commentInput.getTrait3() == 0 ? null : commentInput.getTrait3());
        gameEvaluationDto.setTrait4(commentInput.getTrait4() == 0 ? null : commentInput.getTrait4());
        gameEvaluationDto.setTrait5(commentInput.getTrait5() == 0 ? null : commentInput.getTrait5());

        gameEvaluationDto.setContent(commentInput.getContent());
        Date date = new Date();
        gameEvaluationDto.setCreatedAt(date);
        gameEvaluationDto.setGameId(commentInput.getTarget_id());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (commentInput.getImages() != null) {
                gameEvaluationDto.setImages(objectMapper.writeValueAsString(commentInput.getImages()));
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
        gameEvaluationDto.setMemberId(memberId);
        gameEvaluationDto.setPhoneModel(commentInput.getPhone_model());
        gameEvaluationDto.setState(0);
        gameEvaluationDto.setUpdatedAt(date);

        //-------------若用户未对该游戏发表过评论，则 增加，走MQ推送
        //MQ 业务数据发送给系统用户业务处理
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_COMMUNITY);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_GAMES.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_GAMES.getName());
        routinKey.deleteCharAt(routinKey.length() - 1);
        routinKey.append("game_evaluation_update");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_GAME_EVALUATION_UPDATE.getCode());

        mqResultDto.setBody(gameEvaluationDto);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        //执行MQ发送
        ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);
        logger.info("--社区用户修改游戏评论执行任务--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));


    }


    //添加用户行为-MQ发送
    private void sendActionWithMQ(BasActionDto basActionDtoAdd) {

        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        routinKey.deleteCharAt(routinKey.length() - 1);
        routinKey.append("ACTION_ADD");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_ACTION_MQ_TYPE_ACTION_ADD.getCode());

        mqResultDto.setBody(basActionDtoAdd);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        //执行MQ发送
        ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);
        logger.info("--添加评论-执行添加用户动作行为数据--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));

    }


    //添加游戏游戏评论-通过MQ发送
    private String addGameEvaluationSenderMQ(String memberId, EvaluationInput commentInput) {

        GameEvaluationDto evaluationDtoAdd = new GameEvaluationDto();

        evaluationDtoAdd.setId(UUIDUtils.getUUID());

        //2.4
        //分数限制
        evaluationDtoAdd.setRating(commentInput.getRating());

        evaluationDtoAdd.setTrait1(commentInput.getTrait1() == 0 ? null : commentInput.getTrait1());
        evaluationDtoAdd.setTrait2(commentInput.getTrait2() == 0 ? null : commentInput.getTrait2());
        evaluationDtoAdd.setTrait3(commentInput.getTrait3() == 0 ? null : commentInput.getTrait3());
        evaluationDtoAdd.setTrait4(commentInput.getTrait4() == 0 ? null : commentInput.getTrait4());
        evaluationDtoAdd.setTrait5(commentInput.getTrait5() == 0 ? null : commentInput.getTrait5());

        evaluationDtoAdd.setContent(commentInput.getContent());
        evaluationDtoAdd.setCreatedAt(new Date());
        evaluationDtoAdd.setGameId(commentInput.getTarget_id());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (commentInput.getImages() != null) {
                evaluationDtoAdd.setImages(objectMapper.writeValueAsString(commentInput.getImages()));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        evaluationDtoAdd.setIsRecommend("1");
        evaluationDtoAdd.setLikeNum(0);
        evaluationDtoAdd.setMemberId(memberId);
        evaluationDtoAdd.setPhoneModel(commentInput.getPhone_model());
        evaluationDtoAdd.setState(0);
        evaluationDtoAdd.setUpdatedAt(new Date());

        //游戏名
        // Game game = gameService.selectOne(Condition.create().setSqlSelect("id,name").eq("id", commentInput.getTarget_id()));

        GameDto gameDto = null;
        ResultDto<GameDto> gameDtoResultDto = null;
        try {
            gameDtoResultDto = gameFacedeService.selectGameDetails(commentInput.getTarget_id(), null);
            if (null != gameDtoResultDto) {
                gameDto = gameDtoResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (gameDto != null) {
            evaluationDtoAdd.setGameName(gameDto.getName());
        }
        //通过MQ异步发送给游戏微服务
        //gameEvaluationService.insert(evaluation);

        //-------------若用户未对该游戏发表过评论，则 增加，走MQ推送
        //MQ 业务数据发送给系统用户业务处理
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_COMMUNITY);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_GAMES.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_GAMES.getName());
        routinKey.deleteCharAt(routinKey.length() - 1);
        routinKey.append("game_evaluation_add");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_GAME_EVALUATION_ADD.getCode());

        mqResultDto.setBody(evaluationDtoAdd);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        //执行MQ发送
        ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);
        logger.info("--社区用户发布游戏评论执行任务--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));

        //---------------end----------------

        return evaluationDtoAdd.getId();

    }

    @Override
    public ResultDto<EvaluationOutBean> getEvaluationDetail(String memberId, String commentId) {

        ResultDto<EvaluationOutBean> re = new ResultDto<EvaluationOutBean>();
        EvaluationOutBean bean = new EvaluationOutBean();

        //!fixme 查询游戏评论数据
        //GameEvaluation evaluation = gameEvaluationService.selectById(commentId);

        GameEvaluationDto gameEvaluationDto = null;
        try {
            ResultDto<GameEvaluationDto> evaluationDtoResultDto = gameFacedeService.getGameEvaluationSelectById(commentId);
            if (null != evaluationDtoResultDto) {
                gameEvaluationDto = evaluationDtoResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (gameEvaluationDto == null) {
            return ResultDto.error("-1", "该评论详情不存在");
        }
        //2.4
        if (gameEvaluationDto.getRating() != null) {
            bean.setRating(gameEvaluationDto.getRating());
        }
        bean.setSort(gameEvaluationDto.getSort());


        //!fixme 查询用户数据
        //bean.setAuthor(this.userService.getAuthor(evaluation.getMemberId()));

        AuthorBean replyAuthor = null;
        try {
            ResultDto<AuthorBean> beanResultDtoReply = systemFacedeService.getAuthor(gameEvaluationDto.getMemberId());
            if (null != beanResultDtoReply) {
                replyAuthor = beanResultDtoReply.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        bean.setAuthor(replyAuthor);


        bean.setContent(CommonUtils.filterWord(gameEvaluationDto.getContent()));

        bean.setCreatedAt(DateTools.fmtDate(gameEvaluationDto.getCreatedAt()));

        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<String> imgs = null;

        try {
            if (gameEvaluationDto.getImages() != null) {
                imgs = (ArrayList<String>) objectMapper.readValue(gameEvaluationDto.getImages(), ArrayList.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (imgs == null) {
            imgs = new ArrayList<String>();
        }
        bean.setGameId(gameEvaluationDto.getGameId());
        bean.setImages(imgs);
        bean.setIs_recommend("1".equals(gameEvaluationDto.getIsRecommend()) ? true : false);
        bean.setLike_num(gameEvaluationDto.getLikeNum());
        bean.setPhone_model(gameEvaluationDto.getPhoneModel());
        bean.setReply_count(gameEvaluationDto.getReplyNum());
        bean.setState(gameEvaluationDto.getState());
        bean.setUpdatedAt(DateTools.fmtDate(gameEvaluationDto.getUpdatedAt()));
        bean.setObjectId(gameEvaluationDto.getId());

        //是否点赞
        if ("".equals(memberId) || memberId == null) {
            bean.setIs_liked(false);
        } else {

            //!fixme 查询点赞数
            //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", evaluation.getId()).eq("member_id", memberId));

            BasActionDto basActionDto = new BasActionDto();

            basActionDto.setMemberId(memberId);
            basActionDto.setType(0);
            basActionDto.setState(0);
            basActionDto.setTargetId(bean.getGameId());

            int liked = 0;
            try {
                ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                if (null != resultDto) {
                    liked = resultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


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

        //!fixme 获取游戏数据
        //Game game = gameService.selectById(pageDto.getGame_id());

        GameEvaluationDto gameEvaluationDto = null;
        try {
            ResultDto<GameEvaluationDto> evaluationDtoResultDto = gameFacedeService.getGameEvaluationSelectById(pageDto.getGame_id());
            if (null != evaluationDtoResultDto) {
                gameEvaluationDto = evaluationDtoResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (gameEvaluationDto == null) {
            return FungoPageResultDto.error("-1", "游戏不存在");
        }

        /*
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
        */
        //总记录数
        int total = 0;
        List<GameEvaluationDto> list = new ArrayList<>();
        try {
            FungoPageResultDto<GameEvaluationDto> evaluationListRs = gameFacedeService.getEvaluationEntityWrapperByPageDtoAndMemberId(pageDto, memberId);
            if (null != evaluationListRs) {
                list = evaluationListRs.getData();
                total = evaluationListRs.getCount();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (GameEvaluationDto cmmComment : list) {
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


            //!fixme
            //ctem.setAuthor(this.userService.getAuthor(cmmComment.getMemberId()));

            AuthorBean replyAuthor = null;
            try {
                ResultDto<AuthorBean> beanResultDtoReply = systemFacedeService.getAuthor(cmmComment.getMemberId());
                if (null != beanResultDtoReply) {
                    replyAuthor = beanResultDtoReply.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            ctem.setAuthor(replyAuthor);

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

                //replybean.setAuthor(this.userService.getAuthor(reply.getMemberId()));

                AuthorBean authorBean = null;
                try {
                    ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(reply.getMemberId());
                    if (null != beanResultDto) {
                        authorBean = beanResultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                replybean.setAuthor(authorBean);

                replybean.setContent(CommonUtils.filterWord(reply.getContent()));
                replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
                replybean.setObjectId(reply.getId());
                replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
                replybean.setLike_num(reply.getLikeNum());
                replybean.setReplyToId(reply.getReplayToId());


               /*
                Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));

                if (m != null) {
                    replybean.setReplyToName(m.getUserName());
                }
                */

                List<String> idsList = new ArrayList<String>();
                idsList.add(reply.getReplayToId());

                ResultDto<List<MemberDto>> listMembersByids = null;
                try {
                    listMembersByids = systemFacedeService.listMembersByids(idsList, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                MemberDto memberDto = null;
                if (null != listMembersByids) {
                    List<MemberDto> memberDtoList = listMembersByids.getData();
                    if (null != memberDtoList && !memberDtoList.isEmpty()) {
                        memberDto = memberDtoList.get(0);
                    }
                }

                if (memberDto != null) {
                    replybean.setReplyToName(memberDto.getUserName());
                }


                ctem.getReplys().add(replybean);
            }

            //是否点赞
            if ("".equals(memberId) || memberId == null) {
                ctem.setIs_liked(false);
            } else {

                //!fixme
                //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", cmmComment.getId()).eq("member_id", memberId));

                BasActionDto basActionDto = new BasActionDto();

                basActionDto.setMemberId(memberId);
                basActionDto.setType(0);
                basActionDto.setState(0);
                basActionDto.setTargetId(cmmComment.getId());

                int liked = 0;
                try {
                    ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);
                    if (null != resultDto) {
                        liked = resultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                ctem.setIs_liked(liked > 0 ? true : false);
            }
            relist.add(ctem);
        }

        //设置分页参数
        PageTools.pageToResultDto(re, total, pageDto.getLimit(), pageDto.getPage());

        re.setData(relist);

        //redis cache
        fungoCacheGame.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }

    @Override
    @Transactional
    public ResultDto<ReplyOutBean> addReply(String memberId, ReplyInputBean replyInput, String appVersion) throws Exception {
        if (CommonUtil.isNull(replyInput.getContent())) {
            return ResultDto.error("-1", "内容不能为空!");
        }
        ResultDto<ReplyOutBean> re = new ResultDto<ReplyOutBean>();
        ReplyOutBean t = new ReplyOutBean();
        Reply reply = new Reply();
        reply.setContent(replyInput.getContent());
        reply.setCreatedAt(new Date());
        reply.setLikeNum(0);
        reply.setMemberId(memberId);
        reply.setReplayToId(replyInput.getReply_to());
        reply.setTargetType(replyInput.getTarget_type());
        reply.setTargetId(replyInput.getTarget_id());
        reply.setUpdatedAt(new Date());
        reply.setReplyToContentId(replyInput.getReply_to_content_id());

        //!fixme 获取用户数据
         /*
         Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));
            if (m != null) {
                reply.setReplyName(m.getUserName());
            }*/

        List<String> idsList = new ArrayList<String>();
        idsList.add(reply.getReplayToId());

        ResultDto<List<MemberDto>> listMembersByids = null;
        try {
            listMembersByids = systemFacedeService.listMembersByids(idsList, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        MemberDto memberDto = null;
        if (null != listMembersByids) {
            List<MemberDto> memberDtoList = listMembersByids.getData();
            if (null != memberDtoList && !memberDtoList.isEmpty()) {
                memberDto = memberDtoList.get(0);
            }
        }


        if (memberDto != null) {
            reply.setReplyName(memberDto.getUserName());
        }


        replyService.insert(reply);

        HashMap<String, Object> noticeMap = null;

        if (replyInput.getTarget_type() == 5) {//回复文章评论
            counterService.addCounter("t_cmm_comment", "reply_num", replyInput.getTarget_id());//增加评论数
            //最后回复时间
            CmmComment comment = commentService.selectById(replyInput.getTarget_id());
            if (comment != null) {
                CmmPost post = postService.selectById(comment.getPostId());
                if (post != null) {
                    post.setLastReplyAt(new Date());
                    post.updateById();
                }
            }
            try {//发送通知

                if (CommonUtil.isNull(replyInput.getReply_to_content_id())) { //只有没有三级评论时才会发送消息

                    //this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_GAME, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_COMMENT, replyInput.getContent(), "", "");

                    noticeMap = new HashMap<>();
                    noticeMap.put("eventType", Setting.MSG_TYPE_REPLAY_GAME);
                    noticeMap.put("memberId", memberId);
                    noticeMap.put("target_id", replyInput.getTarget_id());
                    noticeMap.put("target_type", Setting.RES_TYPE_COMMENT);
                    noticeMap.put("information", replyInput.getContent());
                    noticeMap.put("appVersion", "");
                    noticeMap.put("replyToId", "");

                    this.sendNoticeToSystemServcie(noticeMap);

                }

                if (!CommonUtil.isNull(replyInput.getReply_to_content_id())) {

                    //this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_RE, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_COMMENT, replyInput.getContent(), appVersion, replyInput.getReply_to());

                    noticeMap = new HashMap<>();
                    noticeMap.put("eventType", Setting.MSG_TYPE_REPLAY_RE);
                    noticeMap.put("memberId", memberId);
                    noticeMap.put("target_id", replyInput.getTarget_id());
                    noticeMap.put("target_type", Setting.RES_TYPE_COMMENT);
                    noticeMap.put("information", replyInput.getContent());
                    noticeMap.put("appVersion", appVersion);
                    noticeMap.put("replyToId", replyInput.getReply_to());

                    this.sendNoticeToSystemServcie(noticeMap);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (replyInput.getTarget_type() == 6) {//回复游戏评论

            counterService.addCounter("t_game_evaluation", "reply_num", replyInput.getTarget_id());//增加评论数


            try {


                if (CommonUtil.isNull(replyInput.getReply_to_content_id())) { //只有没有三级评论时才会发送消息

                    //this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_GAME, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_EVALUATION, replyInput.getContent(), "", "");

                    noticeMap = new HashMap<>();
                    noticeMap.put("eventType", Setting.MSG_TYPE_REPLAY_GAME);
                    noticeMap.put("memberId", memberId);
                    noticeMap.put("target_id", replyInput.getTarget_id());
                    noticeMap.put("target_type", Setting.RES_TYPE_EVALUATION);
                    noticeMap.put("information", replyInput.getContent());
                    noticeMap.put("appVersion", "");
                    noticeMap.put("replyToId", "");

                    this.sendNoticeToSystemServcie(noticeMap);

                }
                if (!CommonUtil.isNull(replyInput.getReply_to_content_id())) {

                    // this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_RE, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_EVALUATION, replyInput.getContent(), appVersion, replyInput.getReply_to());

                    noticeMap = new HashMap<>();
                    noticeMap.put("eventType", Setting.MSG_TYPE_REPLAY_RE);
                    noticeMap.put("memberId", memberId);
                    noticeMap.put("target_id", replyInput.getTarget_id());
                    noticeMap.put("target_type", Setting.RES_TYPE_EVALUATION);
                    noticeMap.put("information", replyInput.getContent());
                    noticeMap.put("appVersion", appVersion);
                    noticeMap.put("replyToId", replyInput.getReply_to());

                    this.sendNoticeToSystemServcie(noticeMap);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (replyInput.getTarget_type() == 8) {//回复心情评论
            counterService.addCounter("t_moo_message", "reply_num", replyInput.getTarget_id());//增加评论数
            if (CommonUtil.isNull(replyInput.getReply_to_content_id())) { //只有没有三级评论时才会发送消息

                //this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_GAME, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_MESSAGE, replyInput.getContent(), appVersion, "");

                noticeMap = new HashMap<>();
                noticeMap.put("eventType", Setting.MSG_TYPE_REPLAY_GAME);
                noticeMap.put("memberId", memberId);
                noticeMap.put("target_id", replyInput.getTarget_id());
                noticeMap.put("target_type", Setting.RES_TYPE_MESSAGE);
                noticeMap.put("information", replyInput.getContent());
                noticeMap.put("appVersion", appVersion);
                noticeMap.put("replyToId", "");

                this.sendNoticeToSystemServcie(noticeMap);

            }
            if (!CommonUtil.isNull(replyInput.getReply_to_content_id())) {

                //this.gameProxy.addNotice(Setting.MSG_TYPE_REPLAY_RE, memberId, replyInput.getTarget_id(), Setting.RES_TYPE_MESSAGE, replyInput.getContent(), appVersion, replyInput.getReply_to());

                noticeMap = new HashMap<>();
                noticeMap.put("eventType", Setting.MSG_TYPE_REPLAY_RE);
                noticeMap.put("memberId", memberId);
                noticeMap.put("target_id", replyInput.getTarget_id());
                noticeMap.put("target_type", Setting.RES_TYPE_MESSAGE);
                noticeMap.put("information", replyInput.getContent());
                noticeMap.put("appVersion", appVersion);
                noticeMap.put("replyToId", replyInput.getReply_to());

                this.sendNoticeToSystemServcie(noticeMap);
            }
        }


//		gameProxy.addScore(Setting.ACTION_TYPE_REPLY, memberId, replyInput.getTarget_id(), replyInput.getTarget_type());
        //完成任务 获取积分 V2.4.6版本任务之前任务废弃
        //int count = gameProxy.addTaskCore(Setting.ACTION_TYPE_REPLY, memberId, replyInput.getTarget_id(), replyInput.getTarget_type());

        //V2.4.6版本任务
        String tips = "";

        //1 fungo币
        /*
        Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());

        //2 经验值
        Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());
        */

        //任务：
        // 1.调用微服务接口
        // 2.执行失败，发送MQ消息
        Map<String, Object> resMapCoin = null;
        Map<String, Object> resMapExp = null;

        String uuidCoin = UUIDUtils.getUUID();
        String uuidExp = UUIDUtils.getUUID();

        //coin task
        TaskDto taskDtoCoin = new TaskDto();
        taskDtoCoin.setRequestId(uuidCoin);
        taskDtoCoin.setMbId(memberId);
        taskDtoCoin.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoCoin.setTaskType(MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT);
        taskDtoCoin.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN.code());


        //exp task
        TaskDto taskDtoExp = new TaskDto();
        taskDtoExp.setRequestId(uuidExp);
        taskDtoExp.setMbId(memberId);
        taskDtoExp.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoExp.setTaskType(MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT);
        taskDtoExp.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP.code());

        try {

            //coin task
            ResultDto<Map<String, Object>> coinTaskResultDto = systemFacedeService.exTask(taskDtoCoin);
            if (null != coinTaskResultDto) {
                resMapCoin = coinTaskResultDto.getData();
            }

            //exp task
            ResultDto<Map<String, Object>> expTaskResultDto = systemFacedeService.exTask(taskDtoExp);
            if (null != expTaskResultDto) {
                resMapExp = expTaskResultDto.getData();
            }

        } catch (Exception ex) {
            ex.printStackTrace();

            //出现异常mq发送
            //coin task
            doExcTaskSendMQ(taskDtoCoin);
            //exp task
            doExcTaskSendMQ(taskDtoExp);

        }

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

        //!fixme
        //t.setAuthor(this.userService.getAuthor(memberId));

        AuthorBean authorBean = null;
        try {
            ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(memberId);
            if (null != beanResultDto) {
                authorBean = beanResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        t.setAuthor(authorBean);

        t.setContent(CommonUtils.filterWord(replyInput.getContent()));
        t.setCreatedAt(DateTools.fmtDate(new Date()));
        t.setObjectId(reply.getId());
        t.setTarget_id(replyInput.getTarget_id());
        t.setTarget_type(replyInput.getTarget_type());

        if (replyInput.getReply_to() != null) {

            //t.setReply_to(this.userService.getAuthor(replyInput.getReply_to()));

            AuthorBean replyAuthorBean = null;
            try {
                ResultDto<AuthorBean> replybeanResultDto = systemFacedeService.getAuthor(replyInput.getReply_to());
                if (null != replybeanResultDto) {
                    replyAuthorBean = replybeanResultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            t.setReply_to(replyAuthorBean);

        }
        t.setUpdatedAt(DateTools.fmtDate(new Date()));
        re.setData(t);

        if (StringUtils.isNotBlank(tips)) {
            re.show(tips);
        } else {
            re.show("发布成功");
        }
        //clear redis cache
        //帖子/心情评论列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        //游戏评价列表
        fungoCacheGame.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS, "", null);
        return re;
    }

    @Override
    public FungoPageResultDto<ReplyOutPageDto> getReplyList(String memberId, ReplyInputPageDto pageDto) {
        FungoPageResultDto<ReplyOutPageDto> re = new FungoPageResultDto<ReplyOutPageDto>();
        List<ReplyOutPageDto> relist = new ArrayList<ReplyOutPageDto>();
        re.setData(relist);

        Wrapper<Reply> commentWrapper = new EntityWrapper<Reply>();
        commentWrapper.eq("target_id", pageDto.getTarget_id()).eq("state", 0);

        if (pageDto.getSort() == 0 || pageDto.getSort() == 1) {//排序
            commentWrapper.orderBy("created_at", true);
        } else if (pageDto.getSort() == 2) {
            commentWrapper.orderBy("created_at", false);
        } else if (pageDto.getSort() == 3) {
            commentWrapper.orderBy("like_num", true);
        } else if (pageDto.getSort() == 4) {
            commentWrapper.orderBy("like_num", false);
        }
        Page<Reply> page = this.replyService.selectPage(new Page<Reply>(pageDto.getPage(), pageDto.getLimit()), commentWrapper);
        List<Reply> rlist = page.getRecords();

        for (Reply reply : rlist) {

            ReplyOutPageDto bean = new ReplyOutPageDto();

            //!fixme
            //bean.setAuthor(this.userService.getAuthor(reply.getMemberId()));

            AuthorBean replyAuthorBean = null;
            try {
                ResultDto<AuthorBean> replybeanResultDto = systemFacedeService.getAuthor(reply.getMemberId());
                if (null != replybeanResultDto) {
                    replyAuthorBean = replybeanResultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            bean.setAuthor(replyAuthorBean);


            //表情解码
            String content = reply.getContent();
            if (StringUtils.isNotBlank(content)) {
                content = FilterEmojiUtil.decodeEmoji(content);
            }
            bean.setContent(CommonUtils.filterWord(content));

            bean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
            bean.setLike_num(reply.getLikeNum());
            bean.setObjectId(reply.getId());

            if (reply.getReplayToId() != null) {

                //bean.setReply_to(this.userService.getAuthor(reply.getReplayToId()));

                AuthorBean replyToAuthorBean = null;
                try {
                    ResultDto<AuthorBean> replyToBeanResultDto = systemFacedeService.getAuthor(reply.getReplayToId());
                    if (null != replyToBeanResultDto) {
                        replyToAuthorBean = replyToBeanResultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                bean.setReply_to(replyToAuthorBean);

            } else {
//				AuthorBean bean1 =new AuthorBean();
//				bean.setReply_to(bean1);				
            }
            if ("".equals(memberId) || memberId == null) {
                bean.setIs_liked(false);
            } else {

                //!fixme
                //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", reply.getId()).eq("member_id", memberId));

                BasActionDto basActionDto = new BasActionDto();

                basActionDto.setMemberId(memberId);
                basActionDto.setType(0);
                basActionDto.setState(0);
                basActionDto.setTargetId(reply.getId());

                int liked = 0;
                try {
                    ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                    if (null != resultDto) {
                        liked = resultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                bean.setIs_liked(liked > 0 ? true : false);
            }
            bean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
            relist.add(bean);
        }
        PageTools.pageToResultDto(re, page);
        return re;
    }

    @Override
    public ResultDto<EvaluationOutBean> anliEvaluationDetail(String memberId, String evaId) {
        //根据sort和时间来排行
        ResultDto<EvaluationOutBean> result = this.getEvaluationDetail(memberId, evaId);
        EvaluationOutBean eva = result.getData();

        //!fixme 获取游戏数据
        //Game game = gameService.selectById(eva.getGameId());


        GameDto gameDto = null;
        ResultDto<GameDto> gameDtoResultDto = null;
        try {
            gameDtoResultDto = gameFacedeService.selectGameDetails(eva.getGameId(), null);
            if (null != gameDtoResultDto) {
                gameDto = gameDtoResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        if (gameDto != null) {
            eva.setGameIcon(gameDto.getIcon());
            eva.setGameIntro(gameDto.getIntro());
            eva.setGameName(gameDto.getName());
        }
        int sort = eva.getSort();
        String id = eva.getObjectId();

        //!fixme 上一评论
        //GameEvaluation pre = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).gt("created_at", eva.getCreatedAt()).ne("id", id).orderBy("concat(sort,created_at)").last("limit 1"));
        GameEvaluationDto pre = null;
        try {
            ResultDto<GameEvaluationDto> evaluationDtoResultDto = gameFacedeService.getPreGameEvaluation(eva.getCreatedAt(), id);
            if (null != evaluationDtoResultDto) {
                pre = evaluationDtoResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //		GameEvaluation pree = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}",-1).le("sort",sort).ne("id", id).orderBy("concat(sort,created_at)",false).last("limit 1"));
        //!fixme 下一评论
        //GameEvaluation next = gameEvaluationService.selectOne(Condition.create().setSqlSelect("id").eq("type", 2).and("state != {0}", -1).le("created_at", eva.getCreatedAt()).ne("id", id).orderBy("concat(sort,created_at)", false).last("limit 1"));

        GameEvaluationDto next = null;
        try {
            ResultDto<GameEvaluationDto> nextGameEvaluationRs = gameFacedeService.getNextGameEvaluation(eva.getCreatedAt(), id);
            if (null != nextGameEvaluationRs) {
                next = nextGameEvaluationRs.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (pre != null) {
            eva.setPreEvaId(pre.getId());
        }//
        if (next != null) {
            eva.setNextEvaId(next.getId());
        }

        return result;
    }


    @Override
    public Set<String> getGameEvaluationHotAndAnliCount(String mb_id, String startDate, String endDate) {
        Set<String> gameEvaSet = null;
        try {

            //!fixme 获取游戏评论集合
            /*
            EntityWrapper<GameEvaluation> evaluationEntityWrapper = new EntityWrapper<>();
            evaluationEntityWrapper.eq("member_id", mb_id);
            evaluationEntityWrapper.between("updated_at", startDate, endDate);
            evaluationEntityWrapper.eq("state", 0);
            //type  0:普通 1:热门 2:精华
            evaluationEntityWrapper.in("type", new Integer[]{1, 2});

              List<GameEvaluation> gameEvaluationsList = gameEvaluationService.selectList(evaluationEntityWrapper);
            */

            List<GameEvaluationDto> gameEvaluationsList = null;
            try {
                ResultDto<List<GameEvaluationDto>> gameEvaRS = gameFacedeService.getEvaluationEntityWrapper(mb_id, startDate, endDate);
                if (null != gameEvaRS) {
                    gameEvaluationsList = gameEvaRS.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            if (null != gameEvaluationsList && !gameEvaluationsList.isEmpty()) {

                gameEvaSet = new HashSet<String>();

                for (GameEvaluationDto gameEvaluation : gameEvaluationsList) {
                    gameEvaSet.add(gameEvaluation.getId());
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查看用户在指定时间段内游戏评论上热门和安利墙的文章数量出现异常", ex);
        }
        logger.info("查看用户在指定时间段内游戏评论上热门和安利墙的文章数量-gameEvaSet:{}", gameEvaSet);
        return gameEvaSet;
    }

    /**
     * 功能描述: 删除文章评论和心情评论
     * @auther: dl.zhang
     * @date: 2019/8/13 11:50
     */
    @Override
    public ResultDto<String> delCommentList(String memberId, int type , List<String> commentIds) {
        AtomicBoolean istrue = new AtomicBoolean( false );
        try {
            if( DelObjectListVO.TypeEnum.POSTEVALUATE.getKey() == type ){
                commentIds.stream().forEach(s ->{
                    CmmComment cmmComment = new CmmComment();
                    cmmComment.setId(s);
                    cmmComment.setState(-1);
                    istrue.set( commentService.updateById( cmmComment ) );
                });
            }else if(DelObjectListVO.TypeEnum.COMMENTEVALUATE.getKey() == type){
                commentIds.stream().forEach(s ->{
                    MooMessage mooMessage = new MooMessage();
                    mooMessage.setId(s);
                    mooMessage.setState(-1);
                    istrue.set( messageServive.updateById( mooMessage ) );
                });
            } else if(DelObjectListVO.TypeEnum.POSTREPLY.getKey() == type){
                commentIds.stream().forEach(s ->{
                    Reply reply = new Reply();
                    reply.setId(s);
                    reply.setState(-1);
                    istrue.set( replyService.updateById( reply ) );
                });
            }else if(DelObjectListVO.TypeEnum.COMMENTREPLY.getKey() == type){
                commentIds.stream().forEach(s ->{
                    Reply reply = new Reply();
                    reply.setId(s);
                    reply.setState(-1);
                    istrue.set( replyService.updateById( reply ) );
                });
            }else if(DelObjectListVO.TypeEnum.GAMEREPLY.getKey() == type){
                commentIds.stream().forEach(s ->{
                    Reply reply = new Reply();
                    reply.setId(s);
                    reply.setState(-1);
                    istrue.set( replyService.updateById( reply ) );
                });
            }
        }catch (Exception e){
            logger.error( "delCommentList删除评论异常,类型:"+type+",id:"+JSON.toJSONString(commentIds),e);
        }
        return istrue.get() ? ResultDto.success(): ResultDto.error( "-1","删除评论失败");
    }


    //获取回复信息
    private ReplyBean getReplyBean(Reply reply) {
        ReplyBean replybean = new ReplyBean();

        //!fixme
        //replybean.setAuthor(this.userService.getAuthor(reply.getMemberId()));

        AuthorBean replyToAuthorBean = null;
        try {
            ResultDto<AuthorBean> replyToBeanResultDto = systemFacedeService.getAuthor(reply.getMemberId());
            if (null != replyToBeanResultDto) {
                replyToAuthorBean = replyToBeanResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        replybean.setAuthor(replyToAuthorBean);

        replybean.setContent(CommonUtils.filterWord(reply.getContent()));
        replybean.setCreatedAt(DateTools.fmtDate(reply.getCreatedAt()));
        replybean.setObjectId(reply.getId());
        replybean.setUpdatedAt(DateTools.fmtDate(reply.getUpdatedAt()));
        replybean.setLike_num(reply.getLikeNum());
        replybean.setReplyToId(reply.getReplayToId());


        //!fixme
    /*
         Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", reply.getReplayToId()));

        if (m != null) {
            replybean.setReplyToName(m.getUserName());
        }*/

        List<String> idsList = new ArrayList<String>();
        idsList.add(reply.getReplayToId());

        ResultDto<List<MemberDto>> listMembersByids = null;
        try {
            listMembersByids = systemFacedeService.listMembersByids(idsList, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MemberDto memberDto = null;
        if (null != listMembersByids) {
            List<MemberDto> memberDtoList = listMembersByids.getData();
            if (null != memberDtoList && !memberDtoList.isEmpty()) {
                memberDto = memberDtoList.get(0);
            }
        }

        if (null != memberDto) {
            replybean.setReplyToName(memberDto.getUserName());
        }

        return replybean;
    }

    //----------
}
