package com.fungo.system.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.system.dao.BasNoticeDao;
import com.fungo.system.dto.*;
import com.fungo.system.entity.*;
import com.fungo.system.facede.IMemeberProxyService;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.helper.zookeeper.DistributedLockByCurator;
import com.fungo.system.service.*;
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
    private MemberNoticeDaoService memberNoticeDaoService;
    @Autowired
    private FungoCacheNotice fungoCacheNotice;
    @Autowired
    private DistributedLockByCurator distributedLockByCurator;
    @Autowired
    private GamesFeignClient gamesFeignClient;


    //消息
    @Override
    public FungoPageResultDto<Map<String, Object>> getLikeNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception {
        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        re.setData(list);
        String[] types = null;
        //版本不同获取的内容不同
        if (CommonUtils.versionAdapte(appVersion, "2.4.4")) {
            types = new String[]{"0", "1", "2", "7", "11"};
        } else {
            types = new String[]{"0", "1", "2", "7","11"};
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
                    }
                } else if ((int) map.get("type") == 1) {//basNotice.getType()==1
                    map.put("msg_template", "赞了我的评论");
                    CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id"));  //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                    if (post != null) {
                        if (!CommonUtil.isNull(post.getVideo())) {
                            map.put("video", post.getVideo());
                        }
                    }
                } else if ((int) map.get("type") == 2) {
                    map.put("msg_template", "赞了我的游戏评价");
                } else if (basNotice.getType() == 7) {
                    map.put("msg_template", "赞了我的心情");
                } else if (basNotice.getType() == 11) {
                    map.put("msg_template", "赞了我的心情评论");
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
                    }
                } else if (basNotice.getType() == 4) {
                    map.put("msg_template", "回复了我的评论");
                    CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id")); //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                    if (post != null) {
                        if (!CommonUtil.isNull(post.getVideo())) {
                            map.put("video", post.getVideo());
                        }
                    }
                } else if (basNotice.getType() == 5) {
                    map.put("msg_template", "回复了我的游戏评价");
                } else if (basNotice.getType() == 8) {
                    map.put("msg_template", "评论了我的心情");
                } else if (basNotice.getType() == 9) {
                    map.put("msg_template", "回复了我的心情评论");
                } else if (basNotice.getType() == 12) {
                    map.put("msg_template", "回复了我的回复");

                    if (map.get("post_id") != null) {
                        CmmPostDto post = iMemeberProxyService.selectCmmPost((String) map.get("post_id")); //postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
                        if (post != null) {
                            if (!CommonUtil.isNull(post.getVideo())) {
                                map.put("video", post.getVideo());
                            }
                        }
                        map.put("parentId", map.get("post_id"));
                    }else if(map.get("mood_id") != null){
                        map.put("parentId", map.get("mood_id"));
                    }else if(map.get("game_id") != null){
                        map.put("parentId", map.get("game_id"));
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
        String[] types = {"6"};

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

    public void updateNoticeMap(String memberId,String key){
        /**
         * 功能描述: 查询获取点赞我的信息，然后将其在消息临时表中删除掉
         * @auther: dl.zhang
         * @date: 2019/5/29 17:19
         */
        try {
            EntityWrapper<MemberNotice> memberNoticeEntityWrapper = new EntityWrapper<>();
            memberNoticeEntityWrapper.eq("mb_id", memberId);
            memberNoticeEntityWrapper.eq("ntc_type", 7);
            memberNoticeEntityWrapper.eq("is_read", 2);
            distributedLockByCurator.acquireDistributedLock( memberId );
            List<MemberNotice> noticeListDB = memberNoticeDaoService.selectList(memberNoticeEntityWrapper);
            if (null != noticeListDB && !noticeListDB.isEmpty()) {
                for (MemberNotice memberNotice : noticeListDB) {
                    String ntcDataJsonStr = memberNotice.getNtcData();
                    Map<String, Object> msgMap = JSON.parseObject(ntcDataJsonStr);
                    if((int)msgMap.get(key) == 0) continue;
                    Map<String,Object> oldMap = new HashMap<>();
                    oldMap.put(key,msgMap.get(key));
                    updateMap(oldMap,msgMap);
                    msgMap.put("count",((int)msgMap.get("count") - (int)msgMap.get(key)));
                    msgMap.put(key,0);
                    memberNotice.setNtcData(JSONObject.toJSONString(msgMap));
                    memberNoticeDaoService.updateById(memberNotice);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            logger.error("删除未读消息",e);
        }finally {
            distributedLockByCurator.releaseDistributedLock(memberId);
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
