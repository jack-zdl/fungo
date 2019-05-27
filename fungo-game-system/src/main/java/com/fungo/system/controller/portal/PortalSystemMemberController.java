package com.fungo.system.controller.portal;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.dto.*;
import com.fungo.system.entity.BasAction;
import com.fungo.system.proxy.ICommunityProxyService;
import com.fungo.system.proxy.IGameProxyService;
import com.fungo.system.service.BasActionService;
import com.fungo.system.service.IMemberService;
import com.fungo.system.service.IUserService;
import com.fungo.system.service.MemberService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MyCommentBean;
import com.game.common.dto.community.MyPublishBean;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.CommonUtil;
import com.game.common.util.CommonUtils;
import com.game.common.util.PageTools;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@Api(value = "", description = "会员相关接口")
public class PortalSystemMemberController {

    @Autowired
    private IMemberService memberService;

    @ApiOperation(value = "获取点赞我的", notes = "获取点赞我的")
    @RequestMapping(value = "/api/portal/system/mine/like", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getLikeNotice(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPage) throws Exception {

        String appVersion = "";
        appVersion = request.getHeader("appversion");
        return memberService.getLikeNotice(memberUserPrefile.getLoginId(), inputPage, appVersion);
    }


    @ApiOperation(value = "获取评论我的", notes = "获取评论我的")
    @RequestMapping(value = "/api/portal/system/mine/comment", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getCommentNotice(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPage) throws Exception {

        String appVersion = "";
        appVersion = request.getHeader("appversion");
        return memberService.getCommentNotice(memberUserPrefile.getLoginId(), inputPage, appVersion);
    }


    @ApiOperation(value = "获取系统消息", notes = "获取系统消息")
    @RequestMapping(value = "/api/portal/system/mine/system", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<SysNoticeBean> getSystemNotice(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPage) {
        return memberService.getSystemNotice(memberUserPrefile.getLoginId(), inputPage);
    }

}

