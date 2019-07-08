package com.fungo.community.msService.community;


import com.fungo.community.service.msService.IMSServiceCommunityService;
import com.game.common.bean.CommentBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmCommunityDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *      社区-社区微服务接口
 * </p>
 * since: V3.0.0
 * @author mxf
 * @since 2019-05-10
 */
@RestController
public class MSServiceCommunityController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MSServiceCommunityController.class);

    @Autowired
    private IMSServiceCommunityService imsServiceCommunityService;

    /**
     * 分页查询 社区 数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cty/lists")
    public FungoPageResultDto<CmmCommunityDto> queryCmmPostList(@RequestBody CmmCommunityDto cmmCommunityDto) {

        FungoPageResultDto<CmmCommunityDto> resultDto = imsServiceCommunityService.queryCmmCommunityList(cmmCommunityDto);
        return resultDto;
    }

    /**
     * 查询 官方社区id
     * @return
     */
    @GetMapping("/ms/service/cmm/cty/listOfficialCommunityIds")
    ResultDto<List<String>> listOfficialCommunityIds() {
        List<String> cmmPostDtoList = imsServiceCommunityService.listOfficialCommunityIds();
        return ResultDto.success(cmmPostDtoList);
    }

    /**
     * 根据社区id查询游戏id
     * @param list
     * @return
     */
    @GetMapping("/ms/service/cmm/cty/listGameIds")
    ResultDto<List<String>> listGameIds(@RequestParam("communityIds") List<String> list) {
        List<String> cmmPostDtoList = imsServiceCommunityService.listGameIds(list);
        return ResultDto.success(cmmPostDtoList);
    }

    /**
     * 查询单个社区详情数据
     * @return
     */
    @PostMapping("/ms/service/cmm/cty")
    public ResultDto<CmmCommunityDto> queryCmmCtyDetail(@RequestBody CmmCommunityDto cmmCommunityDto) {

        ResultDto<CmmCommunityDto> resultDto = new ResultDto<CmmCommunityDto>();
        CmmCommunityDto cmmCommunityDtoRs = imsServiceCommunityService.queryCmmCtyDetail(cmmCommunityDto);
        resultDto.setData(cmmCommunityDtoRs);
        return resultDto;
    }


    /**
     * 分页查询 我的动态 - 我的评论 数据
     * @param pageNum 当前页码
     * @param limit 每页条数
     * @param userId 用户id
     * @return
     */
    @PostMapping("/ms/service/cmm/user/comments")
    public FungoPageResultDto<CommentBean> getAllComments(@RequestParam("pageNum") int pageNum,
                                                          @RequestParam("limit") int limit, @RequestParam("userId") String userId) {

        FungoPageResultDto<CommentBean> resultDto = imsServiceCommunityService.getAllComments(pageNum, limit, userId);


        return resultDto;
    }


    /**
     * 查询文章表中发表文章大于10条的前10名用户
     * @param ccnt 达到文章条数
     * @param limitSize 达到的用户数
     * @param wathMbsSet 需要排除的用户id集合
     * @return
     */
    @PostMapping("/ms/service/cmm/user/post/ids")
    public ResultDto<List<String>> getRecommendMembersFromCmmPost(@RequestParam("ccnt") long ccnt, @RequestParam("limitSize") long limitSize,
                                                                  @RequestParam("wathMbsSet") List<String> wathMbsSet) {
        ResultDto<List<String>> resultDto = new ResultDto<List<String>>();
        List<String> membersIdsList = imsServiceCommunityService.getRecommendMembersFromCmmPost(ccnt, limitSize, wathMbsSet);
        resultDto.setData(membersIdsList);
        return resultDto;
    }


    /**
     * 分页查询 关注社区  数据
     * @param pageNum 当前页码
     * @param limit 每页条数
     * @param communityIds 社区id
     * @return
     */
    @PostMapping("/ms/service/cmm/user/flw/cmtlists")
    public FungoPageResultDto<Map<String, Object>> getFollowerCommunity(@RequestParam("pageNum") int pageNum,
                                                                        @RequestParam("limit") int limit, @RequestParam("communityIds") List<String> communityIds) {

        FungoPageResultDto<Map<String, Object>> resultDto = new FungoPageResultDto<Map<String, Object>>();

        List<Map<String, Object>> commentBeanList = imsServiceCommunityService.getFollowerCommunity(pageNum, limit, communityIds);

        resultDto.setData(commentBeanList);

        return resultDto;
    }

    //--------
}
