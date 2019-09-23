package com.fungo.community.service.portal.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.config.NacosFungoCircleConfig;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.dao.mapper.CmmPostGameMapper;
import com.fungo.community.dao.service.BasVideoJobDaoService;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.dao.service.impl.ESDAOServiceImpl;
import com.fungo.community.entity.*;
import com.fungo.community.facede.GameFacedeService;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.facede.TSMQFacedeService;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.function.FungoLivelyCalculateUtils;
import com.fungo.community.function.SerUtils;
import com.fungo.community.service.ICounterService;
import com.fungo.community.service.IPostService;
import com.fungo.community.service.IVideoService;
import com.fungo.community.service.portal.IPortalPostService;
import com.game.common.buriedpoint.BuriedPointUtils;
import com.game.common.buriedpoint.constants.BuriedPointEventConstant;
import com.game.common.buriedpoint.model.BuriedPointPostModel;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.consts.Setting;
import com.game.common.dto.*;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.StreamInfo;
import com.game.common.dto.community.*;
import com.game.common.dto.system.TaskDto;
import com.game.common.dto.user.MemberDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.util.*;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.EmojiDealUtil;
import com.game.common.util.emoji.FilterEmojiUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@EnableAsync
@Service
public class PortalPostServiceImpl implements IPortalPostService {

    private static final Logger logger = LoggerFactory.getLogger( PortalPostServiceImpl.class);

    @Autowired
    private CmmPostDaoService postService;

    @Autowired
    private CmmPostDao cmmPostDao;

    @Autowired
    private CmmCommunityDaoService communityService;

    @Autowired
    private ICounterService iCountService;

    @Autowired
    private BasVideoJobDaoService videoJobService;

    @Autowired
    private IVideoService vdoService;


    @Autowired
    private FungoCacheArticle fungoCacheArticle;

    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;

    @Autowired
    private NacosFungoCircleConfig nacosFungoCircleConfig;


    //依赖系统和用户微服务
    @Autowired
    private SystemFacedeService systemFacedeService;

    //依赖游戏微服务
    @Autowired
    private GameFacedeService gameFacedeService;

    @Autowired
    private TSMQFacedeService tSMQFacedeService;

    @Autowired
    private CmmPostCircleMapper cmmPostCircleMapper;

    @Autowired
    private CmmPostGameMapper cmmPostGameMapper;

    @Autowired
    private CmmCircleMapper cmmCircleMapper;

    //依赖系统和用户微服务
    @Autowired(required = false)
    private SystemFeignClient systemFeignClient;

    @Autowired
    private ESDAOServiceImpl esdaoService;



    @Override
    public FungoPageResultDto<PostOutBean> getPostList(String userId, PostInputPageDto postInputPageDto) throws Exception {
        //结果模型
        FungoPageResultDto<PostOutBean> result = null;

        //pc 2.0 stop from redis cache
//        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_COMMUNITYS_POST_LIST;
//        String keySuffix = userId + JSON.toJSONString(postInputPageDto);

//        result = (FungoPageResultDto<PostOutBean>) fungoCacheArticle.getIndexCache(keyPrefix, keySuffix);
        if (null != result && null != result.getData() && result.getData().size() > 0) {
            return result;
        }

        int limit = postInputPageDto.getLimit();
        int page = postInputPageDto.getPage();
        int sort = postInputPageDto.getSort();
        String filter = postInputPageDto.getFilter();
        String communityId = postInputPageDto.getCommunity_id();

        if (communityId == null) {
            return FungoPageResultDto.error("241", "社区id不存在");
        }
        CmmCommunity community = communityService.selectOne(new EntityWrapper<CmmCommunity>().eq("id", communityId).and("state <> {0}", -1));
        if (community == null) {
            return FungoPageResultDto.error("241", "所属社区不存在");
        }


        result = new FungoPageResultDto<PostOutBean>();
        List<PostOutBean> relist = new ArrayList<PostOutBean>();
        Wrapper<CmmPost> wrapper = new EntityWrapper<CmmPost>().eq("community_id", communityId).eq("state", 1); //.ne("type", 3);
//		Wrapper<CmmPost> wrapper = new EntityWrapper<CmmPost>().eq("community_id",communityId ).eq("state", 1).ne("type", 3).ne("topic", 2); eq topic1
        List<CmmPost> postList = new ArrayList<CmmPost>();
        if(filter != null  && !"".equals(filter)){
            wrapper.eq( "type",filter );
        }

        if (sort == 1) {//  时间正序
            wrapper.orderBy("updated_at", true);
        } else if (sort == 2) {//  时间倒序
            wrapper.orderBy("updated_at", false);
        } else if (sort == 3) {// 热力值正序
//            wrapper.orderBy("comment_num,like_num", true);
            wrapper.last("ORDER BY comment_num ASC,like_num ASC");
        } else if (sort == 4) {// 热力值倒序
//            wrapper.orderBy("comment_num,like_num", false);
            wrapper.last("ORDER BY comment_num DESC,like_num DESC");
        } else if (sort == 5) {//最后回复时间
            wrapper.orderBy("last_reply_at", false);
        } else {
            wrapper.orderBy("updated_at", false);
        }
        //int count = postList.size();
        Page<CmmPost> pagePost = postService.selectPage(new Page<>(page, limit), wrapper);
        postList = pagePost.getRecords();

        if (postList == null || postList.isEmpty()) {
            ResultDto.error("221", "找不到符合条件的帖子");
        }

        boolean b = CommonUtil.isNull(userId);

        for (CmmPost post : postList) {
            //表情解码
            if (StringUtils.isNotBlank(post.getTitle())) {
                String interactTitle = FilterEmojiUtil.decodeEmoji(post.getTitle());
                post.setTitle(interactTitle);
            }
            if (StringUtils.isNotBlank(post.getContent())) {
                String interactContent = FilterEmojiUtil.decodeEmoji(post.getContent());
                /**
                 * 功能描述: 取消截取头一行的文章，pc2.0取消,返回完整的文章内容
                 * @auther: dl.zhang
                 * @date: 2019/9/2 16:06
                 */
                //bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
//                interactContent = interactContent.length() > 40 ? Html2Text.removeHtmlTag(interactContent.substring(0, 40)) : Html2Text.removeHtmlTag(interactContent);
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

            if (bean.getAuthor() == null) {
                continue;
            }
            String content = post.getContent();
            if (!CommonUtil.isNull(content)) {
                // bean.setContent(content.length() > 100 ? CommonUtils.filterWord(content.substring(0, 100)) : CommonUtils.filterWord(content));
                bean.setContent(CommonUtils.filterWord(content));
            }

            bean.setUpdated_at(DateTools.fmtDate(post.getUpdatedAt()));

            //fix bug:把V2.4.2存在的createdAt字段，恢复回来 [by mxf 2019-01-08]
            bean.setCreatedAt(DateTools.fmtDate(post.getCreatedAt()));
            //end

            bean.setVideoUrl(post.getVideo());
            bean.setImageUrl(post.getCoverImage());
            bean.setLikeNum(post.getLikeNum());
            bean.setPostId(post.getId());
            bean.setReplyNum(post.getCommentNum());
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

            relist.add(bean);
        }

        result.setData(relist);
        PageTools.pageToResultDto(result, pagePost);
        //redis cache
//        fungoCacheArticle.excIndexCache(true, keyPrefix, keySuffix, result);
        return result;

    }


    //-----------
}