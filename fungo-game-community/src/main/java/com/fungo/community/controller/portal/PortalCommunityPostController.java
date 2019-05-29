package com.fungo.community.controller.portal;

import com.fungo.community.service.IPostService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.community.PostInputPageDto;
import com.game.common.dto.community.PostOutBean;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  PC2.0帖子
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/27 13:25
 */
@RestController
@Api(value = "", description = "PC2.0帖子")
@EnableAsync
public class PortalCommunityPostController {

    @Autowired
    private IPostService bsPostService;

    @ApiOperation(value = "PC2.0帖子列表", notes = "")
    @RequestMapping(value = "/api/portal/communitys/content/posts", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "sort", value = "排序字段", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "page", value = "页数", paramType = "form", dataType = "int"),
            @ApiImplicitParam(name = "community_id", value = "社区id", paramType = "form", dataType = "String")
    })
    public FungoPageResultDto<PostOutBean> getPostContentList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody PostInputPageDto postInputPageDto) {

        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        try {
            return bsPostService.getPostList(userId, postInputPageDto);
        } catch (Exception e) {
            e.printStackTrace();
            return FungoPageResultDto.error("-1", "操作失败");
        }
    }
}
