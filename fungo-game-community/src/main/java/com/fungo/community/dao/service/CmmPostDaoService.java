package com.fungo.community.dao.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.fungo.community.entity.CmmPost;
import com.game.common.bean.CollectionBean;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区帖子 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface CmmPostDaoService extends IService<CmmPost> {

    /**
     * 精品帖子数大于2的用户
     * @return
     */
    public List<Map> getHonorQualificationOfEssencePost();


    /**
     *  查询文章表中发表文章大于10条
     * 前10名的用户
     * @param ccnt
     * @return
     */
    public List<String> getRecommendMembersFromCmmPost( long ccnt,  long limitSize,  List<String> wathMbsSet);


    /**
     * 获取我的收藏（文章）
     * @param page
     * @param postIds
     * @return
     */
    public List<CollectionBean> getCollection(Page<CollectionBean> page,  List<String> postIds);

    /**
     * PC2.0新增浏览量 根据跟用户ID获取文章的浏览量
     * @param cardId
     * @return
     */
    Integer getPostBoomWatchNumByCardId(String cardId);
}
