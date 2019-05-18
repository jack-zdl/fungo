package com.fungo.community.dao.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.community.entity.CmmPost;

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
}
