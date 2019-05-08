package com.fungo.community.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.service.MooMoodDaoService;
import com.fungo.community.entity.MooMood;
import com.fungo.community.service.IMoodService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MoodBean;
import com.game.common.dto.community.MoodInput;
import com.game.common.dto.community.StreamInfo;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheMood;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PKUtil;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import com.sun.corba.se.spi.ior.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class MoodServiceImpl implements IMoodService {


    private static final Logger LOGGER = LoggerFactory.getLogger(MoodServiceImpl.class);

    @Autowired
    private MooMoodDaoService moodService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IActionService iactionService;
    @Autowired
    private GameProxyImpl proxy;
    @Autowired
    private BasVideoJobService videoJobService;
    @Autowired
    private IVdService vdService;
    @Autowired
    private GameService gameService;
    @Autowired
    private GameDao gameDao;
    @Autowired
    private MemberService memberService;
    @Autowired
    private IVideoService vdoService;

    @Autowired
    private FungoCacheArticle fungoCacheArticle;

    @Autowired
    private FungoCacheMood fungoCacheMood;

    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;


    @Override
    @Transactional
    public ResultDto<ObjectId> addMood(String memberId, MoodInput input) throws Exception {
        MooMood mood = new MooMood();
        ObjectMapper objectMapper = new ObjectMapper();
//        if(CommonUtil.isNull(content)) {
//        	return ResultDto.error("-1", "内容不能为空!");
//        }

        Member member = memberService.selectById(memberId);
        if (member == null) {
            return ResultDto.error("-1", "用户不存在!");
        }
        if (member.getLevel() < 2) {
            return ResultDto.error("-1", "等级达到Lv2才可发布内容");
        }
        if (!CommonUtil.isNull(input.getVideo()) || !CommonUtil.isNull(input.getVideoId())) {
            if (member.getLevel() < 3) {
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
            BasVideoJob videoJob = videoJobService.selectOne(new EntityWrapper<BasVideoJob>().eq("video_id", input.getVideoId()));
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
                videoJobService.deleteById(videoJob);
            } else {
                videoJob = new BasVideoJob();
                videoJob.setBizId(mood.getId());
                videoJob.setBizType(2);
                videoJob.setVideoId(input.getVideoId());
                videoJob.setCreatedAt(new Date());
                videoJob.setUpdatedAt(new Date());
                videoJob.setStatus(0);
                videoJobService.insert(videoJob);
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
//			videoJobService.insert(videoJob);
//			//压缩
//			vdService.compress(videoJob.getId(), mood.getVideo());
//		}
        //行为记录
        iactionService.addAction(memberId, 2, 14, mood.getId(), "");
//		proxy.addScore(Setting.ACTION_TYPE_MOOD, memberId, mood.getId(), Setting.RES_TYPE_MOOD);
        //V2.4.6版本之前的任务
        //每日任务
        //int addTaskCore = proxy.addTaskCore(Setting.ACTION_TYPE_MOOD, memberId, mood.getId(), Setting.RES_TYPE_MOOD);
        //V2.4.6版本任务
        String tips = "";
        //1 fungo币
        Map<String, Object> resMapCoin = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN.code());

        //2 经验值
        Map<String, Object> resMapExp = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY.code(),
                MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP.code());

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

    @Autowired
    BasActionService actionService;

    @Override
    public ResultDto<MoodBean> getMood(String memberId, String moodId) throws Exception {
        ResultDto<MoodBean> re = new ResultDto<MoodBean>();
        MooMood mood = moodService.selectOne(new EntityWrapper<MooMood>().ne("state", -1).eq("id", moodId));
        if (mood == null) {
            return ResultDto.error("-1", "心情不存在或已被删除");
        }
        MoodBean moodBean = new MoodBean();
        moodBean.setAuthor(this.userService.getAuthor(mood.getMemberId()));
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
            int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).notIn("state", "-1").eq("target_id", mood.getId()).eq("member_id", memberId));
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
                Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", 0));
                if (game != null) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("gameId", game.getId());
                    map.put("gameName", game.getName());
                    map.put("gameIcon", game.getIcon());
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
                    gameList.add(map);
                }
            }
            moodBean.setGameList(gameList);
        }

        re.setData(moodBean);
        return re;
    }

}
