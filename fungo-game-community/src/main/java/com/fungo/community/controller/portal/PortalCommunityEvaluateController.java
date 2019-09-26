package com.fungo.community.controller.portal;

import com.fungo.community.service.IEvaluateService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.util.ValidateUtils;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  PC2.0评价接口
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/5/31 15:34
 */
@RestController
@Api(value = "", description = "PC2.0评价接口")
public class PortalCommunityEvaluateController {

    @Autowired
    private IEvaluateService evaluateService;

    @ApiOperation(value = "PC2.0帖子/心情评论列表", notes = "")
    @RequestMapping(value = "/api/portal/community/content/comments", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment_id", value = "心情id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户id", paramType = "path", dataType = "string")
    })
    public FungoPageResultDto<CommentOutPageDto> getCommentList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody CommentInputPageDto commentPage) {
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        return this.evaluateService.getCommentList(memberId, commentPage);
    }

    @ApiOperation(value = "PC2.0评论详情", notes = "")
    @RequestMapping(value = "/api/portal/community/content/comment/{commentId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment_id", value = "帖子id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户id", paramType = "path", dataType = "string")
    })
    public ResultDto<CommentBeanOut> getCommentDetail(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("commentId") String commentId) {
        ValidateUtils.is(commentId).notNull();
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        return this.evaluateService.getCommentDetail(memberId, commentId);
    }


    @ApiOperation(value = "PC2.0评论帖子/心情", notes = "")
    @RequestMapping(value = "/api/portal/community/content/comment", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "内容", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "target_id", value = "帖子id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "target_type", value = "资源类型【1：帖子，2：心情】", paramType = "path", dataType = "string")

    })
    public ResultDto<CommentOut> addComment(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody CommentInput commentInput) throws Exception {
        String appVersion = "2.5.1";
        if(StringUtils.isNoneBlank(request.getHeader("appversion"))){
            appVersion = request.getHeader("appversion");
        }
        return this.evaluateService.addComment(memberUserPrefile.getLoginId(), commentInput, appVersion);
    }

    @ApiOperation(value = "PC2.0心情评论详情", notes = "")
    @RequestMapping(value = "/api/portal/community/content/message/{messageId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "messageId", value = "心情id", paramType = "path", dataType = "string"),
    })
    public ResultDto<CommentBeanOut> getMoodMessageDetail(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("messageId") String commentId) {
        ValidateUtils.is(commentId).notNull();
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }

        return this.evaluateService.getMoodMessageDetail(memberId, commentId);
    }
}
