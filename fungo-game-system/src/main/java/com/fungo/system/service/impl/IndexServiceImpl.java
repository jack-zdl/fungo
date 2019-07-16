package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.dao.BannerDao;
import com.fungo.system.entity.Banner;
import com.fungo.system.facede.IGameProxyService;
import com.fungo.system.facede.IMemeberProxyService;
import com.fungo.system.facede.IndexProxyService;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.service.BannerService;
import com.fungo.system.service.IIndexService;
import com.fungo.system.service.IUserService;
import com.fungo.system.service.SysVersionService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.index.ActionBean;
import com.game.common.dto.index.CardDataBean;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.dto.index.CircleCardDataBean;
import com.game.common.enums.BaseEnum;
import com.game.common.enums.CommonEnum;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.Html2Text;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import com.game.common.vo.CircleGamePostVo;
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
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class IndexServiceImpl implements IIndexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexServiceImpl.class);

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
    public FungoPageResultDto<CardIndexBean> index(InputPageDto input, String os, String iosChannel, String app_channel, String appVersion) {

        FungoPageResultDto<CardIndexBean> re = null;
        //先从Redis获取
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_INDEX;
        String keySuffix = JSON.toJSONString(input) + os + iosChannel;
        if (StringUtils.isNotBlank(app_channel)) {
            keySuffix += app_channel;
        }

        re = (FungoPageResultDto<CardIndexBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);

        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        if (os == null) {
            os = "";
        }

        re = new FungoPageResultDto<>();

        List<CardIndexBean> clist = new ArrayList<CardIndexBean>();
        int page = input.getPage();

        //在ios平台下首页是否关闭部分功能
        boolean isCloseIndexSection = isCloseIndexSection(os, app_channel, appVersion);

        LOGGER.info("-----------在ios平台下首页是否关闭部分功能----os:{},------app_channel:{}------appVersion:{}-----isCloseIndexSection:{}", os, app_channel, appVersion,
                isCloseIndexSection);

        //int count = postService.selectCount(new EntityWrapper<CmmPost>().eq("type", 3));
        Page<CardIndexBean> pageBean = null;

        //第一页
        if (1 == page) {

            //position 1 轮播(position_code 0001)
//			CardIndexBean topic = this.topic();
//			clist.add(topic);

            //position 2. 活动位 (position_code 0005)
            //新版本2.5 不需要广告位
//            CardIndexBean activities = this.activities();
//
//            if (activities != null) {
//                clist.add(activities);
//            }

            //ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {

                //本周精选(游戏)
                CardIndexBean hotGames = this.hotGames();
                if (hotGames != null) {
                    clist.add(hotGames);
                }

            }

//			ActionBean a = new ActionBean();
//			indexBean.setUprightAction(a);
            //position 3 游戏攻略(精选文章)
            //精选文章  position 3 (position_code 0006)
            CardIndexBean postHost = this.selectPosts("0006");

            if (postHost != null) {
                clist.add(postHost);
            }

            //position 4 安利墙
            //安利墙 ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {

                CardIndexBean anliWall = this.anliWall();

                if (anliWall != null) {
                    clist.add(anliWall);
                }

            } else {
                ///position 5 精选文章 视频(position_code 0007)
                CardIndexBean postVidoe = this.selectPosts("0007");

                if (postVidoe != null) {
                    clist.add(postVidoe);
                }

            }

            re.setAfter(2);

            re.setBefore(1);

            //第二页
        } else if (page == 2) {

            //安利墙
            //安利墙 ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {
                CardIndexBean postVidoe = this.selectPosts("0007");
                if (postVidoe != null) {
                    clist.add(postVidoe);
                }
            }

            //精选文章 (视频)
            CardIndexBean postFine = this.selectPosts("0008");
            if (postFine != null) {
                clist.add(postFine);
            }

            //大家都在玩
            //大家都在玩 ios 当前版本是否 隐藏
            if (!isCloseIndexSection) {
                CardIndexBean selectedGames = this.selectedGames();
                if (selectedGames != null) {
                    clist.add(selectedGames);
                }
            }
            //置顶
//			ArrayList<CardIndexBean> topicPosts = this.topicPosts(0,3,8);
//			clist.addAll(topicPosts);
            re.setAfter(3);
            re.setBefore(1);


        } else {

            page = page - 2;
//			ArrayList<CardIndexBean> topicPosts = this.topicPosts(10*(page-1) - 7,10,10*(page-1)+1);
            ArrayList<CardIndexBean> topicPosts = this.topicPosts(page, 10, 10 * (page - 1) + 1);
            clist.addAll(topicPosts);
//			PageTools.pageToResultDto(re, pageBean);
            if (topicPosts.size() == 0) {
                re.setAfter(-1);

            } else {

                re.setAfter(page + 3);

            }
//			pageBean = this.pageFormat(clist,page,count);
        }

        re.setData(clist);

        //redis cache
        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);

        return re;
    }

    /**
     * 功能描述: 获取圈子页面上广告位
     * @param: [input, os, iosChannel, app_channel, appVersion]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.index.CardIndexBean>
     * @auther: dl.zhang
     * @date: 2019/6/11 11:05
     */
    @Override
    public FungoPageResultDto<CardIndexBean> circleEventList(InputPageDto input, String os, String iosChannel, String app_channel, String appVersion) {
        FungoPageResultDto<CardIndexBean> re = new FungoPageResultDto<>();
        //先从Redis获取
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_EVENT_INDEX;
        String keySuffix = JSON.toJSONString(input) + os + iosChannel;
        try {
            if (StringUtils.isNotBlank(app_channel)) {
                keySuffix += app_channel;
            }
            //@todo
 //           re =  (FungoPageResultDto<CardIndexBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);

            if (null != re && null != re.getData() && re.getData().size() > 0) {
                return re;
            }
            List<CardIndexBean> clist = new ArrayList<>();
            /****/
            List<Banner> bl = new ArrayList<>();
            Page<Banner> page = new Page<>(input.getPage(), input.getLimit());

            if (BannnerEnum.lving.getValue().equals(input.getFilter())) {
                bl = bannerDao.beforeNewDateBanner(page);
            } else if (BannnerEnum.past.getValue().equals(input.getFilter())) {
                bl = bannerDao.afterNewDateBanner(page);
            }

//            CardIndexBean indexBean = new CardIndexBean();
            if (bl.size() == 0) {
                return FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(CommonEnum.HTTP_WARNING_EMPTY.code(), CommonEnum.HTTP_WARNING_EMPTY.message());
            }
            ArrayList<CircleCardDataBean> list = new ArrayList<>();
            for (Banner b : bl) {
                CircleCardDataBean b1 = new CircleCardDataBean();
                b1.setBannerId(b.getId());
                b1.setMainTitle(b.getGeneralizeTitle());
                b1.setImageUrl(b.getCoverImage());
                b1.setNewImageUrl(b.getCoverImgNew());
                b1.setContent(b.getIntro());
                b1.setHref(b.getHref());
                b1.setActionType(String.valueOf(b.getActionType()));
                b1.setTargetType(b.getTargetType());
                b1.setTargetId(b.getTargetId());
                b1.setStartDate(DateTools.fmtDate(b.getBeginDate()));
                b1.setEndDate(DateTools.fmtDate(b.getEndDate()));
                list.add(b1);
            }
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(list, input.getPage() - 1, page);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取圈子页面上广告位", e);
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildWarning(CommonEnum.ERROR.code(), "获取圈子页面上广告位失败,请联系管理员");
        }
        return re;
    }

    @Override
    public FungoPageResultDto<CardIndexBean> index(InputPageDto input) {
        FungoPageResultDto<CardIndexBean> re = null;
        //先从Redis获取
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_INDEX_RECOMMEND_INDEX;
        String keySuffix = JSON.toJSONString(input);
        re = (FungoPageResultDto<CardIndexBean>) fungoCacheIndex.getIndexCache(keyPrefix, keySuffix);

        if (null != re && null != re.getData() && re.getData().size() > 0) {
            return re;
        }
        re = new FungoPageResultDto<>();
        List<CardIndexBean> clist = new ArrayList<>();
        int page = input.getPage();

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
            CardIndexBean hotGames = this.hotGames();
            if (hotGames != null) {
                clist.add(hotGames);
            }
            //精选文章
            CardIndexBean postHost = this.selectPosts("0006");
            if (postHost != null) {
                clist.add(postHost);
            }
            //安利墙 ios 当前版本是否 隐藏
            CardIndexBean anliWall = this.anliWall();

            if (anliWall != null) {
                clist.add(anliWall);
            }
            CardIndexBean postVidoe = this.selectPosts("0007");
            if (postVidoe != null) {
                clist.add(postVidoe);
            }
            re.setAfter(2);
            re.setBefore(1);

        } else if (page == 2) {

            //安利墙 ios 当前版本是否 隐藏
            CardIndexBean postVidoe = this.selectPosts("0007");
            if (postVidoe != null) {
                clist.add(postVidoe);
            }
            CardIndexBean postFine = this.selectPosts("0008");
            if (postFine != null) {
                clist.add(postFine);
            }
            //大家都在玩 ios 当前版本是否 隐藏
            CardIndexBean selectedGames = this.selectedGames();
            if (selectedGames != null) {
                clist.add(selectedGames);
            }
            //置顶
//			ArrayList<CardIndexBean> topicPosts = this.topicPosts(0,3,8);
//			clist.addAll(topicPosts);
            re.setAfter(3);
            re.setBefore(1);
        } else {
            page = page - 2;
//			ArrayList<CardIndexBean> topicPosts = this.topicPosts(10*(page-1) - 7,10,10*(page-1)+1);
            ArrayList<CardIndexBean> topicPosts = this.topicPosts(page, 10, 10 * (page - 1) + 1);
            clist.addAll(topicPosts);
//			PageTools.pageToResultDto(re, pageBean);
            if (topicPosts.size() == 0) {
                re.setAfter(-1);
            } else {
                re.setAfter(page + 3);
            }
//			pageBean = this.pageFormat(clist,page,count);
        }
        re.setData(clist);
        //redis cache
        fungoCacheIndex.excIndexCache(true, keyPrefix, keySuffix, re);
        return re;
    }


    //安利墙
    public CardIndexBean anliWall() {
//		re.setData(list);
        // @todo
        List<GameEvaluationDto> plist = iGameProxyService.selectGameEvaluationPage();
        //gameEvaluationService.selectPage(new Page<GameEvaluation>(1, 6), new EntityWrapper<GameEvaluation>().eq("type", 2).and("state != {0}", -1).orderBy("RAND()"));
        if (null == plist || plist.isEmpty()) {
            return null;
        }

        ArrayList<CardDataBean> evaDateList = new ArrayList<>();
        CardIndexBean cb = new CardIndexBean();
        for (GameEvaluationDto gameEvaluation : plist) {

            GameDto gameParam = new GameDto();
            gameParam.setId(gameEvaluation.getGameId());
            //state 状态 0：上线，1：下架，-1：删除，3待审核
            gameParam.setState(0);

            GameDto game = iGameProxyService.selectGameById(gameParam);    //this.gameService.selectById(gameEvaluation.getGameId());
            if (null == game) {
                continue;
            }

            CardDataBean bean = new CardDataBean();
            bean.setUserinfo(this.userService.getAuthor(gameEvaluation.getMemberId()));


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
        //@todo
        CmmPostDto cmmPostDto = new CmmPostDto();
        cmmPostDto.setPage(page);
        cmmPostDto.setLimit(limit);
        cmmPostDto.setType(3);
        cmmPostDto.setState(1);
        FungoPageResultDto<CmmPostDto> cmmPostDtoFungoPageResultDto = communityFeignClient.listCmmPostTopicPost(cmmPostDto);
        // Page<CmmPostDto> pageList = indexProxyService.selectCmmPostPage(cmmPostDto);
        //  postService.selectPage(new Page<CmmPost>(page, limit), new EntityWrapper<CmmPost>().eq("type", 3).eq("state", 1).last("ORDER BY sort DESC,updated_at DESC"));
//		List<CmmPost> list = postService.selectList(new EntityWrapper<CmmPost>().eq("type", 3).orderBy("created_at", false).last("limit " + start+","+limit));
//		List<CmmPost> list = p.getRecords();
        List<CmmPostDto> cmmPostDtos = cmmPostDtoFungoPageResultDto.getData();
        ArrayList<CardIndexBean> indexList = new ArrayList<>();
        for (CmmPostDto post : cmmPostDtos) {
            ArrayList<CardDataBean> gameDateList = new ArrayList<>();
            CardDataBean bean = new CardDataBean();
            CardIndexBean index = new CardIndexBean();
            //封装 文章-游戏/圈子信息
            Map map = indexProxyService.getGameMsgByPost(post);
            index.setPostLinkGameOrCircle(map);

            // 图片/标题/用户名/头像/评论数
            CmmCommunityDto communityDto = new CmmCommunityDto();
            communityDto.setId(post.getCommunityId());
            communityDto.setState(-1);
            @SuppressWarnings("unchecked")
            //@todo

                    CmmCommunityDto c = indexProxyService.selectCmmCommuntityDetail(communityDto);
            new CmmCommunityDto();  //communityService.selectOne(Condition.create().setSqlSelect("id,name,icon,cover_image").eq("id", post.getCommunityId()).ne("state", -1));
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
            index.setCardName("社区置顶文章");
            index.setCardType(8);
            index.setOrder(order);
            index.setSize(1);
            order++;
            index.setDataList(gameDateList);
            indexList.add(index);
        }
        return indexList;
    }

    public CardIndexBean activities() {
        //banner
        List<Banner> bl = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0005")
                .eq("state", "0").orderBy("sort", false).last("limit 1"));
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

    //活动
//    public CardIndexBean activities(InputPageDto input) {
////banner
////        List<Banner> bl = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0005")
////                .eq("state", "0").orderBy("sort", false));  // .last("limit "+(page-1)*limit +" , "+(limit)));
////        Wrapper wrapper = new EntityWrapper<Banner>().eq("position_code", "0005")
////                .eq("state", "0").orderBy("sort", false);
////        Page<Banner> bannerPage = bannerService.selectPage(page,wrapper );
////        List<Banner> bl = page.getRecords();
//
//    }

    //本周精选(游戏)
    public CardIndexBean hotGames() {

        //state 状态 -1:删除,  0：上线，1：草稿，  2：下线
        List<Banner> blist = bannerDao.getBannerByIndex();
//        List<Banner> blist = bannerService.selectList(new EntityWrapper<Banner>().eq("position_code", "0001").eq("target_type", 3)
//                .eq("state", 0).orderBy("release_time", false).last("limit 6"));
        if (null == blist || blist.size() == 0) {
            return null;
        }

        ArrayList<CardDataBean> gameDateList = new ArrayList<>();
        CardIndexBean indexBean = new CardIndexBean();

        for (Banner banner : blist) {

            GameDto gameParam = new GameDto();
            gameParam.setId(banner.getTargetId());
            //state 状态 0：上线，1：下架，-1：删除，3待审核
            gameParam.setState(0);

            GameDto game = iGameProxyService.selectGameById(gameParam);   //gameService.selectById(banner.getTargetId());
            if (null == game) {
                continue;
            }

            Map<String, BigDecimal> rateData = indexProxyService.getRateData(banner.getTargetId());  //gameDao.getRateData(banner.getTargetId());

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
    public CardIndexBean selectPosts(String type) {

        //2 6(视频) 7
        //state  状态 -1:删除,  0：上线，1：草稿，2：下线
        Banner videoBanner = bannerService.selectOne(new EntityWrapper<Banner>().eq("position_code", type).eq("target_type", 1)
                .eq("state", "0").orderBy("sort", false).last("limit 1"));
        CardIndexBean cb = new CardIndexBean();

        if (videoBanner == null) {

            return null;

        } else {

            CmmPostDto cmmPostParam = new CmmPostDto();
            cmmPostParam.setId(videoBanner.getTargetId());
            cmmPostParam.setState(1);

            //postService.selectOne(new EntityWrapper<CmmPost>().eq("id", videoBanner.getTargetId()).eq("state", 1));
            CmmPostDto post = indexProxyService.selctCmmPostOne(cmmPostParam);
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

                dataBean.setContent(post.getContent().length() > 40 ? Html2Text.removeHtmlTag(post.getContent().substring(0, 40)) : Html2Text.removeHtmlTag(post.getContent()));
                //dataBean.setContent(post.getContent());
            } else {

                dataBean.setContent(post.getContent());

            }

            //推荐理由
            dataBean.setUpperLeftCorner(videoBanner.getTag());
            //dataBean.setLowerRightCorner(post.getReportNum()+"");

            dataBean.setReplyNum(post.getReportNum());
            CmmCommunityDto communityParam = new CmmCommunityDto();
            communityParam.setId(post.getCommunityId());
            //communityService.selectById(post.getCommunityId());
            CircleGamePostVo circleGamePostVo = new CircleGamePostVo(CircleGamePostVo.CircleGamePostTypeEnum.POSTID.getKey(),"",post.getId());
            ResultDto<CmmCircleDto> resultDto =  communityFeignClient.getCircleByPost(circleGamePostVo);
//            CmmCommunityDto community = iMemeberProxyService.selectCmmCommunityById(communityParam);

            if (resultDto != null && resultDto.getData() != null) {
                dataBean.setLowerRightCorner( resultDto.getData().getCircleName());
            }

            if (!CommonUtil.isNull(post.getVideo()) && CommonUtil.isNull(videoBanner.getCoverImage())) {
                if ( resultDto.getData() != null) {
                    dataBean.setImageUrl(resultDto.getData().getCircleIcon());
                }
            }

            dataBean.setActionType("1");
            dataBean.setHref(videoBanner.getHref());
            dataBean.setTargetType(videoBanner.getTargetType());
            dataBean.setTargetId(videoBanner.getTargetId());


            // app2.5功能
            // 查询出关联的游戏数据
            String gameId = videoBanner.getGameId();
            if (StringUtils.isNoneBlank(gameId)) {
                GameDto gameDtoParam = new GameDto();
                gameDtoParam.setId(gameId);
                //state 状态 0：上线，1：下架，-1：删除，3待审核
                gameDtoParam.setState(0);

                GameDto gameDtoResult = iGameProxyService.selectGameById(gameDtoParam);
                if (null != gameDtoResult) {
                    ArrayList<GameDto> gameDatas = new ArrayList<GameDto>();
                    gameDatas.add(gameDtoResult);
                    dataBean.setGameDatas(gameDatas);
                }
            }

            ArrayList<CardDataBean> cl = new ArrayList<CardDataBean>();
            cl.add(dataBean);
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

    enum BannnerEnum implements BaseEnum<BannnerEnum, String> {
        lving("1", "living"),
        past("1", "past");

        String key;
        String value;

        BannnerEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String getKey() {
            return key;
        }

        @Override
        public String getValue() {
            return value;
        }
    }

    //----------
}
