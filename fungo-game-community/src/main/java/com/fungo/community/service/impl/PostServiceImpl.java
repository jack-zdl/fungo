package com.fungo.community.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.dao.mapper.CmmPostGameMapper;
import com.fungo.community.dao.service.BasVideoJobDaoService;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.*;
import com.fungo.community.facede.GameFacedeService;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.facede.TSMQFacedeService;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.function.FungoLivelyCalculateUtils;
import com.fungo.community.function.SerUtils;
import com.fungo.community.service.ICounterService;
import com.fungo.community.service.IPostService;
import com.fungo.community.service.IVideoService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.consts.Setting;
import com.game.common.dto.*;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.*;
import com.game.common.dto.community.StreamInfo;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.util.*;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.EmojiDealUtil;
import com.game.common.util.emoji.FilterEmojiUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@EnableAsync
@Service
public class PostServiceImpl implements IPostService {

    private static final Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    @Autowired
    private CmmPostDaoService postService;

    @Autowired
    private CmmPostDao cmmPostDao;

    @Autowired
    private CmmCommunityDaoService communityService;

    @Autowired
    private ICounterService iCountService;

    @Autowired
    private BasVideoJobDaoService videoJobService;

    @Autowired
    private IVideoService vdoService;


    @Autowired
    private FungoCacheArticle fungoCacheArticle;


    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    //依赖系统和用户微服务
    @Autowired
    private SystemFacedeService systemFacedeService;

    //依赖游戏微服务
    @Autowired
    private GameFacedeService gameFacedeService;

    @Autowired
    private TSMQFacedeService tSMQFacedeService;

    @Autowired
    private CmmPostCircleMapper cmmPostCircleMapper;

    @Autowired
    private CmmPostGameMapper cmmPostGameMapper;

    @Autowired
    private CmmCircleMapper cmmCircleMapper;

    //依赖系统和用户微服务
    @Autowired(required = false)
    private SystemFeignClient systemFeignClient;


    @Override
    @Transactional
    public ResultDto<ObjectId> addPost(PostInput postInput, String user_id) throws Exception {


        if (postInput == null) {
            return ResultDto.error("222", "不存在的帖子内容");
        }
//		if(CommonUtil.isNull(postInput.getContent())) {
//			return ResultDto.error("-1", "请发布内容!");
//		}
        if (user_id == null) {
            return ResultDto.error("126", "不存在的用户");
        }

        //!fixme 查询用户数据
        //Member member = memberService.selectById(user_id);

        List<String> idsList = new ArrayList<String>();
        idsList.add(user_id);
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


        if (memberDto == null) {
            return ResultDto.error("126", "不存在的用户");
        }

        if (memberDto.getLevel() < 2) {
            return ResultDto.error("-1", "等级达到lv2才可发布内容");
        }

        //表情编码
        if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(postInput.getTitle()))) {
            String hexadecimal = FilterEmojiUtil.encodeEmoji(postInput.getTitle());

            // String hexadecimal = EmojiParser.parseToAliases(postInput.getTitle());

            postInput.setTitle(hexadecimal);
        }

        String htmlData = postInput.getHtml();
        if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(postInput.getHtml()))) {
            String hexadecimalHtml = FilterEmojiUtil.encodeEmoji(postInput.getHtml());
            postInput.setHtml(hexadecimalHtml);
        }


        //fix:
        String txtcontent = "";
        if (StringUtils.isNotBlank(htmlData)) {
            //postInput.getHtml().replaceAll("<img src=.+?>", "");
            txtcontent = htmlData.replaceAll("<img src=.+?>", "");
            txtcontent = txtcontent.replaceAll("</?[^>]+>", "");
            txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");

            txtcontent = FilterEmojiUtil.encodeEmoji(txtcontent);
            // txtcontent = EmojiParser.parseToAliases(txtcontent);
        }


        CmmPost post = new CmmPost();
        post.setMemberId(user_id);
        post.setTitle(postInput.getTitle());
        post.setTags(postInput.getTagId());
        //vedio
        if (!CommonUtil.isNull(postInput.getVideo()) || !CommonUtil.isNull(postInput.getVideoId())) {
            if (memberDto.getLevel() < 3) {
                return ResultDto.error("-1", "等级达到lv3才可发布视频");
            }
//          post.setVideo(postInput.getVideo());
        }

        post.setGameList(parseGameLabelToDB(postInput.getHtml()));


//		String txtcontent = postInput.getHtml().replaceAll("<img src=.+?>", ""); 
//		txtcontent = txtcontent.replaceAll("</?[^>]+>", ""); 
//      txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");


        post.setContent(txtcontent);
        post.setCommunityId(postInput.getCommunity_id());
        post.setHtmlOrigin(SerUtils.saveOrigin(postInput.getHtml()));
        post.setOrigin(postInput.getOrigin());
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(postInput.getHtml());
        ArrayList<String> list = new ArrayList<String>();
        while (m_image.find()) {
            img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                list.add(m.group(1));
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            post.setImages(objectMapper.writeValueAsString(list));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (list.size() != 0) {
            post.setCoverImage(list.get(0));
        }


        Date date = new Date();
        post.setCreatedAt(date);
        post.setEditedAt(date);
//      post.setLastReplyAt(date);
        post.setUpdatedAt(date);
        post.setState(1);
        if (!CommonUtil.isNull(postInput.getVideoId())) {
            post.setState(0);
        }

        //虚列
        Integer clusterIndex_i = Integer.parseInt(clusterIndex);
        post.setPostId(PKUtil.getInstance(clusterIndex_i).longPK());
        post.setRecommend(0);
        boolean flag = postService.insert(post);

        //插入关系 文章-圈子
        if (StringUtil.isNotNull(postInput.getCircleId())) {
            CmmPostCircle postCircle = new CmmPostCircle();
            postCircle.setId(PrimaryKeyUtils.uniqueId());
            postCircle.setCircleId(postInput.getCircleId());
            postCircle.setPostId(post.getId());
            postCircle.setCreatedAt(new Date());
            postCircle.setUpdatedAt(new Date());
            postCircle.setDefaultLink(1);
            cmmPostCircleMapper.insert(postCircle);
        }

        //插入关系  文章-游戏  游戏归属圈子--文章
        List<String> includeGameList = postInput.getIncludeGameList();
        if (includeGameList != null && !includeGameList.isEmpty()) {
            for (String gameid : includeGameList) {
                //gameFacedeService.
                CmmPostGame postGame = new CmmPostGame();
                postGame.setId(PrimaryKeyUtils.uniqueId());
                postGame.setGameId(gameid);
                postGame.setPostId(post.getId());
                postGame.setCreatedAt(new Date());
                postGame.setUpdatedAt(new Date());
                postGame.setPostTitle(post.getTitle());
                //暂时不设置游戏名
                //postGame.setGameName(gameDto.getName());
                //查询出游戏对应的社区id 插入社区id
                CmmCommunity community = communityService.selectOne(new EntityWrapper<CmmCommunity>().eq("game_id", gameid));
                if(community!=null&&StringUtil.isNotNull(community.getId())){
                    postGame.setCmmId(community.getId());
                    postGame.setCmmName(community.getName());
                }
                cmmPostGameMapper.insert(postGame);
                //查询游戏是否有圈子 有圈子-建立文章圈子关系
                String circleId = cmmCircleMapper.selectCircleByGameId(gameid);
                //已经保存过关系，不用重新保存
                if (StringUtil.isNotNull(circleId) && !circleId.equals(postInput.getCircleId())) {
                    CmmPostCircle postCircle = new CmmPostCircle();
                    postCircle.setId(PrimaryKeyUtils.uniqueId());
                    postCircle.setCircleId(circleId);
                    postCircle.setPostId(post.getId());
                    postCircle.setCreatedAt(new Date());
                    postCircle.setUpdatedAt(new Date());
                    postCircle.setDefaultLink(0);
                    cmmPostCircleMapper.insert(postCircle);
                }
            }

        }


        //视频处理
        if (!CommonUtil.isNull(postInput.getVideoId())) {
            BasVideoJob videoJob = videoJobService.selectOne(new EntityWrapper<BasVideoJob>().eq("video_id", postInput.getVideoId()));
            if (videoJob != null) {
                videoJob.setBizType(1);
                videoJob.setBizId(post.getId());
                videoJob.setUpdatedAt(new Date());
                post.setVideoUrls(videoJob.getVideoUrls());
                post.setVideo(videoJob.getReVideoUrl());
                String coverImg = vdoService.getVideoImgInfo(videoJob.getVideoId());
                post.setVideoCoverImage(coverImg);
                post.updateById();
                post.setState(1);

                post.updateById();
                videoJob.updateById();
            } else {
                videoJob = new BasVideoJob();
                videoJob.setBizId(post.getId());
                videoJob.setBizType(1);
                videoJob.setVideoId(postInput.getVideoId());
                videoJob.setCreatedAt(new Date());
                videoJob.setUpdatedAt(new Date());
                videoJob.setStatus(0);
                videoJobService.insert(videoJob);
            }

        }


//		视频压缩 等级 pc
//		if(!CommonUtil.isNull(postInput.getVideo())) {
//			BasVideoJob videoJob = videoJobService.selectOne(new EntityWrapper<BasVideoJob>().eq("biz_id", post.getId()).eq("biz_type", 1));
//			post.setState(0);
//			postService.updateById(post);
//			BasVideoJob videoJob = new BasVideoJob();
//			videoJob.setBizId(post.getId());
//			videoJob.setBizType(1);
//			videoJob.setBizVideoUrl(postInput.getVideo());
//			videoJob.setCreatedAt(new Date());
//			videoJob.setUpdatedAt(new Date());
//			videoJob.setStatus(0);
//			videoJobService.insert(videoJob);
//			//压缩
//			vdService.compress(videoJob.getId(), postInput.getVideo());
//		}


      /*
       //历史代码
        BasAction action = new BasAction();
        action.setCreatedAt(new Date());
        action.setUpdatedAt(new Date());
        action.setMemberId(user_id);
        action.setType(11);
        action.setTargetType(1);
        action.setTargetId(post.getId());
        action.setState(0);
        action.insert();
        */

        //-----start
        //MQ 业务数据发送给系统用户业务处理
        BasActionDto basActionDtoAdd = new BasActionDto();
        basActionDtoAdd.setCreatedAt(new Date());
        basActionDtoAdd.setUpdatedAt(new Date());
        basActionDtoAdd.setMemberId(user_id);
        basActionDtoAdd.setType(11);
        basActionDtoAdd.setTargetType(1);
        basActionDtoAdd.setTargetId(post.getId());
        basActionDtoAdd.setState(0);


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
        logger.info("--添加帖子-执行添加用户动作行为数据--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
        //-----start


//		ResultDto<String> finishTask = logService.finishTask(user_id, "POST_ADD", "");

        //V2.4.6版本之前任务
        //每日任务
        //int addTaskCore = gameProxyService.addTaskCore(Setting.ACTION_TYPE_POST, user_id, post.getId(), Setting.RES_TYPE_POST);

        //V2.4.6版本任务
        String tips = "";
        //1 fungo币
        /*Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(user_id, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_COIN.code());
        */

        //2 经验值
        /*
        Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(user_id, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP.code());
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
        taskDtoCoin.setMbId(user_id);
        taskDtoCoin.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoCoin.setTaskType(MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT);
        taskDtoCoin.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_COIN.code());


        //exp task
        TaskDto taskDtoExp = new TaskDto();
        taskDtoExp.setRequestId(uuidExp);
        taskDtoExp.setMbId(user_id);
        taskDtoExp.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoExp.setTaskType(MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT);
        taskDtoExp.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP.code());

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


        ActionInput actioninput = new ActionInput();
        actioninput.setTarget_type(4);
        actioninput.setTarget_id(postInput.getCommunity_id());
        if(StringUtil.isNotNull(postInput.getCommunity_id())){
            boolean addCounter = iCountService.addCounter(user_id, 7, actioninput);
        }

        ResultDto<ObjectId> resultDto = new ResultDto<ObjectId>();

        if (flag) {
            ObjectId o = new ObjectId();
            o.setId(post.getId());
            resultDto.setData(o);

            if (StringUtils.isNotBlank(tips)) {
                resultDto.show(tips);
            } else {
                resultDto.show("发布成功!");
            }
        }

        //clear redis cache
        //首页文章帖子列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
        //帖子列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST, "", null);
        if(StringUtil.isNotNull(postInput.getCommunity_id())){
            //社区置顶文章(2.4.3)
            fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_TOPIC, postInput.getCommunity_id(), null);
        }
        //我的文章(2.4.3)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_POSTS, "", null);

        return resultDto;
    }


    //社区文章用户执行任务
    private void doExcTaskSendMQ(TaskDto taskDto) {
        //-----start
        //MQ 业务数据发送给系统用户业务处理
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

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
        logger.info("--社区文章用户发布文章执行任务--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
        //-----start
    }


    @Override
    @Transactional
    public ResultDto<String> deletePost(String postId, String userId) {
        if (postId == null) {
            return ResultDto.error("221", "找不到帖子id");
        }
        CmmPost cmmPost = postService.selectById(postId);
        if (cmmPost == null) {
            return ResultDto.error("223", "没有这个帖子");
        }

        if (userId == null) {
            return ResultDto.error("211", "找不到用户id");
        }

        //!fixme 查询用户数据
        //Member user = memberService.selectById(userId);

        List<String> idsList = new ArrayList<String>();
        idsList.add(userId);

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

        if (memberDto == null) {
            return ResultDto.error("126", "用户不存在");
        }
        if (!cmmPost.getMemberId().equals(userId)) {
            return ResultDto.error("224", "用户和帖子不匹配");
        }

        cmmPost.setUpdatedAt(new Date());
//        cmmPost.setEditedAt(new Date());
        cmmPost.setMemberId(userId);
        cmmPost.setState(-1);
        postService.updateById(cmmPost);

        //扣除经验
        int score = 3;

        //!fixme 扣减用户经验值 MQ
        // --------------------------------------------------start-----------------------------------------------
     /*
        IncentAccountScore scoreCount = accountScoreService.selectOne(new EntityWrapper<IncentAccountScore>().eq("mb_id", userId).eq("account_group_id", 1));
        if (scoreCount == null) {
            scoreCount = IAccountDaoService.createAccountScore(user, 1);
        }
        scoreCount.setScoreUsable(scoreCount.getScoreUsable().subtract((new BigDecimal(score))));
        scoreCount.setUpdatedAt(new Date());
        scoreCount.updateById();

        user.setExp(scoreCount.getScoreUsable().intValue());
        user = scoreLogService.updateLevel(user);
        */
        //scoreLogService

        //!fixme 更新用户等级数据
      /*
        IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", userId).eq("rank_type", 1));

        Long prelevel = incentRankedDtoResult.getCurrentRankId();//记录中的等级 可能需要修改
        int curLevel = scoreLogService.getLevel(scoreCount.getScoreUsable().intValue());//现在应有的等级

        //用户需不需要变更等级
        ObjectMapper mapper = new ObjectMapper();
        if (curLevel != prelevel) {

            ranked.setCurrentRankId((long) curLevel);

            ranked.setCurrentRankName("Lv" + curLevel);

            String rankIdtIds = ranked.getRankIdtIds();
            List<HashMap<String, String>> rankList = new ArrayList<>();
            try {
                rankList = mapper.readValue(rankIdtIds, ArrayList.class);
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            boolean add = true;
            for (HashMap m : rankList) {
                if (m.get("rankId").equals(curLevel + "")) {
                    add = false;
                }
            }
            if (add) {
                HashMap<String, String> newMap = new HashMap<String, String>();
                newMap.put("rankId", curLevel + "");
                newMap.put("rankName", "Lv" + curLevel);
                rankList.add(newMap);
                try {
                    ranked.setRankIdtIds(mapper.writeValueAsString(rankList));
                } catch (JsonProcessingException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                //等级变更记录 t_incent_ranked_log 更新
            }
            ranked.setUpdatedAt(new Date());
            ranked.updateById();
            IncentRuleRank rankRule = ruleRankService.selectOne(new EntityWrapper<IncentRuleRank>().eq("id", curLevel));
            scoreLogService.addRankLog(userId, rankRule);

        }
        */
        //--------------------------------------------------end-----------------------------------------------

        //-----start
        //MQ 业务数据发送给系统用户业务处理
        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_POST);

        //发送的队列
        transactionMessageDto.setConsumerQueue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());

        //路由key
        StringBuffer routinKey = new StringBuffer(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_TOPIC_SYSTEM_USER.getName());
        routinKey.deleteCharAt(routinKey.length() - 1);
        routinKey.append("deletePostSubtractExpLevel");

        transactionMessageDto.setRoutingKey(routinKey.toString());

        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.CommunityEnum.CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL.getCode());

        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("mb_id", userId);
        hashMap.put("score", score);

        mqResultDto.setBody(hashMap);

        transactionMessageDto.setMessageBody(JSON.toJSONString(mqResultDto));
        //执行MQ发送
        ResultDto<Long> messageResult = tSMQFacedeService.saveAndSendMessage(transactionMessageDto);
        logger.info("--删除帖子执行扣减用户经验值和等级--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
        //-----start

        ActionInput actioninput = new ActionInput();
        actioninput.setTarget_type(4);
        actioninput.setTarget_id(cmmPost.getCommunityId());
        boolean b = iCountService.subCounter(userId, 7, actioninput);

        //clear redis cache
        //文章内容html-内容
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_CONTENT + postId, "", null);
        //文章内容html-标题
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_TITLE + postId, "", null);
        //帖子详情
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, postId, null);
        //帖子/心情评论列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        //clear redis cache
        //首页文章帖子列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
        //帖子列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST, "", null);
        //社区置顶文章(2.4.3)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_TOPIC, "", null);
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_POSTS, "", null);
        return ResultDto.success("删除成功");

    }

    @Override
    @Transactional
    public ResultDto<String> editPost(PostInput postInput, String userId, String os) throws Exception {
        /*
         * 修改文章时
         * 1，传入video=原始video即保持原来视频
         * 2，传入video=""即删除原有视频
         * 3，传入videoId=新视频videoId即传入新视频
         */
        String postId = postInput.getPostId();
        if (postInput == null || postId == null) {
            return ResultDto.error("222", "找不到帖子内容");
        }
        if (userId == null) {
            return ResultDto.error("211", "找不到用户id");
        }
        CmmPost post = postService.selectById(postId);
        if (post == null) {
            return ResultDto.error("223", "帖子不存在");
        }


        //!fixme 查询用户数据
        /*
            if (memberService.selectById(userId) == null) {
            return ResultDto.error("126", "用户不存在");
        }
        */

        List<String> idsList = new ArrayList<String>();
        idsList.add(userId);
        ResultDto<List<MemberDto>> listMembersByids = null;
        try {
            listMembersByids = systemFacedeService.listMembersByids(idsList, null);
        } catch (Exception ex) {

        }

        MemberDto memberDto = null;
        if (null != listMembersByids) {
            List<MemberDto> memberDtoList = listMembersByids.getData();
            if (null != memberDtoList && !memberDtoList.isEmpty()) {
                memberDto = memberDtoList.get(0);
            }
        }

        if (memberDto == null) {
            return ResultDto.error("126", "用户不存在");
        }


        if (!post.getMemberId().equals(userId)) {
            return ResultDto.error("224", "用户和帖子不匹配");
        }

        //表情编码
        if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(postInput.getTitle()))) {
            String hexadecimal = FilterEmojiUtil.encodeEmoji(postInput.getTitle());
            postInput.setTitle(hexadecimal);
        }

        String htmlData = postInput.getHtml();
        if (StringUtils.isNotBlank(EmojiDealUtil.getEmojiUnicodeString(postInput.getHtml()))) {
            String hexadecimalHtml = FilterEmojiUtil.encodeEmoji(postInput.getHtml());
            postInput.setHtml(hexadecimalHtml);
        }


        //fix:
        String txtcontent = "";
        if (StringUtils.isNotBlank(htmlData)) {
            //去除html标签
            //postInput.getHtml().replaceAll("<img src=.+?>", "");
            txtcontent = htmlData.replaceAll("<img src=.+?>", "");
            txtcontent = txtcontent.replaceAll("</?[^>]+>", "");
            txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");

            //表情编码
            txtcontent = FilterEmojiUtil.encodeEmoji(txtcontent);
        }

        String title = postInput.getTitle();

        String content = postInput.getContent();

        String html = postInput.getHtml();
//		String[] images = postInput.getImages();
        String video = postInput.getVideo();

//		post.setMemberId(userId);

        if (!CommonUtil.isNull(video)) {
            post.setVideo(video);
        } else {
            post.setVideo("");
        }
        if (title != null) {
            post.setTitle(postInput.getTitle());
        }
        if (content != null) {
            post.setContent(txtcontent);
        }
        if (html != null) {
            post.setHtmlOrigin(SerUtils.saveOrigin(postInput.getHtml()));
        }
        if (!CommonUtil.isNull(video)) {
            post.setVideo(video);
        } else {
            post.setVideo("");
        }


        if (title != null) {
            post.setTitle(postInput.getTitle());
        }

        if (txtcontent != null) {
            //post.setContent(postInput.getContent());
            post.setContent(txtcontent);
        }

        if (html != null) {
            post.setHtmlOrigin(SerUtils.saveOrigin(postInput.getHtml()));
        }


//		if(images != null) {
//			String imagesStr = arrayToString(images);
//			post.setImages(imagesStr);
//		}
//		String txtcontent = html.replaceAll("<img src=.+?>", ""); 
//
//		txtcontent = txtcontent.replaceAll("</?[^>]+>", ""); 
//        txtcontent = txtcontent.replaceAll("<a>\\s*|\t|\r|\n</a>", "");
//        post.setContent(txtcontent);

//        if(!CommonUtil.isNull(postInput.getTitle())) {
//        	post.setTitle(postInput.getTitle());
//        }
        post.setGameList(parseGameLabelToDB(postInput.getHtml()));
//		post.setHtmlOrigin(SerUtils.saveOrigin(html));
        post.setOrigin(postInput.getOrigin());

        //取出图片
        String img = "";
        Pattern p_image;
        Matcher m_image;
        String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
        p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
        m_image = p_image.matcher(postInput.getHtml());
        ArrayList<String> list = new ArrayList<String>();
        while (m_image.find()) {
            img = m_image.group();
            Matcher m = Pattern.compile("src\\s*=\\s*\"?(.*?)(\"|>|\\s+)").matcher(img);
            while (m.find()) {
                list.add(m.group(1));
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            post.setImages(objectMapper.writeValueAsString(list));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (list.size() != 0) {
            post.setCoverImage(list.get(0));
        }


//        post.setEditedAt(new Date());
        post.setUpdatedAt(new Date());

        //修改 更新视频
        if (!CommonUtil.isNull(postInput.getVideoId())) {
            BasVideoJob videoJob = videoJobService.selectOne(new EntityWrapper<BasVideoJob>().eq("video_id", postInput.getVideoId()));
            if (videoJob == null) {
                videoJob = new BasVideoJob();
                videoJob.setBizId(post.getId());
                videoJob.setBizType(1);
                videoJob.setVideoId(postInput.getVideoId());
                videoJob.setCreatedAt(new Date());
                videoJob.setUpdatedAt(new Date());
                videoJob.setStatus(0);
                videoJobService.insert(videoJob);

            } else {
                //判断有没有改url 修改视频 无需判断
//				if(videoJob.getReVideoUrl() == null ||!videoJob.getReVideoUrl().equals(post.getVideo())) {
                //改了,压缩
                videoJob.setBizType(1);
                videoJob.setBizId(post.getId());
                videoJob.setBizVideoUrl(post.getVideo() != null ? post.getVideo() : "");
                videoJob.setUpdatedAt(new Date());
                String coverImg = vdoService.getVideoImgInfo(videoJob.getVideoId());
                post.setVideo(videoJob.getReVideoUrl());
                post.setVideoUrls(videoJob.getVideoUrls());
                post.setVideoCoverImage(coverImg);
                videoJobService.deleteById(videoJob);
//				}
            }
        }


        postService.updateAllColumnById(post);


        //clear redis cache
        //文章内容html-内容
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_CONTENT + postId, "", null);
        //文章内容html-标题
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_HTML_TITLE + postId, "", null);
        //帖子详情
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, postId, null);
        //帖子/心情评论列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        //首页文章帖子列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_INDEX_POST_LIST, "", null);
        //帖子列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST, "", null);
        //社区置顶文章(2.4.3)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_TOPIC, post.getCommunityId(), null);
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_POSTS, "", null);
        return ResultDto.success();
    }

    @Override
    public ResultDto<PostOut> getPostDetails(String postId, String userId, String os) throws Exception {
        PostOut out = null;
        //from redis cache
        out = (PostOut) fungoCacheArticle.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, postId);
        if (null != out) {
            //更新文章浏览量数据
            Object postWatchNumCache = fungoCacheArticle.getIndexCache(FungoCacheArticle.FUNGO_CORE_API_POST_CONTENT_DETAIL_WATCHNUM, postId);
            if (null != postWatchNumCache) {
                Integer postWatchNum_i = (Integer) postWatchNumCache;
                postWatchNum_i += 1;
                //浏览量随机增加
                Long viewNewCount = FungoLivelyCalculateUtils.calcViewAndDownloadCount(postWatchNum_i);
                postWatchNum_i = viewNewCount.intValue();
                out.setWatch_num(postWatchNum_i);
                //记录浏览量 同时保存到Redis中
                fungoCacheArticle.excIndexCache(true, FungoCacheArticle.FUNGO_CORE_API_POST_CONTENT_DETAIL_WATCHNUM, postId, postWatchNum_i);
                //更新文章浏览数DB
                iCountService.addCounter("t_cmm_post", "watch_num", postId);
                CmmPost cmmPost = new CmmPost();
                cmmPost.setId(postId);
                cmmPost.setBoomWatchNum(viewNewCount);
                cmmPost.updateById();
            }
            return ResultDto.success(out);
        }

        if (postId == null) {
            return ResultDto.error("221", "找不到帖子id");
        }
        CmmPost cmmPost = postService.selectOne(new EntityWrapper<CmmPost>().eq("id", postId).and("state != {0}", -1));
        if (cmmPost == null) {
            return ResultDto.error("223", "找不到帖子");
        }
        //表情解码
        if (StringUtils.isNotBlank(cmmPost.getTitle())) {
            String interactTitle = FilterEmojiUtil.decodeEmoji(cmmPost.getTitle());
            cmmPost.setTitle(interactTitle);
        }
        if (StringUtils.isNotBlank(cmmPost.getContent())) {
            String interactContent = FilterEmojiUtil.decodeEmoji(cmmPost.getContent());
            cmmPost.setContent(interactContent);
        }
        if (StringUtils.isNotBlank(cmmPost.getHtmlOrigin())) {
            String interactHtmlOrigin = FilterEmojiUtil.decodeEmoji(cmmPost.getHtmlOrigin());
            cmmPost.setHtmlOrigin(interactHtmlOrigin);
        }
        out = new PostOut();
        //video
        out.setVideo(cmmPost.getVideo());
        out.setVideoCoverImage(cmmPost.getVideoCoverImage());
        out.setObjectId(cmmPost.getId());
        out.setTitle(CommonUtils.filterWord(cmmPost.getTitle()));
        String tags = cmmPost.getTags();
     /*   if (!CommonUtil.isNull(tags)) {
            out.setTags(tags);
        }*/
        if (cmmPost.getImages() != null) {
            out.setImages(Arrays.asList(cmmPost.getImages().replace("]", "").replace("[", "").replace("\"", "").split(",")));
        }
        out.setCollect_num(cmmPost.getCollectNum());
        out.setComment_num(cmmPost.getCommentNum());
        out.setCover_image(cmmPost.getCoverImage());
        String origin = cmmPost.getHtmlOrigin();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> gameMapList = new ArrayList<>();
        if (cmmPost.getGameList() != null) {
            gameMapList = mapper.readValue(cmmPost.getGameList(), ArrayList.class);
            for (Map<String, Object> m : gameMapList) {

                //!fixme 获取游戏平均分
                //m.put("rating", iGameService.getGameRating((String) m.get("objectId")) + "");
                //获取游戏平均分
                String gameId = (String) m.get("objectId");
                double gameAverage = 0;
                try {
                    gameAverage = gameFacedeService.selectGameAverage(gameId, 0);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                m.put("gameRating", gameAverage);
            }
        }
        String gameList = mapper.writeValueAsString(gameMapList);
        if (StringUtils.equalsIgnoreCase("iOS", os) || StringUtils.equalsIgnoreCase("Android", os)) {
            if (origin != null && !"".equals(origin)) {
                out.setHtml(SerUtils.returnOriginHrml(SerUtils.getOriginImageContent(CommonUtils.filterWord(origin), out.getImages(), gameList)));
            }
            String content = cmmPost.getContent();
            if (!CommonUtil.isNull(content)) {
                //out.setContent(content.length() > 25 ? CommonUtils.filterWord(content.substring(0, 25)) : CommonUtils.filterWord(content));
                out.setContent(content);
            }
            //!fixme
            out.setHtml_origin(origin);
//          String html_origin = CommonUtils.filter(cmmPost.getHtmlOrigin());
//			out.setHtml_origin(html_origin);
        } else {
            out.setHtml_origin(SerUtils.returnOriginHrml(SerUtils.getOriginImageContent(CommonUtils.filterWord(origin), out.getImages(), gameList)));
            out.setContent(origin);
            out.setTxt(CommonUtils.filterWord(cmmPost.getContent()));
        }
        out.setOrigin(cmmPost.getOrigin());
        out.setCreatedAt(DateTools.fmtDate(cmmPost.getCreatedAt()));
        out.setUpdatedAt(DateTools.fmtDate(cmmPost.getUpdatedAt()));
        out.setState(cmmPost.getState());
        //浏览量
        Integer cmmPostWatchNum = 0;
        if (null == cmmPost.getBoomWatchNum() || 0 == cmmPost.getBoomWatchNum()) {
            cmmPostWatchNum = cmmPost.getWatchNum();
        } else {
            cmmPostWatchNum = cmmPost.getBoomWatchNum().intValue();
        }
        cmmPostWatchNum += 1;
        //浏览量随机增加
        Long viewCountNew = FungoLivelyCalculateUtils.calcViewAndDownloadCount(cmmPostWatchNum);
        cmmPostWatchNum = viewCountNew.intValue();
        out.setWatch_num(cmmPostWatchNum);
        out.setLike_num(cmmPost.getLikeNum());
        out.setReport_num(cmmPost.getReportNum());
        //根据运行环境,不同的链接
        String env = Setting.RUN_ENVIRONMENT;
        if (env.equals("dev")) {
            out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/post/" + cmmPost.getId());
//			out.setHtml_url("http://test.mingbonetwork.com/api/content/post/html/"+cmmPost.getId());
            out.setHtml_url(Setting.DEFAULT_SHARE_URL_DEV + "/api/content/post/html/" + cmmPost.getId());
        } else if (env.equals("pro")) {
            out.setLink_url(Setting.DEFAULT_SHARE_URL_PRO + "/post/" + cmmPost.getId());
//			out.setHtml_url("http://api.fungoweb.com/api/content/post/html/"+cmmPost.getId());
            out.setHtml_url(Setting.DEFAULT_SHARE_URL_PRO + "/api/content/post/html/" + cmmPost.getId());
        } else if (env.equals("uat")) {
            out.setLink_url(Setting.DEFAULT_SHARE_URL_DEV + "/post/" + cmmPost.getId());
//			out.setHtml_url("http://test.mingbonetwork.com/api/content/post/html/"+cmmPost.getId());
            out.setHtml_url(Setting.DEFAULT_SHARE_URL_DEV + "/api/content/post/html/" + cmmPost.getId());
        }

//		Member author = memberService.selectById(cmmPost.getMemberId());
        //查询是否发布到圈子
        Map<String, Object> communityMap = new HashMap<String, Object>();
        //type 0 游戏社区 1：官方社区 2 圈子 3.什么都没有
        communityMap.put("type", 3);
        CmmCircle circle = null;
        String circleId = cmmPostCircleMapper.getCmmCircleByPostId(cmmPost.getId());
        if (StringUtil.isNotNull(circleId)) {
            circle = cmmCircleMapper.selectById(circleId);
        }
        if (circle != null) {
            communityMap.put("objectId", circle.getId());
            communityMap.put("name", circle.getCircleName());
            communityMap.put("icon", circle.getCircleIcon());
            communityMap.put("intro", circle.getIntro());
            //3标识本次是圈子
            communityMap.put("type", 2);
        } else {
            //分两种情况 是否关联了社区，关联社区走之前逻辑，否则走关联游戏逻辑
            CmmCommunity community = null;
            if (StringUtil.isNotNull(cmmPost.getCommunityId())) {
                community = communityService.selectById(cmmPost.getCommunityId());
            }
            if (community == null) {
                //找到文章关联的游戏，选择一个游戏社区
                String communityId = cmmPostGameMapper.getCommunityIdByPostId(cmmPost.getId());
                if (StringUtil.isNotNull(communityId)) {
                    community = communityService.selectById(communityId);
                }
            }
            if (community != null) {
                if (!CommonUtil.isNull(cmmPost.getVideo()) && CommonUtil.isNull(cmmPost.getCoverImage())) {
                    out.setCover_image(community.getCoverImage());
                }
                if (community != null) {
                    communityMap.put("objectId", community.getId());
                    communityMap.put("name", community.getName());
                    communityMap.put("icon", community.getIcon());
                    communityMap.put("intro", community.getIntro());
                    communityMap.put("type", community.getType());
                    //游戏社区的评分 标签
                    if (community.getType() == 0) {
                        communityMap.put("gameId", community.getGameId());
                        //获取游戏平均分
                        double gameAverage = 0;
                        try {
                            gameAverage = gameFacedeService.selectGameAverage(community.getGameId(), 0);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        communityMap.put("gameRating", gameAverage);
                    }
                    if (!CommonUtil.isNull(community.getGameId())) {
                        GameDto gameDto = null;
                        try {
                            ResultDto<GameDto> gameDtoResultDto = gameFacedeService.selectGameDetails(community.getGameId(), 0);
                            if (null != gameDtoResultDto) {
                                gameDtoResultDto.getData();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        if (gameDto != null) {
                            communityMap.put("gameTags", gameDto.getTags() == null ? new String[0] : gameDto.getTags().split(","));
                        } else {
                            communityMap.put("gameTags", new ArrayList<String>());
                        }
                    } else {
                        communityMap.put("gameTags", new ArrayList<String>());
                    }
                }
            }
        }
        //视频详情
        if (!CommonUtil.isNull(cmmPost.getVideoUrls())) {
            ArrayList<StreamInfo> streams = (ArrayList<StreamInfo>) JSON.parseArray(cmmPost.getVideoUrls(), StreamInfo.class);
            out.setVideoList(streams);
        }
        //查询是否关注、收藏、点赞
        if (userId != null) {
            List<String> idsList = new ArrayList<String>();
            idsList.add(userId);
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
                //---like 点赞 | 0
                BasActionDto basActionDtoLike = new BasActionDto();

                basActionDtoLike.setMemberId(userId);
                basActionDtoLike.setType(0);
                basActionDtoLike.setState(0);
                basActionDtoLike.setTargetId(cmmPost.getId());
                int like = 0;
                try {
                    ResultDto<Integer> resultDtoLike = systemFacedeService.countActionNum(basActionDtoLike);
                    if (null != resultDtoLike) {
                        like = resultDtoLike.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                //--收藏 | 4
                BasActionDto basActionDtoCollected = new BasActionDto();
                basActionDtoCollected.setMemberId(userId);
                basActionDtoCollected.setType(4);
                basActionDtoCollected.setState(0);
                basActionDtoCollected.setTargetId(cmmPost.getId());
                int collected = 0;
                try {
                    ResultDto<Integer> resultDtoCollected = systemFacedeService.countActionNum(basActionDtoCollected);

                    if (null != resultDtoCollected) {
                        collected = resultDtoCollected.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                boolean is_liked = like > 0 ? true : false;
                boolean is_collected = collected > 0 ? true : false;
                out.setIs_liked(is_liked);
                out.setIs_collected(is_collected);
                //authorMap.put("is_followed", is_followed);
            }
        }
        out.setLink_community(communityMap);

        //!fixme 获取用户数据
        AuthorBean authorBean = new AuthorBean();
        try {
            if (StringUtils.isNotBlank(cmmPost.getMemberId())) {
                ResultDto<AuthorBean> userCardResult = systemFacedeService.getUserCard(cmmPost.getMemberId(), userId);
                if (null != userCardResult) {
                    authorBean = userCardResult.getData();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        out.setAuthor(authorBean);
        out.setType(cmmPost.getType());

        CmmPost cmmPostUpdate = new CmmPost();
        cmmPostUpdate.setId(postId);
        cmmPostUpdate.setWatchNum(cmmPost.getWatchNum() + 1);
        cmmPostUpdate.setBoomWatchNum(viewCountNew);
        cmmPostUpdate.updateById();

        //redis cache
        fungoCacheArticle.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_DETAIL, postId, out);
        //记录浏览量 同时保存到Redis中
        fungoCacheArticle.excIndexCache(true, FungoCacheArticle.FUNGO_CORE_API_POST_CONTENT_DETAIL_WATCHNUM, postId, cmmPostWatchNum);
        return ResultDto.success(out);
    }


    @Override
    public FungoPageResultDto<PostOutBean> getPostList(String userId, PostInputPageDto postInputPageDto) throws Exception {

        //结果模型
        FungoPageResultDto<PostOutBean> result = null;

        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST;
        String keySuffix = userId + JSON.toJSONString(postInputPageDto);

        result = (FungoPageResultDto<PostOutBean>) fungoCacheArticle.getIndexCache(keyPrefix, keySuffix);
        if (null != result && null != result.getData() && result.getData().size() > 0) {
            return result;
        }

        int limit = postInputPageDto.getLimit();
        int page = postInputPageDto.getPage();
        int sort = postInputPageDto.getSort();
        String communityId = postInputPageDto.getCommunity_id();

        if (communityId == null) {
            return FungoPageResultDto.error("241", "社区id不存在");
        }
        CmmCommunity community = communityService.selectOne(new EntityWrapper<CmmCommunity>().eq("id", communityId).and("state <> {0}", -1));
        if (community == null) {
            return FungoPageResultDto.error("241", "所属社区不存在");
        }


        result = new FungoPageResultDto<PostOutBean>();
        List<PostOutBean> relist = new ArrayList<PostOutBean>();
        Wrapper<CmmPost> wrapper = new EntityWrapper<CmmPost>().eq("community_id", communityId).eq("state", 1).ne("type", 3);
//		Wrapper<CmmPost> wrapper = new EntityWrapper<CmmPost>().eq("community_id",communityId ).eq("state", 1).ne("type", 3).ne("topic", 2); eq topic1
        List<CmmPost> postList = new ArrayList<CmmPost>();

        if (sort == 1) {//  时间正序
            wrapper.orderBy("updated_at", true);
        } else if (sort == 2) {//  时间倒序
            wrapper.orderBy("updated_at", false);
        } else if (sort == 3) {// 热力值正序
//            wrapper.orderBy("comment_num,like_num", true);
            wrapper.last("ORDER BY comment_num ASC,like_num ASC");
        } else if (sort == 4) {// 热力值倒序
//            wrapper.orderBy("comment_num,like_num", false);
            wrapper.last("ORDER BY comment_num DESC,like_num DESC");
        } else if (sort == 5) {//最后回复时间
            wrapper.orderBy("last_reply_at", false);
        } else {
            wrapper.orderBy("updated_at", false);
        }
        //int count = postList.size();
        Page<CmmPost> pagePost = postService.selectPage(new Page<>(page, limit), wrapper);
        postList = pagePost.getRecords();

        if (postList == null || postList.isEmpty()) {
            ResultDto.error("221", "找不到符合条件的帖子");
        }

        boolean b = CommonUtil.isNull(userId);

        for (CmmPost post : postList) {
            //表情解码
            if (StringUtils.isNotBlank(post.getTitle())) {
                String interactTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());
                post.setTitle(interactTitle);
            }
            if (StringUtils.isNotBlank(post.getContent())) {
                String interactContent = FilterEmojiUtil.decodeEmoji(post.getContent());

                //bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                interactContent = interactContent.length() > 40 ? Html2Text.removeHtmlTag(interactContent.substring(0, 40)) : Html2Text.removeHtmlTag(interactContent);

                post.setContent(interactContent);
            }

//	        if (StringUtils.isNotBlank(post.getHtmlOrigin())) {
//	            String interactHtmlOrigin = FilterEmojiUtil.resolveToEmojiFromByte(post.getHtmlOrigin());
//	            post.setHtmlOrigin(interactHtmlOrigin);
//	        }


            PostOutBean bean = new PostOutBean();

            //!fixme 查询用户数据
            //bean.setAuthor(iUserService.getAuthor(post.getMemberId()));
            try {
                ResultDto<AuthorBean> authorBeanResultDto = systemFacedeService.getAuthor(post.getMemberId());
                if (null != authorBeanResultDto) {
                    AuthorBean authorBean = authorBeanResultDto.getData();
                    bean.setAuthor(authorBean);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (bean.getAuthor() == null) {
                continue;
            }
            String content = post.getContent();
            if (!CommonUtil.isNull(content)) {
                // bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                bean.setContent(CommonUtils.filterWord(content));
            }

            bean.setUpdated_at(DateTools.fmtDate(post.getUpdatedAt()));

            //fix bug:把V2.4.2存在的createdAt字段，恢复回来 [by mxf 2019-01-08]
            bean.setCreatedAt(DateTools.fmtDate(post.getCreatedAt()));
            //end

            bean.setVideoUrl(post.getVideo());
            bean.setImageUrl(post.getCoverImage());
            bean.setLikeNum(post.getLikeNum());
            bean.setPostId(post.getId());
            bean.setReplyNum(post.getCommentNum());
            bean.setTitle(CommonUtils.filterWord(post.getTitle()));
//			bean.setCommunityIcon(community.getIcon());
//			bean.setCommunityId(community.getId());
//			bean.setCommunityName(community.getName());
//			if(!CommonUtil.isNull(post.getVideo()) && CommonUtil.isNull(post.getCoverImage())) {
//				bean.setImageUrl(community.getCoverImage());
//			}
            try {
                if (!CommonUtil.isNull(post.getImages())) {
                    ArrayList<String> readValue = new ArrayList<String>();
                    ObjectMapper mapper = new ObjectMapper();
                    readValue = mapper.readValue(post.getImages(), ArrayList.class);

                    //fix bug: Could not read JSON: Cannot construct instance of `java.util.ArrayList$SubList` [by mxf 2019-03-20]
                    int readValueSize = readValue.size();
                    List readValueList = new ArrayList();
                    if (readValueSize > 3) {
                        readValueList.addAll(readValue.subList(0, 3));
                        bean.setImages(readValueList);
                    } else {
                        bean.setImages(readValue);
                    }
                    //老代码
                    //bean.setImages(readValue.size() > 3 ? readValue.subList(0, 3) : readValue);
                    //end
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (CommonUtil.isNull(userId)) {
                bean.setLiked(false);
            } else {

                //!fixme 查询用户点赞数
                //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", post.getId()).eq("member_id", userId));


                BasActionDto basActionDto = new BasActionDto();
                basActionDto.setMemberId(userId);
                basActionDto.setType(0);
                basActionDto.setState(0);
                basActionDto.setTargetId(post.getId());

                int liked = 0;
                try {
                    ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                    if (null != resultDto) {
                        liked = resultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                bean.setLiked(liked > 0 ? true : false);
            }

            //
            bean.setVideoCoverImage(post.getVideoCoverImage());
            bean.setType(post.getType());

            relist.add(bean);
        }

        PageTools.pageToResultDto(result, pagePost);
        result.setData(relist);

        //redis cache
        fungoCacheArticle.excIndexCache(true, keyPrefix, keySuffix, result);

        return result;

    }

    private String arrayToString(String[] str) {
        if (str == null) {
            return "";
        }
        StringBuilder arrStr = new StringBuilder("[");
        for (int i = 0; i < str.length; i++) {
            if (i == str.length - 1) {
                arrStr.append("\"").append(str[i]).append("\"");
            } else {
                arrStr.append("\"").append(str[i]).append("\"").append(",");
            }
        }
        arrStr.append("]");
        return arrStr.toString();
    }

    // 帖子游戏标签解析(发帖)
    public String parseGameLabelToDB(String html) throws Exception {
        String regEx_img = "<game[^>]+>";
        String o = "(objectId|icon|name|category|rating).*?(\".*?){1,1}(\")";

        Matcher m_game = Pattern.compile(regEx_img).matcher(html);
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> maplist = new ArrayList<>();

        while (m_game.find()) {
            Map<String, Object> map = new HashMap<>();
            String group = m_game.group();
            Matcher s = Pattern.compile(o).matcher(group);
            while (s.find()) {
                map.put(s.group().substring(0, s.group().indexOf("=")), s.group().substring(s.group().indexOf("=") + 1, s.group().length()).replace("\"", ""));
                System.out.println(s.group());
            }
            maplist.add(map);
        }

        return mapper.writeValueAsString(maplist);

    }

    // 帖子游戏标签解析(展示)
    public String getGameLabelFormPost(String html, String gameList) throws Exception {

        if (CommonUtil.isNull(html)) {
            return "";
        }
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> readValue = objectMapper.readValue(gameList, ArrayList.class);
        String regEx_img = "<game[^>]+>";
        Matcher m_game = Pattern.compile(regEx_img).matcher(html);
        int i = 0;
        while (m_game.find()) {
            String group = m_game.group();
            Map<String, Object> map = readValue.get(i);
            StringBuilder s = new StringBuilder();
            s.append("<div class=\"game\"> <img src=\"").append(map.get("icon")).append("\" alt=\"游戏图标\" /> <div class=\"info\"> <div class=\"name\">").append(map.get("name")).append("</div> <div class=\"category\">").append(map.get("category")).append("</div> </div>")
                    .append("<div class=\"score\"><span class=\"num\">").append(map.get("rating")).append("</span><span class=\"unit\">分</span></div></div>");
            html = html.replace(group, s.toString());
        }
        return html;

    }

    @Override
    public FungoPageResultDto<Map<String, String>> getTopicPosts(String communityId) {

        FungoPageResultDto<Map<String, String>> re = new FungoPageResultDto<Map<String, String>>();
        List<Map<String, String>> mapList = null;

        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_TOPIC;
        mapList = (List<Map<String, String>>) fungoCacheArticle.getIndexCache(keyPrefix, communityId);
        if (null != mapList && !mapList.isEmpty()) {
            re.setData(mapList);
            return re;
        }

        if (!CommonUtil.isNull(communityId)) {
            return FungoPageResultDto.error("-1", "未找到社区");
        }

        mapList = new ArrayList<>();
        List<CmmPost> list = postService.selectList(new EntityWrapper<CmmPost>().eq("community_id", communityId).eq("type", 3));
        for (CmmPost p : list) {
            Map<String, String> map = new HashMap<>();
            map.put("titile", p.getTitle());
            map.put("objectId", p.getId());
            map.put("video", p.getVideo());
            mapList.add(map);
        }

        //redis cache
        fungoCacheArticle.excIndexCache(true, keyPrefix, communityId, mapList);

        re.setData(mapList);
        return re;
    }


    /**
     * 功能描述: @todo 没有加redis的key
     * @param: []
     * @return: com.game.common.dto.FungoPageResultDto<java.util.Map                                                               <                                                               java.lang.String                                                               ,                                                               java.lang.String>>
     * @auther: dl.zhang
     * @date: 2019/6/18 18:21
     */
    @Override
    public FungoPageResultDto<PostOutBean> getTopicPosts(MemberUserProfile memberUserPrefile, InputPageDto inputPageDto) {
        FungoPageResultDto<PostOutBean> re = new FungoPageResultDto<>();
        List<Map<String, String>> mapList = null;
        mapList = new ArrayList<>();
        Page page = new Page(inputPageDto.getPage(), inputPageDto.getLimit());
        Page<CmmPost> postPage = postService.selectPage(page, new EntityWrapper<CmmPost>().eq("recommend", 1).orderBy("sort", false));
        List<PostOutBean> list = new ArrayList<>();
        if (postPage != null) {
            ObjectMapper mapper = new ObjectMapper();
            for (CmmPost cmmPost : postPage.getRecords()) {
                //表情解码
                if (StringUtils.isNotBlank(cmmPost.getTitle())) {
                    String interactTitle = FilterEmojiUtil.decodeEmoji(cmmPost.getTitle());
                    //String interactTitle = EmojiParser.parseToUnicode(cmmPost.getTitle() );
                    cmmPost.setTitle(interactTitle);
                }
                if (StringUtils.isNotBlank(cmmPost.getContent())) {
                    String interactContent = FilterEmojiUtil.decodeEmoji(cmmPost.getContent());
                    //String interactContent = EmojiParser.parseToUnicode(cmmPost.getContent());
                    cmmPost.setContent(interactContent);
                }

                PostOutBean bean = new PostOutBean();
                CmmCommunity community = communityService.selectById(cmmPost.getCommunityId());
//               if (community == null || community.getState() != 1) {
//                   continue;
//               }
                AuthorBean authorBean = new AuthorBean();
                try {
                    ResultDto<AuthorBean> beanResultDto = systemFeignClient.getAuthor(cmmPost.getMemberId());
                    if (null != beanResultDto) {
                        authorBean = beanResultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                bean.setAuthor(authorBean);
                //
                //systemFeignClient.list

//               if (bean.getAuthor() == null) {
//                   continue;
//               }
                String content = cmmPost.getContent();
                if (!CommonUtil.isNull(content)) {
                    //bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                    bean.setContent(content.length() > 40 ? Html2Text.removeHtmlTag(content.substring(0, 40)) : Html2Text.removeHtmlTag(content));
                }
                bean.setUpdated_at(DateTools.fmtDate(cmmPost.getUpdatedAt()));
                bean.setCreatedAt(DateTools.fmtDate(cmmPost.getCreatedAt()));
                bean.setVideoUrl(cmmPost.getVideo());
                bean.setImageUrl(cmmPost.getCoverImage());
                bean.setLikeNum(cmmPost.getLikeNum());
                bean.setPostId(cmmPost.getId());
                bean.setReplyNum(cmmPost.getCommentNum());
                bean.setTitle(CommonUtils.filterWord(cmmPost.getTitle()));
                if (community != null ) {
                    bean.setCommunityIcon(community.getIcon());
                    bean.setCommunityId(community.getId());
                    bean.setCommunityName(community.getName());
                }


                //文章 row_id
                bean.setRowId(cmmPost.getPostId());

                if (community != null && !CommonUtil.isNull(cmmPost.getVideo()) && CommonUtil.isNull(cmmPost.getCoverImage())) {
                    bean.setImageUrl(community.getCoverImage());
                }
                try {
                    if (!CommonUtil.isNull(cmmPost.getImages())) {
                        ArrayList<String> readValue = new ArrayList<String>();
                        readValue = mapper.readValue(cmmPost.getImages(), ArrayList.class);
                        int readValueListSize = readValue.size();
                        if (readValueListSize > 3) {
                            List threeReadValueList = new ArrayList(readValue.subList(0, 3));
                            bean.setImages(threeReadValueList);
                        } else {
                            bean.setImages(readValue);
                        }
                        //bean.setImages(readValue.size() > 3 ? readValue.subList(0, 3) : readValue);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //是否点赞
                if (memberUserPrefile == null) {

                    bean.setLiked(false);

                } else {

                    //!fixme 获取点赞数
                    //行为类型
                    //点赞 | 0
                    //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", cmmPost.getId()).eq("member_id", memberUserPrefile.getLoginId()));

                    BasActionDto basActionDto = new BasActionDto();

                    basActionDto.setMemberId(memberUserPrefile.getLoginId());
                    basActionDto.setType(0);
                    basActionDto.setState(0);
                    basActionDto.setTargetId(cmmPost.getId());

                    int liked = 0;

                    try {
                        ResultDto<Integer> resultDto = systemFeignClient.countActionNum(basActionDto);

                        if (null != resultDto) {
                            liked = resultDto.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }


                    bean.setLiked(liked > 0 ? true : false);
                }

                //
                bean.setVideoCoverImage(cmmPost.getVideoCoverImage());
                bean.setType(cmmPost.getType());
                /**
                 * 功能描述: 根据文章查询是否有圈子
                 * @auther: dl.zhang
                 * @date: 2019/6/27 15:40
                 */
                CmmCircle cmmCircle = cmmPostCircleMapper.getCircleEntityByPostId(bean.getPostId());
                if(cmmCircle != null){
                    bean.setCircleId(cmmCircle.getId());
                    bean.setCircleName(cmmCircle.getCircleName());
                    bean.setCircleIcon(cmmCircle.getCircleIcon());
                }
                list.add(bean);
            }
        }
        re.setData(list);
        PageTools.pageToResultDto(re, page);
        return re;
    }


    /**
     * 查看用户在指定时间段内文章上推荐/置顶的文章数量
     *
     * @param mb_id
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public Set<String> getArticleRecomAndTopCount(String mb_id, String startDate, String endDate) {

        Set<String> artcleRecomSet = null;

        try {

            EntityWrapper<CmmPost> cmmPostEntityWrapper = new EntityWrapper<CmmPost>();

            cmmPostEntityWrapper.eq("member_id", mb_id);
            cmmPostEntityWrapper.between("updated_at", startDate, endDate);
            cmmPostEntityWrapper.eq("state", 1);

            //type  0:普通 1:热门 2:精华
            cmmPostEntityWrapper.in("type", new Integer[]{2, 3});

            List<CmmPost> cmmPostList = postService.selectList(cmmPostEntityWrapper);
            if (null != cmmPostList && !cmmPostList.isEmpty()) {

                artcleRecomSet = new HashSet<String>();

                for (CmmPost cmmPost : cmmPostList) {
                    artcleRecomSet.add(cmmPost.getId());
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("查看用户在指定时间段内文章上推荐/置顶的文章数量出现异常", ex);
        }
        logger.info("查看用户在指定时间段内文章上推荐/置顶的文章数量-artcleRecomSet:{}", artcleRecomSet);
        return artcleRecomSet;
    }


    @Override
    public FungoPageResultDto<Map<String, Object>> searchPosts(String keyword, int page, int limit) throws Exception {

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
        }

        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
        List<Map<String, Object>> resultData = new ArrayList<>();
        re.setData(resultData);

        Page<CmmPost> pageCmPost = new Page<>(page, limit);

        Wrapper<CmmPost> wrapperCmmPost = Condition.create().setSqlSelect("id,title,content,cover_image as coverImage ,member_id as memberId ,video,created_at as createdAt," +
                "updated_at as updatedAt,video_cover_image as videoCoverImage,SUM( watch_num + comment_num + like_num + collect_num ) AS wclc ");

        wrapperCmmPost.where("state = {0}", 1);
        wrapperCmmPost.andNew("title like '%" + keyword + "%'");
        wrapperCmmPost.or("content like " + "'%" + keyword + "%'");
        wrapperCmmPost.groupBy("id");
        //排序
        StringBuffer orderByStr = new StringBuffer();
        orderByStr.append("LOCATE( '" + keyword + "', title ) DESC ,").append(" wclc DESC,");
        orderByStr.append("LOCATE( '" + keyword + "', content ) DESC,").append(" wclc DESC");

        wrapperCmmPost.orderBy(orderByStr.toString());

        Page<CmmPost> postPage = postService.selectPage(pageCmPost, wrapperCmmPost);
        List<CmmPost> postList = postPage.getRecords();


        for (CmmPost post : postList) {
            Map<String, Object> postData = new HashMap<String, Object>();


            if (StringUtils.isNotBlank(post.getTitle())) {
                String interactTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());
                //String interactTitle = EmojiParser.parseToUnicode(cmmPost.getTitle() );
                post.setTitle(interactTitle);
            }
            if (StringUtils.isNotBlank(post.getContent())) {
                String interactContent = FilterEmojiUtil.decodeEmoji(post.getContent());
                //String interactContent = EmojiParser.parseToUnicode(cmmPost.getContent());
                post.setContent(interactContent);
            }

            postData.put("video", post.getVideo());
            postData.put("objectId", post.getId());
            if (!CommonUtil.isNull(post.getContent())) {
                postData.put("content", CommonUtils.filterWord(post.getContent()));
            } else {
                postData.put("content", "");
            }
            postData.put("images", post.getCoverImage());
            postData.put("title", CommonUtils.filterWord(post.getTitle()));
            postData.put("createdAt", DateTools.fmtDate(post.getCreatedAt()));
            postData.put("updatedAt", DateTools.fmtDate(post.getUpdatedAt()));


            AuthorBean author = null;
            try {
                ResultDto<AuthorBean> authorBeanResultDto = systemFacedeService.getAuthor(post.getMemberId());
                if (null != authorBeanResultDto) {
                    author = authorBeanResultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            postData.put("author", author);
            postData.put("videoCoverImage", post.getVideoCoverImage());

            resultData.add(postData);
        }
        PageTools.pageToResultDto(re, postPage);

        return re;
    }


    @Override
    public FungoPageResultDto<GameDto> queryCmmPostRefGameIds(String keyword, int page, int limit) {

        if (StringUtils.isNotBlank(keyword)) {
            keyword = keyword.trim();
        }
        FungoPageResultDto<GameDto> resultDto = new FungoPageResultDto<GameDto>();
        try {

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("keyword", keyword);
            paramMap.put("pageSize", limit);

            //查询分页列表startOffset
            int startOffset = (page - 1) * limit;
            paramMap.put("startOffset", startOffset);


            List<Map<String, Object>> postRefGameIds = cmmPostDao.queryCmmPostRefGameIds(paramMap);

            if (null != postRefGameIds && !postRefGameIds.isEmpty()) {

                Set<String> gameIdsSet = new HashSet<>();
                //获取gameId
                for (Map<String, Object> postRefGameIdMap : postRefGameIds) {
                    String game_id = (String) postRefGameIdMap.get("game_id");
                    gameIdsSet.add(game_id);
                }

                //调用游戏微服务查询游戏数据
                String gameIds = StringUtils.join(gameIdsSet, ",");
                ResultDto<List<GameDto>> gameDetailsRsDto = gameFacedeService.selectGameDetailsByIds(gameIds);
                if (null != gameDetailsRsDto) {
                    List<GameDto> gameDtoList = gameDetailsRsDto.getData();
                    resultDto.setData(gameDtoList);

                    //设置分页数据
                    //查询总数
                    Map<String, Object> countMap = cmmPostDao.queryCmmPostRefGameIdsCount(paramMap);
                    Long count = 0L;
                    if (null != countMap && !countMap.isEmpty()) {
                        count = (Long) countMap.get("ct");
                    }

                    Page<CmmPost> mallOrderGoodsPage = new Page();
                    mallOrderGoodsPage.setTotal(count.intValue());
                    mallOrderGoodsPage.setSize(limit);
                    mallOrderGoodsPage.setCurrent(page);

                    PageTools.pageToResultDto(resultDto, mallOrderGoodsPage);

                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return resultDto;
    }

    //-----------
}