package com.fungo.community.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.community.config.FungoCircleParameter;
import com.fungo.community.dao.mapper.BasTagDao;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostCircleMapper;
import com.fungo.community.dao.mapper.CmmPostDao;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.facede.SystemFacedeService;
import com.fungo.community.feign.SystemFeignClient;
import com.fungo.community.service.CircleService;
import com.fungo.community.service.CmmCircleService;
import com.game.common.bean.TagBean;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CircleTypeDto;
import com.game.common.dto.community.CmmCircleDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.PostOutBean;
import com.game.common.dto.system.CircleFollow;
import com.game.common.dto.system.CircleFollowVo;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.Html2Text;
import com.game.common.util.PageTools;
import com.game.common.util.date.DateTools;
import com.game.common.util.emoji.FilterEmojiUtil;
import com.game.common.vo.CmmCirclePostVo;
import com.game.common.vo.CmmCircleVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
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
    private FungoCircleParameter fungoCircleParameter;
    @Autowired
    private SystemFacedeService systemFacedeService;

    @Override
    public FungoPageResultDto<CmmCircleDto> selectCircle(String memberId, CmmCircleVo cmmCircleVo) {
        FungoPageResultDto<CmmCircleDto> re ;
        int pageNum = cmmCircleVo.getPage();
        int limitNum  =cmmCircleVo.getLimit();
        try {
            List<CmmCircleDto> cmmCircleDtoList =  new ArrayList<>();
            Page<CmmCircle> page = new Page<>(pageNum, limitNum);
            List<CmmCircle> list = new ArrayList<>();
            if(CmmCircleVo.SorttypeEnum.ALL.getKey().equals(cmmCircleVo.getQuerytype())){
                list = cmmCircleMapper.selectPageByKeyword(page,cmmCircleVo);
                list.stream().forEach(r -> {
                    CmmCircleDto s = new CmmCircleDto();
                    try {
                        BeanUtils.copyProperties(s, r);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    cmmCircleDtoList.add(s);
                });
//            BeanUtils.copyProperties(list,cmmCircleDtoList);
                List<CircleFollow>  circleFollows = new ArrayList<>();
                list.stream().forEach(x -> {
                    CircleFollow circleFollow = new CircleFollow();
                    circleFollow.setCircleId(x.getId());
                    circleFollows.add(circleFollow);
                });
                CircleFollowVo circleFollowVo = new CircleFollowVo();
                circleFollowVo.setMemberId(memberId);
                circleFollowVo.setCircleFollows(circleFollows);
                ResultDto<CircleFollowVo> resultDto = systemFeignClient.circleListFollow(circleFollowVo);
                if(resultDto.isSuccess()){
                    cmmCircleDtoList.stream().forEach(s -> {
                        List<CircleFollow> circleFollow =  resultDto.getData().getCircleFollows().stream().filter(e -> e.getCircleId().equals(s.getId())).collect(Collectors.toList());
                        s.setFollow((circleFollow == null || circleFollow.size() ==0 )? false:circleFollow.get(0).isFollow());
                    });
                }
            }else if(CmmCircleVo.SorttypeEnum.BROWSE.getKey().equals(cmmCircleVo.getQuerytype())){

            }else if(CmmCircleVo.SorttypeEnum.FOLLOW.getKey().equals(cmmCircleVo.getQuerytype())){
                CircleFollowVo param = new CircleFollowVo();
                param.setMemberId(memberId);
//                param.setPage(pageNum);
//                param.setLimit(limitNum); //
                FungoPageResultDto<String> circleFollowVos = systemFeignClient.circleListMineFollow(param);
                if(circleFollowVos.getData().size() > 0){
                    List<String> ids =circleFollowVos.getData();
                    String sortType = cmmCircleVo.getSorttype();
                    List<CmmCircle> cmmCircles = cmmCircleMapper.selectPageByIds(page,sortType,ids);
                    cmmCircles.stream().forEach(r -> {
                        CmmCircleDto s = new CmmCircleDto();
                        try {
                            BeanUtils.copyProperties(s, r);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        s.setFollow(true);
                        cmmCircleDtoList.add(s);
                    });
                }
            }
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildSuccess(cmmCircleDtoList,cmmCircleVo.getPage()-1,page);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取圈子集合",e);
            re = FungoPageResultDto.error("-1","获取圈子集合异常");
        }
        return re;
    }

    @Override
    public ResultDto<CmmCircleDto> selectCircleById(String memberId, String circleId) throws InvocationTargetException, IllegalAccessException {
        ResultDto<CmmCircleDto> re = null;
        CmmCircleDto cmmCircleDto = new CmmCircleDto();
        try {
            CmmCircle cmmCircle = cmmCircleServiceImap.selectById(circleId);
            BeanUtils.copyProperties(cmmCircleDto,cmmCircle);
            CircleFollowVo circleFollowVo = new CircleFollowVo();
            circleFollowVo.setMemberId(memberId);
            ResultDto<CircleFollowVo> resultDto = systemFeignClient.circleListFollow(circleFollowVo);
            if(resultDto != null && resultDto.getData() != null && resultDto.getData().getCircleFollows() != null){
                List<CircleFollow> circleFollows = resultDto.getData().getCircleFollows().stream().filter( r -> r.getCircleId().equals(circleId)).collect(Collectors.toList());
                cmmCircleDto.setFollow(circleFollows.size()>0 ? true:false );
            }
            //获取文章
//            List<CmmPost> cmmPosts = cmmPostCircleMapper.getCmmCircleListByPostId(cmmCircleDto.getId());
//            List<TagBean> tagBeans = basTagDao.getPostTags();
//            cmmPosts.stream().forEach(s -> {
//                CmmCircleDto.TagPost tagPost = new CmmCircleDto.TagPost();
//                List<TagBean> tag =  tagBeans.stream().filter(x -> x.getId().equals(s.getTags())).collect(Collectors.toList());
//                tagPost.setTag(tag.get(0).getName());

//            });
            re  = ResultDto.success(cmmCircleDto);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("获取圈子详情",e);
            re  = ResultDto.error("-1","获取圈子详情异常");
        }
        return re;
    }

    @Override
    public FungoPageResultDto<PostOutBean> selectCirclePost(String memberId, CmmCirclePostVo cmmCirclePostVo) {
        FungoPageResultDto<PostOutBean> re = new FungoPageResultDto<>();
        List<CmmPost> cmmPosts = new ArrayList<>();
        String userId = memberId;
        List<PostOutBean> relist = new ArrayList<>();
        try {
            String circleId = cmmCirclePostVo.getCircleId();
            String tagId = cmmCirclePostVo.getQuerytype();
            String sortType = cmmCirclePostVo.getSorttype();
            Page page = new Page(cmmCirclePostVo.getPage(),cmmCirclePostVo.getLimit());
            if(CmmCirclePostVo.SortTypeEnum.PUBDATE.getKey().equals(cmmCirclePostVo.getSorttype())){
                cmmPosts =  cmmPostDao.getCmmCircleListByCircleId(page,circleId,tagId,sortType);
            }else if(CmmCirclePostVo.SortTypeEnum.PUBREPLY.getKey().equals(cmmCirclePostVo.getSorttype())){
                cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page,circleId,tagId,sortType);
            }else if(CmmCirclePostVo.SortTypeEnum.ESSENCE.getKey().equals(cmmCirclePostVo.getSorttype())){
                cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page,circleId,tagId,sortType);
            }else if(CmmCirclePostVo.SortTypeEnum.DISCUSS.getKey().equals(cmmCirclePostVo.getSorttype())){
                cmmPosts = cmmPostDao.getCmmCircleListByCircleId(page,circleId,tagId,sortType);
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

                bean.setVideoUrl(post.getVideo() == null ? "":post.getVideo());
                bean.setImageUrl(post.getCoverImage()== null ? "":post.getCoverImage());
                bean.setLikeNum(post.getLikeNum() == null?0 : post.getLikeNum());
                bean.setPostId(post.getId());
                bean.setReplyNum(post.getCommentNum() == null?0 : post.getCommentNum());
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
            re.setData(relist);
            PageTools.pageToResultDto(re,page);

        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("圈子获取下属文章",e);
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
    public ResultDto<List<CircleTypeDto>> selectCirclePostType(String memberId, CmmCirclePostVo cmmCirclePostVo) {
        List<CircleTypeDto> circleTypeDtos = new ArrayList<>();
        ResultDto<List<CircleTypeDto>> re = new ResultDto<>();
        try {
            List<CmmPost> cmmPosts = cmmPostDao.getCmmCircleListByPostId(cmmCirclePostVo.getCircleId());
            List<TagBean> tagBeans = basTagDao.getPostTags();
            Map<String, List<CmmPost>> cmmPostMap  = cmmPosts.stream().collect(groupingBy(CmmPost::getTags));
            Iterator<String> iter = cmmPostMap.keySet().iterator();
            while (iter.hasNext()) {
                String key = iter.next();
                List<CmmPost> valueList = cmmPostMap.get(key);
                if (valueList.size() < fungoCircleParameter.getPostnumber())
                    cmmPostMap.remove(key);
            }
            for (String key : cmmPostMap.keySet()) {
                CircleTypeDto circleTypeDto = new CircleTypeDto();
                Optional<TagBean> cmmPost = tagBeans.stream().filter(r -> r.getId().equals(key)).findFirst();
                circleTypeDto.setCirclePostType(cmmPost.get().getId());
                circleTypeDto.setCirclePostName(cmmPost.get().getName());
                circleTypeDtos.add(circleTypeDto);
            }
            re.setData(circleTypeDtos);
        }catch (Exception e){
            e.printStackTrace();
            LOGGER.error("查询圈子的文章类型",e);
        }
        return re;
    }

}
