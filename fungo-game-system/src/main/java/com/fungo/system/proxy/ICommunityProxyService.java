package com.fungo.system.proxy;

import com.baomidou.mybatisplus.plugins.Page;
import com.game.common.bean.CollectionBean;

import com.game.common.dto.community.CommentBean;
import org.apache.ibatis.annotations.Param;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/5/22
 */
public interface ICommunityProxyService {

    List<CollectionBean> getCollection(Page<CollectionBean> page, List<String> list);

    List<Map<String,Object>> getFollowerCommunity(Page page, List<String> communityIds);

    List<Map<String,Object>> getPostFeeds(Map<String, Object> map);

    //用户时间线-心情 新版已弃用
    List<Map<String,Object>> getMoodFeeds(Map<String, Object> map);


    //我的动态 - 我的评论
    List<CommentBean> getAllComments(Page<CommentBean> page, String userId) ;

    /**
     *  查询文章表中发表文章大于10条
     * 前10名的用户
     * @param ccnt
     * @return
     */
    List<String> getRecommendMembersFromCmmPost(@Param("ccnt") long ccnt, @Param("limitSize") long limitSize, @Param("wathMbsSet") List<String> wathMbsSet);

    List<Map> getHonorQualificationOfEssencePost();
}
