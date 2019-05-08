package com.fungo.community.controller;


import com.fungo.community.service.IEvaluateService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.ReplyInputBean;
import com.game.common.dto.community.ReplyInputPageDto;
import com.game.common.dto.community.ReplyOutBean;
import com.game.common.dto.community.ReplyOutPageDto;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 回复
 * @author sam
 *
 */
@RestController
@Api(value = "", description = "回复")
public class RelyController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RelyController.class);


    @Autowired
    private IEvaluateService evaluateService;

    @Autowired
    private IPushService pushService;


    @ApiOperation(value = "发表回复", notes = "")
    @RequestMapping(value = "/api/content/reply", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "评论内容", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "target_id", value = "目标对象id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "reply_to", value = "回复@用户id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "target_type", value = "目标对象类型	5", paramType = "path", dataType = "string")

    })
    public ResultDto<ReplyOutBean> addComment(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody ReplyInputBean reply) throws Exception {
        String os = "";
        os = (String) request.getAttribute("os");
        reply.setOs(os);
        String appVersion = "";
        appVersion = request.getHeader("appversion");

//		System.out.println("addComment-appversion:"+appVersion);

        return this.evaluateService.addReply(memberUserPrefile.getLoginId(), reply, appVersion);
    }


    @ApiOperation(value = "回复列表", notes = "")
    @RequestMapping(value = "/api/content/replys", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target_id", value = "对象id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户id", paramType = "path", dataType = "string")
    })
    public FungoPageResultDto<ReplyOutPageDto> getCommentList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody ReplyInputPageDto commentPage) {
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        return this.evaluateService.getReplyList(memberId, commentPage);
    }

    //-------
}
