package com.fungo.system.controller;

import com.fungo.system.controller.portal.PortalSystemVideoController;
import com.fungo.system.service.IActionService;
import com.fungo.system.service.TaskRechargeService;
import com.game.common.dto.ActionInput;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.RechargeInput;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.BannerBean;
import com.game.common.dto.mall.MallBannersInput;
import com.game.common.util.StringUtil;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.annotation.MD5ParanCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户行为
 * @author sam
 * @updateAuthor lyc
 * @update 2019/5/5 16:26
 */
@RestController
@Api(value="")
public class ActionController {

    private static final Logger LOGGER = LoggerFactory.getLogger( ActionController.class);

    @Resource(name = "actionServiceImpl")
    private IActionService actionService;

    @Resource
    private TaskRechargeService taskRechargeService;

    @ApiOperation(value="点赞", notes="")
    @PostMapping(value="/api/action/like")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> like(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody ActionInput inputDto) throws Exception {
        String appVersion = "";
        appVersion = request.getHeader("appversion");
        return actionService.like(memberUserPrefile.getLoginId(), inputDto,appVersion);
    }

    @ApiOperation(value="取消赞", notes="")
    @RequestMapping(value="/api/action/like", method= RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> unlike(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unLike(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="分享", notes="")
    @RequestMapping(value="/api/action/share", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> share(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.share(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="收藏", notes="")
    @RequestMapping(value="/api/action/collect", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> collect(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception{
        return actionService.collect(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="取消收藏", notes="")
    @RequestMapping(value="/api/action/collect", method= RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> unCollect(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unCollect(memberUserPrefile.getLoginId(), inputDto);
    }


    @ApiOperation(value="批量取消收藏", notes="")
    @RequestMapping(value="/api/action/collects", method= RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> unCollectList(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unCollectList(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="关注", notes="")
    @RequestMapping(value="/api/action/follow", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> follow(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.follow(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="取消关注", notes="")
    @RequestMapping(value="/api/action/follow", method= RequestMethod.DELETE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type"})
    public ResultDto<String> unFollow(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unFollow(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="举报", notes="")
    @RequestMapping(value="/api/action/report", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id","target_type","information"})
    public ResultDto<String> report(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return this.actionService.report(memberUserPrefile.getLoginId(),inputDto);
    }

    @ApiOperation(value="下载", notes="")
    @RequestMapping(value="/api/action/download", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    @MD5ParanCheck(param = {"target_id"})
    public ResultDto<String> downLoad(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody ActionInput inputDto)  throws Exception{
        String memberId="";
        if(memberUserPrefile!=null) {
            memberId=memberUserPrefile.getLoginId();
        }
        return this.actionService.downLoad(memberId, inputDto);
    }

    @ApiOperation(value="忽略", notes="")
    @RequestMapping(value="/api/action/ignore", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> ignore(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.ignore(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="浏览", notes="")
    @RequestMapping(value="/api/action/browse", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> browse(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto)  throws Exception{
        return this.actionService.browse(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="查询用户是否有操作记录", notes="")
    @RequestMapping(value="/api/action/isdone", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "information",value = "备注信息",paramType = "form",dataType = "string")
    })
    public ResultDto<String> whetherIsDone(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.whetherIsDone(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="合集点赞", notes="")
    @RequestMapping(value="/api/action/collectionLike", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string")
    })
    public ResultDto<String> collectionLike(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody ActionInput inputDto) throws Exception {
        String appVersion = "";
        appVersion = request.getHeader("appversion");
        return actionService.collectionLike(memberUserPrefile.getLoginId(), inputDto,appVersion);
    }

    @ApiOperation(value="合集取消赞", notes="")
    @RequestMapping(value="/api/action/unCollectionLike", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string"),
            @ApiImplicitParam(name = "target_type",value = "目标对象类型",paramType = "form",dataType = "string"),
    })
    public ResultDto<String> unCollectionLike(MemberUserProfile memberUserPrefile,@RequestBody ActionInput inputDto) throws Exception {
        return actionService.unCollectionLike(memberUserPrefile.getLoginId(), inputDto);
    }

    @ApiOperation(value="查询合集点赞总数", notes="")
    @RequestMapping(value="/api/action/queryCollectionLike", method= RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id",value = "目标对象",paramType = "form",dataType = "string")
    })
    public ResultDto<BannerBean> queryCollectionLike(MemberUserProfile memberUserPrefile, @RequestBody MallBannersInput mallBannersInput) throws Exception {
        mallBannersInput.setLogin_id(memberUserPrefile.getLoginId());
        return actionService.queryCollectionLike(mallBannersInput);
    }

    @PostMapping("/api/action/completeRechargeTask")
    public ResultDto<String> completeRechargeTask(MemberUserProfile memberUserPrefile, @RequestBody RechargeInput rechargeInput){
        String loginId = memberUserPrefile.getLoginId();
        if(StringUtil.isNull(loginId)||StringUtil.isNull(rechargeInput.getOrderId())||rechargeInput.getRechargeType()==null){
            return  ResultDto.error("-1","参数错误");
        }
        return taskRechargeService.completeRechargeTask(loginId,rechargeInput.getRechargeType(),rechargeInput.getOrderId());
    }

    @PostMapping("/api/action/useSomeTimeFast")
    public ResultDto<String> useSomeTimeFast(MemberUserProfile memberUserPrefile)  throws Exception{
        String memberId = memberUserPrefile.getLoginId();
        if(StringUtil.isNull(memberId)){
            return  ResultDto.error("-1","参数错误");
        }
        return actionService.useSomeTimeFast(memberId);
    }

}
