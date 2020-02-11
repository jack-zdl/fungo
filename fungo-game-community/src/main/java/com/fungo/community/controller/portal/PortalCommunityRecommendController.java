package com.fungo.community.controller.portal;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.MooMoodDaoService;
import com.fungo.community.entity.MooMood;
import com.fungo.community.feign.GameFeignClient;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.service.ICommunityService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.*;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.MoodInputPageDto;
import com.game.common.dto.community.MoodOutBean;
import com.game.common.repo.cache.facade.FungoCacheMood;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import com.game.common.vo.MemberFollowerVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  PC2.0推荐
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/29 17:07
 */
@SuppressWarnings("all")
@RestController
@Api(value = "", description = "PC2.0推荐")
public class PortalCommunityRecommendController {

    @Autowired
    private MooMoodDaoService mooMoodService;

    @Autowired
    private CmmCommunityDaoService cmmCommunityService;

    @Autowired
    private ICommunityService iCommunityService;


    @Autowired
    private FungoCacheMood fungoCacheMood;


    //依赖系统和用户微服务
    @Autowired(required = false)
    private SystemFeignClient systemFeignClient;


    //依赖游戏微服务
    @Autowired(required = false)
    private GameFeignClient gameFeignClient;


    /**
     * PC2.0获取心情动态列表
     * @param memberUserPrefile
     * @param inputPageDto
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "PC2.0获取心情动态列表(v2.4)   filter(1:关注，0:广场)", notes = "")
    @RequestMapping(value = "/api/portal/community/recommend/moods", method = RequestMethod.POST)
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
            //!fixme 获取关注用户id集合
            //List<String> olist = actionDao.getFollowerUserId(memberUserPrefile.getLoginId());
            //获取关注用户id集合
            List<String> olist = new ArrayList<String>();
            MemberFollowerVo memberFollowerVo = new MemberFollowerVo();
            memberFollowerVo.setMemberId(memberUserPrefile.getLoginId());
            try {
                FungoPageResultDto<String> followerUserIdResult = systemFeignClient.getFollowerUserId(memberFollowerVo);
                if (null != followerUserIdResult) {
                    olist.addAll(followerUserIdResult.getData());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
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
            //!fixme 根据用户id查询用户详情
            //bean.setAuthor(userService.getAuthor(mooMood.getMemberId()));
            AuthorBean authorBean = new AuthorBean();
            try {
                ResultDto<AuthorBean> beanResultDto = systemFeignClient.getAuthor(mooMood.getMemberId());
                if (null != beanResultDto) {
                    authorBean = beanResultDto.getData();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            bean.setAuthor(authorBean);
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
                //!fixme 获取点赞数 行为类型 点赞 | 0
                //int followed = actionService.selectCount(new EntityWrapper<BasAction>().notIn("state", "-1").eq("type", 0).eq("target_id", mooMood.getId()).eq("member_id", memberUserPrefile.getLoginId()));
                //获取点赞数 行为类型 点赞 | 0
                BasActionDto basActionDto = new BasActionDto();
                basActionDto.setMemberId(memberUserPrefile.getLoginId());
                basActionDto.setType(0);
                basActionDto.setState(0);
                basActionDto.setTargetId(mooMood.getId());
                int followed = 0;
                try {
                    ResultDto<Integer> resultDto = systemFeignClient.countActionNum(basActionDto);
                    followed = resultDto.getData();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
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
                    //!fixme 根据游戏id和状态查询游戏详情
                    //Game game = gameService.selectOne(new EntityWrapper<Game>().eq("id", gameId).eq("state", 0));
                    GameDto gameDto = null;
                    ResultDto<GameDto> gameDtoResultDto = null;
                    try {
                        gameDtoResultDto = gameFeignClient.selectGameDetails(gameId, 0);
                        if (null != gameDtoResultDto) {
                            gameDto =  gameDtoResultDto.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    if (gameDto != null) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("gameId", gameDto.getId());
                        map.put("gameName", gameDto.getName());
                        map.put("gameIcon", gameDto.getIcon());
                        map.put("gameIdtSn",gameDto.getGameIdtSn());
                        //!fixme 获取游戏平均分
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
                        */
                        //获取游戏平均分
                        double gameAverage = gameFeignClient.selectGameAverage(gameDto.getId(), 0);
                        map.put("gameRating", gameAverage);
                        map.put("category", gameDto.getTags());
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
}
