package com.fungo.system.service.impl;

import com.fungo.system.dao.FollowInptPageDao;
import com.fungo.system.dto.*;
import com.fungo.system.service.IMemberService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class MemberServiceImpl implements IMemberService {
    @Override
    public FungoPageResultDto<CollectionOutBean> getCollection(String memberId, InputPageDto inputPage) {
        return null;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getFollower(String memberId, String memberId2, FollowInptPageDao inputPage) throws Exception {
        return null;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getFollowee(String memberId, String memberId2, InputPageDto inputPage) throws Exception {
        return null;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getHistory(String memberId, InputPageDto inputPage) {
        return null;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getLikeNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception {
        return null;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getCommentNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception {
        return null;
    }

    @Override
    public Map<String, Object> getUnReadNotice(String memberId, String appVersion) {
        return null;
    }

    @Override
    public FungoPageResultDto<SysNoticeBean> getSystemNotice(String memberId, InputPageDto inputPage) {
        return null;
    }

    @Override
    public FungoPageResultDto<Map<String, Object>> getTimeLine(String memberId) {
        return null;
    }

    @Override
    public ResultDto<String> getFeed(String memberId) {
        return null;
    }

    @Override
    public ResultDto<String> getUserCard(String memberId, String cardId) {
        return null;
    }

    @Override
    public FungoPageResultDto<MyGameBean> getGameList(String memberId, MyGameInputPageDto inputPage, String os) {
        return null;
    }

    @Override
    public ResultDto<AuthorBean> getUserInfo(String memberId) {
        return null;
    }

    @Override
    public ResultDto<AuthorBean> getUserInfoPc(String memberId) {
        return null;
    }

    @Override
    public ResultDto<Map<String, Integer>> getPublishCount(String loginId) {
        return null;
    }

    @Override
    public FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(String loginId, InputPageDto input) throws Exception {
        return null;
    }


//    private static final Logger logger = LoggerFactory.getLogger(MemberServiceImpl.class);
//    @Autowired
//    private BasNoticeService noticeService;
//    @Autowired
//    private BasActionService actionService;
//    @Autowired
//    private BasActionDao actionDao;
//    @Autowired
//    private CmmPostService postService;
//    @Autowired
//    private IUserService userService;
//    @Autowired
//    private MemberService memberService;
//    @Autowired
//    private GameSurveyRelService gameSurveyRelService;
//    @Autowired
//    private GameService gameService;
//    @Autowired
//    private DeveloperService developerService;
//    @Autowired
//    private DeveloperGameRelService dgrService;
//    @Autowired
//    private MooMoodService moodService;
//    @Autowired
//    private ReplyService replyService;
//    @Autowired
//    private GameEvaluationService evaluationService;
//    @Autowired
//    private MemberDao memberDao;
//    @Autowired
//    private CmmCommentService commentService;
//    @Autowired
//    private MooMessageService mooMessageService;
//    @Autowired
//    private CmmCommunityService communityService;
//    @Autowired
//    private IMemberIncentRuleRankService IRuleRankService;
//    //	@Autowired
////	private IncentRankedService incentRankedService;
//    @Autowired
//    private IncentRuleRankService rankRuleService;
//    @Autowired
//    private IncentRankedService rankedService;
//    @Autowired
//    private IncentAccountScoreService accountScoreService;
//    @Autowired
//    private BasNoticeDao noticeDao;
//
//
//    //	@Autowired
////	private IncentRuleRankService rankRuleService;
////	@Autowired
////	private IncentRankedService rankedService;
//    @Autowired
//    private ScoreLogService scoreLogService;
//
//    @Autowired
//    private FungoCacheMember fungoCacheMember;
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
//    @Override
//    public FungoPageResultDto<CollectionOutBean> getCollection(String memberId, InputPageDto inputPage) {
//
//        FungoPageResultDto<CollectionOutBean> re = null;
//
//        //from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_COLLECTION + memberId;
//        String keySuffix = JSON.toJSONString(inputPage);
//
//        re = (FungoPageResultDto<CollectionOutBean>) fungoCacheMember.getIndexCache(keyPrefix, keySuffix);
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<CollectionOutBean>();
//        List<CollectionOutBean> list = new ArrayList<CollectionOutBean>();
//        re.setData(list);
//        Page<CollectionBean> p = new Page<CollectionBean>(inputPage.getPage(), inputPage.getLimit());
//        List<CollectionBean> plist = actionDao.getCollection(p, memberId);
//        for (CollectionBean collectionBean : plist) {
//            CollectionOutBean bean = new CollectionOutBean();
//            bean.setAuthor(userService.getAuthor(collectionBean.getMemberId()));
//            bean.setContent(CommonUtils.filterWord(collectionBean.getContent()));
//            bean.setCover_image(collectionBean.getCoverImage());
//            bean.setCreatedAt(collectionBean.getCreatedAt());
//            bean.setObjectId(collectionBean.getId());
//            bean.setTitle(collectionBean.getTitle());
//            bean.setVideo(collectionBean.getVideo());
//            bean.setUpdatedAt(collectionBean.getUpdatedAt());
//            list.add(bean);
//        }
//        PageTools.pageToResultDto(re, p);
//
//        //redis cache
//        fungoCacheMember.excIndexCache(true, keyPrefix, keySuffix, re);
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<Map<String, Object>> getFollower(String myId, String memberId, FollowInptPageDao inputPage) throws Exception {
//
//        FungoPageResultDto<Map<String, Object>> re = null;
//
//        //from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FOLLW + myId;
//        String keySuffix = JSON.toJSONString(inputPage);
//        re = (FungoPageResultDto<Map<String, Object>>) fungoCacheMember.getIndexCache(keyPrefix, memberId + keySuffix);
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<Map<String, Object>>();
//
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        Page p = new Page(inputPage.getPage(), inputPage.getLimit());
//        if (0 == inputPage.getType()) {//关注的用户
//            list = actionDao.getFollowerUser(p, memberId);
//            ObjectMapper mapper = new ObjectMapper();
//            List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
//            for (Map<String, Object> map : list) {
////				IncentRanked ranked = incentRankedService.selectOne(new EntityWrapper<IncentRanked>().eq("rank_type",1).eq("mb_id",map.get("objectId")));
//                map.put("is_followed", false);
//                BasAction action = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", myId).eq("target_id", (String) map.get("objectId")).ne("state", "-1"));
//                if (action != null) {
//                    map.put("is_followed", true);
//                }
////				map.put("level", ranked.getCurrentRankId().intValue());
//                String rankImgs = getLevelRankUrl((int) map.get("level"), levelRankList);
//                ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
//                map.put("dignityImg", (String) urlList.get(0).get("url"));
//                //用户官方身份
//                IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", (String) map.get("objectId")).eq("rank_type", 2));
//                if (ranked != null) {
//                    IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
//                    if (rank != null) {
//                        String rankinf = rank.getRankImgs();
//                        ArrayList<HashMap<String, Object>> infolist = mapper.readValue(rankinf, ArrayList.class);
//                        map.put("statusImg", infolist);
//                    } else {
//                        map.put("statusImg", new ArrayList<>());
//                    }
//                } else {
//                    map.put("statusImg", new ArrayList<>());
//                }
//            }
//
//        } else if (4 == inputPage.getType()) {//关注的社区
//            list = actionDao.getFollowerCommunity(p, memberId);
//            for (Map<String, Object> map : list) {
//                map.put("is_followed", false);
//                BasAction action = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", myId).eq("target_id", (String) map.get("objectId")).ne("state", "-1"));
//                if (action != null) {
//                    map.put("is_followed", true);
//                }
//            }
//        }
//        re.setData(list);
//        PageTools.pageToResultDto(re, p);
//
//        //redis cache
//        fungoCacheMember.excIndexCache(true, keyPrefix, keySuffix, re);
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<Map<String, Object>> getFollowee(String myId, String memberId, InputPageDto inputPage) throws Exception {
//
//        FungoPageResultDto<Map<String, Object>> re = null;
//        //redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FANS + myId;
//        String keySuffix = JSON.toJSONString(inputPage);
//        re = (FungoPageResultDto<Map<String, Object>>) fungoCacheMember.getIndexCache(keyPrefix, memberId + keySuffix);
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<Map<String, Object>>();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        re.setData(list);
//        Page<BasAction> plist = actionService.selectPage(new Page<BasAction>(inputPage.getPage(), inputPage.getLimit()), new EntityWrapper<BasAction>().eq("type", "5").eq("target_id", memberId).notIn("state", "-1"));
//        List<BasAction> list1 = plist.getRecords();
//        ObjectMapper mapper = new ObjectMapper();
//        List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
//        for (BasAction basAction : list1) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            Member m = memberService.selectById(basAction.getMemberId());
//            //fix bug: m 为空导致的NPE [by mxf 2019-04-01]
//            if (null == m) {
//                continue;
//            }
//            //end
////			IncentRanked ranked = incentRankedService.selectOne(new EntityWrapper<IncentRanked>().eq("rank_type",1).eq("mb_id",basAction.getMemberId()));
//            map.put("sign", m.getSign());
//            map.put("username", m.getUserName());
//            map.put("level", m.getLevel());
//            map.put("avatar", m.getAvatar());
//            map.put("is_followed", false);
//            BasAction action = actionService.selectOne(new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", myId).eq("target_id", m.getId()).ne("state", "-1"));
//            if (action != null) {
//                map.put("is_followed", true);
//            }
//            String rankImgs = getLevelRankUrl(m.getLevel(), levelRankList);
//            ArrayList<HashMap<String, Object>> urlList = mapper.readValue(rankImgs, ArrayList.class);
//            map.put("dignityImg", (String) urlList.get(0).get("url"));
//            //用户官方身份
//            IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", m.getId()).eq("rank_type", 2));
//            if (ranked != null) {
//                IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
//                if (rank != null) {
//                    String rankinf = rank.getRankImgs();
//                    ArrayList<HashMap<String, Object>> infolist = mapper.readValue(rankinf, ArrayList.class);
//                    map.put("statusImg", infolist);
//                } else {
//                    map.put("statusImg", new ArrayList<>());
//                }
//            } else {
//                map.put("statusImg", new ArrayList<>());
//            }
//
//            map.put("objectId", m.getId());
//            map.put("createdAt", DateTools.fmtDate(m.getCreatedAt()));
//            map.put("updatedAt", DateTools.fmtDate(m.getUpdatedAt()));
//            list.add(map);
//        }
//        PageTools.pageToResultDto(re, plist);
//
//        //redis cache
//        fungoCacheMember.excIndexCache(true, keyPrefix, keySuffix, re);
//
//        return re;
//    }
//
//
//    @Override
//    public FungoPageResultDto<Map<String, Object>> getHistory(String memberId, InputPageDto inputPage) {
//        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        Page<BasAction> p = new Page<BasAction>(inputPage.getPage(), inputPage.getLimit());
//        re.setData(list);
//        PageTools.pageToResultDto(re, p);
//        return re;
//    }
//
//    //消息
//    @Override
//    public FungoPageResultDto<Map<String, Object>> getLikeNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception {
//        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        re.setData(list);
//
//        String[] types = null;
//        //版本不同获取的内容不同
//        if (CommonUtils.versionAdapte(appVersion, "2.4.4")) {
//            types = new String[]{"0", "1", "2", "7", "11"};
//        } else {
//            types = new String[]{"0", "1", "2", "7"};
//        }
//
//        Page<BasNotice> basNoticePage = new Page<>(inputPage.getPage(), inputPage.getLimit());
//
//        EntityWrapper<BasNotice> noticeEntityWrapper = new EntityWrapper<>();
//        noticeEntityWrapper.eq("member_id", memberId);
//        // noticeEntityWrapper.eq("is_read", 0);
//
//        noticeEntityWrapper.in("type", types);
//        noticeEntityWrapper.orderBy("created_at", false);
//
//        Page<BasNotice> plist = noticeService.selectPage(basNoticePage, noticeEntityWrapper);
//        List<BasNotice> t = plist.getRecords();
//
//        List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        for (BasNotice basNotice : t) {
//            Map<String, Object> map = new HashMap<String, Object>();
//            map.put("video", "");
//            try {
//
//                String noticeData = basNotice.getData();
//                //表情解码
//                if (StringUtils.isNotBlank(noticeData)) {
//                    noticeData = FilterEmojiUtil.decodeEmoji(noticeData);
//                }
//
//                map = objectMapper.readValue(noticeData, Map.class);
//                if ((int) map.get("type") == 0) {
//                    map.put("msg_template", "赞了我的文章");
//                    CmmPost post = postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
//                    if (post != null) {
//                        if (!CommonUtil.isNull(post.getVideo())) {
//                            map.put("video", post.getVideo());
//                        }
//                    }
//                } else if ((int) map.get("type") == 1) {//basNotice.getType()==1
//                    map.put("msg_template", "赞了我的评论");
//                    CmmPost post = postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
//                    if (post != null) {
//                        if (!CommonUtil.isNull(post.getVideo())) {
//                            map.put("video", post.getVideo());
//                        }
//                    }
//                } else if ((int) map.get("type") == 2) {
//                    map.put("msg_template", "赞了我的游戏评价");
//                } else if (basNotice.getType() == 7) {
//                    map.put("msg_template", "赞了我的心情");
//                } else if (basNotice.getType() == 11) {
//                    map.put("msg_template", "赞了我的心情评论");
//                }
//
//                if (basNotice.getIsRead() == 0 || basNotice.getIsPush() == 0) {
//                    basNotice.setIsRead(1);
//                    basNotice.setIsPush(1);
//
//                    basNotice.updateById();
//                }
//
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", (String) map.get("user_id")).eq("rank_type", 1));
//            Member member = memberService.selectById((String) map.get("user_id"));
//
//            if (ranked != null) {
//                String rankImgs = getLevelRankUrl(ranked.getCurrentRankId().intValue(), levelRankList);
//                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
//                map.put("dignityImg", (String) urlList.get(0).get("url"));
//            } else {
//                String rankImgs = getLevelRankUrl((int) map.get("user_level"), levelRankList);
//                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
//                map.put("dignityImg", (String) urlList.get(0).get("url"));
//            }
//
//            if (member != null) {
//                map.put("user_avatar", member.getAvatar());
//                map.put("user_name", member.getUserName());
//            }
//
//            map.put("statusImg", userService.getStatusImage((String) map.get("user_id")));
//            map.put("createdAt", DateTools.fmtDate(basNotice.getCreatedAt()));
//
//            list.add(map);
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("memberId", memberId);
//        map.put("typeList", types);
//        noticeDao.setIsRead(map);
//
//        PageTools.pageToResultDto(re, plist);
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<Map<String, Object>> getCommentNotice(String memberId, InputPageDto inputPage, String appVersion) throws Exception {
//        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        re.setData(list);
//        String[] types = null;
//
//        //版本不同获取的内容不同
//        if (CommonUtils.versionAdapte(appVersion, "2.4.4")) {
//
//            types = new String[]{"3", "4", "5", "8", "9", "12"};
//        } else {
//            types = new String[]{"3", "4", "5", "8"};
//        }
////		String[] types1= {"3","4","5","8"};
//        Page<BasNotice> basNoticePage = new Page<>(inputPage.getPage(), inputPage.getLimit());
//
//        EntityWrapper<BasNotice> noticeEntityWrapper = new EntityWrapper<>();
//        noticeEntityWrapper.eq("member_id", memberId);
//        //noticeEntityWrapper.eq("is_read", 0);
//
//        noticeEntityWrapper.in("type", types);
//        noticeEntityWrapper.orderBy("created_at", false);
//
//        Page<BasNotice> plist = noticeService.selectPage(basNoticePage, noticeEntityWrapper);
//
//        List<BasNotice> t = plist.getRecords();
//
//        List<IncentRuleRank> levelRankList = IRuleRankService.getLevelRankList();
//        ObjectMapper objectMapper = new ObjectMapper();
//
//        for (BasNotice basNotice : t) {
//
//            Map<String, Object> map = new HashMap<String, Object>();
//
//            try {
//
//                //表情解码
//                if (StringUtils.isNotBlank(basNotice.getData())) {
//                    String date = FilterEmojiUtil.decodeEmoji(basNotice.getData());
//                    basNotice.setData(date);
//                }
//
//                map.put("video", "");
//                map = objectMapper.readValue(basNotice.getData(), Map.class);
//                if (basNotice.getType() == 3) {
//                    map.put("msg_template", "评论了我的文章");
//                    CmmPost post = postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
//                    if (post != null) {
//                        if (!CommonUtil.isNull(post.getVideo())) {
//                            map.put("video", post.getVideo());
//                        }
//                    }
//                } else if (basNotice.getType() == 4) {
//                    map.put("msg_template", "回复了我的评论");
//                    CmmPost post = postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
//                    if (post != null) {
//                        if (!CommonUtil.isNull(post.getVideo())) {
//                            map.put("video", post.getVideo());
//                        }
//                    }
//                } else if (basNotice.getType() == 5) {
//                    map.put("msg_template", "回复了我的游戏评价");
//                } else if (basNotice.getType() == 8) {
//                    map.put("msg_template", "评论了我的心情");
//                } else if (basNotice.getType() == 9) {
//                    map.put("msg_template", "回复了我的心情评论");
//                } else if (basNotice.getType() == 12) {
//                    map.put("msg_template", "回复了我的回复");
//
//                    if (map.get("post_id") != null) {
//                        CmmPost post = postService.selectOne(Condition.create().setSqlSelect("id,video").eq("id", (String) map.get("post_id")));
//                        if (post != null) {
//                            if (!CommonUtil.isNull(post.getVideo())) {
//                                map.put("video", post.getVideo());
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", (String) map.get("user_id")).eq("rank_type", 1));
//            Member member = memberService.selectById((String) map.get("user_id"));
//
//            if (ranked != null) {
//                String rankImgs = getLevelRankUrl(ranked.getCurrentRankId().intValue(), levelRankList);
//                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
//                map.put("dignityImg", (String) urlList.get(0).get("url"));
//            } else {
//                String rankImgs = getLevelRankUrl((int) map.get("user_level"), levelRankList);
//                ArrayList<HashMap<String, Object>> urlList = objectMapper.readValue(rankImgs, ArrayList.class);
//                map.put("dignityImg", (String) urlList.get(0).get("url"));
//            }
//
//            if (member != null) {
//                map.put("user_avatar", member.getAvatar());
//                map.put("user_name", member.getUserName());
//            }
//
//            map.put("statusImg", userService.getStatusImage((String) map.get("user_id")));
//            map.put("createdAt", DateTools.fmtDate(basNotice.getCreatedAt()));
//            list.add(map);
//
//            if (basNotice.getIsPush() == 0 || basNotice.getIsRead() == 0) {
//                basNotice.setIsPush(1);
//                basNotice.setIsRead(1);
//
//                basNotice.updateById();
//            }
//
//
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("memberId", memberId);
//        map.put("typeList", types);
//        noticeDao.setIsRead(map);
//
//        PageTools.pageToResultDto(re, plist);
//        return re;
//    }
//
//    @Override
//    public Map<String, Object> getUnReadNotice(String memberId, String appVersion) {
//
//        Map<String, Object> map = new HashMap<String, Object>();
//
//        int count = noticeService.selectCount(new EntityWrapper<BasNotice>().eq("is_read", 0).eq("member_id", memberId));
//
//        Integer[] types = null;
//        //版本不同获取的内容不同
//        if (CommonUtils.versionAdapte(appVersion, "2.4.4")) {
//            types = new Integer[]{0, 1, 2, 7, 11};
//        } else {
//            types = new Integer[]{0, 1, 2, 7};
//        }
//        int like_count = noticeService.selectCount(new EntityWrapper<BasNotice>().eq("is_read", 0).eq("member_id", memberId).in("type", types));
//
//        Integer[] types1 = null;
//        //版本不同获取的内容不同
//        if (CommonUtils.versionAdapte(appVersion, "2.4.4")) {
//            types1 = new Integer[]{3, 4, 5, 8, 9, 12};
//        } else {
//            types1 = new Integer[]{3, 4, 5, 8};
//        }
//
////		Integer[] types1= {3,4,5,8};
////		types1= new Integer[]{3,4,5,8,9};
//
//        int comment_count = noticeService.selectCount(new EntityWrapper<BasNotice>().eq("is_read", 0).eq("member_id", memberId).in("type", types1));
//        int notice_count = noticeService.selectCount(new EntityWrapper<BasNotice>().eq("is_read", 0).eq("member_id", memberId).eq("type", 6));
//       /* List<BasNotice> noticeList = noticeService.selectList(new EntityWrapper<BasNotice>().eq("is_read", 0).eq("member_id", memberId).eq("type", 6));
//        for (BasNotice notice : noticeList) {
//            //将未被推送的消息设置为已推送
//            if (notice.getIsPush() == 0) {
//                notice.setIsPush(1);//已经推送
//            }
//            if (notice.getIsRead() == 0) {
//                notice.setIsRead(1);//消息已读
//            }
//            notice.updateById();
//        }*/
//
//        map.put("count", count);
//        map.put("like_count", like_count);
//        map.put("comment_count", comment_count);
//        //map.put("notice_count", noticeList.size());
//        map.put("notice_count", notice_count);
//
//        return map;
//    }
//
//    @Override
//    public FungoPageResultDto<SysNoticeBean> getSystemNotice(String memberId, InputPageDto inputPage) {
//        FungoPageResultDto<SysNoticeBean> re = new FungoPageResultDto<SysNoticeBean>();
//        List<SysNoticeBean> list = new ArrayList<SysNoticeBean>();
//        re.setData(list);
//        String[] types = {"6"};
//
////		Page<BasNotice> plist=noticeService.selectPage(new Page<BasNotice>(inputPage.getPage(),inputPage.getLimit()), new EntityWrapper<BasNotice>().in("type", types));
//        //孟 根据是否推送来获取消息，add is_push = 0
//
//        Page<BasNotice> noticePage = new Page<>(inputPage.getPage(), inputPage.getLimit());
//
//        EntityWrapper<BasNotice> noticeEntityWrapper = new EntityWrapper<>();
//        noticeEntityWrapper.eq("member_id", memberId);
//        //noticeEntityWrapper.eq("is_read", 0);
//
//        noticeEntityWrapper.in("type", types);
//        noticeEntityWrapper.orderBy("created_at", false);
//
//        Page<BasNotice> plist = noticeService.selectPage(noticePage, noticeEntityWrapper);
//
//        List<BasNotice> t = plist.getRecords();
//        ObjectMapper objectMapper = new ObjectMapper();
//        for (BasNotice basNotice : t) {
//
//            SysNoticeBean bean = new SysNoticeBean();
//
//            Map<String, String> map = new HashMap<String, String>();
//
//            try {
//                try{
//                    map = objectMapper.readValue(basNotice.getData(), Map.class);
//                }catch (Exception e){
//                    plist.setTotal(plist.getTotal()-1);//去除当前记录
//                    logger.warn("解析basNotice的data出错",e);
//                    break;
//                }
//                Integer actionType = Integer.valueOf(map.get("actionType"));
//                bean.setContent(map.get("content"));
//                bean.setActionType(actionType);
//
//                if (actionType == 1) {
//                    Integer targetType = Integer.valueOf(map.get("targetType"));
//                    if (targetType == 1) {
//                        CmmPost post = postService.selectById(map.get("targetId"));
//                        if (!CommonUtil.isNull(post.getVideo())) {
//                            bean.setVideo(post.getVideo());
//                        }
//                    }
//                    bean.setHref(map.get("href"));
//                    bean.setMsgId(basNotice.getId());
//                    bean.setTargetId(map.get("targetId"));
//                    bean.setTargetType(targetType);
//                    bean.setUserAvatar(map.get("userAvatar"));
//                    bean.setUserId(map.get("userId"));
//                    bean.setUserType(Integer.valueOf(map.get("userType")));
//                    bean.setUserName(map.get("userName"));
//                    bean.setMsgTime(map.get("msgTime"));
//                } else {
//                    bean.setUserId(map.get("userId"));
//                    bean.setMsgId(basNotice.getId());
//                    bean.setMsgTime(map.get("msgTime"));
//                }
//
//                if (basNotice.getIsRead() == 0 || basNotice.getIsPush() == 0) {
//                    basNotice.setIsRead(1);
//                    basNotice.setIsPush(1);
//
//                    basNotice.updateById();
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            list.add(bean);
//        }
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("memberId", memberId);
//        map.put("typeList", types);
//        noticeDao.setIsRead(map);
//
//        PageTools.pageToResultDto(re, plist);
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<MyGameBean> getGameList(String memberId, MyGameInputPageDto inputPage,String os) {
//
//        FungoPageResultDto<MyGameBean> re = new FungoPageResultDto<MyGameBean>();
//        List<MyGameBean> list = null;
//
//
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_GAMELIST + memberId;
//        String keySuffix = JSON.toJSONString(inputPage+os);
//
//        list = (List<MyGameBean>) fungoCacheMember.getIndexCache(keyPrefix, keySuffix);
//        if (null != list && !list.isEmpty()) {
//            re.setData(list);
//            return re;
//        }
//
//        list = new ArrayList<MyGameBean>();
//        re.setData(list);
//
//        if (2 == inputPage.getType()) {
//            Page<GameSurveyRel> page = gameSurveyRelService.selectPage(new Page<GameSurveyRel>(inputPage.getPage(), inputPage.getLimit()), new EntityWrapper<GameSurveyRel>().eq("member_id", memberId).eq("state", 0));
//            List<GameSurveyRel> plist = page.getRecords();
//            for (GameSurveyRel gameSurveyRel : plist) {
//                Game game = gameService.selectById(gameSurveyRel.getGameId());
//                MyGameBean bean = new MyGameBean();
//                bean.setAndroidState(game.getAndroidState());
//                bean.setGameContent(game.getDetail());
//                bean.setGameIcon(game.getIcon());
//                bean.setGameId(gameSurveyRel.getGameId());
//                bean.setGameName(game.getName());
//                bean.setIosState(game.getIosState());
//                bean.setMsgCount(0);
//                bean.setPhoneModel(gameSurveyRel.getPhoneModel());
//                list.add((os == null || os.equalsIgnoreCase(""))? bean:(os.equalsIgnoreCase(bean.getPhoneModel())?bean:null));
//            }
//        }
//
//        //redis cache
//        fungoCacheMember.excIndexCache(true, keyPrefix, keySuffix, list);
//        return re;
//    }
//
//
//    @Override
//    public FungoPageResultDto<Map<String, Object>> getTimeLine(String memberId) {
//        FungoPageResultDto<Map<String, Object>> re = new FungoPageResultDto<Map<String, Object>>();
//        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
//        re.setData(list);
//        return re;
//    }
//
//    @Override
//    public ResultDto<String> getFeed(String memberId) {
//        return null;
//    }
//
//    @Override
//    public ResultDto<String> getUserCard(String memberId, String cardId) {
//
//        return null;
//    }
//
//    @Override
//    public ResultDto<AuthorBean> getUserInfo(String memberId) {
//        AuthorBean author = userService.getAuthor(memberId);
//        int count = actionService.selectCount(new EntityWrapper<BasAction>().eq("member_id", memberId).eq("type", Setting.ACTION_TYPE_FOLLOW).eq("target_type", Setting.RES_TYPE_COMMUNITY));
//        author.setCommunityCount(count);
//
//        return ResultDto.success(author);
//    }
//
//    @Override
//    public ResultDto<AuthorBean> getUserInfoPc(String memberId) {
//        AuthorBean author = null;
//
//        //from redis cache
//        author = (AuthorBean) fungoCacheMember.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + memberId, "");
//        if (null != author) {
//            return ResultDto.success(author);
//        }
//
//
//        author = userService.getAuthor(memberId);
//
//        int count = actionService.selectCount(new EntityWrapper<BasAction>().eq("member_id", memberId).eq("type", Setting.ACTION_TYPE_FOLLOW).eq("target_type", Setting.RES_TYPE_COMMUNITY));
//
//        author.setCommunityCount(count);
//        //开发者信息
//        Developer developer = developerService.selectOne(new EntityWrapper<Developer>().eq("member_id", memberId));
//        if (developer != null) {
//            author.setDeveloperState(developer.getApproveState());
//        }
//        int dc = dgrService.selectCount(new EntityWrapper<DeveloperGameRel>().eq("member_id", memberId));
//        if (dc > 0) {
//            author.setHasLinkedGame(true);
//        }
//
//        //redis cache
//        fungoCacheMember.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_WEBIINFO + memberId, "", author);
//        return ResultDto.success(author);
//    }
//
//    @Override
//    public ResultDto<Map<String, Integer>> getPublishCount(String userId) {
//
//        Map<String, Integer> map = null;
//
//        map = (Map<String, Integer>) fungoCacheMember.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_PUBLISH, userId);
//        if (null != map && !map.isEmpty()) {
//            return ResultDto.success(map);
//        }
//        int postCount = postService.selectCount(new EntityWrapper<CmmPost>().eq("member_id", userId).eq("state", 1));
//        int moodCount = moodService.selectCount(new EntityWrapper<MooMood>().eq("member_id", userId).eq("state", 0));
//        int repluCount = replyService.selectCount(new EntityWrapper<Reply>().eq("member_id", userId).eq("state", 0));
//
//
//        map = new HashMap<String, Integer>();
//        map.put("postCount", postCount);
//        map.put("moodCount", moodCount);
//        map.put("repluCount", repluCount);
//
//        //redis cache
//        fungoCacheMember.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_PUBLISH, userId, map);
//
//        return ResultDto.success(map);
//    }
//
//    @Override
//    public FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(String loginId, InputPageDto input) throws Exception {
//        FungoPageResultDto<MyEvaluationBean> re = null;
//
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_EVALUATIONLIST;
//        String keySuffix = loginId + JSON.toJSONString(input);
//        re = (FungoPageResultDto<MyEvaluationBean>) fungoCacheGame.getIndexCache(keyPrefix, keySuffix);
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<MyEvaluationBean>();
//        Page<GameEvaluation> p = evaluationService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<GameEvaluation>()
//                .eq("member_id", loginId).eq("state", 0).orderBy("updated_at", false));
//        List<GameEvaluation> elist = p.getRecords();
//        List<MyEvaluationBean> olist = new ArrayList<>();
//
////		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
////		Date today = format.parse(format.format(new Date()));
////		Long time = (long) (24 * 60 * 60 * 1000); // 一天
////		SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
//        ObjectMapper mapper = new ObjectMapper();
//        for (GameEvaluation eva : elist) {
//            MyEvaluationBean bean = new MyEvaluationBean();
//            bean.setContent(eva.getContent());
////			bean.setGameName(eva.getGameName());
//            bean.setRating(eva.getRating());
//            bean.setUpdatedAt(DateTools.fmtDate(eva.getUpdatedAt()));
//            bean.setGameId(eva.getGameId());
//            bean.setEvaId(eva.getId());
//            if (eva.getImages() != null) {
//                ArrayList<String> images = mapper.readValue(eva.getImages(), ArrayList.class);
//                bean.setImages(images);
//            }
//            Game game = gameService.selectOne(Condition.create().setSqlSelect("id,icon,name").eq("id", eva.getGameId()));
//            if (game != null) {
//                bean.setIcon(game.getIcon());
//                bean.setGameName(game.getName());
//            }
//
//            olist.add(bean);
//        }
//        PageTools.pageToResultDto(re, p);
//        re.setData(olist);
//
//        //redis cache
//        fungoCacheGame.excIndexCache(true, keyPrefix, keySuffix, re);
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<MyPublishBean> getMyPosts(String loginId, InputPageDto input) throws Exception {
//        FungoPageResultDto<MyPublishBean> re = null;
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_POSTS;
//        String keySuffix = loginId + JSON.toJSONString(input);
//
//        re = (FungoPageResultDto<MyPublishBean>) fungoCacheArticle.getIndexCache(keyPrefix, keySuffix);
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<MyPublishBean>();
//        Page<CmmPost> page = postService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<CmmPost>().eq("member_id", loginId).ne("state", -1).orderBy("updated_at", false));
//        List<CmmPost> plist = page.getRecords();
//        List<MyPublishBean> blist = new ArrayList<>();
//        ObjectMapper mapper = new ObjectMapper();
//        for (CmmPost post : plist) {
//            MyPublishBean bean = new MyPublishBean();
//
//            if (StringUtils.isNotBlank(post.getTitle())) {
//                String interactTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());
//                //String interactTitle = EmojiParser.parseToUnicode(cmmPost.getTitle() );
//                post.setTitle(interactTitle);
//            }
//            if (StringUtils.isNotBlank(post.getContent())) {
//                String interactContent = FilterEmojiUtil.decodeEmoji(post.getContent());
//                //String interactContent = EmojiParser.parseToUnicode(cmmPost.getContent());
//                post.setContent(interactContent);
//            }
//
//
//            if (!CommonUtil.isNull(post.getContent())) {
//                bean.setContent(post.getContent());
//            }
//
//            bean.setType(post.getType());
//
//            if (!CommonUtil.isNull(post.getImages())) {
//                bean.setImages(mapper.readValue(post.getImages(), ArrayList.class));
//            }
//            bean.setCoverImage(post.getCoverImage());
//            bean.setObjectId(post.getId());
//            bean.setCommentNum(post.getCommentNum());
//            bean.setLikeNum(post.getLikeNum());
//            bean.setTitle(post.getTitle());
//            bean.setVideo(post.getVideo());
//            bean.setUpdatedAt(DateTools.fmtDate(post.getUpdatedAt()));
//            CmmCommunity community = communityService.selectById(post.getCommunityId());
//            if (community != null) {
//                Map<String, Object> communityMap = new HashMap<>();
//                communityMap.put("objectId", community.getId());
//                communityMap.put("name", community.getName());
//                communityMap.put("icon", community.getIcon());
//                bean.setLink_community(communityMap);
//                if (CommonUtil.isNull(post.getCoverImage())) {
//                    bean.setCoverImage(community.getCoverImage());
//                }
//            }
//            //
//            bean.setVideoCoverImage(post.getVideoCoverImage());
//
//            blist.add(bean);
//        }
//        PageTools.pageToResultDto(re, page);
//        re.setData(blist);
//
//        //redis cache
//        fungoCacheArticle.excIndexCache(true, keyPrefix, keySuffix, re);
//
//        return re;
//    }
//
//    @Override
//    public FungoPageResultDto<MyPublishBean> getMyMoods(String loginId, InputPageDto input) throws Exception {
//
//        FungoPageResultDto<MyPublishBean> re = null;
//
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_MOODS;
//        String keySuffix = loginId + JSON.toJSONString(input);
//        re = (FungoPageResultDto<MyPublishBean>) fungoCacheMood.getIndexCache(keyPrefix, keySuffix);
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<MyPublishBean>();
//        Page<MooMood> page = moodService.selectPage(new Page<>(input.getPage(), input.getLimit()), new EntityWrapper<MooMood>().eq("member_id", loginId).ne("state", -1).orderBy("updated_at", false));
//        List<MooMood> mlist = page.getRecords();
//        List<MyPublishBean> blist = new ArrayList<>();
//        // TODO Auto-generated method stub
//        ObjectMapper mapper = new ObjectMapper();
//        for (MooMood mood : mlist) {
//            MyPublishBean bean = new MyPublishBean();
//
//            if (StringUtils.isNotBlank(mood.getContent())) {
//                String interactContent = FilterEmojiUtil.decodeEmoji(mood.getContent());
//                //String interactContent = EmojiParser.parseToUnicode(cmmPost.getContent());
//                mood.setContent(interactContent);
//            }
//
//
//            if (!CommonUtil.isNull(mood.getContent())) {
//                bean.setContent(mood.getContent());
//            }
//            if (!CommonUtil.isNull(mood.getGameList())) {
//                ArrayList<String> gameIdList = mapper.readValue(mood.getGameList(), ArrayList.class);
//                List<Map<String, String>> gameList = new ArrayList<>();
//                for (String gameId : gameIdList) {
//                    Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", 0));
//                    if (game != null) {
//                        Map<String, String> map = new HashMap<>();
//                        map.put("gameId", game.getId());
//                        map.put("gameName", game.getName());
//                        map.put("gameIcon", game.getIcon());
//                        gameList.add(map);
//                    }
//                }
//                bean.setGameList(gameList);
//            }
//            if (!CommonUtil.isNull(mood.getImages())) {
//                bean.setImages(mapper.readValue(mood.getImages(), ArrayList.class));
//            }
//            bean.setObjectId(mood.getId());
//            bean.setCommentNum(mood.getCommentNum());
//            bean.setLikeNum(mood.getLikeNum());
//            bean.setVideo(mood.getVideo());
//            bean.setUpdatedAt(DateTools.fmtDate(mood.getUpdatedAt()));
//
//            bean.setVideoCoverImage(mood.getVideoCoverImage());
//
//            //视频详情
//            if (!CommonUtil.isNull(mood.getVideoUrls())) {
//                ArrayList<StreamInfo> streams = mapper.readValue(mood.getVideoUrls(),  mapper.getTypeFactory().constructCollectionType(List.class, StreamInfo.class));
//                bean.setVideoList(streams);
//            }
//
//            if (bean.getImages() == null) {
//                bean.setImages(new ArrayList<>());
//            }
//
//            blist.add(bean);
//        }
//        PageTools.pageToResultDto(re, page);
//        re.setData(blist);
//
//        //redis cache
//        fungoCacheArticle.excIndexCache(true, keyPrefix, keySuffix, re);
//
//        return re;
//    }
//
//    @Override
//    public ResultDto<MemberLevelBean> getMemberLevel(String loginId) {
////		IncentAccountScore accountScore = accountScoreService.selectOne(new EntityWrapper<IncentAccountScore>().eq("account_group_id", 1).eq("mb_id", loginId));
////		IncentRanked ranked = incentRankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", loginId).eq("rank_type", 1));
//        MemberLevelBean bean = null;
//
//        bean = (MemberLevelBean) fungoCacheMember.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + loginId, "");
//        if (null != bean) {
//            return ResultDto.success(bean);
//        }
//
//        Member member = memberService.selectById(loginId);
//        if (member == null) {
//            return ResultDto.error("-1", "用户不存在");
//        }
////		int exp = 0;
//        int level = member.getLevel();
////		if(ranked != null) {
////			level = ranked.getCurrentRankId().intValue();
////		}
////		if(accountScore != null) {
////			exp = accountScore.getScoreUsable().intValue();
////		}else {
////			exp = member.getExp();
////		}
//        int exp = member.getExp();
//
//        bean = new MemberLevelBean();
//        bean.setCurrentExp(exp);
//        bean.setLevel(level);
//        bean.setNextLevel(level + 1);
//        bean.setUpgradeExp(getUpgradeExp(level, exp));
//        bean.setNextLevelExp(getNextLevelExp(level));
//
//        //redis cache
//        fungoCacheMember.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_RANKS_LEVEL + loginId, "", bean);
//
//        return ResultDto.success(bean);
//    }
//
//    public int getUpgradeExp(int curLv, int curtExp) {
//
//        if (curLv == 1) {
//            return 11 - curtExp;
//        } else if (curLv == 2) {
//            return 51 - curtExp;
//        } else if (curLv == 3) {
//            return 101 - curtExp;
//        } else if (curLv == 4) {
//            return 201 - curtExp;
//        } else if (curLv == 5) {
//            return 401 - curtExp;
//        } else if (curLv == 6) {
//            return 601 - curtExp;
//        } else if (curLv == 7) {
//            return 1001 - curtExp;
//        } else if (curLv == 8) {
//            return 2001 - curtExp;
//        } else if (curLv == 9) {
//            return 3001 - curtExp;
//        } else if (curLv == 10) {
//            return 5001 - curtExp;
//        } else if (curLv == 11) {
//            return 10001 - curtExp;
//        }
//
//        return 0;
//
//    }
//
//    public int getNextLevelExp(int curLv) {
//
//        if (curLv == 1) {
//            return 11;
//        } else if (curLv == 2) {
//            return 51;
//        } else if (curLv == 3) {
//            return 101;
//        } else if (curLv == 4) {
//            return 201;
//        } else if (curLv == 5) {
//            return 401;
//        } else if (curLv == 6) {
//            return 601;
//        } else if (curLv == 7) {
//            return 1001;
//        } else if (curLv == 8) {
//            return 2001;
//        } else if (curLv == 9) {
//            return 3001;
//        } else if (curLv == 10) {
//            return 5001;
//        } else if (curLv == 11) {
//            return 10001;
//        }
//
//        return 0;
//
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public FungoPageResultDto<MyCommentBean> getMyComments(String loginId, InputPageDto input) {
//        FungoPageResultDto<MyCommentBean> re = null;
//
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_COMMENTS;
//        String keySuffix = loginId + JSON.toJSONString(input);
//        re = (FungoPageResultDto<MyCommentBean>) fungoCacheArticle.getIndexCache(keyPrefix, keySuffix);
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
//
//        re = new FungoPageResultDto<MyCommentBean>();
//        Page<CommentBean> page = new Page<>(input.getPage(), input.getLimit());
//        List<CommentBean> all = memberDao.getAllComments(page, loginId);
//        List<MyCommentBean> blist = new ArrayList<>();
//        for (CommentBean c : all) {
//            //文章评论 心情评论 二级评论
//            MyCommentBean bean = new MyCommentBean();
//
//            String content = c.getContent();
//            if (StringUtils.isNotBlank(content)) {
//                content = FilterEmojiUtil.decodeEmoji(content);
//                bean.setCommentConetnt(content);
//            }
//
//            bean.setCommentId(c.getId());
//            bean.setCommentType(c.getType());
//            bean.setTargetType(c.getTargetType());
//            bean.setTargetId(c.getTargetId());
//            bean.setTargetType(c.getTargetType());
//            bean.setUpdatedAt(DateTools.fmtDate(c.getUpdatedAt()));
//
//            //回复二级回复
//            if (!CommonUtil.isNull(c.getReplyToId()) && !CommonUtil.isNull(c.getReplyContentId())) {
//                bean.setReplyToId(c.getReplyToId());
//                Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", c.getReplyToId()));
//                if (m != null) {
//                    bean.setReplyToName(m.getUserName());
//                }
//
//                Reply reply = replyService.selectById(c.getReplyContentId());
//                if (reply != null) {
//
//                    String replyContent = reply.getContent();
//
//                    if (StringUtils.isNotBlank(replyContent)) {
//                        replyContent = FilterEmojiUtil.decodeEmoji(replyContent);
//                        bean.setTargetConetnt(replyContent);
//                    }
//
//                    bean.setReplyToConentId(c.getReplyContentId());
//                }
//                blist.add(bean);
//                continue;
//            }
//            if (c.getTargetType() == 1) {// 1帖子 2心情 5帖子评论 6游戏评论 8心情评论
//                CmmPost post = postService.selectOne(Condition.create().setSqlSelect("id,content,title,video").eq("id", c.getTargetId()));
//                if (post != null) {
//
//                    String title = CommonUtils.filterWord(post.getTitle());
//                    if (StringUtils.isNotBlank(title)) {
//                        title = FilterEmojiUtil.decodeEmoji(title);
//                        bean.setTargetConetnt(title);
//                    }
//
//                    if (!CommonUtil.isNull(post.getVideo())) {
//                        bean.setVideo(post.getVideo());
//                    }
//                } else {
//                    bean.setTargetConetnt("该文章已删除");
//                }
//            } else if (c.getTargetType() == 2) {
//                MooMood mood = moodService.selectOne(Condition.create().setSqlSelect("id,content").eq("id", c.getTargetId()));
//                if (mood != null) {
//
//                    String contentMood = CommonUtils.filterWord(CommonUtils.reduceString(mood.getContent(), 50));
//                    if (StringUtils.isNotBlank(contentMood)) {
//                        contentMood = FilterEmojiUtil.decodeEmoji(contentMood);
//                        bean.setTargetConetnt(contentMood);
//                    }
//// if(!CommonUtil.isNull(mood.getVideo())) {
////						bean.setVideo(mood.getVideo());
////					}
//                } else {
//                    bean.setTargetConetnt("该心情已删除");
//                }
//            } else if (c.getTargetType() == 5) {
//                CmmComment comment = commentService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
//                if (comment != null) {
//
//                    String contentCmmComment = CommonUtils.filterWord(CommonUtils.reduceString(comment.getContent(), 50));
//                    if (StringUtils.isNotBlank(contentCmmComment)) {
//                        contentCmmComment = FilterEmojiUtil.decodeEmoji(contentCmmComment);
//                        bean.setTargetConetnt(contentCmmComment);
//                    }
//
//                    bean.setReplyToId(comment.getMemberId());
//                    Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", comment.getMemberId()));
//                    if (m != null) {
//                        bean.setReplyToName(m.getUserName());
//                    }
//                }
//            } else if (c.getTargetType() == 6) {
//                GameEvaluation evaluation = evaluationService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
//                if (evaluation != null) {
//
//                    String contentGameEval = CommonUtils.filterWord(CommonUtils.reduceString(evaluation.getContent(), 50));
//                    if (StringUtils.isNotBlank(contentGameEval)) {
//                        contentGameEval = FilterEmojiUtil.decodeEmoji(contentGameEval);
//                        bean.setTargetConetnt(contentGameEval);
//                    }
//
//                    bean.setReplyToId(evaluation.getMemberId());
//                    Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", evaluation.getMemberId()));
//                    if (m != null) {
//                        bean.setReplyToName(m.getUserName());
//                    }
//                }
//            } else if (c.getTargetType() == 8) {
//                MooMessage message = mooMessageService.selectOne(Condition.create().setSqlSelect("id,content,member_id").eq("id", c.getTargetId()));
//                if (message != null) {
//
//                    String contentMoodMsg = CommonUtils.filterWord(CommonUtils.reduceString(message.getContent(), 50));
//                    if (StringUtils.isNotBlank(contentMoodMsg)) {
//                        contentMoodMsg = FilterEmojiUtil.decodeEmoji(contentMoodMsg);
//                        bean.setTargetConetnt(contentMoodMsg);
//                    }
//
//                    bean.setReplyToId(message.getMemberId());
//                    Member m = memberService.selectOne(Condition.create().setSqlSelect("id,user_name").eq("id", message.getMemberId()));
//                    if (m != null) {
//                        bean.setReplyToName(m.getUserName());
//                    }
//                }
//            }
//            blist.add(bean);
//
//        }
//
//        PageTools.pageToResultDto(re, page);
//        re.setData(blist);
//
//        //redis cache
//        fungoCacheArticle.excIndexCache(true, keyPrefix, keySuffix, re);
//
//        return re;
//    }
//
//    /**
//     * 初始化Fun身份证
//     * @throws Exception
//     */
//    @Override
//    public void initRank() throws Exception {
//        //获取所有用户
//        List<Member> memberList = memberService.selectList(Condition.create().setSqlSelect("id,user_name,created_at"));
//        //IncentRuleRank selectById = rankRuleService.selectById(24);
//
//        ObjectMapper mapper = new ObjectMapper();
//        System.out.println("-------------------------正在进行数据初始化！");
//        for (Member member : memberList) {
//            scoreLogService.updateRanked(member.getId(), mapper, 24, member.getCreatedAt());
//        }
//        System.out.println("-------------------------初始化完毕！");
//    }
//
//
//    @Override
//    public String getLevelRankUrl(int level, List<IncentRuleRank> levelRankList) {
//        String url = "";
//        for (IncentRuleRank ruleRank : levelRankList) {
//            if (ruleRank.getRankIdt() == level) {
//                url = ruleRank.getRankImgs();
//                break;
//            }
//        }
//
//        return url;
//    }


}
