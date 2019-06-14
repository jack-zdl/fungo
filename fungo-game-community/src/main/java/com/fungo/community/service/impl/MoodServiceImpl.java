package com.fungo.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.service.BasVideoJobDaoService;
import com.fungo.community.dao.service.MooMoodDaoService;
import com.fungo.community.entity.BasVideoJob;
import com.fungo.community.entity.MooMood;
import com.fungo.community.facede.GameFacedeService;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.facede.TSMQFacedeService;
import com.fungo.community.service.IMoodService;
import com.fungo.community.service.IVideoService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.GameDto;
import com.game.common.dto.ObjectId;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.MoodBean;
import com.game.common.dto.community.MoodInput;
import com.game.common.dto.community.StreamInfo;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheMood;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PKUtil;
import com.game.common.util.UUIDUtils;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class MoodServiceImpl implements IMoodService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MoodServiceImpl.class);

    @Autowired
    private MooMoodDaoService moodService;

    @Autowired
    private BasVideoJobDaoService videoJobDaoService;

    @Autowired
    private IVideoService vdoService;

    @Autowired
    private FungoCacheArticle fungoCacheArticle;

    @Autowired
    private FungoCacheMood fungoCacheMood;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Autowired
    private GameFacedeService gameFacedeService;

    @Autowired
    private SystemFacedeService systemFacedeService;

    @Autowired
    private TSMQFacedeService tSMQFacedeService;




    @Override
    @Transactional
    public ResultDto<ObjectId> addMood(String memberId, MoodInput input) throws Exception {

        MooMood mood = new MooMood();
        ObjectMapper objectMapper = new ObjectMapper();
//        if(CommonUtil.isNull(content)) {
//        	return ResultDto.error("-1", "内容不能为空!");
//        }

        //!fixme 获取用户数据
        //Member member = memberService.selectById(memberId);

        List<String> idsList = new ArrayList<String>();
        idsList.add(memberId);

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
            return ResultDto.error("-1", "用户不存在!");
        }
        if (memberDto.getLevel() < 2) {
            return ResultDto.error("-1", "等级达到Lv2才可发布内容");
        }
        if (!CommonUtil.isNull(input.getVideo()) || !CommonUtil.isNull(input.getVideoId())) {
            if (memberDto.getLevel() < 3) {
                return ResultDto.error("-1", "等级达到Lv3才可发布视频");
            }
        }

        //fix:
        if (StringUtils.isNotBlank(input.getContent())) {
            String content = FilterEmojiUtil.encodeEmoji(input.getContent());
            mood.setContent(content);
        }

//		mood.setContent(input.getContent());
        if (input.getCover_image() != null && input.getCover_image().size() > 0) {
            try {
                mood.setImages(objectMapper.writeValueAsString(input.getCover_image()));
                mood.setCoverImage(input.getCover_image().get(0));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        mood.setMemberId(memberId);
        mood.setState(0);
        if (!CommonUtil.isNull(input.getVideoId())) {
            mood.setState(1);
        }

        mood.setCreatedAt(new Date());
        mood.setUpdatedAt(new Date());
        if (input.getVideo() != null) {
            mood.setVideo(input.getVideo());
        }

        if (input.getGameList() != null && input.getGameList().size() > 0) {
            mood.setGameList(objectMapper.writeValueAsString(input.getGameList()));
        }
        //权限判断

        //虚列
        Integer clusterIndex_i = Integer.parseInt(clusterIndex);
        mood.setMooId(PKUtil.getInstance(clusterIndex_i).longPK());

        //添加心情
        mood.insert();

        //视频处理
        if (!CommonUtil.isNull(input.getVideoId())) {
            BasVideoJob videoJob = videoJobDaoService.selectOne(new EntityWrapper<BasVideoJob>().eq("video_id", input.getVideoId()));
            if (videoJob != null) {
                videoJob.setBizType(2);
                videoJob.setBizId(mood.getId());
                videoJob.setUpdatedAt(new Date());
                String coverImg = vdoService.getVideoImgInfo(videoJob.getVideoId());
                mood.setVideoUrls(videoJob.getVideoUrls());
                mood.setVideo(videoJob.getReVideoUrl());
                mood.setVideoCoverImage(coverImg);
                mood.setState(0);
                mood.updateById();
//				videoJob.updateById();
                videoJobDaoService.deleteById(videoJob);
            } else {
                videoJob = new BasVideoJob();
                videoJob.setBizId(mood.getId());
                videoJob.setBizType(2);
                videoJob.setVideoId(input.getVideoId());
                videoJob.setCreatedAt(new Date());
                videoJob.setUpdatedAt(new Date());
                videoJob.setStatus(0);
                videoJobDaoService.insert(videoJob);
            }
        }
//		
//		if(!CommonUtil.isNull(input.getVideo())) {
//			mood.setState(1);
//			moodService.updateById(mood);
//			BasVideoJob videoJob = new BasVideoJob();
//			videoJob.setBizId(mood.getId());
//			videoJob.setBizType(2);
//			videoJob.setBizVideoUrl(mood.getVideo());
//			videoJob.setCreatedAt(new Date());
//			videoJob.setUpdatedAt(new Date());
//			videoJob.setStatus(0);
//			videoJobDaoService.insert(videoJob);
//			//压缩
//			vdService.compress(videoJob.getId(), mood.getVideo());
//		}

        //!fixme  行为记录
        //iactionService.addAction(memberId, 2, 14, mood.getId(), "");

        //通过MQ发送到系统微服务处理
        BasActionDto basActionDtoAdd = new BasActionDto();
        basActionDtoAdd.setCreatedAt(new Date());
        basActionDtoAdd.setInformation("");
        basActionDtoAdd.setMemberId(memberId);
        basActionDtoAdd.setState(0);
        basActionDtoAdd.setTargetId(mood.getId());
        basActionDtoAdd.setTargetType(2);
        basActionDtoAdd.setType(14);
        basActionDtoAdd.setUpdatedAt(new Date());

        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();

        //消息类型
        transactionMessageDto.setMessageDataType(TransactionMessageDto.MESSAGE_DATA_TYPE_MOOD);

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

        LOGGER.info("--添加心情-执行添加用户动作行为数据--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
        //-----end-------------


//		proxy.addScore(Setting.ACTION_TYPE_MOOD, memberId, mood.getId(), Setting.RES_TYPE_MOOD);

        //!fixme  V2.4.6版本之前的任务
        //每日任务
        //int addTaskCore = proxy.addTaskCore(Setting.ACTION_TYPE_MOOD, memberId, mood.getId(), Setting.RES_TYPE_MOOD);
        //V2.4.6版本任务
        String tips = "";

        //1 fungo币
        /*
        Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN.code());

        //2 经验值
        Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP.code());
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
        taskDtoCoin.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN.code());


        //exp task
        TaskDto taskDtoExp = new TaskDto();
        taskDtoExp.setRequestId(uuidExp);
        taskDtoExp.setMbId(memberId);
        taskDtoExp.setTaskGroupFlag(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code());
        taskDtoExp.setTaskType(MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT);
        taskDtoExp.setTypeCodeIdt(FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP.code());

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
        //--------------end----------------

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

        ResultDto<ObjectId> re = new ResultDto<ObjectId>();

        ObjectId o = new ObjectId();
        o.setId(mood.getId());

        re.setData(o);

        if (StringUtils.isNotBlank(tips)) {
            re.show(tips);
        } else {
            re.show("发布成功!");
        }

        //clear cache
        //获取心情动态列表(v2.4)
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
        //帖子/心情评论列表
        fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
        //我的心情(2.4.3)
        fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_MOODS, "", null);

        return re;
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
        LOGGER.info("--社区文章用户发布文章执行任务--MQ执行结果：messageResult:{}", JSON.toJSONString(messageResult));
        //-----start
    }


    @Override
    public ResultDto<String> delMood(String memberId, String moodId) {
        MooMood mood = moodService.selectOne(new EntityWrapper<MooMood>().eq("member_id", memberId).eq("id", moodId));
        if (mood != null) {
            mood.setState(-1);
            mood.updateById();

            //clear cache
            //帖子/心情评论列表 + moodId
            fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
            //获取心情动态列表(v2.4)
            fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST, "", null);
            //我的心情(2.4.3)
            fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_MOODS, "", null);
            return ResultDto.success();
        } else {
            return ResultDto.error("-1", "心情不存在");
        }
    }


    @Override
    public ResultDto<MoodBean> getMood(String memberId, String moodId) throws Exception {

        ResultDto<MoodBean> re = new ResultDto<MoodBean>();

        MooMood mood = moodService.selectOne(new EntityWrapper<MooMood>().ne("state", -1).eq("id", moodId));

        if (mood == null) {
            return ResultDto.error("-1", "心情不存在或已被删除");
        }

        MoodBean moodBean = new MoodBean();

        //!fixme 获取用户数据
        //moodBean.setAuthor(this.userService.getAuthor(mood.getMemberId()));

        AuthorBean authorBean = new AuthorBean();
        try {
            ResultDto<AuthorBean> beanResultDto = systemFacedeService.getAuthor(mood.getMemberId());
            if (null != beanResultDto) {
                authorBean = beanResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        moodBean.setAuthor(authorBean);


        ObjectMapper objectMapper = new ObjectMapper();

        ArrayList<String> imgs = null;

        try {
            if (mood.getImages() != null) {
                imgs = (ArrayList<String>) objectMapper.readValue(mood.getImages(), ArrayList.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (imgs == null) {
            imgs = new ArrayList<String>();
        }
        //图片处理
        moodBean.setImages(imgs);
        moodBean.setVideoCoverImage(mood.getVideoCoverImage());

        if (StringUtils.isNotBlank(mood.getContent())) {
            String interactContent = FilterEmojiUtil.decodeEmoji(mood.getContent());
            mood.setContent(interactContent);
        }

        if (!CommonUtil.isNull(mood.getContent())) {
            moodBean.setContent(CommonUtils.filterWord(mood.getContent()));
        }
        if ("".equals(memberId) || memberId == null) {
            moodBean.setIs_liked(false);
        } else {
            //!fixme 获取点赞数
            //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", mood.getId()).eq("member_id", memberId));

            BasActionDto basActionDto = new BasActionDto();

            basActionDto.setMemberId(memberId);
            basActionDto.setType(0);
            basActionDto.setState(0);
            basActionDto.setTargetId(mood.getId());

            int liked = 0;
            try {
                ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                if (null != resultDto) {
                    liked = resultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            moodBean.setIs_liked(liked > 0 ? true : false);
        }
        moodBean.setCover_image(mood.getCoverImage());


        //fix: 打印时间，排查时间与数据库记录不一致问题 [by mxf 2018-12-23]
        String createDateStr = DateTools.fmtDate(mood.getCreatedAt());
        String updateDateStr = DateTools.fmtDate(mood.getUpdatedAt());

        LOGGER.info("---getMood-createDateStr:{}:--updateDateStr:{}", createDateStr, updateDateStr);

        moodBean.setCreatedAt(createDateStr);
        moodBean.setUpdatedAt(updateDateStr);
        //end

        moodBean.setComment_num(mood.getCommentNum());
        moodBean.setLike_num(mood.getLikeNum());

        //视频
        moodBean.setVideo(mood.getVideo());
        if (!CommonUtil.isNull(mood.getVideoUrls())) {
            ArrayList<StreamInfo> streams = objectMapper.readValue(mood.getVideoUrls(), ArrayList.class);
            moodBean.setVideoList(streams);
        }

        //游戏链接
        if (!CommonUtil.isNull(mood.getGameList())) {
            ArrayList<String> gameIdList = objectMapper.readValue(mood.getGameList(), ArrayList.class);
            List<Map<String, Object>> gameList = new ArrayList<>();
            for (String gameId : gameIdList) {

                //!fixme 获取游戏数据
                //Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", 0));

                GameDto gameDto = null;
                try {
                    ResultDto<GameDto> gameDtoResultDto = gameFacedeService.selectGameDetails(gameId, 0);
                    if (null != gameDtoResultDto) {
                        gameDto = gameDtoResultDto.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (gameDto != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("gameId", gameDto.getId());
                    map.put("gameName", gameDto.getName());
                    map.put("gameIcon", gameDto.getIcon());

                    //!fixme 游戏平均分
                    /*
                    HashMap<String, BigDecimal> rateData = gameDao.getRateData(game.getId());
                    if (rateData != null) {
                        if (rateData.get("avgRating") != null) {
                            map.put("gameRating", (Double.parseDouble(rateData.get("avgRating").toString())));
                        } else {
                            map.put("gameRating", 0.0);
                        }
                    } else {
                        map.put("gameRating", 0.0);
                    }
                    map.put("category", game.getTags());
                    */

                    //获取游戏平均分'
                    double gameAverage = 0;
                    try {
                        gameAverage = gameFacedeService.selectGameAverage(gameDto.getId(), 0);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    map.put("gameRating", gameAverage);


                    gameList.add(map);
                }
            }
            moodBean.setGameList(gameList);
        }

        re.setData(moodBean);
        return re;
    }

    //------------
}
