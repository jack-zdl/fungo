package com.fungo.community.dao.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.community.dao.mapper.CmmCommunityDao;
import com.fungo.community.dao.service.CmmCommunityDaoService;
import com.fungo.community.entity.CmmCommunity;
import com.fungo.community.entity.portal.CmmCommunityIndex;
import com.game.common.bean.CommentBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
@Service
public class CmmCommunityDaoServiceImap extends ServiceImpl<CmmCommunityDao, CmmCommunity> implements CmmCommunityDaoService {
    @Autowired
    private CmmCommunityDao communityDao;

    @Override
    public List<CommentBean> getAllComments(Page<CommentBean> page, String userId) {
        return communityDao.getAllComments(page, userId);
    }

    @Override
    public List<Map<String, Object>> getFollowerCommunity(Page page, List<String> communityIds) {
        return communityDao.getFollowerCommunity(page, communityIds);
    }

    /**
     * PC2.0圈子首页列表
     * @param pageLimiter
     */
    @Override
    public List<CmmCommunityIndex> getCommunityListPC2_0(Map<String, Integer> pageLimiter) {
        return communityDao.getCommunityListPC2_0(pageLimiter);
    }

    /**
     * PC2.0圈子首页列表总数
     * @return
     */
    @Override
    public int getCommunityListPC2_0Count() {
        return communityDao.getCommunityListPC2_0Count();
    }
}
