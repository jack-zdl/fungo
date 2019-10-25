package com.fungo.system.service.portal.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.dao.BannerDao;
import com.fungo.system.dto.PCBannerDto;
import com.fungo.system.entity.Banner;
import com.fungo.system.facede.IGameProxyService;
import com.fungo.system.facede.IMemeberProxyService;
import com.fungo.system.facede.IndexProxyService;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.service.BannerService;
import com.fungo.system.service.IUserService;
import com.fungo.system.service.SysVersionService;
import com.fungo.system.service.portal.PortalSystemIIndexService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.advert.AdvertOutBean;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.index.ActionBean;
import com.game.common.dto.index.CardDataBean;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/27
 */
@Service
public class PortalSystemIndexServiceImpl implements PortalSystemIIndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalSystemIndexServiceImpl.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private FungoCacheIndex fungoCacheIndex;
    @Autowired
    private SysVersionService sysVersionService;
    @Autowired
    private IMemeberProxyService iMemeberProxyService;
    @Autowired
    private IGameProxyService iGameProxyService;
    @Autowired
    private IndexProxyService indexProxyService;
    @Autowired
    private BannerService bannerService;
    @Autowired
    private BannerDao bannerDao;
    @Autowired
    private CommunityFeignClient communityFeignClient;

    @Override
    public FungoPageResultDto<CardIndexBean> index(InputPageDto input) {
        FungoPageResultDto<CardIndexBean> re = null;
//        //先从Redis获取
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_INDEX;
//        String keySuffix = JSON.toJSONString(input);
//         re = (FungoPageResultDto<CardIndexBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);
//
//        if (null != re && null != re.getData() && re.getData().size() > 0) {
//            return re;
//        }
        re = new FungoPageResultDto<>();
        List<CardIndexBean> clist = new ArrayList<>();
        int page = input.getPage();
        int limit = input.getLimit();

        Page<CardIndexBean> pageBean = null;

        if (1 == page) {
            //轮播
//			CardIndexBean topic = this.topic();
//			clist.add(topic);
            //活动位
            CardIndexBean activities = this.activities();

            if (activities != null) {
                clist.add(activities);
            }
            //本周精选(游戏)
//            CardIndexBean hotGames = this.hotGames();
//            if (hotGames != null) {
//                clist.add(hotGames);
//            }
            //精选文章1
            CardIndexBean postHost = this.selectPosts("0006",1);
            if (postHost != null) {
                clist.add(postHost);
            }
            //安利墙 ios 当前版本是否 隐藏
            CardIndexBean anliWall = this.anliWall();

            if (anliWall != null) {
                clist.add(anliWall);
            }
            // 精选文章2（视频）
            CardIndexBean postVidoe = this.selectPosts("0007",1);
            if (postVidoe != null) {
                clist.add(postVidoe);
            }
            //精选文章3 (视频)
            CardIndexBean postFine = this.selectPosts("0008",1);
            if (postFine != null) {
                clist.add(postFine);
            }
            ArrayList<CardIndexBean> topicPosts = this.topicPosts(page, limit, 10 * (page - 1) + 1);
            clist.addAll(topicPosts);
            re.setAfter(2);
            re.setBefore(1);

        } else {
            ArrayList<CardIndexBean> topicPosts = this.topicPosts(page, limit, 10 * (page - 1) + 1);
            clist.addAll(topicPosts);
        }
        re.setData(clist);
//        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;
    }

    @Override
    public ResultDto<List<AdvertOutBean>> getAdvertWithPc() {
        ResultDto re = null;
        List<AdvertOutBean> list = new ArrayList<>();
        try {
            List<PCBannerDto> blist = bannerDao.getPCBannerByIndex();
            //获取广告位
            if (null != blist && !blist.isEmpty()) {
                list = new ArrayList<>();
                for (PCBannerDto banner : blist) {
                    AdvertOutBean bean = new AdvertOutBean();
                    bean.setBizId(banner.getTargetId());
                    bean.setBizType(1);
                    bean.setTargetType( banner.getTargetType());
                    bean.setContent(CommonUtils.filterWord(banner.getIntro()));
                    bean.setImageUrl(banner.getCoverImage());
                    bean.setName(banner.getTag());
                    bean.setTitle(CommonUtils.filterWord(banner.getTitle()));
                    bean.setPcImagesUrl(banner.getBannerImage());
                    list.add(bean);
                }
            }
            re = ResultDto.ResultDtoFactory.buildSuccess(list);
        }catch (Exception e){
            LOGGER.error("PC首页轮播异常",e);
            re = ResultDto.ResultDtoFactory.buildError("PC首页轮播异常");
        }
        return re;
    }


    //安利墙
    public CardIndexBean anliWall() {
//		re.setData(list);
        List<GameEvaluationDto> plist = iGameProxyService.selectGameEvaluationPage(); //gameEvaluationService.selectPage(new Page<GameEvaluation>(1, 6), new EntityWrapper<GameEvaluation>().eq("type", 2).and("state != {0}", -1).orderBy("RAND()"));
        ArrayList<CardDataBean> evaDateList = new ArrayList<>();
        CardIndexBean cb = new CardIndexBean();
        for (GameEvaluationDto gameEvaluation : plist) {
            CardDataBean bean = new CardDataBean();
            bean.setUserinfo(this.userService.getAuthor(gameEvaluation.getMemberId()));
            GameDto gameParam = new GameDto();
            gameParam.setId(gameEvaluation.getGameId());
            GameDto game = iGameProxyService.selectGameById(gameParam);    //this.gameService.selectById(gameEvaluation.getGameId());
            if(game == null){
                LOGGER.error( "系统微服务无法访问游戏微服务getGamePage接口" );
                return null;
            }
            bean.setMainTitle(game.getName());
            if (gameEvaluation.getRating() != null) {
                bean.setSubtitle(gameEvaluation.getRating().toString());
            } else {
                bean.setSubtitle("0");
            }

            ActionBean source = new ActionBean();
            source.setIcon(game.getIcon());
            source.setIconType("0");
            source.setName(game.getName());
            source.setActionType("1");
            source.setTargetType(3);
            source.setTargetId(game.getId());
            bean.setSource(source);

            bean.setContent(gameEvaluation.getContent());
            bean.setImageUrl(game.getCoverImage());
            bean.setActionType("1");
            bean.setTargetType(6);
            bean.setTargetId(gameEvaluation.getId());
            evaDateList.add(bean);
        }
        cb.setDataList(evaDateList);
        cb.setSize(evaDateList.size());
        cb.setCardName("安利墙");
        cb.setCardType(5);
        cb.setOrder(5);
        return cb;
    }

    //大家都在玩
    public CardIndexBean selectedGames() {
        return iGameProxyService.selectedGames();
    }

    //社区置顶文章
    public ArrayList<CardIndexBean> topicPosts(int page, int limit, int order) {
        CmmPostDto cmmPostDto = new CmmPostDto();
        cmmPostDto.setPage(page);
        cmmPostDto.setLimit(limit);
        cmmPostDto.setType(3);
        cmmPostDto.setState(1);
        FungoPageResultDto<CmmPostDto> cmmPostDtoFungoPageResultDto = communityFeignClient.listCmmPostTopicPost(cmmPostDto);
//        Page<CmmPostDto> pageList = indexProxyService.selectCmmPostPage(cmmPostDto);    //  postService.selectPage(new Page<CmmPost>(page, limit), new EntityWrapper<CmmPost>().eq("type", 3).eq("state", 1).last("ORDER BY sort DESC,updated_at DESC"));
//		List<CmmPost> list = postService.selectList(new EntityWrapper<CmmPost>().eq("type", 3).orderBy("created_at", false).last("limit " + start+","+limit));
//		List<CmmPost> list = p.getRecords();
        ArrayList<CardIndexBean> indexList = new ArrayList<>();
        CardIndexBean index = new CardIndexBean();
        ArrayList<CardDataBean> gameDateList = new ArrayList<>();
        for (CmmPostDto post : cmmPostDtoFungoPageResultDto.getData()) {

            CardDataBean bean = new CardDataBean();
            // 图片/标题/用户名/头像/评论数
            CmmCommunityDto communityDto = new CmmCommunityDto();
            communityDto.setId(post.getCommunityId());
            communityDto.setState(-1);
            @SuppressWarnings("unchecked")
            CmmCommunityDto c = indexProxyService.selectCmmCommuntityDetail(communityDto);   //communityService.selectOne(Condition.create().setSqlSelect("id,name,icon,cover_image").eq("id", post.getCommunityId()).ne("state", -1));
            bean.setImageUrl(post.getCoverImage());
//			if(!CommonUtil.isNull(post.getCoverImage())) {
//				bean.setImageUrl(post.getCoverImage());
//			}else {
//				bean.setImageUrl(c.getCoverImage());
//			}
            bean.setVideoUrl(post.getVideo());

            String postTitle = post.getTitle();
            String postContent = post.getContent();

            if (StringUtils.isNotBlank(postTitle)) {
                postTitle = FilterEmojiUtil.decodeEmoji(postTitle);
            }
            if (StringUtils.isNotBlank(postContent)) {
                postContent = FilterEmojiUtil.decodeEmoji(postContent);
            }
            bean.setMainTitle(CommonUtils.filterWord(postTitle));
            bean.setSubtitle(postContent);
            bean.setContent(postContent);

            bean.setUserinfo(userService.getAuthor(post.getMemberId()));

            bean.setReplyNum(post.getCommentNum());

            if (!CommonUtil.isNull(post.getVideo()) && CommonUtil.isNull(post.getCoverImage())) {
                bean.setImageUrl(c.getCoverImage());
            }

            ActionBean source = new ActionBean();
            source.setIcon(c.getIcon());
            source.setIconType("0");
            source.setName(c.getName());
            source.setActionType("1");
            source.setTargetType(4);
            source.setTargetId(c.getId());
            bean.setSource(source);

            bean.setActionType("1");
            bean.setTargetId(post.getId());
            bean.setTargetType(1);
            gameDateList.add(bean);
        }
        Map map = new HashMap<>(  );
        if(cmmPostDtoFungoPageResultDto.getPages() <= page){
            map.put( "hasNext",false );
            index.setExtendData( map );
        }else {
            map.put( "hasNext",true );
            index.setExtendData( map );
        }
        index.setCardName("社区置顶文章");
        index.setCardType(8);
        index.setOrder(order);
        index.setSize(gameDateList.size());
//        order++;
        index.setDataList(gameDateList);
        indexList.add(index);
        return indexList;
    }

    //活动
    public CardIndexBean activities() {
        //banner
        List<Banner> bl = bannerService.selectList(new EntityWrapper<Banner>().in("position_code", "0005,0010").eq("state", "0").orderBy("sort", false).last("limit 1"));
        CardIndexBean indexBean = new CardIndexBean();
        if (bl.size() == 0) {
            return null;
        }
        ArrayList<CardDataBean> list = new ArrayList<>();
        for (Banner b : bl) {
            CardDataBean b1 = new CardDataBean();
            b1.setLowerLeftCorner("活动");
            b1.setImageUrl(b.getCoverImage());
            b1.setMainTitle(b.getTitle());
            b1.setContent(b.getIntro());
            b1.setActionType(String.valueOf(b.getActionType()));
            b1.setHref(b.getHref());
            b1.setTargetType(b.getTargetType());
            b1.setTargetId(b.getTargetId());
            list.add(b1);
        }
        indexBean.setCardName("活动位");
        indexBean.setOrder(1);
        indexBean.setCardType(2);
        indexBean.setDataList(list);
        indexBean.setSize(list.size());
        return indexBean;
    }

    //本周精选(游戏)
    public CardIndexBean hotGames() {
        List<Banner> blist = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0001").eq("target_type", 3).eq("state", 2).orderBy("release_time", false).last("limit 6"));
        if (blist.size() == 0) {
            return null;
        }
        ArrayList<CardDataBean> gameDateList = new ArrayList<>();
        CardIndexBean indexBean = new CardIndexBean();
        for (Banner banner : blist) {
            Map<String, BigDecimal> rateData = indexProxyService.getRateData(banner.getTargetId());  //gameDao.getRateData(banner.getTargetId());
            //
            GameDto gameParam = new GameDto();
            gameParam.setId(banner.getTargetId());
            GameDto game =  iGameProxyService.selectGameById(gameParam);   //gameService.selectById(banner.getTargetId());
            CardDataBean b = new CardDataBean();
            b.setImageUrl(game.getIcon());
            b.setMainTitle(banner.getTitle());//游戏名称
            b.setContent(banner.getIntro());//游戏简介
            if (rateData != null && rateData.get("avgRating") != null) {
                b.setLowerLeftCorner(rateData.get("avgRating").toString());//评分
            } else {
                b.setLowerLeftCorner("0.0");
            }
            b.setLowerRightCorner(banner.getTag());//推荐理由

            b.setCreatedAt(DateTools.fmtDate(banner.getReleaseTime()));
            b.setUpdatedAt(DateTools.fmtDate(banner.getUpdatedAt()));
            b.setActionType("1");
            b.setTargetId(banner.getTargetId());
            b.setTargetType(3);
            gameDateList.add(b);
        }
        indexBean.setDataList(gameDateList);
        indexBean.setOrder(2);
        indexBean.setCardName("本周精选");
        indexBean.setCardType(3);
        indexBean.setSize(gameDateList.size());
        return indexBean;
    }

    //精选文章
    public CardIndexBean selectPosts(String type,int limit) {
        //2 6(视频) 7
        // 2.5 需求  若未标记排序数，则同一位置的内容按照上线时间从当前到过去排序。
        ArrayList<CardDataBean> cl = new ArrayList<CardDataBean>();
            List<Banner> videoBanners = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", type).eq("target_type", 1).eq("state", "0").orderBy("sort desc , release_time", false).last("limit "+limit));
        CardIndexBean cb = new CardIndexBean();
        if (videoBanners == null) {
            return null;
        } else {
            for(Banner videoBanner : videoBanners){
                CmmPostDto cmmPostParam = new CmmPostDto();
                cmmPostParam.setId(videoBanner.getTargetId());
                cmmPostParam.setState(1);
                CmmPostDto post =  indexProxyService.selctCmmPostOne(cmmPostParam);
                if (post == null) {
                    return null;
                }
                CardDataBean dataBean = new CardDataBean();
                dataBean.setMainTitle(videoBanner.getTitle());
                dataBean.setImageUrl(videoBanner.getCoverImage());
                dataBean.setSubtitle(videoBanner.getIntro());
                dataBean.setUserinfo(userService.getAuthor(post.getMemberId()));
                dataBean.setVideoUrl(post.getVideo());
                if (!CommonUtil.isNull(post.getContent())) {
//				dataBean.setContent(post.getContent().length()>25?CommonUtils.filterWord(post.getContent().substring(0, 25)):CommonUtils.filterWord(post.getContent()));
                    dataBean.setContent(post.getContent());
                } else {
                    dataBean.setContent(post.getContent());
                }
                dataBean.setUpperLeftCorner(videoBanner.getTag());
//			dataBean.setLowerRightCorner(post.getReportNum()+"");

                dataBean.setReplyNum(post.getReportNum());
                CmmCommunityDto communityParam = new CmmCommunityDto();
                communityParam.setId(post.getCommunityId());
                CmmCommunityDto community = iMemeberProxyService.selectCmmCommunityById(communityParam); //  communityService.selectById(post.getCommunityId());
                if (community != null) {
                    dataBean.setLowerRightCorner(community.getName());
                }
                if (!CommonUtil.isNull(post.getVideo()) && CommonUtil.isNull(videoBanner.getCoverImage())) {
                    if (community != null) {
                        dataBean.setImageUrl(community.getCoverImage());
                    }
                }

                dataBean.setActionType("1");
                dataBean.setHref(videoBanner.getHref());
                dataBean.setTargetType(videoBanner.getTargetType());
                dataBean.setTargetId(videoBanner.getTargetId());

                ActionBean actionBean = new ActionBean();
                actionBean.setTargetId(community.getId());
                dataBean.setSource(actionBean);


                cl.add(dataBean);
            }
            cb.setDataList(cl);
            cb.setCardName("精品文章");
            if ("0007".equals(type)) {
                cb.setCardType(6);
            } else {
                cb.setCardType(4);
            }
            cb.setOrder(6);
            cb.setSize(1);
        }
        return cb;
    }


    //社区置顶文章
    public CardIndexBean selectCommunity(String type,int limit) {
        //2 6(视频) 7
        Banner videoBanner = bannerService.selectOne(new EntityWrapper<Banner>().eq("position_code", type).eq("target_type", 1).eq("state", "0").orderBy("sort", false).last("limit "+limit));
        CardIndexBean cb = new CardIndexBean();
        if (videoBanner == null) {
            return null;
        } else {
            CmmPostDto cmmPostParam = new CmmPostDto();
            cmmPostParam.setId(videoBanner.getTargetId());
            cmmPostParam.setState(1);
            CmmPostDto post =  indexProxyService.selctCmmPostOne(cmmPostParam);   //  postService.selectOne(new EntityWrapper<CmmPost>().eq("id", videoBanner.getTargetId()).eq("state", 1));
            if (post == null) {
                return null;
            }
            CardDataBean dataBean = new CardDataBean();
            dataBean.setMainTitle(videoBanner.getTitle());
            dataBean.setImageUrl(videoBanner.getCoverImage());
            dataBean.setSubtitle(videoBanner.getIntro());
            dataBean.setUserinfo(userService.getAuthor(post.getMemberId()));
            dataBean.setVideoUrl(post.getVideo());
            if (!CommonUtil.isNull(post.getContent())) {
//				dataBean.setContent(post.getContent().length()>25?CommonUtils.filterWord(post.getContent().substring(0, 25)):CommonUtils.filterWord(post.getContent()));
                dataBean.setContent(post.getContent());
            } else {
                dataBean.setContent(post.getContent());
            }
            dataBean.setUpperLeftCorner(videoBanner.getTag());
//			dataBean.setLowerRightCorner(post.getReportNum()+"");

            dataBean.setReplyNum(post.getReportNum());
            CmmCommunityDto communityParam = new CmmCommunityDto();
            communityParam.setId(post.getCommunityId());
            CmmCommunityDto community = iMemeberProxyService.selectCmmCommunityById(communityParam); //  communityService.selectById(post.getCommunityId());
            if (community != null) {
                dataBean.setLowerRightCorner(community.getName());
            }
            if (!CommonUtil.isNull(post.getVideo()) && CommonUtil.isNull(videoBanner.getCoverImage())) {
                if (community != null) {
                    dataBean.setImageUrl(community.getCoverImage());
                }
            }

            dataBean.setActionType("1");
            dataBean.setHref(videoBanner.getHref());
            dataBean.setTargetType(videoBanner.getTargetType());
            dataBean.setTargetId(videoBanner.getTargetId());

            ActionBean actionBean = new ActionBean();
            actionBean.setTargetId(community.getId());
            dataBean.setSource(actionBean);

            ArrayList<CardDataBean> cl = new ArrayList<CardDataBean>();
            cl.add(dataBean);
            cb.setDataList(cl);
            cb.setCardName("社区置顶文章");
            if ("0007".equals(type)) {
                cb.setCardType(6);
            } else {
                cb.setCardType(4);
            }
            cb.setOrder(6);
            cb.setSize(1);
        }
        return cb;
    }



    /**
     * 是否关闭本周精选、安利墙、大家都在玩三个功能
     *  配合ios审核隐藏和显示设置
     * @param os 移动端系统类型  Android/iOS
     * @param app_channel  app渠道编码
     * @param appVersion app端版本号
     * @return true关闭这些功能，false不关闭
     */
    private boolean isCloseIndexSection(String os, String app_channel, String appVersion) {

        if (StringUtils.isBlank(os) || StringUtils.isBlank(app_channel) || StringUtils.isBlank(appVersion)) {
            return false;
        }

        //非ios平台不处理
        if (!StringUtils.equalsIgnoreCase("ios", os)) {
            return false;
        }
        String version = appVersion;

        String mobile_type = "";
        if (StringUtils.equalsIgnoreCase("ios", os)) {
            mobile_type = "IOS";
        }

        String channel_code = app_channel;
        HashMap<String, Object> versionInfoMap = sysVersionService.queryAppVersionInfo(version, mobile_type, channel_code);
        if (null != versionInfoMap && versionInfoMap.containsKey("game_download_switch")) {
            Integer game_download_switch = (Integer) versionInfoMap.get("game_download_switch");
            //游戏下载开关:
            //0 关
            //1 开
            if (0 == game_download_switch.intValue()) {
                return true;
            }
        }
        return false;
    }

}
