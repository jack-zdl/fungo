package com.fungo.system.controller.portal;

import com.fungo.system.service.IActionService;
import com.game.common.dto.ActionInput;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  PC2.0用户行为
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/27 13:12
 */
@RestController
@Api(value="",description="PC2.0用户行为")
public class PortalSystemActionController {

    @Autowired
    private IActionService actionService;

    @ApiOperation(value="PC2.0点赞", notes="")
    @RequestMapping(value="/api/portal/system/action/like", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> like(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody ActionInput inputDto) throws Exception {
        String appVersion = "";
        appVersion = request.getHeader("appversion");
        return actionService.like(memberUserPrefile.getLoginId(), inputDto,appVersion);
    }

    @ApiOperation(value="PC2.0取消赞", notes="")
    @RequestMapping(value="/api/portal/system/action/like", method= RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
    })
    public ResultDto<String> unlike(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unLike(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="关注", notes="")
    @RequestMapping(value="/api/portal/system/action/follow", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> follow(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.follow(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="取消关注", notes="")
    @RequestMapping(value="/api/portal/system/action/follow", method= RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> unFollow(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unFollow(memberUserPrefile.getLoginId(), inputDto);
    }
}
