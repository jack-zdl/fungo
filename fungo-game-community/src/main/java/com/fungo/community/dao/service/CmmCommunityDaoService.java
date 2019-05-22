package com.fungo.community.dao.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.fungo.community.entity.CmmCommunity;
import com.game.common.bean.CommentBean;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
public interface CmmCommunityDaoService extends IService<CmmCommunity> {

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
     * @param communityIds 社区id
     * @return
     */
    public List<Map<String,Object>> getFollowerCommunity(Page page,List<String> communityIds);

}
