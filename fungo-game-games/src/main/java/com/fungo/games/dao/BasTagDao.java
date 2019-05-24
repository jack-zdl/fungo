package com.fungo.games.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.games.entity.BasTag;
import com.game.common.bean.TagBean;

import java.util.List;

/**
 * <p>
  * 标签 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
public interface BasTagDao extends BaseMapper<BasTag> {

    //根据游戏id获取标签(后台设置)
    public List<TagBean> getGameTags(String gameId);

    //获取游戏后台分类标签
    public List<TagBean> getSortTags(String gameId);
}