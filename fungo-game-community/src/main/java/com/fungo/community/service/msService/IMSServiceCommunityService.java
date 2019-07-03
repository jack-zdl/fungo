package com.fungo.community.service.msService;


import com.game.common.bean.CommentBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.community.CmmCommunityDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *      社区 - 社区微服务业务层
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
public interface IMSServiceCommunityService {


    /**
     * 查询社区数据列表
     * @return
     */
    public FungoPageResultDto<CmmCommunityDto> queryCmmCommunityList(@RequestBody CmmCommunityDto communityDto);

    /**
     * 查询单个社区详情数据
     * @param cmmCommunityDto
     * @return
     */
    CmmCommunityDto queryCmmCtyDetail(CmmCommunityDto cmmCommunityDto);


    /**
     *  我的动态 - 我的评论
     * @param pageNum
     * @param limit
     * @param userId
     * @return
     */
    public FungoPageResultDto<CommentBean>  getAllComments(int pageNum, int limit, String userId) ;


    /**
     *
     * @param ccnt 多少条
     * @return
     */
    /**
     * 查询文章表中发表文章大于10条
     *     前10名的用户
     * @param ccnt 多少条
     * @param limitSize 多少名
     * @param wathMbsSet 例外用户ID
     * @return
     */
    public List<String> getRecommendMembersFromCmmPost(long ccnt, long limitSize, List<String> wathMbsSet);




    /**
     * 分页获取关注社区
     * @param pageNum 每页条数
     * @param limit
     * @param communityIds
     * @return
     */
    public List<Map<String,Object>> getFollowerCommunity(int pageNum,int limit, List<String> communityIds);

    List<String> listOfficialCommunityIds();

    List<String> listGameIds(List<String> list);
}
