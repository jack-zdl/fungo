package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.mapper.CmmCircleMapper;
import com.fungo.community.dao.mapper.CmmPostCircleMapper;
import com.fungo.community.dao.mapper.CmmPostGameMapper;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.CmmPostCircleDaoService;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmCircle;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.entity.TCmmPostCircle;
import com.fungo.community.facede.GameFacedeService;
import com.fungo.community.service.CmmCircleService;
import com.fungo.community.service.impl.PostServiceImpl;
import com.fungo.community.service.msService.IMSServicePostService;
import com.game.common.bean.CollectionBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import com.game.common.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class MSServicePostServiceImpl implements IMSServicePostService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServicePostServiceImpl.class);

    @Autowired
    private CmmPostDaoService postDaoService;
    @Autowired
    private CmmCommunityDaoService communityService;
    @Autowired
    private CmmPostGameMapper cmmPostGameMapper;
    @Autowired
    private CmmPostCircleMapper cmmPostCircleMapper;
    @Autowired
    private CmmCircleMapper cmmCircleMapper;
    //依赖游戏微服务
    @Autowired
    private GameFacedeService gameFacedeService;
    @Autowired
    private PostServiceImpl postService;
    @Autowired
    private CmmPostCircleDaoService cmmPostCircleDaoService;
    @Autowired
    private CmmCircleService cmmCircleService;


    @Override
    public FungoPageResultDto<CmmPostDto> queryCmmPostList(CmmPostDto postDto) {

        FungoPageResultDto<CmmPostDto> resultDto = new FungoPageResultDto<CmmPostDto>();
        List<CmmPostDto> cmmPostList = null;
        Page<CmmPost> cmmPostPageSelect = null;
        try {


            int page = postDto.getPage();
            int limit = postDto.getLimit();


            EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<CmmPost>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<CmmPost> cmmPostPage = null;

            if (page > 0 && limit > 0) {
                cmmPostPage = new Page<>(page, limit);
            }

            //社区ID
            String communityId = postDto.getCommunityId();
            if (StringUtils.isNotBlank(communityId)) {
                param.put("community_id", communityId);
            }

            //帖子ID
            String id = postDto.getId();
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            //会员ID
            String memberId = postDto.getMemberId();
            if (StringUtils.isNotBlank(memberId)) {
                param.put("member_id", memberId);
            }

            //类型
            Integer type = postDto.getType();
            if (null != type) {
                param.put("type", type);
            }

            //置顶状态
            Integer topic = postDto.getTopic();
            if (null != topic) {
                param.put("topic", topic);
            }

            postEntityWrapper.allEq(param);

            //标题
            String title = postDto.getTitle();
            if (StringUtils.isNotBlank(title)) {
                postEntityWrapper.orNew("title like '%" + title + "%'");
            }
            //标签
            String tags = postDto.getTags();
            if (StringUtils.isNotBlank(tags)) {
                postEntityWrapper.orNew("tags like '%" + tags + "%'");
            }
            //帖子内容
            String content = postDto.getContent();
            if (StringUtils.isNotBlank(content)) {
                postEntityWrapper.orNew("content like '%" + content + "%'");
            }

            if(postDto.getState() != null){
                //根据修改时间倒叙 状态 -1:已删除 0:压缩转码中 1:正常
                postEntityWrapper.eq("state", postDto.getState());
            }


            //排序
            postEntityWrapper.orderBy("sort desc ,updated_at", false);

            //postEntityWrapper .orderBy("updated_at", false);

            List<CmmPost> selectRecords = null;

            if (null != cmmPostPage) {

                cmmPostPageSelect = this.postDaoService.selectPage(cmmPostPage, postEntityWrapper);

                if (null != cmmPostPageSelect) {
//                    selectRecords = cmmPostPageSelect.getRecords();
                   // PageTools.pageToResultDto(resultDto, cmmPostPageSelect);
                    selectRecords = cmmPostPageSelect.getRecords();

                    resultDto.setCount(cmmPostPageSelect.getTotal());
                    resultDto.setPages(cmmPostPageSelect.getPages());
                }

            } else {
                selectRecords = this.postDaoService.selectList(postEntityWrapper);
            }

            if (null != selectRecords) {

                cmmPostList = new ArrayList<CmmPostDto>();

                for (CmmPost cmmPostEntity : selectRecords) {

                    CmmPostDto cmmPostDto = new CmmPostDto();

                    BeanUtils.copyProperties(cmmPostEntity, cmmPostDto);

                    TCmmPostCircle tCmmPostCircle = cmmPostCircleDaoService.selectOne(new EntityWrapper<TCmmPostCircle>().eq("post_id", cmmPostEntity.getId()));
                    if(null != tCmmPostCircle && StringUtils.isNotBlank(tCmmPostCircle.getCircleId())){
                        CmmCircle cmmCircle = cmmCircleService.selectById(tCmmPostCircle.getCircleId());
                        if(null != cmmCircle){
                            cmmPostDto.setCircleId(cmmCircle.getId());
                            cmmPostDto.setCircleName(cmmCircle.getCircleName());
                        }
                    }
                    cmmPostList.add(cmmPostDto);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/post/lists--queryCmmPostList-出现异常:参数="+postDto.toString(), ex);
        }
        resultDto.setData(cmmPostList);
       // PageTools.pageToResultDto(resultDto, cmmPostPageSelect);
        return resultDto;
    }


    @Override
    public List<Map> getHonorQualificationOfEssencePost() {
        try {
            return postDaoService.getHonorQualificationOfEssencePost();
        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/post/essences--getHonorQualificationOfEssencePost-出现异常:", ex);
        }
        return null;
    }

    @Override
    public Integer queryCmmPostCount(CmmPostDto postDto) {
        String keyword = postDto.getTitle();
        try {

           /* int page = postDto.getPage();
            int limit = postDto.getLimit();

            EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<CmmPost>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            //社区ID
            String communityId = postDto.getCommunityId();
            if (StringUtils.isNotBlank(communityId)) {
                param.put("community_id", communityId);
            }

            //帖子ID
            String id = postDto.getId();
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }

            //会员ID
            String memberId = postDto.getMemberId();
            if (StringUtils.isNotBlank(memberId)) {
                param.put("member_id", memberId);
            }

            //类型
            Integer type = postDto.getType();
            if (null != type) {
                param.put("type", type);
            }

            //置顶状态
            Integer topic = postDto.getTopic();
            if (null != topic) {
                param.put("topic", topic);
            }


            postEntityWrapper.allEq(param);

            //标题
            String title = postDto.getTitle();
            if (StringUtils.isNotBlank(title)) {
                postEntityWrapper.orNew("title like '%" + title + "%'");
            }
            //标签
            String tags = postDto.getTags();
            if (StringUtils.isNotBlank(tags)) {
                postEntityWrapper.orNew("tags like '%" + tags + "%'");
            }
            //帖子内容
            String content = postDto.getContent();
            if (StringUtils.isNotBlank(content)) {
                postEntityWrapper.orNew("content like '%" + content + "%'");
            }
            int selectCount  = postDaoService.selectCount(postEntityWrapper);*/
           //new EntityWrapper<CmmPost>().where("state = {0}", 1).andNew("title like '%" + keyword + "%'")
            //                    .or("content like " + "'%" + keyword + "%'").or("content like " + "'%" + keyword + "%'")
            int selectCount = postDaoService.selectCount(postService.getWrapper(keyword,new EntityWrapper<CmmPost>()));
            return selectCount;
        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/post/count--queryCmmPostCount-出现异常:关键字="+keyword, ex);
        }

        return 0;
    }

    /**
     * 功能描述: 获取所有的置顶文章
     * @param: [cmmPostDto]
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.community.CmmPostDto>
     * @auther: dl.zhang
     * @date: 2019/7/17 16:57
     */
    @Override
    public FungoPageResultDto<CmmPostDto> listCmmPostTopicPost(CmmPostDto cmmPostDto) {

        FungoPageResultDto<CmmPostDto> resultDto = new FungoPageResultDto<CmmPostDto>();
        List<CmmPostDto> cmmPostDtoList = null;
        try {

            if (null == cmmPostDto) {
                return resultDto;
            }
            int page = cmmPostDto.getPage();
            int limit = cmmPostDto.getLimit();

            EntityWrapper<CmmPost> postEntityWrapper = new EntityWrapper<>();
            postEntityWrapper.eq("type", 3);
            postEntityWrapper.eq("state", 1);
//            postEntityWrapper.last("ORDER BY sort DESC,updated_at DESC");
            postEntityWrapper.last("ORDER BY sort DESC,edited_at DESC");
            Page<CmmPost> postPage = new Page<>(page, limit);

            Page<CmmPost> pageList = postDaoService.selectPage(postPage, postEntityWrapper);

            if (null != pageList) {

                cmmPostDtoList = new ArrayList<CmmPostDto>();

                List<CmmPost> listRecords = pageList.getRecords();

                for (CmmPost cmmPost : listRecords) {

                    CmmPostDto cmmPostDtoRs = new CmmPostDto();

                    BeanUtils.copyProperties(cmmPost, cmmPostDtoRs);

                    cmmPostDtoList.add(cmmPostDtoRs);
                }

                resultDto.setCount(pageList.getTotal());
                resultDto.setPages(pageList.getPages());
            }
        } catch (Exception ex) {
            LOGGER.error("ms/service/cmm/post/topicPosts--listCmmPostTopicPost-出现异常:", ex);
        }
        resultDto.setData(cmmPostDtoList);
        return resultDto;
    }

    @Override
    public FungoPageResultDto<CollectionBean>  getCollection(int pageNum, int limit, List<String> postIds) {
        try {
            FungoPageResultDto<CollectionBean> re = new FungoPageResultDto<>( );
            if (null == postIds || postIds.isEmpty()) {
                return null;
            }
            Page<CollectionBean> page = new Page<CollectionBean>(pageNum, limit);
            List<CollectionBean> collectionBeans =  postDaoService.getCollection(page, postIds);
            re.setData(collectionBeans);
            PageTools.pageToResultDto( re,page);
            return  re;
        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/post/user/collect--getCollection-出现异常:文章id="+postIds, ex);
        }
        return null;
    }

    /**
     * PC2.0新增浏览量 根据跟用户ID获取文章的浏览量
     *
     * @param cardId
     * @return
     */
    @Override
    public Integer getPostBoomWatchNumByCardId(String cardId) {
        try {
            return postDaoService.getPostBoomWatchNumByCardId(cardId);
        }catch (Exception e){
            LOGGER.error("fegin根据跟用户ID获取文章的浏览量异常，用户id="+cardId,e);
        }
        return 0;

    }

    /**
     * 功能描述: 通过文章查询游戏信息
     * @param: [cmmPost]
     * @return: java.util.Map
     * @auther: dl.zhang
     * @date: 2019/7/17 16:55
     */
    public Map getGameMsgByPost(CmmPostDto cmmPost) {
        Map<String, Object> communityMap = new HashMap<String, Object>();
        try {
            //type 0 游戏社区 1：官方社区 2 圈子 3.什么都没有
            communityMap.put("type", 3);
            if (cmmPost == null || StringUtil.isNull(cmmPost.getId())) {
                return communityMap;
            }
            // 获取文章是否有游戏社区 有游戏社区 优先取游戏社区对应游戏
            CmmCommunity community = null;
            if (StringUtil.isNotNull(cmmPost.getCommunityId())) {
                community = communityService.selectById(cmmPost.getCommunityId());
            }
            //无社区 或是官方社区 查找文章关联游戏
            if (community == null || community.getType() == 1) {
                //找到文章关联的游戏，选择一个游戏社区
                String communityId = cmmPostGameMapper.getCommunityIdByPostId(cmmPost.getId());
                if (StringUtil.isNotNull(communityId)) {
                    community = communityService.selectById(communityId);
                }
            }
            // 若有游戏社区且有对应的游戏id 则
            if (community != null && community.getType() != 1 && !CommonUtil.isNull(community.getGameId())) {
                ResultDto<GameDto> resultDto = gameFacedeService.selectGameDetails(community.getGameId(), 0);
                if (resultDto.isSuccess()) {
                    GameDto gameDto = resultDto.getData();
                    communityMap.put("type", 0);
                    communityMap.put("objectId", community.getId());
                    communityMap.put("name", gameDto.getName());
                    communityMap.put("icon", gameDto.getIcon());
                    communityMap.put("gameId", community.getGameId());
                }
            } else {
                //查询文章是否有圈子
                CmmCircle circle = null;
                String circleId = cmmPostCircleMapper.getCmmCircleByPostId(cmmPost.getId());
                if (StringUtil.isNotNull(circleId)) {
                    circle = cmmCircleMapper.selectById(circleId);
                }
                if (circle != null) {
                    communityMap.put("objectId", circle.getId());
                    communityMap.put("name", circle.getCircleName());
                    communityMap.put("icon", circle.getCircleIcon());
                    communityMap.put("intro", circle.getIntro());
                    //2标识本次是圈子
                    communityMap.put("type", 2);
                }
            }
        }catch (Exception e){
            LOGGER.error("fegin通过文章查询游戏信息,文章id="+cmmPost.getId(),e);
        }
        return communityMap;
    }
}
