package com.fungo.community.service.msService.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.service.msService.IMSServiceCommunityService;
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

@Service
public class MSServiceCommunityServiceImpl implements IMSServiceCommunityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceCommunityServiceImpl.class);

    @Autowired
    private CmmCommunityDaoService cmmCommunityDaoService;

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
            if (null == cmmCommunityDtoRs) {
                return cmmCommunityDtoRs;
            }

            CmmCommunity cmmCommunity = cmmCommunityDaoService.selectOne(Condition.create().setSqlSelect("id,name,icon,cover_image").eq("id", cmmCommunityDto.getId()).ne("state", -1));
            if (null != cmmCommunity) {
                cmmCommunityDtoRs = new CmmCommunityDto();

                BeanUtils.copyProperties(cmmCommunity, cmmCommunityDtoRs);
            }

        } catch (Exception ex) {
            LOGGER.error("/ms/service/cmm/cty--queryCmmCtyDetail-出现异常:", ex);
        }
        return cmmCommunityDtoRs;
    }

    //----------
}
