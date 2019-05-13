package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.entity.CmmPost;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 社区帖子 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
public interface CmmPostDao extends BaseMapper<CmmPost> {

    /**
     * 获取关注用户和社区的帖子 分页数据
     */
    public List<CmmPost> getAllFollowerPost(Page page, Map<String, Object> map);

    /**
     * 获取关注用户和社区的帖子 分页数据 重载方法
     * @param map
     * @return
     */
    public List<CmmPost> getAllFollowerPostWithPage(Map<String, Object> map);

    /**
     * 获取关注用户和社区的帖子总数
     * @param page
     * @param map
     * @return
     */
    public Map<String, Object> getAllFollowerPostCount(Page page, Map<String, Object> map);

    /**
     * 获取帖子最后回复时间
     * @param postId
     * @return
     */
    public List<HashMap<String, Object>> getLastReplyTime(String postId);


    /**
     * 精品帖子数大于2的用户
     * @return
     */
    public List<Map> getHonorQualificationOfEssencePost();


}