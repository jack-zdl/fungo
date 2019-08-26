package com.fungo.community.dao.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.community.entity.CmmPost;
import com.game.common.bean.CollectionBean;
import org.apache.ibatis.annotations.Param;

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


    /**
     *  查询文章表中发表文章大于10条
     * 前10名的用户
     * @param ccnt
     * @return
     */
    public List<String> getRecommendMembersFromCmmPost(@Param("ccnt") long ccnt, @Param("limitSize") long limitSize,
                                                       @Param("wathMbsSet") List<String> wathMbsSet);


    /**
     * 获取我的收藏（文章）
     * @param page
     * @param postIds
     * @return
     */
    public List<CollectionBean> getCollection(Page<CollectionBean> page, @Param("postIds") List<String>  postIds);

    /**
     * PC2.0新增浏览量 根据跟用户ID获取文章的浏览量
     * @param cardId
     * @return
     */
    Integer getPostBoomWatchNumByCardId(@Param("cardId")String cardId);


    /**
     * 基于模糊查询文章数据对应的游戏ids
     * @param paramMap
     * @return
     */
    public List<Map<String,Object>>  queryCmmPostRefGameIds(Map<String,Object> paramMap);


    /**
     * 基于模糊查询文章数据对应的游戏总数
     * @param paramMap
     * @return
     */
    public Map<String,Object> queryCmmPostRefGameIdsCount(Map<String,Object> paramMap);

    List<CmmPost> getCmmCircleListByPostId(String circleId);

    List<CmmPost> getCmmCircleListByCircleId(Page page ,@Param("circleId") String circleId, @Param("tagId") String tagId,@Param("cream") String cream , @Param("sortType") String sortType);

    List<CmmPost> getAllCmmCircleListByCircleId(Page page ,@Param("circleId") String circleId, @Param("tagId") String tagId,@Param("cream") String cream , @Param("sortType") String sortType);


    List<CmmPost> getCmmPostByGameId(Page page,@Param("gameId")String gameId );

    List<CmmPost> getCmmPostByRecommend(Page page);


    boolean updateCmmPostCommentNum(@Param("commentId") String commentId);
}