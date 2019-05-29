package com.fungo.community.controller.portal;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  PC2.0回复
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/27 13:04
 */
@RestController
@Api(value = "", description = "PC2.0回复")
public class PortalCommunityRelyController {

    @Autowired
    private IEvaluateService evaluateService;

    @ApiOperation(value = "PC2.0发表回复", notes = "")
    @RequestMapping(value = "/api/portal/communitys/content/reply", method = RequestMethod.POST)
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
        return this.evaluateService.addReply(memberUserPrefile.getLoginId(), reply, appVersion);
    }


    @ApiOperation(value = "PC2.0回复列表", notes = "")
    @RequestMapping(value = "/api/portal/community/content/replys", method = RequestMethod.POST)
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
}
