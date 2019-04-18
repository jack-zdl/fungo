package com.game.common.mapper;

import com.game.common.bean.CommentBean;
import com.game.common.entity.MooMessage;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

/**
 * <p>
  * 心情评论 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface MooMessageDao extends BaseMapper<MooMessage> {

	//后台运营  心情评论条件查询
	public List<CommentBean> getMessagesAll(Page page,Map map);
}