package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fungo.community.entity.CmmCommunity;

/**
 * <p>
 * 社区 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
public interface CmmCommunityDao extends BaseMapper<CmmCommunity> {

    /**
     * 社区玩家榜
     * @param page
     * @param map
     * @return
     */
    //public List<MemberPulishFromCommunity> getMemberOrder(Page page, Map map);

    /**
     * 查询社区评论数
     * @param communityId
     * @return
     */
    public int getCommentNumOfCommunity(String communityId);
}