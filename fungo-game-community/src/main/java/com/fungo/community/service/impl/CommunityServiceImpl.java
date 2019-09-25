package com.fungo.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.mapper.CmmCommunityDao;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.facede.GameFacedeService;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.service.ICommunityService;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.*;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.IncentRuleRankDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.repo.cache.facade.FungoCacheCommunity;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class CommunityServiceImpl implements ICommunityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommunityServiceImpl.class);

    @Autowired
    private CmmCommunityDaoService communityService;

    @Autowired
    private CmmPostDaoService postService;

    @Autowired
    private CmmCommunityDao communityDao;

    @Autowired
    private FungoCacheCommunity fungoCacheCommunity;

    //依赖系统和用户微服务
    @Autowired(required = false)
    private SystemFacedeService systemFacedeService;

    //依赖游戏微服务
    @Autowired(required = false)
    private GameFacedeService gameFacedeService;




    @Override
    public ResultDto<CommunityOut> getCommunityDetail(String communityId, String userId)
            throws Exception {
        CommunityOut out = null;
        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_DETAIL + communityId;
        String keySuffix = userId;
        out = (CommunityOut) fungoCacheCommunity.getIndexCache(keyPrefix, keySuffix);
        if (null != out) {
            return ResultDto.success(out);
        }
        if (communityId == null || communityService.selectById(communityId) == null) {
            return ResultDto.error("241", "找不到指定社区");
        }
        Wrapper<CmmCommunity> wrapper = new EntityWrapper<CmmCommunity>().eq("id", communityId).eq("state", 1);
        CmmCommunity community = communityService.selectOne(wrapper);
        if (community == null) {
            return ResultDto.success();
        }

        out = new CommunityOut();

        out.setObjectId(community.getId());
        out.setName(community.getName());
        out.setCover_image(community.getCoverImage());
        out.setIcon(community.getIcon());
        out.setIntro(community.getIntro());
        out.setLink_game(community.getGameId());
        out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
        out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));
        out.setState(community.getState());
        out.setType(community.getType());
        out.setPostNum( community.getPostNum());

//        int followNum = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", communityId).eq("target_type", 4).and("state = 0"));
        int comment_num = communityDao.getCommentNumOfCommunity(communityId);
        //社区热度
        out.setHot_value(community.getFolloweeNum() + community.getPostNum() + comment_num);

        out.setFollowee_num(community.getFolloweeNum());

        //!fixme 查询是否关注 根据用户id查询用户详情
        List<String> idsList = new ArrayList<String>();
        idsList.add(userId);

        ResultDto<List<MemberDto>> mbsListResultDto = null;
        try {
            mbsListResultDto = systemFacedeService.listMembersByids(idsList, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        MemberDto memberDto = null;
        if (null != mbsListResultDto) {
            final List<MemberDto> memberDtoList = mbsListResultDto.getData();
            if (null != memberDtoList && !memberDtoList.isEmpty()) {
                memberDto = memberDtoList.get(0);
            }
        }
        //历史代码
        //if (menberService.selectById(userId) != null) {
        if (null != memberDto) {
            //!fixme 从动作表中 根据用户id,类型，目标id，目前类型，状态等查询 社区关注数量
            //int count = actionService.selectCount(new EntityWrapper<BasAction>().eq("member_id", userId)
            // .eq("type", 5).eq("target_id", communityId).eq("target_type", 4).and("state = 0"));

            BasActionDto basActionDto = new BasActionDto();

            basActionDto.setMemberId(userId);
            basActionDto.setType(5);
            basActionDto.setState(0);
            basActionDto.setTargetType(4);
            basActionDto.setTargetId(communityId);

            int count = 0;

            try {
                ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                if (null != resultDto) {
                    count = resultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


            out.setIs_followed(count == 0 ? false : true);
        }

        //置顶文章
        List<CmmPost> plist = postService.selectList(Condition.create().setSqlSelect("id,title,member_id as memberId,video").eq("community_id", communityId).eq("type", 3).ne("state", -1));
        List<Map<String, String>> topicPosts = new ArrayList<>();
        for (CmmPost p : plist) {
            Map<String, String> map = new HashMap<>();
            map.put("objectId", p.getId());
            map.put("title", p.getTitle());
            map.put("video", p.getVideo());
            topicPosts.add(map);
        }
//		CmmPost selectOne = postService.selectOne(Condition.create().setSqlSelect("id,title,member_id,video").eq("community_id", communityId).orderBy("created_at", false).last("limit 2"));
//		Map<String,String> mapx = new HashMap<>();
//		mapx.put("objectId",selectOne.getId());
//		mapx.put("title",selectOne.getTitle());
//		mapx.put("video", selectOne.getVideo());
//		topicPosts.add(mapx);
        out.setTopicPosts(topicPosts);

        Map<String, Object> map = new HashMap<>();
        Page page = new Page<>(1, 6);
        //社区玩家榜 前6位
        List<Map<String, Object>> userList = new ArrayList<>();
        if (community != null) {

            String game_id = community.getId();
            map.put("communityId", community.getId());
            if (!CommonUtil.isNull(community.getGameId())) {
                map.put("gameId", community.getGameId());

                game_id = community.getGameId();
            }

            //!fixme 从文章表、社区一级评论表 和 游戏评论表获取用户数量
            boolean isOrder = false;
            List<MemberPulishFromCommunity> memberPulishCountOrderList = new ArrayList<MemberPulishFromCommunity>();

            List<MemberPulishFromCommunity> pulishFromCommunityList = new ArrayList<MemberPulishFromCommunity>();

            //从文章表、社区一级评论表获取用户数量
            List<MemberPulishFromCommunity> mlist = communityDao.getMemberOrder(page, map);
            if (null != mlist && !mlist.isEmpty()) {
                pulishFromCommunityList.addAll(mlist);
            }
            //从游戏评论表获取用户数量
            ResultDto<List<MemberPulishFromCommunity>> gameMemberCtmRs = null;
            try {
                gameMemberCtmRs = gameFacedeService.getMemberOrder(game_id, null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (null != gameMemberCtmRs) {

                List<MemberPulishFromCommunity> pulishFromCmtList = gameMemberCtmRs.getData();

                if (null != pulishFromCmtList && !pulishFromCmtList.isEmpty()) {

                    if (null != mlist && !mlist.isEmpty()) {

                        isOrder = true;

                        //遍历 游戏评论用户
                        for (MemberPulishFromCommunity gamePulisher : pulishFromCmtList) {

                            String gameMemberId = gamePulisher.getMemberId();

                            //遍历文章和社区评论用户
                            for (MemberPulishFromCommunity postCmtPulisher : mlist) {

                                String postCmtMemberId = postCmtPulisher.getMemberId();
                                if (StringUtils.equalsIgnoreCase(gameMemberId, postCmtMemberId)) {

                                    //社区评论数
                                    postCmtPulisher.setCommentNum(postCmtPulisher.getCommentNum() + gamePulisher.getCommentNum());

                                    //文章评论数
                                    postCmtPulisher.setPostNum(postCmtPulisher.getPostNum() + gamePulisher.getPostNum());

                                    //平均评论数
                                    postCmtPulisher.setEvaNum(postCmtPulisher.getEvaNum() + gamePulisher.getEvaNum());
                                } else {
                                    pulishFromCommunityList.add(gamePulisher);
                                }
                            }
                        }

                        pulishFromCmtList.clear();

                    } else {
                        memberPulishCountOrderList.addAll(pulishFromCmtList);
                    }

                } else {
                    memberPulishCountOrderList.addAll(pulishFromCommunityList);
                }
            } else {
                memberPulishCountOrderList.addAll(pulishFromCommunityList);
            }


            //对所有用户按 CommentNum + PostNum + EvaNum ，从大到小排序 (冒泡排序)
            if (isOrder) {
                int pulisherSize = memberPulishCountOrderList.size();
                for (int i = 0; i < pulisherSize - 1; i++) {

                    for (int j = 0; j < pulisherSize - 1 - i; j++) {

                        MemberPulishFromCommunity pulishPre = memberPulishCountOrderList.get(j);
                        int pulishCountPre = pulishPre.getCommentNum() + pulishPre.getEvaNum() + pulishPre.getPostNum();

                        MemberPulishFromCommunity pulishNext = memberPulishCountOrderList.get(j + 1);
                        int pulishCountNext = pulishNext.getCommentNum() + pulishNext.getEvaNum() + pulishNext.getPostNum();

                        if (pulishCountPre < pulishCountNext) {

                            MemberPulishFromCommunity temp = pulishPre;

                            memberPulishCountOrderList.set(j, pulishNext);
                            memberPulishCountOrderList.set(j + 1, temp);

                        }
                    }
                }

                //清除
                mlist.clear();
            }

            ObjectMapper mapper = new ObjectMapper();
            for (MemberPulishFromCommunity m : memberPulishCountOrderList) {
                Map<String, Object> mp = new HashMap<>();
                mp.put("objectId", m.getMemberId());

                //!fixme 获取用户数据
                //Member member = menberService.selectById(m.getMemberId());

                List<String> mbIdsList = new ArrayList<String>();
                mbIdsList.add(m.getMemberId());

                ResultDto<List<MemberDto>> listMembersByids = null;
                try {
                    listMembersByids = systemFacedeService.listMembersByids(mbIdsList, null);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                MemberDto memberDtoPulish = null;
                if (null != listMembersByids) {
                    List<MemberDto> memberDtoList = listMembersByids.getData();
                    if (null != memberDtoList && !memberDtoList.isEmpty()) {
                        memberDtoPulish = memberDtoList.get(0);
                    }
                }

                if (memberDtoPulish != null) {

                    mp.put("avatar", memberDtoPulish.getAvatar());

                    //!fixme 根据用户id和用户权益(等级、身份、荣誉)类型，获取用户权益数据
                    //IncentRanked ranked = rankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", member.getId()).eq("rank_type", 2));

                    IncentRankedDto incentRankedDto = new IncentRankedDto();
                    incentRankedDto.setMbId(memberDtoPulish.getId());
                    incentRankedDto.setRankType(2);

                    IncentRankedDto mBIncentRankedDto = null;
                    try {
                        FungoPageResultDto<IncentRankedDto> incentRankedPageRs = systemFacedeService.getIncentRankedList(incentRankedDto);
                        if (null != incentRankedPageRs) {
                            List<IncentRankedDto> rankedDtoList = incentRankedPageRs.getData();
                            if (null != rankedDtoList && !rankedDtoList.isEmpty()) {
                                mBIncentRankedDto = rankedDtoList.get(0);
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (mBIncentRankedDto != null) {

                        //!fixme 获取权益规则
                        //IncentRuleRank rank = rankRuleService.selectById(ranked.getCurrentRankId());//最近获得
                        IncentRuleRankDto incentRuleRankDto = null;

                        try {
                            ResultDto<IncentRuleRankDto> IncentRuleRankResultDto = systemFacedeService.getIncentRuleRankById(String.valueOf(mBIncentRankedDto.getCurrentRankId().longValue()));

                            if (null != IncentRuleRankResultDto) {
                                incentRuleRankDto = IncentRuleRankResultDto.getData();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        if (incentRuleRankDto != null) {
                            String rankinf = incentRuleRankDto.getRankImgs();
                            ArrayList<HashMap<String, Object>> infolist = mapper.readValue(rankinf, ArrayList.class);
                            mp.put("statusImg", infolist);
                        } else {
                            mp.put("statusImg", new ArrayList<>());
                        }
                    } else {
                        mp.put("statusImg", new ArrayList<>());
                    }
                }
                userList.add(mp);
            }
        }
        out.setEliteMembers(userList);

        //!fixme 社区关注数
        //type 关注 | 5
        //target_type 帖子评论 | 5
        //state  状态 0 正常 | -1 删除
        //int c = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).eq("target_id", communityId).eq("target_type", 4).and("state = 0"));


        BasActionDto basActionDtoLike = new BasActionDto();

//        basActionDtoLike.setMemberId(userId);
        basActionDtoLike.setType(5);
        basActionDtoLike.setState(0);
        basActionDtoLike.setTargetId(communityId);
        basActionDtoLike.setTargetType(4);

        int focusCount = 0;
        try {
            ResultDto<Integer> resultDtoLike = systemFacedeService.countActionNum(basActionDtoLike);

            if (null != resultDtoLike) {
                focusCount = resultDtoLike.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        out.setMemberNum(focusCount);

        //redis cache
        fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, out);

        return ResultDto.success(out);
    }

    @SuppressWarnings("unchecked")
    @Override
    public FungoPageResultDto<CommunityOutPageDto> getCmmCommunityList(String userId, CommunityInputPageDto communityInputPageDto) {

        FungoPageResultDto<CommunityOutPageDto> re = null;
        //from redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_LIST;
        String keySuffix = userId + JSON.toJSONString(communityInputPageDto);

        re = (FungoPageResultDto<CommunityOutPageDto>) fungoCacheCommunity.getIndexCache(keyPrefix, keySuffix);
        if (null != re) {
            return re;
        }

        String filter = communityInputPageDto.getFilter();
        String keyword = communityInputPageDto.getKey_word();

        int limit = communityInputPageDto.getLimit();
        int page = communityInputPageDto.getPage();
        int sort = communityInputPageDto.getSort();

        List<CmmCommunity> communityList = new ArrayList<>();
        Wrapper<CmmCommunity> entityWrapper = new EntityWrapper<CmmCommunity>();
        entityWrapper.and("state = {0}", 1);

        // 关键字
        if (keyword != null && !keyword.equals("")) {
            keyword = keyword.replace(" ", "");
            entityWrapper = entityWrapper.like("name", keyword);
        }


        re = new FungoPageResultDto<CommunityOutPageDto>();
        List<CommunityOutPageDto> dataList = new ArrayList<>();
        re.setData(dataList);

        //!fixme 获取用户详情
        // Member user = menberService.selectById(userId);

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

        if (filter.equals("official")) {
            //官方社区
            entityWrapper = entityWrapper.eq("type", 1);
        } else if (filter.equals("game")) {
            //普通社区
            entityWrapper = entityWrapper.eq("type", 0);
        } else if (filter.equals("mine")) {//我关注的社区
            if (memberDto == null) {
                return FungoPageResultDto.error("1", "找不到用户");
            }

            //!fixme 根据用户id，动作类型，目前类型，状态获取目前id集合
            /*
            List<BasAction> actionList = actionService.selectList(Condition.create().setSqlSelect("target_id")
                    .eq("member_id", userId).eq("type", 5).eq("target_type", 4).and("state = 0"));
            */
//			if(actionList == null || actionList.size() == 0) {
//				return FungoPageResultDto.error("241","没有找到用户关注的社区");
//			}

            List<BasActionDto> actionList = null;

            BasActionDto basActionDto = new BasActionDto();

            basActionDto.setMemberId(userId);
            basActionDto.setType(5);
            basActionDto.setTargetType(4);
            basActionDto.setState(0);

            try {
                ResultDto<List<BasActionDto>> actionByConditionRs = systemFacedeService.listActionByCondition(basActionDto);
                if (null != actionByConditionRs) {
                    actionList = actionByConditionRs.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            List<String> communityIdList = new ArrayList<>();
            Page<CmmCommunity> cmmPage = new Page<CmmCommunity>();

            if (actionList != null && actionList.size() > 0) {
                for (BasActionDto action : actionList) {
                    if (!CommonUtil.isNull(action.getTargetId())) {
                        communityIdList.add(action.getTargetId());
                    }
                }
                cmmPage = communityService
                        .selectPage(new Page<>(page, limit), Condition.create()
                                .where("state != {0}", -1).in("id", communityIdList).orderBy("created_at", false));
                communityList = cmmPage.getRecords();
            }
//			if(communityList == null || communityList.size() == 0) {
//				return FungoPageResultDto.error("241","没有找到用户关注的社区");
//			}
            for (CmmCommunity community : communityList) {
                CommunityOutPageDto out = new CommunityOutPageDto();
                out.setObjectId(community.getId());
                out.setName(community.getName());
                out.setIntro(community.getIntro());
                out.setIcon(community.getIcon());
                out.setIs_followed(true);

                //社区文章评论数
                int commentNum = communityDao.getCommentNumOfCommunity(community.getId());
                //社区热度
                out.setHot_value(community.getFolloweeNum() + community.getPostNum() + commentNum);
                out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
                out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));
                out.setType(community.getType());
                dataList.add(out);
            }

            PageTools.pageToResultDto(re, cmmPage);

            //redis cache
            fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, re);

            return re;
        }
        // sort
        if (sort == 4) {//排序 热度值 关注数+帖子数
            entityWrapper = entityWrapper.orderBy("(followee_num+post_num)", false);
        } else {
            entityWrapper.orderBy("(followee_num+post_num)", false);
        }
        Page<CmmCommunity> cmmPage = communityService
                .selectPage(new Page<>(page, limit), entityWrapper);
        communityList = cmmPage.getRecords();

        //封装返回数据
        for (CmmCommunity community : communityList) {
            CommunityOutPageDto out = new CommunityOutPageDto();
            out.setObjectId(community.getId());
            out.setName(community.getName());
            out.setIcon(community.getIcon());
            out.setIntro(community.getIntro());
            out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
            out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));

            int commentNum = communityDao.getCommentNumOfCommunity(community.getId());

            out.setHot_value(community.getFolloweeNum() + community.getPostNum() + commentNum);
            out.setType(community.getType());

            if (memberDto != null) {

                //!fixme  是否关注
                /*
                int selectCount = actionService.selectCount(new EntityWrapper<BasAction>().eq("target_id", community.getId())
                        .eq("target_type", 4).eq("type", 5).eq("member_id", userId).and("state = 0"));
                */


                BasActionDto basActionDtoLike = new BasActionDto();

                basActionDtoLike.setMemberId(userId);
                basActionDtoLike.setType(5);
                basActionDtoLike.setState(0);
                basActionDtoLike.setTargetId(community.getId());
                basActionDtoLike.setTargetType(4);

                int selectCount = 0;
                try {
                    ResultDto<Integer> resultDtoLike = systemFacedeService.countActionNum(basActionDtoLike);

                    if (null != resultDtoLike) {
                        selectCount = resultDtoLike.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (selectCount != 0) {
                    out.setIs_followed(true);
                } else {
                    out.setIs_followed(false);
                }
            }
            dataList.add(out);
        }
        PageTools.pageToResultDto(re, cmmPage);

        //redis cache
        fungoCacheCommunity.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }


    @Override
    public FungoPageResultDto<CommunityMember> memberList(String userId, CommunityInputPageDto input) {
        FungoPageResultDto<CommunityMember> re = new FungoPageResultDto<CommunityMember>();

        CmmCommunity community = communityService.selectById(input.getCommunityId());
        Map<String, String> map = new HashMap<>();
        Page page = new Page<>(input.getPage(), input.getLimit());
        List<CommunityMember> userList = new ArrayList<>();

        if (community != null) {
            //查找条件 map
            map.put("communityId", input.getCommunityId());

            if (!CommonUtil.isNull(community.getGameId())) {//如果是游戏社区,添加游戏id
                map.put("gameId", community.getGameId());
            }
            //查找玩家榜
            List<MemberPulishFromCommunity> mlist = communityDao.getMemberOrder(page, map);

            for (MemberPulishFromCommunity m : mlist) {
                CommunityMember c = new CommunityMember();

                //!fixme 获取用户数据
                // c.setAuthorBean(userService.getAuthor(m.getMemberId()));
                try {
                    ResultDto<AuthorBean> authorBeanResultDto = systemFacedeService.getAuthor(m.getMemberId());
                    if (null != authorBeanResultDto) {
                        AuthorBean authorBean = authorBeanResultDto.getData();
                        c.setAuthorBean(authorBean);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }


                c.setMerits(m.getEvaNum() + m.getCommentNum() + m.getPostNum());
                if (userId.equals(m.getMemberId())) {
                    c.setYourself(true);
                } else {

                    //!fixme 获取用户的粉丝
                    //MemberFollower follower = followService.selectOne(new EntityWrapper<MemberFollower>().eq("member_id", userId).eq("follower_id", m.getMemberId()).andNew("state = {0}", 1).or("state = {0}", 2));

                    MemberFollowerDto memberFollowerDtoParam = new MemberFollowerDto();
                    memberFollowerDtoParam.setMemberId(userId);
                    memberFollowerDtoParam.setFollowerId(m.getMemberId());

                    MemberFollowerDto memberFollowerDtoData = null;

                    try {
                        ResultDto<MemberFollowerDto> followerDtoResultDto = systemFacedeService.getMemberFollower1(memberFollowerDtoParam);
                        if (null != followerDtoResultDto) {
                            memberFollowerDtoData = followerDtoResultDto.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                    if (memberFollowerDtoData != null) {
                        c.setFollowed(true);
                    }
                }

                userList.add(c);
            }
        }
        re.setData(userList);
        PageTools.pageToResultDto(re, page);


        return re;
    }


    @Override
    public List<MemberDto> getRecomMembers(int limit, String currentMb_id) {

        List<MemberDto> recommendedMbsList = new ArrayList<MemberDto>();

        //查询当前登录用户关注的所有用户
        List<MemberDto> watchMebmberList = null;
        StringBuffer mbWatchedMbsWithSql = new StringBuffer();

        List<String> wathMbsSet = new ArrayList<String>();

        if (StringUtils.isNotBlank(currentMb_id)) {

            //!fixme 关注用户List
            //watchMebmberList = this.getWatchMebmber(0, currentMb_id);

            ResultDto<List<MemberDto>> listWatchMebmberRs = systemFacedeService.listWatchMebmber(0, currentMb_id);
            if (null != listWatchMebmberRs) {
                watchMebmberList = listWatchMebmberRs.getData();
            }

            if (null != watchMebmberList && !watchMebmberList.isEmpty()) {
                for (MemberDto member : watchMebmberList) {
                    member.setFollowed(true);
                    wathMbsSet.add(member.getId());
                }
            }
        }
        LOGGER.info("查询当前登录用户关注的所有用户:{}", wathMbsSet.toString());

        //先获取官方推荐和符合条件推荐用户
        List<MemberDto> ml1 = getRecommeMembers(limit, currentMb_id, wathMbsSet);

        int recommeMbs = 0;
        if (null != ml1 && !ml1.isEmpty()) {
            recommeMbs = ml1.size();
            for (MemberDto member : ml1) {
                member.setFollowed(false);
            }
        }

        if (null != ml1 && !ml1.isEmpty()) {
            recommendedMbsList.addAll(ml1);
            ml1.clear();
        }

        //及时刷新出推荐用户
        //若推荐用户不足limit数量，查询该用户已关注用户
        limit -= recommeMbs;
        if (limit > 0 && null != watchMebmberList && !watchMebmberList.isEmpty()) {
            for (int i = 0; i < limit; i++) {
                if (i < watchMebmberList.size()) {
                    recommendedMbsList.add(watchMebmberList.get(i));
                }
            }

        }
        LOGGER.info("===========recommendedMbsList:{}", recommendedMbsList);
        return recommendedMbsList;

    }


    /**
     * 废弃--依赖系统用户微服务
     * 查询当前登录用户所关注的其他用户
     * @param limit
     * @param currentMb_id
     * @return
     */
    /*
    private List<MemberDto> getWatchMebmber(int limit, String currentMb_id) {

        EntityWrapper actionEntityWrapper = new EntityWrapper<BasAction>();
        actionEntityWrapper.setSqlSelect("target_id");

        //target_type 0 关注用户
        actionEntityWrapper.eq("state", "0").eq("type", 5).eq("member_id", currentMb_id).eq("target_type", 0);
        actionEntityWrapper.orderBy("created_at", false);
        if (limit > 0) {
            actionEntityWrapper.last("limit " + limit);
        }
        List<BasAction> watchMebmberIdsList = actionService.selectList(actionEntityWrapper);

        if (null != watchMebmberIdsList && !watchMebmberIdsList.isEmpty()) {
            StringBuffer mbIds = new StringBuffer();
            for (BasAction basAction : watchMebmberIdsList) {
                if (null != basAction) {
                    mbIds = mbIds.append(basAction.getTargetId());
                    mbIds = mbIds.append(",");
                }
            }
            mbIds.deleteCharAt(mbIds.length() - 1);

            //查询出符合推荐条件用户的详情数据
            if (mbIds.length() > 0) {
                List<Member> watchMebmberList = menberService.selectList(new EntityWrapper<Member>().in("id", mbIds.toString()).eq("state", 0));
                return watchMebmberList;
            }
        }
        return null;
    }
    */


    /**
     * 获取官方推荐和符合条件推荐用户
     * 玩家推荐规则：
     * 规则一 玩家推荐：
     * 发布文章数 大于10 或者 游戏评论数大于14
     * 规则二 玩家已关注列表替换规则：
     * 1.显示已经关注用户数量：最大10个
     * 2.若有新的推荐用户，且未关注，替换到玩家已关注列表的前面。且 该类别数量恒定10人
     *
     * @param limit
     * @param currentMb_id 当前登录用户ID
     * @param wathMbsSet   当前登录用户，已经关注的用户IDS
     * @return
     */
    private List<MemberDto> getRecommeMembers(int limit, String currentMb_id, List<String> wathMbsSet) {
        //查官方推荐和符合条件推荐用户
        //推荐用户最大数
        long limitSize = 10L;
        if (limit > 0) {
            limitSize = limit;
        }
        //发布文章数10条
        long sendArticles = 10L;
        //发布评论数14条
        long sendComments = 14L;



        /*
         * 1.找出官方推荐玩家 (数据库状态)
         * 2.发布文章数>10或游戏评论>14 （精选文章/评论 *5）
         * 3.合并去重
         */
        //--------依赖系统用户微服务----
        //fix: 从用户中查询出所有被推荐的用户，且状态是0(正常)  [by mxf 2019-01-09]
       /*
           EntityWrapper memberSqlWrapper = new EntityWrapper<Member>();
            memberSqlWrapper.eq("type", 2).eq("state", 0);

            //去除当前登录用户和已经关注用户
            if (StringUtils.isNotBlank(currentMb_id) || !wathMbsSet.isEmpty()) {
                wathMbsSet.add(currentMb_id);
                memberSqlWrapper.notIn("id", wathMbsSet);
            }

            memberSqlWrapper.orderBy("sort", false);
            memberSqlWrapper.last("limit " + limitSize);

            List<Member> ml1 = menberService.selectList(memberSqlWrapper);
       */

        List<MemberDto> ml1 = null;
        try {
            ResultDto<List<MemberDto>> mbListResultDto = systemFacedeService.listRecommendedMebmber(Integer.valueOf(limitSize + ""), currentMb_id, wathMbsSet);
            if (null != mbListResultDto) {
                ml1 = mbListResultDto.getData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (null == ml1) {
            ml1 = new ArrayList<>();
        }
        if (ml1.size() < limitSize) {

            //官方推荐用户id
            List<String> reIds = new ArrayList<>();

            for (MemberDto member : ml1) {
                reIds.add(member.getId());
            }

            //fix:查询 符合 发布文章数>10或游戏评论>14 （精选文章/评论 *5)的用户 的业务逻辑修改 [by mxf 2019-01-09]
            //条件用户
            //List<HashMap<String, Object>> members = memberDao.getRecommendMembers();
            Set<String> members = new HashSet<String>();


            //1.先查询发布文章数大于10条的用户
            List<String> sendArticleMembers = postService.getRecommendMembersFromCmmPost(sendArticles, limitSize, wathMbsSet);
            LOGGER.info("查询发布文章数大于10条的用户:{}", sendArticleMembers.toString());

            if (null != sendArticleMembers && !sendArticleMembers.isEmpty()) {
                members.addAll(sendArticleMembers);
                sendArticleMembers.clear();
            }

            //2.再查询发布游戏评论>大于14条的，前10名用户
            //memberDao.getRecommendMembersFromEvaluation(sendComments,   limitSize, wathMbsSet);

            List<String> sendCommentMembers = new ArrayList<>();

            try {
                ResultDto<List<String>> gameEvaRs = gameFacedeService.getRecommendMembersFromEvaluation(Integer.valueOf(sendComments + ""), Integer.valueOf(limitSize + ""), wathMbsSet);
                if (null != gameEvaRs) {
                    sendCommentMembers = gameEvaRs.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            LOGGER.info("查询发布游戏评论>大于14条的，前10名用户:{}", sendCommentMembers.toString());
            if (null != sendCommentMembers && !sendCommentMembers.isEmpty()) {
                members.addAll(sendCommentMembers);
                sendCommentMembers.clear();
            }

            int ormCount = 0;
            if (null == ml1) {
                ml1 = new ArrayList<MemberDto>();
            } else {
                ormCount = ml1.size();
            }

            //所有符合推荐条件用户id
            List<String> memberIds = new ArrayList<>();

            //查询未包含在官方推荐中且符合推荐条件的用户id
            for (String mb_id : members) {
                int remMebs = ormCount + memberIds.size();
                if (remMebs == limitSize) {
                    break;
                }
                if (!reIds.contains(mb_id)) {
                    memberIds.add(mb_id);
                }
            }

            //查询出符合推荐条件用户的详情数据
            if (memberIds.size() > 0) {
                //!fixme 查询用户id集合获取用户数据
                //List<Member> recommendList = menberService.selectList(new EntityWrapper<Member>().in("id", memberIds).eq("state", 0));
                //ml1.addAll(recommendList);

                try {
                    ResultDto<List<MemberDto>> mbsListResultDto = systemFacedeService.listMembersByids(memberIds, 0);

                    if (null != mbsListResultDto) {
                        List<MemberDto> memberDtoList = mbsListResultDto.getData();
                        ml1.addAll(memberDtoList);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }

        //---end
        return ml1;
    }


    @Override
    public FungoPageResultDto<CommunitySearchOut> searchCommunitys(int page, int limit, String keyword, String userId) {
//		keyword = keyword.replace(" ", "");
        @SuppressWarnings("unchecked")
        Page<CmmCommunity> cmmPage = communityService.selectPage(new Page<>(page, limit), Condition.create()
                .setSqlSelect("id,icon,name,intro,created_at as createdAt ,updated_at as  updatedAt  ,followee_num as followeeNum,post_num as postNum,type")
                .like("name", keyword).and("state = {0}", 1));
        List<CmmCommunity> communityList = cmmPage.getRecords();
//		if (communityList == null || communityList.isEmpty()) {
//			return FungoPageResultDto.error("241", "找不到符合条件的查询结果");
//		}
        if (communityList == null) {
            communityList = new ArrayList<>();
        }
        FungoPageResultDto<CommunitySearchOut> re = new FungoPageResultDto<CommunitySearchOut>();


        List<CommunitySearchOut> dataList = new ArrayList<>();
        for (CmmCommunity community : communityList) {
            CommunitySearchOut out = new CommunitySearchOut();
            out.setObjectId(community.getId());
            out.setName(community.getName());
            out.setIcon(community.getIcon());
            out.setCreatedAt(DateTools.fmtDate(community.getCreatedAt()));
            out.setUpdatedAt(DateTools.fmtDate(community.getUpdatedAt()));

            int commentNum = communityDao.getCommentNumOfCommunity(community.getId());
            out.setHot_value(community.getFolloweeNum() + community.getPostNum() + commentNum);
            out.setIntro(community.getIntro());
            //
            out.setType(community.getType());

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


            if (memberDto != null) {//用户存在，查询是否关注


                /*List<BasAction> actionList = actionService
                        .selectList(new EntityWrapper<BasAction>().eq("target_id", community.getId())
                                .eq("target_type", 4).eq("type", 5).eq("member_id", userId).and("state = 0"));*/

                BasActionDto basActionDtoLike = new BasActionDto();

                basActionDtoLike.setMemberId(userId);
                basActionDtoLike.setType(5);
                basActionDtoLike.setState(0);
                basActionDtoLike.setTargetType(4);
                basActionDtoLike.setTargetId(community.getId());

                int like = 0;
                try {

                    ResultDto<Integer> resultDtoLike = systemFacedeService.countActionNum(basActionDtoLike);
                    if (null != resultDtoLike) {
                        like = resultDtoLike.getData();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (like > 0) {
                    out.setIs_followed(true);
                } else {
                    out.setIs_followed(false);
                }
            }
            dataList.add(out);
        }
        PageTools.pageToResultDto(re, cmmPage);
        re.setData(dataList);
        return re;
    }

    @Override
    public ResultDto<Map<String, Integer>> listCommunityFolloweeNum(List<String> communityIds) {
        HashMap<String, Integer> map1 = new HashMap<>();
        if (communityIds == null || communityIds.isEmpty()) {
            return ResultDto.success(map1);
        }
        Map<String, CmmCommunity> map = communityDao.listCommunityFolloweeNum(communityIds);
        for (String s : map.keySet()) {
            CmmCommunity community = map.get(s);
            if (community != null && community.getFolloweeNum() != null) {
                map1.put(s, community.getFolloweeNum());
            }
        }
        return ResultDto.success(map1);
    }

    //-------
}
