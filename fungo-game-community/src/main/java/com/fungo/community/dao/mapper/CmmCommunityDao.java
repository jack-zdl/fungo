package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.entity.CmmCommunity;
import com.game.common.bean.CommentBean;
import com.game.common.bean.MemberPulishFromCommunity;

import java.util.List;
import java.util.Map;

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
    public List<MemberPulishFromCommunity> getMemberOrder(Page page, Map map);

    /**
     * 查询社区评论数
     * @param communityId
     * @return
     */
    public int getCommentNumOfCommunity(String communityId);



    /**
     * 我的动态 - 我的评论
     * @param page
     * @param userId
     * @return
     */
    public List<CommentBean> getAllComments(Page<CommentBean> page, String userId);



    /**
     * 获取关注社区
     * @param page 分页
     * @param communityId 社区id
     * @return
     */
    public List<Map<String,Object>> getFollowerCommunity(Page page,String communityId);
}