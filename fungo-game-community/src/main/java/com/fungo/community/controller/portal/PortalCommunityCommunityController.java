package com.fungo.community.controller.portal;

import com.fungo.community.service.ICommunityService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CommunityOut;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  PC2.0社区
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/27 11:15
 */
@SuppressWarnings("all")
@RestController
@Api(value = "", description = "PC2.0社区")
public class PortalCommunityCommunityController {

    @Autowired
    private ICommunityService communityService;

    @ApiOperation(value = "PC2.0社区详情", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "communityId", value = "社区id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "userId", value = "用户id", paramType = "form", dataType = "string")
    })
    @RequestMapping(value = "/api/portal/communitys/content/community/{communityId}", method = RequestMethod.GET)
    public ResultDto<CommunityOut> getCommunityDetail(@Anonymous MemberUserProfile memberUserPrefile,
                                                      @PathVariable("communityId") String communityId) {
        String userId = "";
        if (memberUserPrefile != null) {
            userId = memberUserPrefile.getLoginId();
        }
        try {
            return communityService.getCommunityDetail(communityId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultDto.error("-1", "操作失败");
        }
    }
}
