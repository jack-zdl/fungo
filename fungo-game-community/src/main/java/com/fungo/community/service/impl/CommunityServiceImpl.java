package com.fungo.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fungo.community.service.ICommunityService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CommunityInputPageDto;
import com.game.common.dto.community.CommunityMember;
import com.game.common.dto.community.CommunityOut;
import com.game.common.dto.community.CommunityOutPageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class CommunityServiceImpl implements ICommunityService {


    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityServiceImpl.class);

//    @Autowired
//    private CmmCommunityService communityService;
//
//    @Autowired
//    private MemberService menberService;
//
//    @Autowired
//    private BasActionService actionService;
//
//    @Autowired
//    private CmmPostService postService;
//    @Autowired
//    private CmmCommunityDao communityDao;
//
//    //@Autowired
//    //private GameService gameService;
//
//    @Autowired
//    private IncentRuleRankService rankRuleService;
//    @Autowired
//    private IncentRankedService rankedService;
//
//
//    @Autowired
//    private IUserService userService;
//    @Autowired
//    private MemberFollowerService followService;
//
//    @Autowired
//    private MemberDao memberDao;
//
//
//    @Autowired
//    private FungoCacheCommunity fungoCacheCommunity;

    @Override
    public ResultDto<CommunityOut> getCommunityDetail(String communityId, String userId)
            throws Exception {

        CommunityOut out = null;
//
//        //from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_DETAIL + communityId;
//        String keySuffix = userId;
//        out =  (CommunityOut)fungoCacheCommunity.getIndexCache(keyPrefix, keySuffix);
//
//        if (null != out){
//            return ResultDto.success(out);
//        }
//
//
//        if (communityId == null || communityService.selectById(communityId) == null) {
//            return ResultDto.error("241", "找不到指定社区");
//        }
//        Wrapper<CmmCommunity> wrapper = new EntityWrapper<CmmCommunity>().eq("id", communityId).eq("state", 1);
//
//        CmmCommunity community = communityService.selectOne(wrapper);
//        if (community == null) {
//            return ResultDto.success();
//        }
//
//        out = new CommunityOut();
//
//        out.setObjectId(community.getId());
//        out.setName(community.getName());
//        out.setCover_image(community.getCoverImage());
//        out.setIcon(community.getIcon());
//        out.setIntro(community.getIntro());
//        out.setLink_game(community.getGameId());
//        out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
//        out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));
//        out.setState(community.getState());
//        out.setType(community.getType());
//
////        int followNum = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", communityId).eq("target_type", 4).and("state = 0"));
//        int comment_num = communityDao.getCommentNumOfCommunity(communityId);
//        //社区热度
//        out.setHot_value(community.getFolloweeNum() + community.getPostNum() + comment_num);
//
//        out.setFollowee_num(community.getFolloweeNum());
//
//        if (menberService.selectById(userId) != null) {// 查询是否关注
//            int count = actionService.selectCount(new EntityWrapper<BasAction>().eq("member_id", userId)
//                    .eq("type", 5).eq("target_id", communityId).eq("target_type", 4).and("state = 0"));
//            out.setIs_followed(count == 0 ? false : true);
//        }
//
//        //置顶文章
//        List<CmmPost> plist = postService.selectList(Condition.create().setSqlSelect("id,title,member_id,video").eq("community_id", communityId).eq("type", 3).ne("state", -1));
//        List<Map<String, String>> topicPosts = new ArrayList<>();
//        for (CmmPost p : plist) {
//            Map<String, String> map = new HashMap<>();
//            map.put("objectId", p.getId());
//            map.put("title", p.getTitle());
//            map.put("video", p.getVideo());
//            topicPosts.add(map);
//        }
////		CmmPost selectOne = postService.selectOne(Condition.create().setSqlSelect("id,title,member_id,video").eq("community_id", communityId).orderBy("created_at", false).last("limit 2"));
////		Map<String,String> mapx = new HashMap<>();
////		mapx.put("objectId",selectOne.getId());
////		mapx.put("title",selectOne.getTitle());
////		mapx.put("video", selectOne.getVideo());
////		topicPosts.add(mapx);
//        out.setTopicPosts(topicPosts);
//
//        Map<String, Object> map = new HashMap<>();
//        Page page = new Page<>(1, 6);
//        //社区玩家榜 前6位
//        List<Map<String, Object>> userList = new ArrayList<>();
//        if (community != null) {
//            map.put("communityId", community.getId());
//            if (!CommonUtil.isNull(community.getGameId())) {
//                map.put("gameId", community.getGameId());
//            }
//            List<MemberPulishFromCommunity> mlist = communityDao.getMemberOrder(page, map);
//            ObjectMapper mapper = new ObjectMapper();
//            for (MemberPulishFromCommunity m : mlist) {
//                Map<String, Object> mp = new HashMap<>();
//                mp.put("objectId", m.getMemberId());
//                Member member = menberService.selectById(m.getMemberId());
//                if (member != null) {
//                    mp.put("avatar", member.getAvatar());
//                }
//
//                IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", member.getId()).eq("rank_type", 2));
//                if (ranked != null) {
//                    IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
//                    if (rank != null) {
//                        String rankinf = rank.getRankImgs();
//                        ArrayList<HashMap<String, Object>> infolist = mapper.readValue(rankinf, ArrayList.class);
//                        mp.put("statusImg", infolist);
//                    } else {
//                        mp.put("statusImg", new ArrayList<>());
//                    }
//                } else {
//                    mp.put("statusImg", new ArrayList<>());
//                }
//                userList.add(mp);
//            }
//        }
//        out.setEliteMembers(userList);
//        //社区关注数
//        int c = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", communityId).eq("target_type", 4).and("state = 0"));
//        out.setMemberNum(c);
//
//        //redis cache
//        fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, out);
//
        return ResultDto.success(out);
    }

    @SuppressWarnings("unchecked")
    @Override
    public FungoPageResultDto<CommunityOutPageDto> getCmmCommunityList(String userId, CommunityInputPageDto communityInputPageDto) {

        FungoPageResultDto<CommunityOutPageDto> re = null;
//        //from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_LIST;
//        String keySuffix = userId + JSON.toJSONString(communityInputPageDto);
//
//        re = (FungoPageResultDto<CommunityOutPageDto>) fungoCacheCommunity.getIndexCache(keyPrefix, keySuffix);
//        if (null != re) {
//            return re;
//        }
//
//        String filter = communityInputPageDto.getFilter();
//        String keyword = communityInputPageDto.getKey_word();
//
//        int limit = communityInputPageDto.getLimit();
//        int page = communityInputPageDto.getPage();
//        int sort = communityInputPageDto.getSort();
//
//        List<CmmCommunity> communityList = new ArrayList<>();
//        Wrapper<CmmCommunity> entityWrapper = new EntityWrapper<CmmCommunity>();
//        entityWrapper.and("state = {0}", 1);
//
//        // 关键字
//        if (keyword != null && !keyword.equals("")) {
//            keyword = keyword.replace(" ", "");
//            entityWrapper = entityWrapper.like("name", keyword);
//        }
//
//
//        re = new FungoPageResultDto<CommunityOutPageDto>();
//        List<CommunityOutPageDto> dataList = new ArrayList<>();
//        re.setData(dataList);
//
//        // 过滤
//        Member user = menberService.selectById(userId);
//        if (filter.equals("official")) {
//            //官方社区
//            entityWrapper = entityWrapper.eq("type", 1);
//        } else if (filter.equals("game")) {
//            //普通社区
//            entityWrapper = entityWrapper.eq("type", 0);
//        } else if (filter.equals("mine")) {//我关注的社区
//            if (user == null) {
//                return FungoPageResultDto.error("1", "找不到用户");
//            }
//            List<BasAction> actionList = actionService.selectList(Condition.create().setSqlSelect("target_id")
//                    .eq("member_id", userId).eq("type", 5).eq("target_type", 4).and("state = 0"));
////			if(actionList == null || actionList.size() == 0) {
////				return FungoPageResultDto.error("241","没有找到用户关注的社区");
////			}
//
//            List<String> communityIdList = new ArrayList<>();
//            Page<CmmCommunity> cmmPage = new Page<CmmCommunity>();
//
//            if (actionList != null && actionList.size() > 0) {
//                for (BasAction action : actionList) {
//                    if (!CommonUtil.isNull(action.getTargetId())) {
//                        communityIdList.add(action.getTargetId());
//                    }
//                }
//                cmmPage = communityService
//                        .selectPage(new Page<>(page, limit), Condition.create()
//                                .where("state != {0}", -1).in("id", communityIdList).orderBy("created_at", false));
//                communityList = cmmPage.getRecords();
//            }
////			if(communityList == null || communityList.size() == 0) {
////				return FungoPageResultDto.error("241","没有找到用户关注的社区");
////			}
//            for (CmmCommunity community : communityList) {
//                CommunityOutPageDto out = new CommunityOutPageDto();
//                out.setObjectId(community.getId());
//                out.setName(community.getName());
//                out.setIntro(community.getIntro());
//                out.setIcon(community.getIcon());
//                out.setIs_followed(true);
//
//                //社区文章评论数
//                int commentNum = communityDao.getCommentNumOfCommunity(community.getId());
//                //社区热度
//                out.setHot_value(community.getFolloweeNum() + community.getPostNum() + commentNum);
//                out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
//                out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));
//                out.setType(community.getType());
//                dataList.add(out);
//            }
//
//            PageTools.pageToResultDto(re, cmmPage);
//
//            //redis cache
//            fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, re);

//            return re;
//        }
//        // sort
//        if (sort == 4) {//排序 热度值 关注数+帖子数
//            entityWrapper = entityWrapper.orderBy("(followee_num+post_num)", false);
//        } else {
//            entityWrapper.orderBy("(followee_num+post_num)", false);
//        }
//        Page<CmmCommunity> cmmPage = communityService
//                .selectPage(new Page<>(page, limit), entityWrapper);
//        communityList = cmmPage.getRecords();
//
//        //封装返回数据
//        for (CmmCommunity community : communityList) {
//            CommunityOutPageDto out = new CommunityOutPageDto();
//            out.setObjectId(community.getId());
//            out.setName(community.getName());
//            out.setIcon(community.getIcon());
//            out.setIntro(community.getIntro());
//            out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
//            out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));
//
//            int commentNum = communityDao.getCommentNumOfCommunity(community.getId());
//
//            out.setHot_value(community.getFolloweeNum() + community.getPostNum() + commentNum);
//            out.setType(community.getType());
//
//            if (user != null) {//是否关注
//                int selectCount = actionService.selectCount(new EntityWrapper<BasAction>().eq("target_id", community.getId())
//                        .eq("target_type", 4).eq("type", 5).eq("member_id", userId).and("state = 0"));
//                if (selectCount != 0) {
//                    out.setIs_followed(true);
//                } else {
//                    out.setIs_followed(false);
//                }
//            }
//            dataList.add(out);
//        }
//        PageTools.pageToResultDto(re, cmmPage);
//
//        //redis cache
//        fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }

    @Override
    public FungoPageResultDto<CommunityMember> memberList(String userId, CommunityInputPageDto input) {
        FungoPageResultDto<CommunityMember> re = new FungoPageResultDto<CommunityMember>();

//        CmmCommunity community = communityService.selectById(input.getCommunityId());
//        Map<String, String> map = new HashMap<>();
//        Page page = new Page<>(input.getPage(), input.getLimit());
//        List<CommunityMember> userList = new ArrayList<>();
//
//        if (community != null) {
//            //查找条件 map
//            map.put("communityId", input.getCommunityId());
//
//            if (!CommonUtil.isNull(community.getGameId())) {//如果是游戏社区,添加游戏id
//                map.put("gameId", community.getGameId());
//            }
//            //查找玩家榜
//            List<MemberPulishFromCommunity> mlist = communityDao.getMemberOrder(page, map);
//
//            for (MemberPulishFromCommunity m : mlist) {
//                CommunityMember c = new CommunityMember();
//                c.setAuthorBean(userService.getAuthor(m.getMemberId()));
//                c.setMerits(m.getEvaNum() + m.getCommentNum() + m.getPostNum());
//                if (userId.equals(m.getMemberId())) {
//                    c.setYourself(true);
//                } else {
//                    MemberFollower follower = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", userId).eq("follower_id", m.getMemberId()).andNew("state = {0}", 1).or("state = {0}", 2));
//                    if (follower != null) {
//                        c.setFollowed(true);
//                    }
//                }
//
//                userList.add(c);
//            }
//        }
//        PageTools.pageToResultDto(re, page);
//        re.setData(userList);

        return re;
    }


    //@Cacheable(cacheNames = {FunGoGameConsts.CACHE_EH_NAME}, key = "'" + FunGoGameConsts.CACHE_EH_KEY_PRE_MEMBER + "_recommendMembers'+ #currentMb_id")
//    @Override
//    public List<Member> getRecomMembers(int limit, String currentMb_id) {
//
//        List<Member> recommendedMbsList = new ArrayList<>();
//
//        //查询当前登录用户关注的所有用户
//        List<Member> watchMebmberList = null;
//        StringBuffer mbWatchedMbsWithSql = new StringBuffer();
//
//        List<String> wathMbsSet = new ArrayList<String>();
//
//        if (StringUtils.isNotBlank(currentMb_id)) {
//            watchMebmberList = this.getWatchMebmber(0, currentMb_id);
//            if (null != watchMebmberList && !watchMebmberList.isEmpty()) {
//                for (Member member : watchMebmberList) {
//                    member.setFollowed(true);
//                    wathMbsSet.add(member.getId());
//                }
//            }
//        }
//        LOGGER.info("查询当前登录用户关注的所有用户:{}", wathMbsSet.toString());
//
//        //先获取官方推荐和符合条件推荐用户
//        List<Member> ml1 = getRecommeMembers(limit, currentMb_id, wathMbsSet);
//
//        int recommeMbs = 0;
//        if (null != ml1 && !ml1.isEmpty()) {
//            recommeMbs = ml1.size();
//            for (Member member : ml1) {
//                member.setFollowed(false);
//            }
//        }
//
//        if (null != ml1 && !ml1.isEmpty()) {
//            recommendedMbsList.addAll(ml1);
//            ml1.clear();
//        }
//
//        //及时刷新出推荐用户
//        //若推荐用户不足limit数量，查询该用户已关注用户
//        limit -= recommeMbs;
//        if (limit > 0 && null != watchMebmberList && !watchMebmberList.isEmpty()) {
//            for (int i = 0; i < limit; i++) {
//                if (i < watchMebmberList.size()) {
//                    recommendedMbsList.add(watchMebmberList.get(i));
//                }
//            }
//
//        }
//        LOGGER.info("===========recommendedMbsList:{}", recommendedMbsList);
//        return recommendedMbsList;
//
//    }

    /**
     * 查询当前登录用户所关注的其他用户
     * @param limit
     * @param currentMb_id
     * @return
     */
//    private List<Member> getWatchMebmber(int limit, String currentMb_id) {
//
//        EntityWrapper actionEntityWrapper = new EntityWrapper<BasAction>();
//        actionEntityWrapper.setSqlSelect("target_id");
//
//        //target_type 0 关注用户
//        actionEntityWrapper.eq("state", "0").eq("type", 5).eq("member_id", currentMb_id).eq("target_type", 0);
//        actionEntityWrapper.orderBy("created_at", false);
//        if (limit > 0) {
//            actionEntityWrapper.last("limit " + limit);
//        }
//        List<BasAction> watchMebmberIdsList = actionService.selectList(actionEntityWrapper);
//
//        if (null != watchMebmberIdsList && !watchMebmberIdsList.isEmpty()) {
//            StringBuffer mbIds = new StringBuffer();
//            for (BasAction basAction : watchMebmberIdsList) {
//                if (null != basAction) {
//                    mbIds = mbIds.append(basAction.getTargetId());
//                    mbIds = mbIds.append(",");
//                }
//            }
//            mbIds.deleteCharAt(mbIds.length() - 1);
//
//            //查询出符合推荐条件用户的详情数据
//            if (mbIds.length() > 0) {
//                List<Member> watchMebmberList = menberService.selectList(new EntityWrapper<Member>().in("id", mbIds.toString()).eq("state", 0));
//                return watchMebmberList;
//            }
//        }
//        return null;
//    }


    /**
     * 获取官方推荐和符合条件推荐用户
     *       玩家推荐规则：
     *          规则一 玩家推荐：
     *           发布文章数 大于10 或者 游戏评论数大于14
     *          规则二 玩家已关注列表替换规则：
     *           1.显示已经关注用户数量：最大10个
     *           2.若有新的推荐用户，且未关注，替换到玩家已关注列表的前面。且 该类别数量恒定10人
     * @param limit
     * @param currentMb_id 当前登录用户ID
     * @param mbWatchedMbs 当前登录用户，已经关注的用户IDS
     * @return
     */
//    private List<Member> getRecommeMembers(int limit, String currentMb_id, List<String> wathMbsSet) {
//        //查官方推荐和符合条件推荐用户
//        //推荐用户最大数
//        long limitSize = 10L;
//        if (limit > 0) {
//            limitSize = limit;
//        }
//        //发布文章数10条
//        long sendArticles = 10L;
//        //发布评论数14条
//        long sendComments = 14L;
//
//
//        /*
//         * 1.找出官方推荐玩家 (数据库状态)
//         * 2.发布文章数>10或游戏评论>14 （精选文章/评论 *5）
//         * 3.合并去重
//         */
//        //fix: 从用户中查询出所有被推荐的用户，且状态是0(正常)  [by mxf 2019-01-09]
//        EntityWrapper memberSqlWrapper = new EntityWrapper<Member>();
//        memberSqlWrapper.eq("type", 2).eq("state", 0);
//
//        //去除当前登录用户和已经关注用户
//        if (StringUtils.isNotBlank(currentMb_id) || !wathMbsSet.isEmpty()) {
//            wathMbsSet.add(currentMb_id);
//            memberSqlWrapper.notIn("id", wathMbsSet);
//        }
//        //
//
//        memberSqlWrapper.orderBy("sort", false);
//        memberSqlWrapper.last("limit " + limitSize);
//
//        List<Member> ml1 = menberService.selectList(memberSqlWrapper);
//
//        if (null == ml1 || ml1.isEmpty() || ml1.size() < limitSize) {
//
//            //官方推荐用户id
//            List<String> reIds = new ArrayList<>();
//
//            for (Member member : ml1) {
//                reIds.add(member.getId());
//            }
//
//            //fix:查询 符合 发布文章数>10或游戏评论>14 （精选文章/评论 *5)的用户 的业务逻辑修改 [by mxf 2019-01-09]
//            //条件用户
//            //List<HashMap<String, Object>> members = memberDao.getRecommendMembers();
//            Set<String> members = new HashSet<>();
//
//
//            //1.先查询发布文章数大于10条的用户
//            List<String> sendArticleMembers = memberDao.getRecommendMembersFromCmmPost(sendArticles,
//                    limitSize, wathMbsSet);
//            LOGGER.info("查询发布文章数大于10条的用户:{}", sendArticleMembers.toString());
//
//            if (null != sendArticleMembers && !sendArticleMembers.isEmpty()) {
//                members.addAll(sendArticleMembers);
//                sendArticleMembers.clear();
//            }
//
//            //2.再查询发布游戏评论>大于14条的，前10名用户
//            List<String> sendCommentMembers = memberDao.getRecommendMembersFromEvaluation(sendComments,
//                    limitSize, wathMbsSet);
//
//            LOGGER.info("查询发布游戏评论>大于14条的，前10名用户:{}", sendCommentMembers.toString());
//            if (null != sendCommentMembers && !sendCommentMembers.isEmpty()) {
//                members.addAll(sendCommentMembers);
//                sendCommentMembers.clear();
//            }
//
//            int ormCount = 0;
//            if (null == ml1) {
//                ml1 = new ArrayList<Member>();
//            } else {
//                ormCount = ml1.size();
//            }
//
//            //所有符合推荐条件用户id
//            List<String> memberIds = new ArrayList<>();
//
//            //查询未包含在官方推荐中且符合推荐条件的用户id
//            for (String mb_id : members) {
//                int remMebs = ormCount + memberIds.size();
//                if (remMebs == limitSize) {
//                    break;
//                }
//                if (!reIds.contains(mb_id)) {
//                    memberIds.add(mb_id);
//                }
//            }
//
//            //查询出符合推荐条件用户的详情数据
//            if (memberIds.size() > 0) {
//                List<Member> recommendList = menberService.selectList(new EntityWrapper<Member>().in("id", memberIds).eq("state", 0));
//                ml1.addAll(recommendList);
//            }
//        }
//
//        //---end
//        return ml1;
//    }

    //-------
}
