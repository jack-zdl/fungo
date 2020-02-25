package com.fungo.system.controller.portal;

import com.fungo.system.service.IActionService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.ActionInput;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.enums.CommonEnum;
import com.game.common.repo.cache.facade.FungoCacheMood;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
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

    @Resource(name = "actionServiceImpl")
    private IActionService actionService;
    @Resource(name = "protalSystemActionServiceImpl")
    private IActionService protalSystemActionServiceImpl;
    @Autowired
    private FungoCacheMood fungoCacheMood;

    @ApiOperation(value="PC2.0点赞", notes="")
    @RequestMapping(value="/api/portal/system/action/like", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> like(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody ActionInput inputDto) throws Exception {
        String appVersion = "2.5.1";
        if(StringUtils.isNoneBlank(request.getHeader("appversion"))){
            appVersion = request.getHeader("appversion");
        }
        ResultDto<String>  resultDto = actionService.like(memberUserPrefile.getLoginId(), inputDto,appVersion);
        if(CommonEnum.SUCCESS.code().equals(String.valueOf(resultDto.getStatus()))){
            fungoCacheMood.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_GAME_EVALUATIONS, "", null);
        }
        return resultDto;
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

    @ApiOperation(value="PC2.0关注", notes="")
    @RequestMapping(value="/api/portal/system/action/follow", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> follow(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return protalSystemActionServiceImpl.follow(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="PC2.0取消关注", notes="")
    @RequestMapping(value="/api/portal/system/action/follow", method= RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> unFollow(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unFollow(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="PC2.0举报", notes="")
    @RequestMapping(value="/api/portal/system/action/report", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> report(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return this.actionService.report(memberUserPrefile.getLoginId(),inputDto);
    }

    @ApiOperation(value="下载", notes="")
    @RequestMapping(value="/api/portal/system/action/download", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> downLoad(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody ActionInput inputDto)  throws Exception{
        String memberId="";
        if(memberUserPrefile!=null) {
            memberId=memberUserPrefile.getLoginId();
        }
        return this.actionService.downLoad(memberId, inputDto);
    }

    @ApiOperation(value="分享", notes="")
    @RequestMapping(value="/api/portal/system/action/share", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> share(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.share(memberUserPrefile.getLoginId(), inputDto);
    }
}
