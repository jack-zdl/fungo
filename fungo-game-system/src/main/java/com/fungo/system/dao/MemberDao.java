package com.fungo.system.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.entity.Member;
import com.game.common.bean.CommentBean;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户会员 Mapper 接口
 * </p>
 *
 * @author lzh
 * @since 2018-06-15
 */
public interface MemberDao extends  BaseMapper<Member> {


    //我的动态 - 我的评论
    public List<CommentBean> getAllComments(Page<CommentBean> page, String userId);




    /**
     * 查询游戏评论表中发表评论大于14条，
     * 前10名的用户
     * @param ecnt
     * @return
     */
    public List<String> getRecommendMembersFromEvaluation(@Param("ecnt") long ecnt, @Param("limitSize") long limitSize, @Param("wathMbsSet") List<String> wathMbsSet);


    /**
     *  查询文章表中发表文章大于10条
     * 前10名的用户
     * @param ccnt
     * @return
     */
    public List<String> getRecommendMembersFromCmmPost(@Param("ccnt") long ccnt, @Param("limitSize") long limitSize, @Param("wathMbsSet") List<String> wathMbsSet);


    /**
     * 被点赞数>50的用户
     * @return
     */
    public List<Map> getHonorQualificationOfBeLiked();

    /**
     * 查询没有等级和fungo身份证图片的用户
     * @return
     */
    public List<Member> queryMbWithoutLevelFungoImgs();


     List<String> getMemberList();

     List<Member> getUnfollerMemberList(@Param("ids") List<String> ids,@Param("memberId") String memberId);

}