package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.service.CmmPostDaoService;
import com.fungo.community.entity.CmmPost;
import com.fungo.community.service.msService.IMSServicePostService;
import com.game.common.dto.community.CmmPostDto;
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

    @Override
    public List<CmmPostDto> queryCmmPostList(CmmPostDto postDto) {

        List<CmmPostDto> cmmPostList = null;

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

            //根据修改时间倒叙
            postEntityWrapper.orderBy("updated_at", false);

            List<CmmPost> selectRecords = null;

            if (null != cmmPostPage) {

                Page<CmmPost> cmmPostPageSelect = this.postDaoService.selectPage(cmmPostPage, postEntityWrapper);

                if (null != cmmPostPageSelect) {
                    selectRecords = cmmPostPageSelect.getRecords();
                }

            } else {
                selectRecords = this.postDaoService.selectList(postEntityWrapper);
            }

            if (null != selectRecords) {

                cmmPostList = new ArrayList<CmmPostDto>();

                for (CmmPost cmmPostEntity : selectRecords) {

                    CmmPostDto cmmPostDto = new CmmPostDto();

                    BeanUtils.copyProperties(cmmPostEntity, cmmPostDto);

                    cmmPostList.add(cmmPostDto);
                }
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/post/lists--queryCmmPostList-出现异常:", ex);
        }
        return cmmPostList;
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

        try {

            int page = postDto.getPage();
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


            int selectCount = postDaoService.selectCount(postEntityWrapper);

            return selectCount;

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/post/count--queryCmmPostCount-出现异常:", ex);
        }

        return 0;
    }


    //------
}
