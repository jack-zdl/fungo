package com.fungo.system.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.BasNoticeDao;
import com.fungo.system.dao.IncentRuleRankGroupDao;
import com.fungo.system.dto.*;
import com.fungo.system.entity.*;
import com.fungo.system.facede.IMemeberProxyService;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.function.GameFilterService;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.service.*;
import com.fungo.system.service.impl.MemberNoticeServiceImpl;
import com.fungo.system.service.portal.PortalSystemIMemberService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.*;
import com.game.common.dto.community.*;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.repo.cache.facade.*;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.StringUtil;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProtalSystemMemberServiceImpl implements PortalSystemIMemberService {

    private static final Logger logger = LoggerFactory.getLogger(ProtalSystemMemberServiceImpl.class);

    @Autowired
    private BasNoticeService noticeService;
    @Autowired
    private IUserService userService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private IMemeberProxyService iMemeberProxyService;
    @Autowired
    private IMemberIncentRuleRankService IRuleRankService;
    @Autowired
    private BasNoticeDao noticeDao;
    @Autowired
    private IncentRankedService rankedService;
    @Autowired
    private FungoCacheNotice fungoCacheNotice;
    @Autowired
    private GamesFeignClient gamesFeignClient;
    @Autowired(required = false)
    private CommunityFeignClient communityFeignClient;
    @Autowired
    private MemberNoticeServiceImpl memberNoticeService;
    @Autowired
    private IncentRuleRankService rankRuleService;
    @Autowired
    private IncentRuleRankGroupDao incentRuleRankGroupDao;
    @Autowired
    private GameFilterService gameFilterService;

    //消息
    @Override
    public FungoPageResultDto<Map<String, Object>> getLikeNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception {
        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        re.setData(list);
        String[] types = null;
        //版本不同获取的内容不同
        if (CommonUtils.versionAdapte(appVersion, "2.4.4")) {
            types = new String[]{"0", "1", "2","10", "7", "11"};
        } else {
            types = new String[]{"0", "1", "2","10","7","11"};
        }

        Page<BasNotice> basNoticePage = new Page<>(inputPage.getPage(), inputPage.getLimit());

        EntityWrapper<BasNotice> noticeEntityWrapper = new EntityWrapper<>();
        noticeEntityWrapper.eq("member_id", memberId);
        noticeEntityWrapper.in("type", types);
        noticeEntityWrapper.orderBy("created_at", false);

        Page<BasNotice> plist = noticeService.selectPage(basNoticePage, noticeEntityWrapper);
        List<BasNotice> t = plist.getRecords();

        List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
        ObjectMapper objectMapper = new ObjectMapper();

        for (BasNotice basNotice : t) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("video", "");
            try {

                String noticeData = basNotice.getData();
                //表情解码
                if (StringUtils.isNotBlank(noticeData)) {
                    noticeData = FilterEmojiUtil.decodeEmoji(noticeData);
                }

                map = objectMapper.readValue(noticeData, Map.class);
                if ((int) map.get("type") == 0) {
                    map.put("msg_template", "赞了我的文章");
                    CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id"));   //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                    if (post != null) {
                        if (!CommonUtil.isNull(post.getVideo())) {
                            map.put("video", post.getVideo());
                        }
                        map.put("one_level_deltype", post.getState() == -1  ? -1 :  !memberId.equals( post.getMemberId()) ? (((post.getAuth() & 1 ) == 1) ? -1 : 0) :   (((post.getAuth() & 1 ) == 1)? 1 : 0));
                    }
                } else if ((int) map.get("type") == 1) {//basNotice.getType()==1
                    map.put("msg_template", "赞了我的评论");
                    CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id"));  //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                    if (post != null) {
                        if (!CommonUtil.isNull(post.getVideo())) {
                            map.put("video", post.getVideo());
                        }
                        map.put("two_level_deltype", post.getState() == -1  ? -1 :  !memberId.equals( post.getMemberId()) ? (((post.getAuth() & 1 ) == 1) ? -1 : 0) :   (((post.getAuth() & 1 ) == 1)? 1 : 0));
                    }
                    CmmCommentDto  cmmCommentDto = new CmmCommentDto();
                    cmmCommentDto.setId((String) map.get("comment_id"));
                    cmmCommentDto.setState(null);
                    FungoPageResultDto<CmmCommentDto>  cmmCommentDtoFungoPageResultDto = communityFeignClient.queryFirstLevelCmtList(cmmCommentDto);
                    CmmCommentDto commentDto = (  cmmCommentDtoFungoPageResultDto.getData() != null  && cmmCommentDtoFungoPageResultDto.getData().size() > 0 ) ? cmmCommentDtoFungoPageResultDto.getData().get(0) : null;  //iMemeberProxyService.selectCmmPost((String) map.get("post_id"));   //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                    if (commentDto != null) {
                        map.put("one_level_deltype", commentDto.getState() == -1 ? -1 : 0);
                    }
                } else if ((int) map.get("type") == 2) {
                    map.put("msg_template", "赞了我的游戏评价");
                    String evaluationId = (String) map.get("evaluation_id");
                    GameEvaluationDto param = new GameEvaluationDto();
                    param.setId(evaluationId);
                    FungoPageResultDto<GameEvaluationDto>  resultDto = gamesFeignClient.getGameEvaluationPage(param);
                    GameEvaluationDto gameEvaluationDto = (resultDto.getData() != null && resultDto.getData().size() > 0 ) ? resultDto.getData().get(0) : null;
                    if(gameEvaluationDto != null){
                        map.put( "one_level_deltype",gameEvaluationDto.getState() == -1 ? -1 : 0 );
                        map.put( "gameIdtSn",gameFilterService.getGameDto( gameEvaluationDto.getGameId()) );
                    }
                    String gameId = (String) map.get("game_id");
                    GameDto gameDto = new GameDto();
                    gameDto.setId(gameId);
                    gameDto.setState(null);
                    FungoPageResultDto<GameDto>  resultDto1 = gamesFeignClient.getGamePage(gameDto);
                    GameDto gameDto1 = (resultDto1.getData() != null && resultDto1.getData().size() > 0 ) ? resultDto1.getData().get(0) : null;
                    if(gameDto1 != null){
                        map.put( "two_level_deltype",gameDto1.getState() == -1 ? -1 : 0 );
                    }
                } else if (basNotice.getType() == 7) {
                    map.put("msg_template", "赞了我的心情");
                    String moodId = (String) map.get("mood_id");
                    MooMoodDto param = new MooMoodDto();
                    param.setId(moodId);
                    param.setState(null);
                    FungoPageResultDto<MooMoodDto> resultDto = communityFeignClient.queryCmmMoodList(param);
                    MooMoodDto mood =  (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;  //iGameProxyService.selectMooMoodById(commentBean.getTargetId());  // new MooMoodDto();// moodService.selectOne(Condition.create().setSqlSelect("id,content").eq("id", c.getTargetId()));
                    if (mood != null) {
                        map.put( "one_level_deltype",mood.getState()  == -1 ? -1 : 0  );
                    }
                } else if (basNotice.getType() == 11) {
                    map.put("msg_template", "赞了我的心情评论");
                    String  mooMessageId = (String) map.get("message_id");
                    MooMessageDto mooMessageDto = new MooMessageDto();
                    mooMessageDto.setId(mooMessageId);
                    mooMessageDto.setState(null);
                    FungoPageResultDto<MooMessageDto> resultDto = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
                    MooMessageDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                    if (message != null) {
                        map.put( "one_level_deltype",message.getState()  == -1 ? -1 : 0   );
                    }
                    String moodId = (String) map.get("mood_id");
                    MooMoodDto param = new MooMoodDto();
                    param.setId(moodId);
                    param.setState(null);
                    FungoPageResultDto<MooMoodDto> result = communityFeignClient.queryCmmMoodList(param);
                    MooMoodDto mood =  (result.getData() != null && result.getData().size() >0 ) ? result.getData().get(0) : null ;  //iGameProxyService.selectMooMoodById(commentBean.getTargetId());  // new MooMoodDto();// moodService.selectOne(Condition.create().setSqlSelect("id,content").eq("id", c.getTargetId()));
                    if (mood != null) {
                        map.put( "two_level_deltype",mood.getState() == -1 ? -1 : 0   );
                    }
                }else if (basNotice.getType() == 10) {
                    map.put("msg_template", "赞了我的评论");
                    String replyId = (String) map.get("replyId");
                    if(replyId != null){
                        CmmCmtReplyDto cmmCmtReplyDto = new CmmCmtReplyDto();
                        cmmCmtReplyDto.setId(replyId);
                        FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                        CmmCmtReplyDto cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if (cmmCmtReplyDto1 != null) {
                            map.put( "one_level_deltype",cmmCmtReplyDto1.getState()  == -1 ? -1 : 0 );
                            if(cmmCmtReplyDto1.getReplayToId() != null){
                                cmmCmtReplyDto.setId(cmmCmtReplyDto1.getReplyToContentId());
                                replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                                cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                                if (cmmCmtReplyDto1 != null) {
                                    map.put( "two_level_deltype",cmmCmtReplyDto1.getState()  == -1 ? -1 : 0 );
                                    CmmCmtReplyDto cmmCmtReply = new CmmCmtReplyDto();
                                    cmmCmtReplyDto.setId(replyId);
                                    FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResult = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                                    CmmCmtReplyDto cmmCmtReply1 =    (replyDtoFungoPageResult.getData() != null && replyDtoFungoPageResult.getData().size() >0 ) ? replyDtoFungoPageResult.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                                    if (cmmCmtReply1 != null) {
                                        if(cmmCmtReply1.getTargetType() == 5){ //社区一级评论t_cmm_message 5
                                            CmmCommentDto cmmCommentDto = new CmmCommentDto();
                                            cmmCommentDto.setId(cmmCmtReply1.getTargetId());
                                            cmmCommentDto.setState(null);
                                            FungoPageResultDto<CmmCommentDto> resultDto = communityFeignClient.queryFirstLevelCmtList(cmmCommentDto);
                                            CmmCommentDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                                            if (message != null) {
                                                map.put( "three_level_deltype",message.getState() == -1  ? -1 : 0 );
                                                map.put( "parentType",5);
                                                map.put( "parentId",message.getPostId() );
                                                Member member = memberService.selectById(message.getMemberId());
                                                map.put( "parentMemberId",member.getId() );
                                                map.put( "parentMemberName",member.getUserName() );
                                                CmmPostDto post = iMemeberProxyService.selectCmmPost(message.getPostId());
                                                if(post != null){
                                                    map.put( "four_level_deltype",post.getState() == -1  ? -1 :  !memberId.equals( post.getMemberId()) ? (((post.getAuth() & 1 ) == 1) ? -1 : 0) : (((post.getAuth() & 1 ) == 1) ? 1 : 0));
                                                }
                                            }
                                        }else if(cmmCmtReply1.getTargetType() == 6){  //游戏评测 t_game_evation 6
                                            GameEvaluationDto param = new GameEvaluationDto();
                                            param.setId(cmmCmtReply1.getTargetId());
                                            FungoPageResultDto<GameEvaluationDto>  resultDto = gamesFeignClient.getGameEvaluationPage(param);
                                            GameEvaluationDto gameEvaluationDto = (resultDto.getData() != null && resultDto.getData().size() > 0 ) ? resultDto.getData().get(0) : null;
                                            if(gameEvaluationDto != null){
                                                map.put( "three_level_deltype",gameEvaluationDto.getState()  == -1 ? -1 : 0 );
                                                map.put( "parentType",6);
                                                map.put( "parentId",gameEvaluationDto.getGameId() );
                                                map.put( "gameIdtSn",gameFilterService.getGameDto( gameEvaluationDto.getGameId()) );
                                                Member member = memberService.selectById(gameEvaluationDto.getMemberId());
                                                map.put( "parentMemberId",member.getId() );
                                                map.put( "parentMemberName",member.getUserName() );
                                            }
                                        }else if(cmmCmtReply1.getTargetType() == 8){ //心情评论  t_moo_message 8
                                            MooMessageDto mooMessageDto = new MooMessageDto();
                                            mooMessageDto.setId(cmmCmtReply1.getTargetId());
                                            mooMessageDto.setState(null);
                                            FungoPageResultDto<MooMessageDto> resultDto = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
                                            MooMessageDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                                            if (message != null) {
                                                map.put( "three_level_deltype",message.getState()  == -1 ? -1 : 0 );
                                                map.put( "parentType",8);
                                                map.put( "parentId",message.getMoodId()  );
                                                Member member = memberService.selectById(message.getMemberId());
                                                map.put( "parentMemberId",member.getId() );
                                                map.put( "parentMemberName",member.getUserName() );
                                            }
                                        }
                                    }
                                }
                            }else if(cmmCmtReplyDto1.getTargetType() == 5){ //社区一级评论t_cmm_message 5
                                CmmCommentDto cmmCommentDto = new CmmCommentDto();
                                cmmCommentDto.setId(cmmCmtReplyDto1.getTargetId());
                                cmmCommentDto.setState(null);
                                FungoPageResultDto<CmmCommentDto> resultDto = communityFeignClient.queryFirstLevelCmtList(cmmCommentDto);
                                CmmCommentDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                                if (message != null) {
                                    map.put( "two_level_deltype",message.getState()  == -1 ? -1 : 0 );
                                    map.put( "parentType",5);
                                    map.put( "parentId",message.getPostId() );
                                    Member member = memberService.selectById(message.getMemberId());
                                    map.put( "parentMemberId",member.getId() );
                                    map.put( "parentMemberName",member.getUserName() );
                                }
                            }else if(cmmCmtReplyDto1.getTargetType() == 6){  //游戏评测 t_game_evation 6
                                GameEvaluationDto param = new GameEvaluationDto();
                                param.setId(cmmCmtReplyDto1.getTargetId());
                                FungoPageResultDto<GameEvaluationDto>  resultDto = gamesFeignClient.getGameEvaluationPage(param);
                                GameEvaluationDto gameEvaluationDto = (resultDto.getData() != null && resultDto.getData().size() > 0 ) ? resultDto.getData().get(0) : null;
                                if(gameEvaluationDto != null){
                                    map.put( "two_level_deltype",gameEvaluationDto.getState() == -1 ? -1 : 0 );
                                    map.put( "parentType",6);
                                    map.put( "parentId",gameEvaluationDto.getGameId() );
                                    map.put( "gameIdtSn",gameFilterService.getGameDto( gameEvaluationDto.getGameId()) );
                                    Member member = memberService.selectById(gameEvaluationDto.getMemberId());
                                    map.put( "parentMemberId",member.getId() );
                                    map.put( "parentMemberName",member.getUserName() );
                                }
                            }else if(cmmCmtReplyDto1.getTargetType() == 8){ //心情评论  t_moo_message 8
                                MooMessageDto mooMessageDto = new MooMessageDto();
                                mooMessageDto.setId(cmmCmtReplyDto1.getTargetId());
                                mooMessageDto.setState(null);
                                FungoPageResultDto<MooMessageDto> resultDto = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
                                MooMessageDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                                if (message != null) {
                                    map.put( "two_level_deltype",message.getState()  == -1 ? -1 : 0 );
                                    map.put( "parentType",8);
                                    map.put( "parentId",message.getMoodId()  );
                                    Member member = memberService.selectById(message.getMemberId());
                                    map.put( "parentMemberId",member.getId() );
                                    map.put( "parentMemberName",member.getUserName() );
                                }
                            }
                        }
                    }
                    if(replyId != null){
                        CmmCmtReplyDto cmmCmtReplyDto = new CmmCmtReplyDto();
                        cmmCmtReplyDto.setId(replyId);
                        FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                        CmmCmtReplyDto cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if(cmmCmtReplyDto1 != null){
                            memberNoticeService.updateNotice(cmmCmtReplyDto1,map);
                        }
                    }
                }
                if (basNotice.getIsRead() == 0 || basNotice.getIsPush() == 0) {
                    basNotice.setIsRead(1);
                    basNotice.setIsPush(1);

                    basNotice.updateById();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", (String) map.get("user_id")).eq("rank_type", 1));
            Member member = memberService.selectById((String) map.get("user_id"));

            if (ranked != null) {
                String rankImgs = getLevelRankUrl(ranked.getCurrentRankId().intValue(), levelRankList);
                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
                map.put("dignityImg", (String) urlList.get(0).get("url"));
            } else {
                String rankImgs = getLevelRankUrl((int) map.get("user_level"), levelRankList);
                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
                map.put("dignityImg", (String) urlList.get(0).get("url"));
            }

            if (member != null) {
                map.put("user_avatar", member.getAvatar());
                map.put("user_name", member.getUserName());
            }
            map.put("statusImgs", new ArrayList<>(  ));
            List<IncentRanked> incentRankeds = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", map.get("user_id")));
            for (IncentRanked identityIncentRanked : incentRankeds) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    if (identityIncentRanked.getRankType() == 2) {
                        IncentRuleRank rank = rankRuleService.selectById(identityIncentRanked.getCurrentRankId());//最近获得
//                        String rankImgs = rank.getRankImgs();
//                        ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
//                        author.setStatusImg(urlList);
                        String rankIdtIds = identityIncentRanked.getRankIdtIds();
                        List<HashMap<String,Object>> list1 = mapper.readValue(rankIdtIds, ArrayList.class);
                        List<List<HashMap<String,Object>>> statusLists = new ArrayList<>(  );
//                        int groupLevel = 0;
//                        int circleLevel = 0;
                        for (HashMap<String,Object> map1 : list1){
                            Integer rankId = (Integer) map1.get( "1" );
                            IncentRuleRank rank1 = rankRuleService.selectById(rankId);//最近获得
                            String rankImgs = rank1.getRankImgs();
                            ArrayList<HashMap<String, Object>> urlList = null;
                            IncentRuleRankGroup incentRuleRankGroup = incentRuleRankGroupDao.selectById( rank1.getRankGroupId());
//                            groupLevel =  incentRuleRankGroup.getAuth() > groupLevel ? incentRuleRankGroup.getAuth() : groupLevel;
//                            circleLevel =  incentRuleRankGroup.getAuth() == 2 ? incentRuleRankGroup.getAuth() : circleLevel;
                            try {
                                urlList = mapper.readValue(rankImgs, ArrayList.class);
                                urlList.stream().forEach( x ->{
                                    x.put( "auth", incentRuleRankGroup.getAuth());
                                    x.put( "group", incentRuleRankGroup.getId());
                                    x.put( "groupNmae", incentRuleRankGroup.getGroupName());
                                } );
                                statusLists.add( urlList );
                            } catch (Exception e) {
                                logger.error( "对象转换异常",e );
                            }
                        }
//                        author.setGroupLevel(groupLevel);
//                        author.setCircleLevel( circleLevel );
                        map.put("statusImgs", (statusLists));
                    }
                }catch (Exception e){
                    logger.error( "",e);
                }
            }
            map.put("statusImg", userService.getStatusImage((String) map.get("user_id")));
            map.put("createdAt", DateTools.fmtDate(basNotice.getCreatedAt()));
            list.add(map);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("typeList", types);
        noticeDao.setIsRead(map);

        PageTools.pageToResultDto(re, plist);
       updateNoticeMap(memberId,"like_count");
        String keyPrefix = FungoCoreApiConstant.REDIS_MEMBER_NOTICE + memberId + "7";
        //清除redis缓存
        fungoCacheNotice.excIndexCacheWithOutSecurity(false, keyPrefix, "", null);
        return re;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getCommentNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception {
        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        re.setData(list);
        String[] types = null;

        //版本不同获取的内容不同
        if (CommonUtils.versionAdapte(appVersion, "2.4.4")) {
            types = new String[]{"3", "4", "5", "8", "9", "12"};
        } else {
            types = new String[]{"3", "4", "5", "8", "9", "12"};
        }
//		String[] types1= {"3","4","5","8"};
        Page<BasNotice> basNoticePage = new Page<>(inputPage.getPage(), inputPage.getLimit());

        EntityWrapper<BasNotice> noticeEntityWrapper = new EntityWrapper<>();
        noticeEntityWrapper.eq("member_id", memberId);
        //noticeEntityWrapper.eq("is_read", 0);

        noticeEntityWrapper.in("type", types);
        noticeEntityWrapper.orderBy("created_at", false);

        Page<BasNotice> plist = noticeService.selectPage(basNoticePage, noticeEntityWrapper);

        List<BasNotice> t = plist.getRecords();

        List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
        ObjectMapper objectMapper = new ObjectMapper();

        for (BasNotice basNotice : t) {

            Map<String, Object> map = new HashMap<String, Object>();

            try {
                //表情解码
                if (StringUtils.isNotBlank(basNotice.getData())) {
                    String date = FilterEmojiUtil.decodeEmoji(basNotice.getData());
                    basNotice.setData(date);
                }
                map.put("video", "");
                map = objectMapper.readValue(basNotice.getData(), Map.class);
                if (basNotice.getType() == 3) {
                    map.put("msg_template", "评论了我的文章");
                    CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id")); //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                    if (post != null) {
                        if (!CommonUtil.isNull(post.getVideo())) {
                            map.put("video", post.getVideo());
                        }
                        map.put("two_level_deltype", post.getState() == -1  ? -1 :  !memberId.equals( post.getMemberId()) ? (((post.getAuth() & 1 ) == 1) ? -1 : 0) :   (((post.getAuth() & 1 ) == 1) ? 1 : 0));
                    }
                    String commentId = (String) map.get("commentId");
                    if(StringUtil.isNotNull(commentId)){
                        CmmCommentDto cmmCommentDto = new CmmCommentDto();
                        cmmCommentDto.setId(commentId);
                        cmmCommentDto.setState(null);
                        FungoPageResultDto<CmmCommentDto> resultDto = communityFeignClient.queryFirstLevelCmtList(cmmCommentDto);
                        CmmCommentDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if (message != null) {
                            map.put( "one_level_deltype",message.getState()  == -1 ? -1 : 0 );
                        }
                    }
                } else if (basNotice.getType() == 4) {
                    map.put("msg_template", "回复了我的评论");
                    CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id")); //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                    if (post != null) {
                        if (!CommonUtil.isNull(post.getVideo())) {
                            map.put("video", post.getVideo());
                        }
                        map.put("three_level_deltype", post.getState() == -1  ? -1 :  !memberId.equals( post.getMemberId()) ? (((post.getAuth() & 1 ) == 1) ? -1 : 0) :   (((post.getAuth() & 1 ) == 1) ? 1 : 0));
                    }

                    String commentId = (String) map.get("comment_id");
                    if(StringUtil.isNotNull(commentId)) {
                        CmmCommentDto cmmCommentDto = new CmmCommentDto();
                        cmmCommentDto.setId( commentId );
                        cmmCommentDto.setState( null );
                        FungoPageResultDto<CmmCommentDto> resultDto = communityFeignClient.queryFirstLevelCmtList( cmmCommentDto );
                        CmmCommentDto message = (resultDto.getData() != null && resultDto.getData().size() > 0) ? resultDto.getData().get( 0 ) : null;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if (message != null) {
                            map.put( "two_level_deltype", message.getState() == -1 ? -1 : 0 );
                        }
                    }
                    String replyId = (String) map.get("replyId");
                    if(StringUtil.isNotNull(replyId)){
                        CmmCmtReplyDto cmmCmtReplyDto = new CmmCmtReplyDto();
                        cmmCmtReplyDto.setId(replyId);
                        cmmCmtReplyDto.setState(null);
                        FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                        CmmCmtReplyDto cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if (cmmCmtReplyDto1 != null) {
                            map.put( "one_level_deltype",cmmCmtReplyDto1.getState()  == -1 ? -1 : 0 );
                        }
                    }
                } else if (basNotice.getType() == 5) {
                    map.put("msg_template", "回复了我的游戏评价");
                    String evaluationId = (String) map.get("evaluation_id");
                    GameEvaluationDto param = new GameEvaluationDto();
                    param.setId(evaluationId);
                    param.setState(null);
                    FungoPageResultDto<GameEvaluationDto>  resultDto = gamesFeignClient.getGameEvaluationPage(param);
                    GameEvaluationDto gameEvaluationDto = (resultDto.getData() != null && resultDto.getData().size() > 0 ) ? resultDto.getData().get(0) : null;
                    if(gameEvaluationDto != null){
                        map.put( "two_level_deltype",gameEvaluationDto.getState()  == -1 ? -1 : 0 );
                        map.put( "gameIdtSn",gameFilterService.getGameDto( gameEvaluationDto.getGameId()) );
                    }
                    String replyId = (String) map.get("replyId");
                    if(StringUtil.isNotNull(replyId)){
                        CmmCmtReplyDto cmmCmtReplyDto = new CmmCmtReplyDto();
                        cmmCmtReplyDto.setId(replyId);
                        cmmCmtReplyDto.setState(null);
                        FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                        CmmCmtReplyDto cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if (cmmCmtReplyDto1 != null) {
                            map.put( "one_level_deltype",cmmCmtReplyDto1.getState()  == -1 ? -1 : 0 );
                        }
                    }
                } else if (basNotice.getType() == 8) {
                    map.put("msg_template", "评论了我的心情");
                    String commentId = (String) map.get("commentId");
                    if(StringUtil.isNotNull(commentId)){
                        MooMessageDto mooMessageDto = new MooMessageDto();
                        mooMessageDto.setId(commentId);
                        mooMessageDto.setState(null);
                        FungoPageResultDto<MooMessageDto> resultDto = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
                        MooMessageDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if (message != null) {
                            map.put( "one_level_deltype",message.getState()== -1 ? -1 : 0 );
                        }
                    }
                    String moodId = (String) map.get("mood_id");
                    if(StringUtil.isNotNull(moodId)){
                        MooMoodDto param = new MooMoodDto();
                        param.setId(moodId);
                        param.setState(null);
                        FungoPageResultDto<MooMoodDto> result = communityFeignClient.queryCmmMoodList(param);
                        MooMoodDto mood =  (result.getData() != null && result.getData().size() >0 ) ? result.getData().get(0) : null ;  //iGameProxyService.selectMooMoodById(commentBean.getTargetId());  // new MooMoodDto();// moodService.selectOne(Condition.create().setSqlSelect("id,content").eq("id", c.getTargetId()));
                        if (mood != null) {
                            map.put( "two_level_deltype",mood.getState()== -1 ? -1 : 0 );
                        }
                    }
                } else if (basNotice.getType() == 9) {
                    map.put("msg_template", "回复了我的心情评论");
                    String moodId = (String) map.get("mood_id");
                    if(moodId != null){
                        MooMoodDto param = new MooMoodDto();
                        param.setId(moodId);
                        param.setState(null);
                        FungoPageResultDto<MooMoodDto> result = communityFeignClient.queryCmmMoodList(param);
                        MooMoodDto mooMoodDto = (result.getData() != null && result.getData().size()>0) ? result.getData().get(0) : null;
                        if(mooMoodDto != null){
                            Member member = memberService.selectById(mooMoodDto.getMemberId());
                            map.put( "mooderName",member.getUserName());
                            map.put( "three_level_deltype",mooMoodDto.getState()== -1 ? -1 : 0 );
                        }
                        String commentId = (String) map.get("message_id");
                        if(StringUtil.isNotNull(commentId)){
                            MooMessageDto mooMessageDto = new MooMessageDto();
                            mooMessageDto.setId(commentId);
                            mooMessageDto.setState(null);
                            FungoPageResultDto<MooMessageDto> resultDto = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
                            MooMessageDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                            if (message != null) {
                                map.put( "two_level_deltype",message.getState()== -1 ? -1 : 0 );
                            }
                        }
                        String replyId = (String) map.get("replyId");
                        if(StringUtil.isNotNull(replyId)){
                            CmmCmtReplyDto cmmCmtReplyDto = new CmmCmtReplyDto();
                            cmmCmtReplyDto.setId(replyId);
                            cmmCmtReplyDto.setState(null);
                            FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                            CmmCmtReplyDto cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                            if (cmmCmtReplyDto1 != null) {
                                map.put( "one_level_deltype",cmmCmtReplyDto1.getState()  == -1 ? -1 : 0 );
                            }
                        }

                    }
                } else if (basNotice.getType() == 12) {
                    map.put("msg_template", "回复了我的回复");

                    if (map.get("post_id") != null) {
                        CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id")); //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                        if (post != null) {
                            if (!CommonUtil.isNull(post.getVideo())) {
                                map.put("video", post.getVideo());
                            }
                            map.put( "three_level_deltype",post.getState() == -1  ? -1 :  !memberId.equals( post.getMemberId()) ? (((post.getAuth() & 1 ) == 1) ? -1 : 0) :   (((post.getAuth() & 1 ) == 1) ? 1 : 0));
                        }
                        map.put("parentId", map.get("post_id"));
                        CmmCommentDto  cmmCommentDto = new CmmCommentDto();
                        cmmCommentDto.setId((String) map.get("comment_id"));
                        cmmCommentDto.setState(null);
                        FungoPageResultDto<CmmCommentDto>  cmmCommentDtoFungoPageResultDto = communityFeignClient.queryFirstLevelCmtList(cmmCommentDto);
                        CmmCommentDto commentDto = (  cmmCommentDtoFungoPageResultDto.getData() != null  && cmmCommentDtoFungoPageResultDto.getData().size() > 0 ) ? cmmCommentDtoFungoPageResultDto.getData().get(0) : null;  //iMemeberProxyService.selectCmmPost((String) map.get("post_id"));   //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                        if (commentDto != null) {
                            Member member = memberService.selectById(commentDto.getMemberId());
                            map.put( "parentName",member.getUserName());
                            map.put( "two_level_deltype",commentDto.getState()  == -1 ? -1 : 0 );

                        }

                    }else if(map.get("mood_id") != null){
                        map.put("parentId", map.get("mood_id"));
                        String  mooMessageId = (String) map.get("message_id");
                        MooMessageDto mooMessageDto = new MooMessageDto();
                        mooMessageDto.setId(mooMessageId);
                        mooMessageDto.setState(null);
                        FungoPageResultDto<MooMessageDto> resultDto = communityFeignClient.queryCmmMoodCommentList(mooMessageDto);
                        MooMessageDto message =    (resultDto.getData() != null && resultDto.getData().size() >0 ) ? resultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                        if (message != null) {
                            Member member = memberService.selectById(message.getMemberId());
                            map.put( "parentName",member.getUserName());
                            map.put( "two_level_deltype",message.getState()== -1 ? -1 : 0 );
                        }


                        // 文章評論
                        String commentId = (String) map.get( "comment_id" );
                        if(StringUtil.isNotNull(commentId)){
                            CmmCommentDto cmmCommentDto = new CmmCommentDto();
                            cmmCommentDto.setId(commentId);
                            cmmCommentDto.setState(null);
                            FungoPageResultDto<CmmCommentDto> cmmCommentDtoFungoPageResultDto = communityFeignClient.queryFirstLevelCmtList(cmmCommentDto);
                            CmmCommentDto cmmCommentDto1 =    (cmmCommentDtoFungoPageResultDto.getData() != null && cmmCommentDtoFungoPageResultDto.getData().size() >0 ) ? cmmCommentDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                            if (cmmCommentDto1 != null) {
                                map.put( "two_level_deltype",cmmCommentDto1.getState()  == -1 ? -1 : 0 );
                                CmmPostDto post = iMemeberProxyService.selectCmmPost(cmmCommentDto1.getPostId());
                                if(post != null){
                                    map.put( "three_level_deltype",post.getState() == -1  ? -1 :  !memberId.equals( post.getMemberId()) ? (post.getAuth() == 1 ? -1 : 0) :   (post.getAuth() == 1 ? 1 : 0));
                                }
                            }
                        }
                        // 游戲评测
                        String evaluationId = (String) map.get("evaluation_id");
                        if(StringUtil.isNotNull(evaluationId)){
                            GameEvaluationDto param = new GameEvaluationDto();
                            param.setId(evaluationId);
                            param.setState(null);
                            FungoPageResultDto<GameEvaluationDto>  gameEvaluationPage = gamesFeignClient.getGameEvaluationPage(param);
                            GameEvaluationDto gameEvaluationDto = (gameEvaluationPage.getData() != null && gameEvaluationPage.getData().size() > 0 ) ? gameEvaluationPage.getData().get(0) : null;
                            if(gameEvaluationDto != null){
                                map.put( "two_level_deltype",gameEvaluationDto.getState()  == -1 ? -1 : 0 );
                                map.put( "gameIdtSn",gameFilterService.getGameDto( gameEvaluationDto.getGameId()) );
                            }
                        }

                        String replyId = (String) map.get("replyId");
                        if(StringUtil.isNotNull(replyId)){
                            CmmCmtReplyDto cmmCmtReplyDto = new CmmCmtReplyDto();
                            cmmCmtReplyDto.setId(replyId);
                            cmmCmtReplyDto.setState(null);
                            FungoPageResultDto<CmmCmtReplyDto>  replyDtoFungoPageResultDto = communityFeignClient.querySecondLevelCmtList(cmmCmtReplyDto);
                            CmmCmtReplyDto cmmCmtReplyDto1 =    (replyDtoFungoPageResultDto.getData() != null && replyDtoFungoPageResultDto.getData().size() >0 ) ? replyDtoFungoPageResultDto.getData().get(0) : null ;   //iGameProxyService.selectMooMessageById(commentBean.getTargetId());//mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
                            if (cmmCmtReplyDto1 != null) {
                                map.put( "one_level_deltype",cmmCmtReplyDto1.getState()  == -1 ? -1 : 0 );
                            }
                        }

                    }else if(map.get("game_id") != null){
                        map.put("parentId", map.get("game_id"));
                        map.put( "gameIdtSn",gameFilterService.getGameDto( (String) map.get("game_id")) );
                        String evaluationId = (String) map.get("evaluation_id");
                        GameEvaluationDto param = new GameEvaluationDto();
                        param.setId(evaluationId);
                        FungoPageResultDto<GameEvaluationDto>  resultDto = gamesFeignClient.getGameEvaluationPage(param);
                        GameEvaluationDto gameEvaluationDto = (resultDto.getData() != null && resultDto.getData().size() > 0 ) ? resultDto.getData().get(0) : null;
                        if(gameEvaluationDto != null){
                            Member member = memberService.selectById(gameEvaluationDto.getMemberId());
                            map.put( "parentName",member.getUserName());
                            map.put( "gameIdtSn",gameFilterService.getGameDto( gameEvaluationDto.getGameId()) );
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", (String) map.get("user_id")).eq("rank_type", 1));
            Member member = memberService.selectById((String) map.get("user_id"));

            if (ranked != null) {
                String rankImgs = getLevelRankUrl(ranked.getCurrentRankId().intValue(), levelRankList);
                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
                map.put("dignityImg", (String) urlList.get(0).get("url"));
            } else {
                String rankImgs = getLevelRankUrl((int) map.get("user_level"), levelRankList);
                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
                map.put("dignityImg", (String) urlList.get(0).get("url"));
            }
            if (member != null) {
                map.put("user_avatar", member.getAvatar());
                map.put("user_name", member.getUserName());
            }
            map.put("statusImgs", new ArrayList<>(  ));
            List<IncentRanked> incentRankeds = rankedService.selectList(new EntityWrapper<IncentRanked>().eq("mb_id", map.get("user_id")));
            for (IncentRanked identityIncentRanked : incentRankeds) {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    if (identityIncentRanked.getRankType() == 2) {
                        IncentRuleRank rank = rankRuleService.selectById(identityIncentRanked.getCurrentRankId());//最近获得
//                        String rankImgs = rank.getRankImgs();
//                        ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
//                        author.setStatusImg(urlList);
                        String rankIdtIds = identityIncentRanked.getRankIdtIds();
                        List<HashMap<String,Object>> list1 = mapper.readValue(rankIdtIds, ArrayList.class);
                        List<List<HashMap<String,Object>>> statusLists = new ArrayList<>(  );
//                        int groupLevel = 0;
//                        int circleLevel = 0;
                        for (HashMap<String,Object> map1 : list1){
                            Integer rankId = (Integer) map1.get( "1" );
                            IncentRuleRank rank1 = rankRuleService.selectById(rankId);//最近获得
                            String rankImgs = rank1.getRankImgs();
                            ArrayList<HashMap<String, Object>> urlList = null;
                            IncentRuleRankGroup incentRuleRankGroup = incentRuleRankGroupDao.selectById( rank1.getRankGroupId());
//                            groupLevel =  incentRuleRankGroup.getAuth() > groupLevel ? incentRuleRankGroup.getAuth() : groupLevel;
//                            circleLevel =  incentRuleRankGroup.getAuth() == 2 ? incentRuleRankGroup.getAuth() : circleLevel;
                            try {
                                urlList = mapper.readValue(rankImgs, ArrayList.class);
                                urlList.stream().forEach( x ->{
                                    x.put( "auth", incentRuleRankGroup.getAuth());
                                    x.put( "group", incentRuleRankGroup.getId());
                                    x.put( "groupNmae", incentRuleRankGroup.getGroupName());
                                } );
                                statusLists.add( urlList );
                            } catch (Exception e) {
                                logger.error( "对象转换异常",e );
                            }
                        }
//                        author.setGroupLevel(groupLevel);
//                        author.setCircleLevel( circleLevel );
                        map.put("statusImgs", (statusLists));
                    }
                }catch (Exception e){
                    logger.error( "",e);
                }
            }
            map.put("statusImg", userService.getStatusImage((String) map.get("user_id")));
            map.put("createdAt", DateTools.fmtDate(basNotice.getCreatedAt()));
            list.add(map);

            if (basNotice.getIsPush() == 0 || basNotice.getIsRead() == 0) {
                basNotice.setIsPush(1);
                basNotice.setIsRead(1);
                basNotice.updateById();
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("typeList", types);
        noticeDao.setIsRead(map);

        PageTools.pageToResultDto(re, plist);
        updateNoticeMap(memberId,"comment_count");
        String keyPrefix = FungoCoreApiConstant.REDIS_MEMBER_NOTICE + memberId + "7";
        //清除redis缓存
        fungoCacheNotice.excIndexCacheWithOutSecurity(false, keyPrefix, "", null);
        return re;
    }

    @Override
    public FungoPageResultDto<SysNoticeBean> getSystemNotice(String memberId, InputPageDto inputPage,String os) {
        FungoPageResultDto<SysNoticeBean> re = new FungoPageResultDto<SysNoticeBean>();
        List<SysNoticeBean> list = new ArrayList<SysNoticeBean>();
        re.setData(list);
        String[] types = {"6","15"};

//		Page<BasNotice> plist=noticeService.selectPage(new Page<BasNotice>(inputPage.getPage(),inputPage.getLimit()), new EntityWrapper<BasNotice>().in("type", types));
        //孟 根据是否推送来获取消息，add is_push = 0

        Page<BasNotice> noticePage = new Page<>(inputPage.getPage(), inputPage.getLimit());

        EntityWrapper<BasNotice> noticeEntityWrapper = new EntityWrapper<>();
        noticeEntityWrapper.eq("member_id", memberId);
        //noticeEntityWrapper.eq("is_read", 0);

        noticeEntityWrapper.in("type", types);
        noticeEntityWrapper.orderBy("created_at", false);

        Page<BasNotice> plist = noticeService.selectPage(noticePage, noticeEntityWrapper);

        List<BasNotice> basNotices = plist.getRecords();
        List<BasNotice> t = basNotices.stream().filter( s -> ( os.equals(s.getChannel()) || StringUtil.isNull(s.getChannel()) )).collect( Collectors.toList());
        ObjectMapper objectMapper = new ObjectMapper();
        for (BasNotice basNotice : t) {

            SysNoticeBean bean = new SysNoticeBean();

            Map<String, String> map = new HashMap<String, String>();

            try {
                try {
                    map = objectMapper.readValue(basNotice.getData(), Map.class);
                } catch (Exception e) {
                    plist.setTotal(plist.getTotal() - 1);//去除当前记录
                    logger.warn("解析basNotice的data出错", e);
                    break;
                }
                Integer actionType = Integer.valueOf(map.get("actionType"));
                bean.setContent(map.get("content"));
                bean.setActionType(actionType);

                if (actionType == 1) {
                    Integer targetType = Integer.valueOf(map.get("targetType"));
                    if (targetType == 1) {
                        CmmPostDto post = iMemeberProxyService.selectCmmPost(map.get("targetId")); //postService.selectById(map.get("targetId"));
                        if (!CommonUtil.isNull(post.getVideo())) {
                            bean.setVideo(post.getVideo());
                        }
                    }else if(6 == targetType ){
                        GameEvaluationDto gameEvaluationDto = new GameEvaluationDto();
                        gameEvaluationDto.setId(map.get("targetId"));
                        FungoPageResultDto<GameEvaluationDto> resultDto = gamesFeignClient.getGameEvaluationPage(gameEvaluationDto);
                        if(resultDto != null && resultDto.getData()!= null && resultDto.getData().size() > 0){
                            gameEvaluationDto = resultDto.getData().get(0);
                            bean.setParentId(gameEvaluationDto.getGameId());
                            GameDto gameDto = gamesFeignClient.selectOne(gameEvaluationDto.getGameId());
                            bean.setGameIdtSn(gameDto != null ? gameDto.getGameIdtSn() : 0L);
                        }
                    }
                    bean.setHref(map.get("href"));
                    bean.setMsgId(basNotice.getId());
                    bean.setTargetId(map.get("targetId"));
                    bean.setTargetType(targetType);
                    bean.setUserAvatar(map.get("userAvatar"));
                    bean.setUserId(map.get("userId"));
                    bean.setUserType(Integer.valueOf(map.get("userType")));
                    bean.setUserName(map.get("userName"));
                    bean.setMsgTime(map.get("msgTime"));
                } else {
                    bean.setUserId(map.get("userId"));
                    bean.setMsgId(basNotice.getId());
                    bean.setMsgTime(map.get("msgTime"));
                }

                if (basNotice.getIsRead() == 0 || basNotice.getIsPush() == 0) {
                    basNotice.setIsRead(1);
                    basNotice.setIsPush(1);

                    basNotice.updateById();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            list.add(bean);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("memberId", memberId);
        map.put("typeList", types);
        noticeDao.setIsRead(map);

        PageTools.pageToResultDto(re, plist);
        updateNoticeMap(memberId,"notice_count");
        String keyPrefix = FungoCoreApiConstant.REDIS_MEMBER_NOTICE + memberId + "7";
        //清除redis缓存
        fungoCacheNotice.excIndexCacheWithOutSecurity(false, keyPrefix, "", null);
        return re;
    }


    public String getLevelRankUrl(int level, List<IncentRuleRank> levelRankList) {
        String url = "";
        for (IncentRuleRank ruleRank : levelRankList) {
            if (ruleRank.getRankIdt() == level) {
                url = ruleRank.getRankImgs();
                break;
            }
        }
        return url;
    }

    @Deprecated
    @Async
    public void updateNoticeMap(String memberId,String key){
        /**
         * 功能描述: 查询获取点赞我的信息，然后将其在消息临时表中删除掉
         * @auther: dl.zhang
         * @date: 2019/5/29 17:19
         */
        try {
//            EntityWrapper<MemberNotice> memberNoticeEntityWrapper = new EntityWrapper<>();
//            memberNoticeEntityWrapper.eq("mb_id", memberId);
//            memberNoticeEntityWrapper.eq("ntc_type", 7);
//            memberNoticeEntityWrapper.eq("is_read", 2);
////            distributedLockByCurator.acquireDistributedLock( memberId );
//            List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList(memberNoticeEntityWrapper);
//            if (null != noticeListDB && !noticeListDB.isEmpty()) {
//                for (MemberNotice memberNotice : noticeListDB) {
//                    String ntcDataJsonStr = memberNotice.getNtcData();
//                    Map<String, Object> msgMap = JSON.parseObject(ntcDataJsonStr);
//                    if((int)msgMap.get(key) == 0) continue;
//                    Map<String,Object> oldMap = new HashMap<>();
//                    oldMap.put(key,msgMap.get(key));
//                    updateMap(oldMap,msgMap);
//                    msgMap.put("count",((int)msgMap.get("count") - (int)msgMap.get(key)));
//                    msgMap.put(key,0);
//                    memberNotice.setNtcData(JSONObject.toJSONString(msgMap));
//                    memberNoticeDaoService.updateById(memberNotice);
//                }
//            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除未读消息",e);
        }finally {
//            distributedLockByCurator.releaseDistributedLock(memberId);
        }
    }

    public void updateMap(Map<String,Object> oldMap,Map noticeMap){
        if(noticeMap.get("expire") != null){
            Map<String,Object> expire = (Map<String, Object>) noticeMap.get("expire");
            for(String key : oldMap.keySet()){
                if(!expire.containsKey(key)){
                    expire.putAll(oldMap);
                    noticeMap.putAll(expire);
                }
            }
        }else {
            noticeMap.put("expire",oldMap);
        }
    }

    private static String comparingByName(Map<String, Object> map){
        return (String) map.get("name");
    }


}
