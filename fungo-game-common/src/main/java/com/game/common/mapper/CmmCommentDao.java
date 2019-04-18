package com.game.common.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.bean.CommentBean;
import com.game.common.entity.CmmComment;

/**
 * <p>
  * 社区一级评论 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface CmmCommentDao extends BaseMapper<CmmComment> {

	//管控台  文章评论条件查询
	public List<CommentBean> getCommentsAll(Page page,Map map);
}