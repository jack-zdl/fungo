package com.game.common.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.bean.MemberPulishFromCommunity;
import com.game.common.entity.CmmCommunity;

/**
 * <p>
  * 社区 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
public interface CmmCommunityDao extends BaseMapper<CmmCommunity> {
		
		//社区玩家榜
		public List<MemberPulishFromCommunity> getMemberOrder(Page page,Map map);
		
		//查询社区评论数
		public int getCommentNumOfCommunity(String communityId);
}