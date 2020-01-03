package com.fungo.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.config.NacosFungoCircleConfig;
import com.fungo.community.dao.mapper.BasTagDao;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.dao.service.CmmCommentDaoService;
import com.fungo.community.dto.PostCircleDto;
import com.fungo.community.entity.BasTag;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmComment;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.facede.GameFacedeService;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.feign.GameFeignClient;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.helper.RedisActionHelper;
import com.fungo.community.service.CircleService;
import com.fungo.community.service.CmmCircleService;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.bean.TagBean;
import com.game.common.consts.FunGoGameConsts;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.circle.CircleMemberPulishDto;
import com.game.common.dto.community.*;
import com.game.common.dto.system.CircleFollow;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.dto.user.IncentRankedDto;
import com.game.common.dto.user.IncentRuleRankDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.dto.user.MemberFollowerDto;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.enums.ActionTypeEnum;
import com.game.common.enums.PostTypeEnum;
import com.game.common.enums.circle.CircleTypeEnum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.util.*;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import com.game.common.vo.CircleGamePostVo;
import com.game.common.vo.CmmCirclePostVo;
import com.game.common.vo.CmmCircleVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.game.common.consts.FunGoGameConsts.CACHE_EH_KEY_POST;
import static com.game.common.consts.FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_POST_CACHE;
import static java.util.stream.Collectors.groupingBy;

/**
 * <p>圈子接口实现类</p>
 * @Author: dl.zhang
 * @Date: 2019/6/11
 */
@Service
public class CircleServiceImpl implements CircleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircleServiceImpl.class);

    @Autowired
    private CmmCircleService cmmCircleServiceImap;
    @Autowired
    private CmmCircleMapper cmmCircleMapper;
    @Autowired
    private SystemFeignClient systemFeignClient;
    @Autowired
    private CmmPostCircleMapper cmmPostCircleMapper;
    @Autowired
    private CmmPostDao cmmPostDao;
    @Autowired
    private BasTagDao basTagDao;
    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;
    @Autowired
    private SystemFacedeService systemFacedeService;
    //依赖游戏微服务
    @Autowired(required = false)
    private GameFacedeService gameFacedeService;
    @Autowired
    private CmmCommentDaoService commentService;
    @Autowired
    private FungoCacheArticle fungoCacheArticle;

    @Autowired
    private GameFeignClient gameFeignClient;


    @Override
    public FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re = null;
        int pageNum = cmmCircleVo.getPage();
        int limitNum = cmmCircleVo.getLimit();
        try {
            List<CmmCircleDto> cmmCircleDtoList = new ArrayList<>();
            Page<CmmCircle> page = new Page<>(pageNum, limitNum);
            List<CmmCircle> list = new ArrayList<>();
            if (CmmCircleVo.SorttypeEnum.ALL.getKey().equals(cmmCircleVo.getQueryType())) {
                list = cmmCircleMapper.selectPageByKeyword(page, cmmCircleVo);
                list.stream().forEach(r -> {
                    CmmCircleDto s = new CmmCircleDto();
                    try {
                        BeanUtils.copyProperties(s, r);
                        s.setMemberNum(r.getFolloweeNum());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    LOGGER.info(r.getId());
                    Long postTotal = cmmPostDao.getPostTotalByCircleId(r.getId());
                    s.setPostNum(postTotal);
                    cmmCircleDtoList.add(s);
                });
//            BeanUtils.copyProperties(list,cmmCircleDtoList);
                List<CircleFollow> circleFollows = new ArrayList<>();
                list.stream().forEach(x -> {
                    CircleFollow circleFollow = new CircleFollow();
                    circleFollow.setCircleId(x.getId());
                    circleFollows.add(circleFollow);
                });
                CircleFollowVo circleFollowVo = new CircleFollowVo();
                circleFollowVo.setMemberId(memberId);
                circleFollowVo.setCircleFollows(circleFollows);
                circleFollowVo.setActionType(ActionTypeEnum.FOLLOW.getKey());
                ResultDto<CircleFollowVo> resultDto = systemFeignClient.circleListFollow(circleFollowVo);
                if (resultDto.isSuccess()) {
                    cmmCircleDtoList.stream().forEach(s -> {
                        List<CircleFollow> circleFollow = resultDto.getData().getCircleFollows().stream().filter(e -> e.getCircleId().equals(s.getId())).collect(Collectors.toList());
                        s.setFollow((circleFollow == null || circleFollow.size() == 0) ? false : circleFollow.get(0).isFollow());
                    });
                }
            } else if (CmmCircleVo.SorttypeEnum.BROWSE.getKey().equals(cmmCircleVo.getQueryType())) {
                CircleFollowVo param = new CircleFollowVo();
                param.setMemberId(memberId);
                param.setActionType(ActionTypeEnum.BROWSE.getKey());
                FungoPageResultDto<String> circleFollowVos = systemFeignClient.circleListMineFollow(param);
                if (circleFollowVos != null &&  circleFollowVos.getData().size() > 0) {
                    List<String> ids = circleFollowVos.getData();
                    String sortType = cmmCircleVo.getSortType();
                    List<CmmCircle> cmmCircles = cmmCircleMapper.selectPageByIds(page, sortType, ids);
                    cmmCircles.stream().forEach(r -> {
                        CmmCircleDto s = new CmmCircleDto();
                        try {
                            BeanUtils.copyProperties(s, r);
                            s.setMemberNum(r.getFolloweeNum());
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        s.setFollow(true);
                        LOGGER.info(r.getId());
                        Long postTotal = cmmPostDao.getPostTotalByCircleId(r.getId());
                        s.setPostNum(postTotal);
                        cmmCircleDtoList.add(s);
                    });
                }

            } else if (CmmCircleVo.SorttypeEnum.FOLLOW.getKey().equals(cmmCircleVo.getQueryType())) {
                CircleFollowVo param = new CircleFollowVo();
                param.setMemberId(memberId);
                param.setActionType(ActionTypeEnum.FOLLOW.getKey());
                FungoPageResultDto<String> circleFollowVos = systemFeignClient.circleListMineFollow(param);
                if (circleFollowVos != null && circleFollowVos.getData().size() > 0) {
                    List<String> ids = circleFollowVos.getData();
                    String sortType = cmmCircleVo.getSortType();
                    List<CmmCircle> cmmCircles = cmmCircleMapper.selectPageByIds(page, sortType, ids);
                    cmmCircles.stream().forEach(r -> {
                        CmmCircleDto s = new CmmCircleDto();
                        try {
                            BeanUtils.copyProperties(s, r);
                            s.setMemberNum(r.getFolloweeNum());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        s.setFollow(true);
                        LOGGER.info(r.getId());
                        Long postTotal = cmmPostDao.getPostTotalByCircleId(r.getId());
                        s.setPostNum(postTotal);
                        cmmCircleDtoList.add(s);
                    });
                }
            }
            re = new FungoPageResultDto();
            re.setData(cmmCircleDtoList);
            PageTools.pageToResultDto(re, page);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取圈子集合异常", e);
            re = FungoPageResultDto.error("-1", "获取圈子集合异常");
        }
        return re;
    }

    @Cacheable(value = FunGoGameConsts.CACHE_EH_KEY_PRE_COMMUNITY, key = "'" + FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_INFO_CACHE +" ' +#memberId + #circleId ")
    @Override
    public ResultDto<CmmCircleDto> selectCircleById(String memberId, String circleId) {
        ResultDto<CmmCircleDto> re = new ResultDto<>( );
        CmmCircleDto cmmCircleDto = null;
        try {
            String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_INFO;
            String keySuffix = memberId + circleId;
            cmmCircleDto = (CmmCircleDto) fungoCacheArticle.getIndexDecodeCache(keyPrefix, keySuffix);
            if (null != cmmCircleDto ) {
                re = ResultDto.success(cmmCircleDto);
                return re;
            }
            cmmCircleDto = new CmmCircleDto();
            //fix bug:管控台 下架游戏圈 进入对应的游戏内 不显示圈子选项 [by mxf 2019-7-11]
            EntityWrapper<CmmCircle> cmmCircleEntityWrapper = new EntityWrapper<CmmCircle>();
            cmmCircleEntityWrapper.eq("id", circleId);
            cmmCircleEntityWrapper.eq("state", 1);

            CmmCircle cmmCircle = cmmCircleServiceImap.selectOne(cmmCircleEntityWrapper);
            //end

            if (cmmCircle == null) {
                return ResultDto.error("-1", "该圈子不存在");
            }
            BeanUtils.copyProperties(cmmCircleDto, cmmCircle);
            cmmCircleDto.setMemberNum(cmmCircle.getFolloweeNum());
            CircleFollowVo circleFollowVo = new CircleFollowVo();
            circleFollowVo.setMemberId(memberId);
            circleFollowVo.setActionType(ActionTypeEnum.FOLLOW.getKey());
            Long postTotal = cmmPostDao.getPostTotalByCircleId(circleId);
            cmmCircleDto.setPostNum(postTotal);
            CircleFollow circleFollow = new CircleFollow();
            circleFollow.setCircleId(circleId);
            circleFollowVo.setCircleFollows(Arrays.asList(circleFollow));
            ResultDto<CircleFollowVo> resultDto = systemFeignClient.circleListFollow(circleFollowVo);
            if (resultDto != null && resultDto.getData() != null && resultDto.getData().getCircleFollows() != null) {
                List<CircleFollow> circleFollows = resultDto.getData().getCircleFollows().stream().filter(r -> r.getCircleId().equals(circleId)).collect(Collectors.toList());
                cmmCircleDto.setFollow((circleFollows == null || circleFollows.size() == 0) ? false : circleFollows.get(0).isFollow());
            }
            ResultDto<GameDto>  resultGameDto = gameFeignClient.selectGameDetails(cmmCircleDto.getGameId(),0);
            if(resultGameDto != null && resultGameDto.getData() != null && !CommonUtil.isNull( resultGameDto.getData().getId()) ){
                cmmCircleDto.setGameStatus(1);
            }
            List<Map<String, Object>> map = getCirclePayer(cmmCircle);
            cmmCircleDto.setEliteMembers(map);
            fungoCacheArticle.excIndexDecodeCache(true, keyPrefix, keySuffix, cmmCircleDto, RedisActionHelper.getRandomRedisCacheTime());
            re = ResultDto.success(cmmCircleDto);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取圈子详情异常，圈子id="+circleId, e);
            re = ResultDto.error("-1", "获取圈子详情异常");
        }
        return re;
    }

    @Cacheable(value = CACHE_EH_KEY_POST,key = "'" + FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_POST_CACHE +" ' +#memberId + #cmmCirclePostVo.circleId + #cmmCirclePostVo.queryType + #cmmCirclePostVo.sortType + #cmmCirclePostVo.page + #cmmCirclePostVo.limit " )
    @Override
    public FungoPageResultDto<PostOutBean> selectCirclePost(String memberId, CmmCirclePostVo cmmCirclePostVo) {
        FungoPageResultDto<PostOutBean> re = null;
        List<CmmPost> cmmPosts = new ArrayList<>();
        String userId = memberId;
        List<PostOutBean> relist = null;
        String circleId = cmmCirclePostVo.getCircleId();
        Page page = new Page(cmmCirclePostVo.getPage(), cmmCirclePostVo.getLimit());

        try {
            String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_POST;
            String keySuffix = JSON.toJSONString(cmmCirclePostVo)+memberId;
            re = (FungoPageResultDto<PostOutBean>) fungoCacheArticle.getIndexDecodeCache(keyPrefix, keySuffix);
            if (null != re) {
                return re;
            }
            relist = new ArrayList<>();
            re = new FungoPageResultDto<>();
            String tagId = cmmCirclePostVo.getQueryType();
            String sortType = cmmCirclePostVo.getSortType();
            if (CmmCirclePostVo.QueryTypeEnum.ALL.getKey().equals(cmmCirclePostVo.getQueryType())) {  // 全部查询
                cmmPosts = cmmPostDao.getAllCmmCircleListByCircleId(page, circleId, null, null, sortType);
            } else if (CmmCirclePostVo.QueryTypeEnum.ESSENCE.getKey().equals(cmmCirclePostVo.getQueryType())) { // 精华查询
                cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page, circleId, null, PostTypeEnum.CREAM.getKey(), sortType);
            }else if(CmmCirclePostVo.QueryTypeEnum.TOP.getKey().equals(cmmCirclePostVo.getQueryType())){
                cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page, circleId, null, PostTypeEnum.TOP.getKey(), CmmCirclePostVo.SortTypeEnum.PUBDATE.getKey());
            } else {
                if (CmmCirclePostVo.SortTypeEnum.PUBDATE.getKey().equals(cmmCirclePostVo.getSortType())) {
                    cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page, circleId, tagId, null, sortType);
                } else if (CmmCirclePostVo.SortTypeEnum.PUBREPLY.getKey().equals(cmmCirclePostVo.getSortType())) {
                    cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page, circleId, tagId, null, sortType);
                } else if (CmmCirclePostVo.SortTypeEnum.ESSENCE.getKey().equals(cmmCirclePostVo.getSortType())) {
                    cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page, circleId, tagId, null, sortType);
                } else if (CmmCirclePostVo.SortTypeEnum.DISCUSS.getKey().equals(cmmCirclePostVo.getSortType())) {
                    cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page, circleId, tagId, null, sortType);
                }
            }
            for (CmmPost post : cmmPosts) {
                //表情解码
                if (StringUtils.isNotBlank(post.getTitle())) {
                    String interactTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());
                    post.setTitle(interactTitle);
                }
                if (StringUtils.isNotBlank(post.getContent())) {
                    String interactContent = FilterEmojiUtil.decodeEmoji(post.getContent());
                    //bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                    interactContent = interactContent.length() > 40 ? Html2Text.removeHtmlTag(interactContent.substring(0, 40)) : Html2Text.removeHtmlTag(interactContent);

                    post.setContent(interactContent);
                }

//	        if (StringUtils.isNotBlank(post.getHtmlOrigin())) {
//	            String interactHtmlOrigin = FilterEmojiUtil.resolveToEmojiFromByte(post.getHtmlOrigin());
//	            post.setHtmlOrigin(interactHtmlOrigin);
//	        }


                PostOutBean bean = new PostOutBean();
                // 设置分类名称
                String tags = post.getTags();
                if(StringUtil.isNotNull(tags)){
                    BasTag basTag = basTagDao.selectById(tags);
                    if(basTag!=null){
                        bean.setTagName(basTag.getName());
                    }
                }
                bean.setMemberId( post.getMemberId());
                //!fixme 查询用户数据
//                bean.setAuthor(iUserService.getAuthor(post.getMemberId()));
//                try {
//                    ResultDto<AuthorBean> authorBeanResultDto = systemFacedeService.getAuthor(post.getMemberId());
//                    if (null != authorBeanResultDto) {
//                        AuthorBean authorBean = authorBeanResultDto.getData();
//                        bean.setAuthor(authorBean);
//                    }
//                } catch (Exception ex) {
//                    ex.printStackTrace();
//                }
//
//                if (bean.getAuthor() == null) {
//                    continue;
//                }
                String content = post.getContent();
                if (!CommonUtil.isNull(content)) {
                    // bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                    bean.setContent(CommonUtils.filterWord(content));
                }

                bean.setUpdated_at(DateTools.fmtDate(post.getUpdatedAt()));

                //fix bug:把V2.4.2存在的createdAt字段，恢复回来 [by mxf 2019-01-08]
                bean.setCreatedAt(DateTools.fmtDate(post.getCreatedAt()));
                //end

                bean.setVideoUrl(post.getVideo() == null ? "" : post.getVideo());
                bean.setImageUrl(post.getCoverImage() == null ? "" : post.getCoverImage());
                bean.setLikeNum(post.getLikeNum() == null ? 0 : post.getLikeNum());
                bean.setPostId(post.getId());
                bean.setReplyNum(post.getCommentNum() == null ? 0 : post.getCommentNum());
                bean.setTitle(CommonUtils.filterWord(post.getTitle()));
//			bean.setCommunityIcon(community.getIcon());
//			bean.setCommunityId(community.getId());
//			bean.setCommunityName(community.getName());
//			if(!CommonUtil.isNull(post.getVideo()) && CommonUtil.isNull(post.getCoverImage())) {
//				bean.setImageUrl(community.getCoverImage());
//			}
                try {
                    if (!CommonUtil.isNull(post.getImages())) {
                        ArrayList<String> readValue = new ArrayList<String>();
                        ObjectMapper mapper = new ObjectMapper();
                        readValue = mapper.readValue(post.getImages(), ArrayList.class);

                        //fix bug: Could not read JSON: Cannot construct instance of `java.util.ArrayList$SubList` [by mxf 2019-03-20]
                        int readValueSize = readValue.size();
                        List readValueList = new ArrayList();
                        if (readValueSize > 3) {
                            readValueList.addAll(readValue.subList(0, 3));
                            bean.setImages(readValueList);
                        } else {
                            bean.setImages(readValue);
                        }
                        //老代码
                        //bean.setImages(readValue.size() > 3 ? readValue.subList(0, 3) : readValue);
                        //end
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (CommonUtil.isNull(userId)) {
                    bean.setLiked(false);
                } else {

                    //!fixme 查询用户点赞数
                    //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", post.getId()).eq("member_id", userId));

                    BasActionDto basActionDto = new BasActionDto();
                    basActionDto.setMemberId(userId);
                    basActionDto.setType(0);
                    basActionDto.setState(0);
                    basActionDto.setTargetId(post.getId());
                    basActionDto.setCircleId(cmmCirclePostVo.getCircleId());
                    int liked = 0;
                    try {
                        ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);

                        if (null != resultDto) {
                            liked = resultDto.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    bean.setLiked(liked > 0 ? true : false);
                }
                //
                bean.setVideoCoverImage(post.getVideoCoverImage());
                bean.setType(post.getType());
                /**
                 * 功能描述: 根据文章查询是否有圈子
                 * @auther: dl.zhang
                 * @date: 2019/6/27 15:40
                 */
//                CmmCircle cmmCircle = cmmPostCircleMapper.getCircleEntityByPostId(bean.getPostId());
//                if (cmmCircle != null) {
//                    bean.setCircleId(cmmCircle.getId());
//                    bean.setCircleName(cmmCircle.getCircleName());
//                    bean.setCircleIcon(cmmCircle.getCircleIcon());
//                }
                relist.add(bean);
            }
            List<String> postIds = cmmPosts.stream().map( CmmPost::getId ).collect( Collectors.toList());
           List<PostCircleDto> postCircleDtos =  cmmPostCircleMapper.getCircleEntityByPostIds(postIds);
            List<String> memberIds = cmmPosts.stream().map( CmmPost::getMemberId ).collect( Collectors.toList());
            FungoPageResultDto<AuthorBean>  resultDto =  systemFeignClient.getAuthorList(String.join(",", memberIds));
            List<AuthorBean> authorBeans = resultDto != null ? resultDto.getData() : new ArrayList<>();
           relist.stream().forEach( s ->{
                Optional<PostCircleDto> postCircles = postCircleDtos.stream().filter( x-> s.getPostId().equals( x.getPostId())).findFirst();
                if(postCircles.isPresent()){
                    PostCircleDto postCircleDto = postCircles.get();
                    s.setCircleId( postCircleDto.getId());
                    s.setCircleName(postCircleDto.getCircleName());
                    s.setCircleIcon(postCircleDto.getCircleIcon());
                }
               Optional<AuthorBean> authorBean = authorBeans.stream().filter(  c ->s.getMemberId().equals( c.getObjectId())).findFirst();
                if(authorBean.isPresent()){
                    s.setAuthor( authorBean.get());
                }
            });
            re.setData(relist);
            PageTools.pageToResultDto(re, page);
            fungoCacheArticle.excIndexDecodeCache(true, keyPrefix, keySuffix, re, RedisActionHelper.getRandomRedisCacheTime());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("圈子获取下属文章异常，圈子id="+circleId, e);
        }
        return re;
    }

    /**
     * 功能描述: 查询圈子的文章类型
     * @param: [memberId, cmmCirclePostVo]
     * @return: com.game.common.dto.ResultDto<com.game.common.dto.community.CircleTypeDto>
     * @auther: dl.zhang
     * @date: 2019/6/20 15:38
     */
    @Override
    public ResultDto<List<CirclePostTypeDto>> selectCirclePostType(String memberId, CmmCirclePostVo cmmCirclePostVo) {
        List<CirclePostTypeDto> circleTypeDtos = new ArrayList<>();
        ResultDto<List<CirclePostTypeDto>> re = new ResultDto<>();
        String circleId = cmmCirclePostVo.getCircleId();
        try {
            List<CmmPost> cmmPosts = cmmPostDao.getCmmCircleListByPostId(circleId);
            List<TagBean> tagBeans = basTagDao.getPostTags();
            Map<String, List<CmmPost>> cmmPostMap = cmmPosts.stream().filter(x ->( x.getTags() != null && !x.getTags().equals("")) ).collect(groupingBy(CmmPost::getTags));
            Iterator<String> iter = cmmPostMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List<CmmPost> valueList = cmmPostMap.get(key);
                if (valueList.size() < nacosFungoCircleConfig.getValue())
                    iter.remove();
            }
            for (String key : cmmPostMap.keySet()) {
                CirclePostTypeDto circleTypeDto = new CirclePostTypeDto();
                TagBean cmmPost = tagBeans.stream().filter(r -> r.getId().equals(key)).findFirst().orElse(new TagBean());
                circleTypeDto.setCirclePostType(cmmPost.getId());
                circleTypeDto.setCirclePostName(cmmPost.getName());
                circleTypeDto.setSort(cmmPost.getSort());
                circleTypeDtos.add(circleTypeDto);
            }
            circleTypeDtos = circleTypeDtos.stream().sorted(Comparator.comparing(CirclePostTypeDto::getSort)).collect(Collectors.toList());
            re.setData(circleTypeDtos);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询圈子的文章类型异常，圈子id="+circleId, e);
        }
        return re;
    }

    @Override
    public ResultDto<List<CircleTypeDto>> selectCircleType(String memberId) {
        List<CircleTypeDto> circleTypeDtos = new ArrayList<>();
        ResultDto<List<CircleTypeDto>> re = new ResultDto<>();
        try {
            List<CmmCircle> cmmCircleList = cmmCircleServiceImap.selectList(new EntityWrapper<CmmCircle>().eq("state", "1"));
            Map<Integer, List<CmmCircle>> cmmCircleMap = cmmCircleList.stream().collect(groupingBy(CmmCircle::getType));
            for (Integer key : cmmCircleMap.keySet()) {
                CircleTypeDto cmmCircle = new CircleTypeDto();
                cmmCircle.setCirclePostType(key.toString());
                cmmCircle.setCirclePostName(CmmCircleVo.TypeEnum.getValueByKey(key.toString()));
                circleTypeDtos.add(cmmCircle);
            }
            re.setData(circleTypeDtos);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询圈子的文章类型", e);
        }
        return re;
    }

    /**
     * 功能描述: 根据游戏id或者文章id查询是否有圈子
     * @param: [memberId, circleGamePostVo]
     * @return: com.game.common.dto.ResultDto<com.game.common.dto.community.CmmCircleDto>
     * @auther: dl.zhang
     * @date: 2019/7/16 15:54
     */
    @Override
    public ResultDto<CmmCircleDto> selectCircleByGame(String memberId, CircleGamePostVo circleGamePostVo) {
        ResultDto<CmmCircleDto> resultDto = new ResultDto<>();
        try {
            List<CmmCircle> cmmCircleDtoList = new ArrayList<>();
            Wrapper wrapper = new EntityWrapper<CmmCircle>();

            //fix bug:圈子状态 状态  -1:已删除,0:待上架,1:运营中  [by mxf 2019-07-11]


            //end
            if(CircleGamePostVo.CircleGamePostTypeEnum.GAMESID.getKey() == circleGamePostVo.getType()){
                wrapper.eq("state", 1);
                wrapper.eq("game_id", circleGamePostVo.getGameId());
                cmmCircleDtoList = cmmCircleServiceImap.selectList(wrapper);
            }else if(CircleGamePostVo.CircleGamePostTypeEnum.POSTID.getKey() == circleGamePostVo.getType()) {
                CmmCircle cmmCircle = cmmPostCircleMapper.getCircleByPostId(circleGamePostVo.getPost());
                if(cmmCircle != null)
                    cmmCircleDtoList.add(cmmCircle);
            }
            if (cmmCircleDtoList.size() > 0) {
                CmmCircle cmmCircle = cmmCircleDtoList.get(0);
                CmmCircleDto cmmCircleDto = new CmmCircleDto();
                BeanUtils.copyProperties(cmmCircleDto, cmmCircle);
                cmmCircleDto.setMemberNum(cmmCircle.getFolloweeNum());
                resultDto.setData(cmmCircleDto);
                resultDto.setCode(AbstractResultEnum.CODE_ONE.getKey());
                resultDto.setMessage(AbstractResultEnum.CODE_ONE.getSuccessValue());
            } else {
                resultDto.setCode(AbstractResultEnum.CODE_TWO.getKey());
                resultDto.setMessage(AbstractResultEnum.CODE_TWO.getSuccessValue());// 代表没有圈子
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("查询该游戏是否是游戏圈或文章获取关联圈，gameId="+circleGamePostVo.getGameId()+"postId="+circleGamePostVo.getPost() , e);
        }
        return resultDto;
    }

    /**
     * 功能描述:  根据传递的游戏id查询社区的文章和游戏关联的文章（不在社区内）
     * @param: [memberId, circleGamePostVo]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.community.PostOutBean>
     * @auther: dl.zhang
     * @date: 2019/6/21 14:28
     */
    @Cacheable(cacheNames={FunGoGameConsts.CACHE_EH_KEY_POST} ,key = "'" + FungoCoreApiConstant.FUNGO_CORE_API_GAMR_POSTS_CACHE +" ' +#userId + #circleGamePostVo.gameId + #circleGamePostVo.page + #circleGamePostVo.limit ")
    @Override
    public FungoPageResultDto<PostOutBean> selectCircleGamePost(String userId, CircleGamePostVo circleGamePostVo) {
        FungoPageResultDto<PostOutBean> re = null;
        List<PostOutBean> relist = null;
        Page page = new Page(circleGamePostVo.getPage(), circleGamePostVo.getLimit());
        String gameId = circleGamePostVo.getGameId();
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_GAMR_POSTS;
//        String keySuffix = JSON.toJSONString(circleGamePostVo);
//        relist = (List<PostOutBean>) fungoCacheArticle.getIndexDecodeCache(keyPrefix, keySuffix);
//        if (null != relist &&  relist.size() > 0) {
//            re.setData(relist);
//            PageTools.pageToResultDto(re, page);
//            return re;
//        }
        try {
            relist = new ArrayList<>();
            List<CmmPost> cmmPosts = cmmPostDao.getCmmPostByGameId(page, gameId);
            for (CmmPost post : cmmPosts) {
                //表情解码
                if (StringUtils.isNotBlank(post.getTitle())) {
                    String interactTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());
                    post.setTitle(interactTitle);
                }
                if (StringUtils.isNotBlank(post.getContent())) {
                    String interactContent = FilterEmojiUtil.decodeEmoji(post.getContent());

                    //bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                    interactContent = interactContent.length() > 40 ? Html2Text.removeHtmlTag(interactContent.substring(0, 40)) : Html2Text.removeHtmlTag(interactContent);

                    post.setContent(interactContent);
                }

//	        if (StringUtils.isNotBlank(post.getHtmlOrigin())) {
//	            String interactHtmlOrigin = FilterEmojiUtil.resolveToEmojiFromByte(post.getHtmlOrigin());
//	            post.setHtmlOrigin(interactHtmlOrigin);
//	        }
                PostOutBean bean = new PostOutBean();
                //!fixme 查询用户数据
                //bean.setAuthor(iUserService.getAuthor(post.getMemberId()));
                try {
                    ResultDto<AuthorBean> authorBeanResultDto = systemFacedeService.getAuthor(post.getMemberId());
                    if (null != authorBeanResultDto) {
                        AuthorBean authorBean = authorBeanResultDto.getData();
                        bean.setAuthor(authorBean);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                if (bean.getAuthor() == null) {
//                    continue;
//                }
                String content = post.getContent();
                if (!CommonUtil.isNull(content)) {
                    // bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                    bean.setContent(CommonUtils.filterWord(content));
                }

                bean.setUpdated_at(DateTools.fmtDate(post.getUpdatedAt()));

                //fix bug:把V2.4.2存在的createdAt字段，恢复回来 [by mxf 2019-01-08]
                bean.setCreatedAt(DateTools.fmtDate(post.getCreatedAt()));
                //end

                bean.setVideoUrl(post.getVideo() == null ? "" : post.getVideo());
                bean.setImageUrl(post.getCoverImage() == null ? "" : post.getCoverImage());
                bean.setLikeNum(post.getLikeNum() == null ? 0 : post.getLikeNum());
                bean.setPostId(post.getId());
                bean.setReplyNum(post.getCommentNum() == null ? 0 : post.getCommentNum());
                bean.setTitle(CommonUtils.filterWord(post.getTitle()));
//			bean.setCommunityIcon(community.getIcon());
//			bean.setCommunityId(community.getId());
//			bean.setCommunityName(community.getName());
//			if(!CommonUtil.isNull(post.getVideo()) && CommonUtil.isNull(post.getCoverImage())) {
//				bean.setImageUrl(community.getCoverImage());
//			}
                try {
                    if (!CommonUtil.isNull(post.getImages())) {
                        ArrayList<String> readValue = new ArrayList<String>();
                        ObjectMapper mapper = new ObjectMapper();
                        readValue = mapper.readValue(post.getImages(), ArrayList.class);

                        //fix bug: Could not read JSON: Cannot construct instance of `java.util.ArrayList$SubList` [by mxf 2019-03-20]
                        int readValueSize = readValue.size();
                        List readValueList = new ArrayList();
                        if (readValueSize > 3) {
                            readValueList.addAll(readValue.subList(0, 3));
                            bean.setImages(readValueList);
                        } else {
                            bean.setImages(readValue);
                        }
                        //老代码
                        //bean.setImages(readValue.size() > 3 ? readValue.subList(0, 3) : readValue);
                        //end
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (CommonUtil.isNull(userId)) {
                    bean.setLiked(false);
                } else {
                    //!fixme 查询用户点赞数
                    //int liked = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 0).ne("state", "-1").eq("target_id", post.getId()).eq("member_id", userId));
                    BasActionDto basActionDto = new BasActionDto();
                    basActionDto.setMemberId(userId);
                    basActionDto.setType(0);
                    basActionDto.setState(0);
                    basActionDto.setTargetId(post.getId());
                    int liked = 0;
                    try {
                        ResultDto<Integer> resultDto = systemFacedeService.countActionNum(basActionDto);
                        if (null != resultDto) {
                            liked = resultDto.getData();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    bean.setLiked(liked > 0 ? true : false);
                }
                //
                bean.setVideoCoverImage(post.getVideoCoverImage());
                bean.setType(post.getType());
                /**
                 * 功能描述: 根据文章查询是否有圈子
                 * @auther: dl.zhang
                 * @date: 2019/6/27 15:40
                 */
                CmmCircle cmmCircle = cmmPostCircleMapper.getCircleEntityByPostId(bean.getPostId());
                if (cmmCircle != null) {
                    bean.setCircleId(cmmCircle.getId());
                    bean.setCircleName(cmmCircle.getCircleName());
                    bean.setCircleIcon(cmmCircle.getCircleIcon());
                }
                relist.add(bean);
            }
            re = new FungoPageResultDto<>();
//            fungoCacheArticle.excIndexDecodeCache(true, keyPrefix, keySuffix, relist, RedisActionHelper.getRandomRedisCacheTime());
            re.setData(relist);
            PageTools.pageToResultDto(re, page);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("根据游戏id获取文章,游戏id="+gameId, e);
            re = FungoPageResultDto.error("-1", "根据游戏id获取文章异常");
        }
        return re;
    }

    @Cacheable(cacheNames={FunGoGameConsts.CACHE_EH_KEY_PRE_COMMUNITY} ,key = "'" + FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_GAME_CACHE +" ' +#memberId + #circleGamePostVo.gameId + #circleGamePostVo.page + #circleGamePostVo.limit ")
    @Override
    public FungoPageResultDto<CmmCircleDto> selectGameCircle(String memberId, CircleGamePostVo circleGamePostVo) {
        FungoPageResultDto<CmmCircleDto> re = new FungoPageResultDto<>();
        List<CmmCircleDto> cmmCircleDtoList = null;
        Page page = new Page(circleGamePostVo.getPage(), circleGamePostVo.getLimit());
        String gameId = circleGamePostVo.getGameId();
        try {
            String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_CIRCLE_GAME;
            String keySuffix = JSON.toJSONString(circleGamePostVo);
            cmmCircleDtoList = (List<CmmCircleDto>) fungoCacheArticle.getIndexDecodeCache(keyPrefix, keySuffix);
            if (null != cmmCircleDtoList &&  cmmCircleDtoList.size() > 0) {
                re.setData(cmmCircleDtoList);
                PageTools.pageToResultDto(re, page);
                return re;
            }
            cmmCircleDtoList = new ArrayList<>();
            List<CmmCircle> cmmCircles = cmmCircleMapper.selectCircleByGame(page, gameId);
            if (cmmCircles != null && cmmCircles.size() > 0) {
                List<CmmCircleDto> finalCmmCircleDtoList = cmmCircleDtoList;
                cmmCircles.stream().forEach( s -> {
                    CmmCircleDto cmmCircleDto = new CmmCircleDto();
                    try {
                        BeanUtils.copyProperties(cmmCircleDto, s);
                        cmmCircleDto.setMemberNum(s.getFolloweeNum());
                        finalCmmCircleDtoList.add(cmmCircleDto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            re = new FungoPageResultDto<>();
            fungoCacheArticle.excIndexDecodeCache(true, keyPrefix, keySuffix, cmmCircleDtoList, RedisActionHelper.getRandomRedisCacheTime());
            re.setData(cmmCircleDtoList);
            PageTools.pageToResultDto(re, page);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取游戏相关的圈子异常，游戏id="+gameId, e);
            re = FungoPageResultDto.error("-1", "根据游戏查询相关圈子异常");
        }
        return re;
    }

    @Override
    public void updateCircleHotValue() throws Exception {
        try {
            Wrapper wrapper = new EntityWrapper<CmmCircle>();
            List<CmmCircle> cmmCircleList = cmmCircleMapper.selectList(wrapper);
            cmmCircleList.stream().forEach(s -> {
                try {
                    int hotValueTotalNumber = cmmPostCircleMapper.getSumByCircleId(s.getId());
                    s.setHotValue(hotValueTotalNumber);
                    cmmCircleMapper.updateByPrimaryKey(s);
                } catch (Exception e) {
                    LOGGER.error("更新圈子热度失败,圈子id=" + s.getId(), e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("更新圈子热度失败", e);
            throw new Exception("更新圈子热度失败");
        }
    }

    @Override
    public FungoPageResultDto<CommunityMember> selectCirclePlayer(String memberId, CmmCirclePostVo cmmCirclePostVo) {
        FungoPageResultDto<CommunityMember> re = null;
        String circleId = cmmCirclePostVo.getCircleId();
        try {
            List<CommunityMember> userList = new ArrayList<>();
            Page<CircleMemberPulishDto> page = new Page<>(cmmCirclePostVo.getPage(), cmmCirclePostVo.getLimit());
            List<CircleMemberPulishDto> cmmPostCircles = cmmPostCircleMapper.getCmmPostCircleByCircleId(page, circleId);
            CmmCircle cmmCircle = cmmCircleMapper.selectById(circleId);
            //从游戏评论表获取用户数量
            ResultDto<List<MemberPulishFromCommunity>> gameMemberCtmRs = gameFacedeService.getMemberOrder(cmmCircle.getGameId(), null);
            if (null != gameMemberCtmRs) {
                List<MemberPulishFromCommunity> pulishFromCmtList = gameMemberCtmRs.getData();
                if (null != pulishFromCmtList && !pulishFromCmtList.isEmpty()) {
                    if (null != cmmPostCircles && !cmmPostCircles.isEmpty()) {
                        cmmPostCircles.stream().forEach(s -> {
                            List<MemberPulishFromCommunity> list = pulishFromCmtList.stream().filter(r -> r.getMemberId().equals(s.getMemberId())).collect(Collectors.toList());
                            if (list != null && list.size() > 0) {
                                MemberPulishFromCommunity entity = list.get(0);
                                s.setGamecommentNum(entity.getCommentNum());
                                s.setLikeNum(entity.getLikeNum());
                            }
                        });
                    }
                }
            }
//            cmmPostCircles = cmmPostCircles.stream().sorted((u1, u2) -> {
//                Integer u1sum = (u1.getCommentNum() + u1.getEvaNum() + u1.getGamecommentNum() + u1.getLikeNum() + u1.getPostCommentNum() + u1.getPostLikeNum());
//                Integer u2sum = (u1.getCommentNum() + u1.getEvaNum() + u1.getGamecommentNum() + u1.getLikeNum() + u1.getPostCommentNum() + u1.getPostLikeNum());
//                return u1sum.compareTo(u2sum);
//            }).collect(Collectors.toList());
            cmmPostCircles.stream().forEach(x -> {
                x.setTotalNum( x.getTotalNum() + x.getLikeNum() + x.getGamecommentNum());
            });
            cmmPostCircles = cmmPostCircles.stream().sorted(Comparator.comparing(CircleMemberPulishDto::getTotalNum).reversed()).collect(Collectors.toList());
            for (CircleMemberPulishDto circleMemberPulishDto : cmmPostCircles) {
                CommunityMember communityMember = new CommunityMember();
                try {
                    ResultDto<AuthorBean> authorBeanResultDto = systemFacedeService.getAuthor(circleMemberPulishDto.getMemberId());
                    if (null != authorBeanResultDto) {
                        AuthorBean authorBean = authorBeanResultDto.getData();
                        communityMember.setAuthorBean(authorBean);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                communityMember.setMerits(circleMemberPulishDto.getTotalNum());
                if (memberId.equals(circleMemberPulishDto.getMemberId())) {
                    communityMember.setYourself(true);
                } else {
                    MemberFollowerDto memberFollowerDtoParam = new MemberFollowerDto();
                    memberFollowerDtoParam.setMemberId(memberId);
                    memberFollowerDtoParam.setFollowerId(circleMemberPulishDto.getMemberId());
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
                        communityMember.setFollowed(true);
                    }
                }
                userList.add(communityMember);
            }
            re = new FungoPageResultDto();
            re.setData(userList);
            PageTools.pageToResultDto(re,page);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取圈子玩家榜异常,圈子id="+circleId, e);
            re = FungoPageResultDto.error("-1", "获取玩家榜异常");
        }
        return re;
    }

    @Override
    public ResultDto<List<String>> listCircleNameByPost(String postId) {
        List<String> circleNameByPost = cmmCircleMapper.listCircleNameByPost(postId);
        return ResultDto.success(circleNameByPost);
    }

   @Override
    public ResultDto<List<String>> listCircleNameByComment(String commentId) {
    //根据评论id获取文章
    CmmComment comment = commentService.selectById(commentId);
    if (comment != null) {
        List<String> circleNameByPost = cmmCircleMapper.listCircleNameByPost(comment.getPostId());
        return ResultDto.success(circleNameByPost);
    }
    return ResultDto.success();
}
    public ResultDto<CmmCircleDto> selectCircleByPostId(String postId) throws Exception {
        ResultDto<CmmCircleDto> re ;
        CmmCircleDto cmmCircleDto = new CmmCircleDto();
        try {
          List<CmmCircle> cmmCircleDtoList =  cmmCircleMapper.selectCircleByPostId(postId);
          if(cmmCircleDtoList != null && cmmCircleDtoList.size() > 0){
              CmmCircle cmmCircle = cmmCircleDtoList.get(0);
               cmmCircleDto = new CmmCircleDto();
                BeanUtils.copyProperties( cmmCircleDto, cmmCircle);
          }
            re = ResultDto.ResultDtoFactory.buildSuccess( cmmCircleDto );
        }catch (Exception e){
            LOGGER.error( "根据文章id查询圈子信息,文章id="+postId,e);
            re = ResultDto.ResultDtoFactory.buildError( "根据文章id查询圈子信息异常" );
        }
        return re;
    }

    @Override
    public FungoPageResultDto<CmmCircleDto> getCircleListByType(CircleGamePostVo circleGamePostVo) {
        FungoPageResultDto<CmmCircleDto> resultDto = null;
        try {
            List<CmmCircle> cmmCircleDtoList =   cmmCircleMapper.selectList( new EntityWrapper<CmmCircle>().setSqlSelect( "id" ).eq( "state","1" ).eq( "type",circleGamePostVo.getType() ) );
            List<CmmCircleDto> cmmCircleDtos = new ArrayList<>( );
            cmmCircleDtoList.stream().forEach( s ->{
                CmmCircleDto cmmCircleDto = new CmmCircleDto();
                cmmCircleDto.setId(s.getId());
                cmmCircleDtos.add( cmmCircleDto);
            });
            resultDto =new  FungoPageResultDto();
            resultDto.setData( cmmCircleDtos );

        }catch (Exception e){
            LOGGER.error( "获取官方圈子失败",e );
            resultDto = FungoPageResultDto.FungoPageResultDtoFactory.buildError( "获取官方圈子失败" );
        }
        return resultDto;
    }


    /**
     * 功能描述: 
     * @param: []
     * @return: java.util.List 前六名玩家用户集合
     * @auther: dl.zhang
     * @date: 2019/6/26 15:19
     */
    private List<Map<String, Object>> getCirclePayer(CmmCircle cmmCircle) {
        List<Map<String, Object>> userList = new ArrayList<>();
        try {
            Page<CircleMemberPulishDto> page = new Page<>(1, 10);
            List<CircleMemberPulishDto> cmmPostCircles = cmmPostCircleMapper.getCmmPostCircleByCircleId(page, cmmCircle.getId());
            if (CircleTypeEnum.GAME.getKey().equals(cmmCircle.getType().toString())) {
                //从游戏评论表获取用户数量
                ResultDto<List<MemberPulishFromCommunity>> gameMemberCtmRs = gameFacedeService.getMemberOrder(cmmCircle.getGameId(), null);
                if (null != gameMemberCtmRs) {
                    List<MemberPulishFromCommunity> pulishFromCmtList = gameMemberCtmRs.getData();
                    if (null != pulishFromCmtList && !pulishFromCmtList.isEmpty()) {
                        if (null != cmmPostCircles && !cmmPostCircles.isEmpty()) {
                            cmmPostCircles.stream().forEach(s -> {
                                List<MemberPulishFromCommunity> list = pulishFromCmtList.stream().filter(r -> r.getMemberId().equals(s.getMemberId())).collect(Collectors.toList());
                                if (list != null && list.size() > 0) {
                                    MemberPulishFromCommunity entity = list.get(0);
                                    s.setGamecommentNum(entity.getCommentNum());
                                    s.setLikeNum(entity.getLikeNum());
                                }
                            });
                        }
                    }
                }

            }
            cmmPostCircles = cmmPostCircles.stream().sorted((u1, u2) -> {
                Integer u1sum = (u1.getCommentNum() + u1.getEvaNum() + u1.getGamecommentNum() + u1.getLikeNum() + u1.getPostCommentNum() + u1.getPostLikeNum());
                Integer u2sum = (u1.getCommentNum() + u1.getEvaNum() + u1.getGamecommentNum() + u1.getLikeNum() + u1.getPostCommentNum() + u1.getPostLikeNum());
                return u1sum.compareTo(u2sum);
            }).limit(6).collect(Collectors.toList());


            ObjectMapper mapper = new ObjectMapper();
            for (CircleMemberPulishDto m : cmmPostCircles) {
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
                            ArrayList<HashMap<String, Object>> infolist = null;
                            try {
                                infolist = mapper.readValue(rankinf, ArrayList.class);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取当前圈子的前六名玩家榜异常，圈子id="+cmmCircle.getId(),e);
        }
        return userList;
    }
}
