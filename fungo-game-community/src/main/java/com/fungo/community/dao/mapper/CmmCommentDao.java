package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.entity.CmmComment;
import com.game.common.bean.CommentBean;

import java.util.List;
import java.util.Map;

/**
 * <p>
  * 社区一级评论 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface CmmCommentDao extends BaseMapper<CmmComment> {

	/**
	 * 管控台  文章评论条件查询
	 * @param page
	 * @param map
	 * @return
	 */
	public List<CommentBean> getCommentsAll(Page page, Map map);
}