package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;

import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.BasTag;
import com.fungo.games.entity.BasTagGroup;
import com.fungo.games.service.*;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.ResultDto;
import com.game.common.repo.cache.facade.FungoCacheGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements ITagService {

    @Autowired
    private BasTagService basTagService;

    @Autowired
    private BasTagGroupService basTagGroupService;

    @Autowired
    private GameTagService gameTagService;

    @Autowired
    private GameTagAttitudeService gameTagAttitudeService;

    @Autowired
    private GameService gameService;


    @Autowired
    private FungoCacheGame fungoCacheGame;

    @SuppressWarnings("rawtypes")
    @Override
    /**
     * 以tag_group为单位分组,查出group和tag的所有内容 data : map集合 map:tagGroup List<tag>
     *
     */
    public ResultDto<List> getTagListAll() {
        // 第一集合
        List<Map<String, Object>> totalList = null;

        //redis cache
        totalList = (List<Map<String, Object>>) fungoCacheGame.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_GAME_TAG_LIST, "");
        if (null != totalList && !totalList.isEmpty()) {
            return ResultDto.success(totalList);
        }
        List<BasTagGroup> basTagGroupList = basTagGroupService.selectList(new EntityWrapper<BasTagGroup>().ne("type",2));
        if (basTagGroupList == null || basTagGroupList.isEmpty()) {
            return ResultDto.error("251", "找不到标签分类信息");
        }

        // 第一集合
        totalList = new ArrayList<>();
        // 根据标签分类整理标签
        for (BasTagGroup tagGroup : basTagGroupList) {
            // 第二集合
            Map<String, Object> groupData = new HashMap<String, Object>();
            String groupId = tagGroup.getId();
            groupData.put("name", tagGroup.getName());
            groupData.put("objectId", groupId);
            groupData.put("icon", tagGroup.getIcon());
            // 找出分类关联标签
            List<BasTag> unionTagList = basTagService.selectList(new EntityWrapper<BasTag>().eq("group_id", groupId));
            // 标签个数
            int count = 0;
            // 加入标签
            if (unionTagList != null && unionTagList.size() != 0) {
                // 第三集合
                List<Map<String, String>> tagList = new ArrayList<>();
                for (BasTag tag : unionTagList) {
                    // 第四集合
                    Map<String, String> tagData = new HashMap<String, String>();
                    tagData.put("name", tag.getName());
                    tagData.put("tagId", tag.getId());
                    tagList.add(tagData);
                    count++;
                }
                groupData.put("tagList", tagList);
            }
            groupData.put("count", count);
            totalList.add(groupData);
        }

        //redis cache
        fungoCacheGame.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_GAME_TAG_LIST, "", totalList);
        return ResultDto.success(totalList);
    }

    @Override
    public ResultDto<List<BasTag>> listPostTag() {
        BasTagGroup basTagGroup = basTagGroupService.selectOne(new EntityWrapper<BasTagGroup>().eq("type",2));
        List<BasTag> basTags = basTagService.selectList(new EntityWrapper<BasTag>().eq("group_id", basTagGroup.getId()).orderBy("sort",true));
        return ResultDto.success(basTags);
    }


}
