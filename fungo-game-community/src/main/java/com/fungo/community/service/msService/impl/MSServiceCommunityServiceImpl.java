package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.dao.service.impl.CmmPostDaoServiceImap;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.service.msService.IMSServiceCommunityService;
import com.game.common.bean.CommentBean;
import com.game.common.dto.community.CmmCommunityDto;
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
public class MSServiceCommunityServiceImpl implements IMSServiceCommunityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceCommunityServiceImpl.class);

    @Autowired
    private CmmCommunityDaoService cmmCommunityDaoService;

    @Autowired
    private CmmPostDaoServiceImap cmmPostDaoServiceImap;


    @Override
    public List<CmmCommunityDto> queryCmmCommunityList(CmmCommunityDto communityDto) {

        List<CmmCommunityDto> cmmCommunityDtoList = null;

        try {

            int page = communityDto.getPage();
            int limit = communityDto.getLimit();


            EntityWrapper<CmmCommunity> cmmCommunityEntityWrapper = new EntityWrapper<CmmCommunity>();
            HashMap<String, Object> param = new HashMap<String, Object>();

            Page<CmmCommunity> cmmCommunityPage = null;

            if (page > 0 && limit > 0) {
                cmmCommunityPage = new Page<CmmCommunity>(page, limit);
            }

            //PK
            String id = communityDto.getId();
            if (StringUtils.isNotBlank(id)) {
                param.put("id", id);
            }


            //游戏id
            String game_id = communityDto.getGameId();
            if (StringUtils.isNotBlank(game_id)) {
                param.put("game_id", game_id);
            }

            //社区类型 1：官方 0：游戏
            Integer type = communityDto.getType();
            if (null != type) {
                param.put("type", type);
            }

            //状态  -1:已删除,0:未上架,1:运营中
            Integer state = communityDto.getState();
            if (null != state) {
                param.put("state", state);
            }


            //推荐状态 0:未推荐,1:推荐
            Integer recommend_state = communityDto.getRecommendState();
            if (null != recommend_state) {
                param.put("recommend_state", recommend_state);
            }

            cmmCommunityEntityWrapper.allEq(param);

            //社区名称
            String name = communityDto.getName();
            if (StringUtils.isNotBlank(name)) {
                cmmCommunityEntityWrapper.orNew("name like '%" + name + "%'");
            }


            //根据修改时间倒叙
            cmmCommunityEntityWrapper.orderBy("updated_at", false);


            List<CmmCommunity> selectRecords = null;

            if (null != cmmCommunityPage) {

                Page<CmmCommunity> cmmCommunityPageResult = this.cmmCommunityDaoService.selectPage(cmmCommunityPage, cmmCommunityEntityWrapper);

                if (null != cmmCommunityPageResult) {
                    selectRecords = cmmCommunityPage.getRecords();
                }

            } else {
                selectRecords = this.cmmCommunityDaoService.selectList(cmmCommunityEntityWrapper);
            }

            if (null != selectRecords) {

                cmmCommunityDtoList = new ArrayList<CmmCommunityDto>();

                for (CmmCommunity cmmCommunity : selectRecords) {

                    CmmCommunityDto cmmCommunityDto = new CmmCommunityDto();

                    BeanUtils.copyProperties(cmmCommunity, cmmCommunityDto);

                    cmmCommunityDtoList.add(cmmCommunityDto);
                }
            }


        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/cty/lists--queryCmmCommunityList-出现异常:", ex);
        }
        return cmmCommunityDtoList;
    }

    @Override
    public CmmCommunityDto queryCmmCtyDetail(CmmCommunityDto cmmCommunityDto) {

        CmmCommunityDto cmmCommunityDtoRs = null;
        try {
            if (null == cmmCommunityDto) {
                return cmmCommunityDtoRs;
            }

            CmmCommunity cmmCommunity = cmmCommunityDaoService.selectOne(Condition.create().setSqlSelect("id,name,icon,cover_image as coverImage").eq("id", cmmCommunityDto.getId()).ne("state", -1));
            if (null != cmmCommunity) {
                cmmCommunityDtoRs = new CmmCommunityDto();

                BeanUtils.copyProperties(cmmCommunity, cmmCommunityDtoRs);
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/cty--queryCmmCtyDetail-出现异常:", ex);
        }
        return cmmCommunityDtoRs;
    }


    @Override
    public List<CommentBean> getAllComments(int pageNum, int limit, String userId) {

        List<CommentBean> commentBeanList = null;
        try {
            if (StringUtils.isBlank(userId)) {
                return commentBeanList;
            }
            Page<CommentBean> page = new Page<CommentBean>(pageNum, limit);

            commentBeanList = cmmCommunityDaoService.getAllComments(page, userId);
        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/user/comments--getAllComments-出现异常:", ex);
        }
        return commentBeanList;
    }


    @Override
    public List<String> getRecommendMembersFromCmmPost(long ccnt, long limitSize, List<String> wathMbsSet) {
        try {

            return cmmPostDaoServiceImap.getRecommendMembersFromCmmPost(ccnt, limitSize, wathMbsSet);

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/user/post/ids--getRecommendMembersFromCmmPost-出现异常:", ex);
        }
        return null;
    }


    @Override
    public List<Map<String, Object>> getFollowerCommunity(int pageNum, int limit, List<String> communityIds) {
        try {

            if (null == communityIds || communityIds.isEmpty()) {
                return null;
            }

            Page page = new Page(pageNum, limit);
            return cmmCommunityDaoService.getFollowerCommunity(page, communityIds);
        } catch (Exception ex) {
            LOGGER.error("//ms/service/cmm/user/flw/cmtlists--getFollowerCommunity-出现异常:", ex);
        }
        return null;
    }


    //----------
}
