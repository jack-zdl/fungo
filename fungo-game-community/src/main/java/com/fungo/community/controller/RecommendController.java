package com.fungo.community.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.MooMoodDaoService;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.MooMood;
import com.fungo.community.service.ICommunityService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.community.CommunityOutBean;
import com.game.common.dto.community.MoodInputPageDto;
import com.game.common.dto.community.MoodOutBean;
import com.game.common.repo.cache.facade.FungoCacheMood;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Api(value = "", description = "推荐")
public class RecommendController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecommendController.class);

    @Autowired
    private MooMoodDaoService mooMoodService;

    @Autowired
    private CmmCommunityDaoService cmmCommunityService;

    @Autowired
    private ICommunityService iCommunityService;


    @Autowired
    private FungoCacheMood fungoCacheMood;


    @Autowired
    private IUserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BasActionDao actionDao;

    @Autowired
    private BasActionService actionService;

    @Autowired
    private GameService gameService;
    @Autowired
    private GameDao gameDao;

    @Autowired
    private IncentRankedService incentRankedService;


    /**
     * InputPageDto
     * @param memberUserPrefile
     * @param inputPageDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取心情动态列表(v2.4)   filter(1:关注，0:广场)", notes = "")
    @RequestMapping(value = "/api/recommend/moods", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MoodOutBean> getDynamicsMoodList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MoodInputPageDto inputPageDto) throws Exception {


        FungoPageResultDto<MoodOutBean> re = null;

        //from redis
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MOODS_LIST;
        String keySuffix = JSON.toJSONString(inputPageDto);

        //若是关注 必须登录
        if (null != memberUserPrefile) {
            keySuffix += memberUserPrefile.getLoginId();
        }

        re = (FungoPageResultDto<MoodOutBean>) fungoCacheMood.getIndexCache(keyPrefix, keySuffix);
        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        //------------------
        re = new FungoPageResultDto<MoodOutBean>();

        List<MoodOutBean> list = new ArrayList<MoodOutBean>();
        re.setData(list);

        Page<MooMood> page = null;
        //心情每页数据
        List<MooMood> plist = null;

        //总记录数
        int total = 0;

        Long rowId = inputPageDto.getRowId();
        String lastUpdateDate = inputPageDto.getLastUpdateDate();

        boolean isHaveRowIDAndLastUpdateDate = true;
        if (null == rowId || rowId.longValue() == 0 || StringUtils.isBlank(lastUpdateDate)) {
            isHaveRowIDAndLastUpdateDate = false;
        }

        //1:关注， 0:广场
        if ("1".equals(inputPageDto.getFilter())) {
            if (memberUserPrefile == null) {
                return re;
            }
            List<String> olist = actionDao.getFollowerUserId(memberUserPrefile.getLoginId());
            if (olist.size() > 0) {
                EntityWrapper moodEntityWrapper = new EntityWrapper<MooMood>();
                moodEntityWrapper.in("member_id", olist);
                moodEntityWrapper.eq("state", 0);


                //设置社区-心情分页查询条件 数据row_id和lastUpdateDate
                queryPageMoodWithRowIdUpdate(rowId, lastUpdateDate, moodEntityWrapper);
                moodEntityWrapper.orderBy("updated_at", false);


                //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
                if (!isHaveRowIDAndLastUpdateDate) {
                    Page<MooMood> mooMoodPage = new Page<MooMood>(inputPageDto.getPage(), inputPageDto.getLimit());
                    page = mooMoodService.selectPage(mooMoodPage, moodEntityWrapper);
                    if (null != page) {
                        plist = page.getRecords();
                        total = page.getTotal();
                    }

                } else {

                    //设置分页
                    moodEntityWrapper.last("limit " + inputPageDto.getLimit());
                    plist = mooMoodService.selectList(moodEntityWrapper);

                    //查询总记录数
                    EntityWrapper moodEntityWrapperCount = new EntityWrapper<MooMood>();
                    moodEntityWrapperCount.in("member_id", olist);
                    moodEntityWrapperCount.eq("state", 0);
                    total = mooMoodService.selectCount(moodEntityWrapperCount);
                }

            } else {
                return re;
            }
        } else {

            EntityWrapper moodEntityWrapper = new EntityWrapper<MooMood>();
            moodEntityWrapper.eq("state", 0);

            //设置社区-心情分页查询条件 数据row_id和lastUpdateDate
            queryPageMoodWithRowIdUpdate(rowId, lastUpdateDate, moodEntityWrapper);

            moodEntityWrapper.orderBy("updated_at", false);

            //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
            if (!isHaveRowIDAndLastUpdateDate) {
                page = mooMoodService.selectPage(new Page<MooMood>(inputPageDto.getPage(), inputPageDto.getLimit()), moodEntityWrapper);
                if (null != page) {
                    plist = page.getRecords();
                    total = page.getTotal();
                }

            } else {

                //设置分页
                moodEntityWrapper.last("limit " + inputPageDto.getLimit());
                plist = mooMoodService.selectList(moodEntityWrapper);

                //查询总记录数
                EntityWrapper moodEntityWrapperCount = new EntityWrapper<MooMood>();
                moodEntityWrapperCount.eq("state", 0);
                total = mooMoodService.selectCount(moodEntityWrapperCount);
            }
        }

        //fix bug: 修改查询小于id和updated_at分页数据 [by mxf 2019-05-05]
        //List<MooMood> plist = page.getRecords();
        //end

        for (MooMood mooMood : plist) {
            MoodOutBean bean = new MoodOutBean();
            bean.setAuthor(userService.getAuthor(mooMood.getMemberId()));
            if (StringUtils.isNotBlank(mooMood.getContent())) {
                String interactContent = FilterEmojiUtil.decodeEmoji(mooMood.getContent());
                mooMood.setContent(interactContent);
            }

            //数据行id
            bean.setRowId(mooMood.getMooId());

            bean.setContent(CommonUtils.filterWord(mooMood.getContent()));
            bean.setCoverImage(mooMood.getCoverImage());
            bean.setCreatedAt(DateTools.fmtDate(mooMood.getCreatedAt()));
            ObjectMapper objectMapper = new ObjectMapper();
            ArrayList<String> imgs = null;
            try {
                if (mooMood.getImages() != null) {
                    imgs = (ArrayList<String>) objectMapper.readValue(mooMood.getImages(), ArrayList.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (imgs == null) {
                imgs = new ArrayList<String>();
            }
            bean.setImages(imgs);
            bean.setLiked(false);
            if (memberUserPrefile != null) {
                int followed = actionService.selectCount(new EntityWrapper<BasAction>().notIn("state", "-1").eq("type", 0).eq("target_id", mooMood.getId()).eq("member_id", memberUserPrefile.getLoginId()));
                bean.setLiked(followed > 0 ? true : false);
            }
            bean.setLikeNum(mooMood.getLikeNum());
            bean.setMoodId(mooMood.getId());
            bean.setReplyNum(mooMood.getCommentNum());
            bean.setTimer("");
            bean.setUpdatedAt(DateTools.fmtDate(mooMood.getUpdatedAt()));

            //视频封面
            bean.setVideoCoverImage(mooMood.getVideoCoverImage());

            //视频详情
            if (!CommonUtil.isNull(mooMood.getVideoUrls())) {
                ArrayList streams = objectMapper.readValue(mooMood.getVideoUrls(), ArrayList.class);
//                ArrayList<StreamInfo> streamInfos = (ArrayList<StreamInfo>)streams;
                bean.setVideoList(streams);
            }

            //游戏链接
            if (!CommonUtil.isNull(mooMood.getGameList())) {
                ArrayList<String> gameIdList = objectMapper.readValue(mooMood.getGameList(), ArrayList.class);
                List<HashMap<String, Object>> gameList = new ArrayList<>();
                for (String gameId : gameIdList) {
                    Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", 0));
                    if (game != null) {
                        HashMap<String, Object> map = new HashMap<>();
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
                bean.setGameList(gameList);
            }


            bean.setVideo(mooMood.getVideo());
            list.add(bean);
        }

        //设置分页参数
        PageTools.pageToResultDto(re, total, inputPageDto.getLimit(), inputPageDto.getPage());

        //redis cache
        fungoCacheMood.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }


    /**
     * 玩家推荐规则：
     *   规则一 玩家推荐：
     *     发布文章数 大于10 或者 游戏评论数大于14
     *   规则二 玩家已关注列表替换规则：
     *    1.显示已经关注用户数量：最大10个
     *    2.若有新的推荐用户，且未关注，替换到玩家已关注列表的前面。且 该类别数量恒定10人
     *
     */
    @Autowired
    private MemberDao memberDao;

    @ApiOperation(value = "推荐用户列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/recommend/users", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<FollowUserOutBean> getDynamicsUsersList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPageDto) {


        // LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@/api/recommend/users----");

        FungoPageResultDto<FollowUserOutBean> re = new FungoPageResultDto<FollowUserOutBean>();

        List<FollowUserOutBean> list = new ArrayList<FollowUserOutBean>();

        re.setData(list);

        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }

        //按规则一 查询出官方推荐的玩家数据
        List<Member> members = iCommunityService.getRecomMembers(inputPageDto.getLimit(), memberId);


        Page<Member> pageFormat = pageFormat(members, inputPageDto.getPage(), inputPageDto.getLimit());
        members = pageFormat.getRecords();


        for (Member member : members) {

            FollowUserOutBean bean = new FollowUserOutBean();
            bean.setAvatar(member.getAvatar());
            bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));

            bean.setLevel(member.getLevel());
            bean.setMemberNo(member.getMemberNo());
            bean.setObjectId(member.getId());
            bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
            bean.setUsername(member.getUserName());
            bean.setFollowed(member.isFollowed());

            try {
                bean.setStatusImg(userService.getStatusImage(member.getId()));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (!CommonUtil.isNull(member.getSign())) {
                bean.setSign(member.getSign());
            } else {
                bean.setSign("活跃达人");
            }

            list.add(bean);
        }

        PageTools.pageToResultDto(re, pageFormat);


        return re;
    }


    @ApiOperation(value = "社区推荐列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/recommend/communitys", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<CommunityOutBean> getcommunityList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPageDto) {
        FungoPageResultDto<CommunityOutBean> re = new FungoPageResultDto<CommunityOutBean>();
        List<CommunityOutBean> list = new ArrayList<CommunityOutBean>();
        re.setData(list);
        Page<CmmCommunity> page = cmmCommunityService.selectPage(new Page<CmmCommunity>(inputPageDto.getPage(), inputPageDto.getLimit()), new EntityWrapper<CmmCommunity>().eq("recommend_state", 1).orderBy("RAND()"));
        List<CmmCommunity> plist = page.getRecords();
        for (CmmCommunity cmmCommunity : plist) {
            CommunityOutBean bean = new CommunityOutBean();
            bean.setCommunityId(cmmCommunity.getId());
            bean.setCreatedAt(DateTools.fmtDate(cmmCommunity.getCreatedAt()));
            bean.setFollowed(false);
            bean.setIntro(cmmCommunity.getIntro());
            if (memberUserPrefile != null) {
                int followed = actionService.selectCount(new EntityWrapper<BasAction>().eq("state", 0).eq("type", 5).eq("target_id", cmmCommunity.getId()).eq("member_id", memberUserPrefile.getLoginId()));
                bean.setFollowed(followed > 0 ? true : false);
            }
            bean.setHotNun(cmmCommunity.getFolloweeNum() + cmmCommunity.getPostNum());
            bean.setIcon(cmmCommunity.getIcon());
            bean.setName(cmmCommunity.getName());
            list.add(bean);
        }
        return re;
    }


    //手动分页
    public Page<Member> pageFormat(List<Member> members, int page, int limit) {
        int totalCount = members.size();//总条数

        int totalPage = (int) Math.ceil((double) totalCount / limit);//总页数

        if (members.size() == 0) {

        } else if (page == totalPage) {
            members = members.subList(limit * (page - 1), totalCount);
        } else {
            members = members.subList(limit * (page - 1), limit * page);
        }

        Page<Member> memberPage = new Page<>(page, limit);
        memberPage.setRecords(members);
        memberPage.setCurrent(page);
        memberPage.setTotal(totalCount);

        return memberPage;
    }


    /**
     * 设置社区-心情分页查询条件
     * @param rowId
     * @param lastUpdateDate
     * @param cmmPostEntityWrapper
     */
    private void queryPageMoodWithRowIdUpdate(Long rowId, String lastUpdateDate, EntityWrapper<MooMood> cmmPostEntityWrapper) {
        if (null != rowId && rowId.longValue() > 0 && StringUtils.isNotBlank(lastUpdateDate)) {
            cmmPostEntityWrapper.lt("moo_id", rowId);
            cmmPostEntityWrapper.le("updated_at", lastUpdateDate);
        }
    }

    //-----
}
