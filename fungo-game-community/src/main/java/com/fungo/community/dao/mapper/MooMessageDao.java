package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.entity.MooMessage;
import com.game.common.bean.CommentBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 心情评论 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface MooMessageDao extends BaseMapper<MooMessage> {

    /**
     * 后台运营  心情评论条件查询
     * @param page
     * @param map
     * @return
     */
    public List<CommentBean> getMessagesAll(Page page, Map map);

    int updateMooMessageCommentNum(@Param("reolyId") String reolyId);
}