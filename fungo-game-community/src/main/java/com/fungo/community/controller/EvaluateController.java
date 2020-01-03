package com.fungo.community.controller;


import com.fungo.community.service.IEvaluateService;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.*;
import com.game.common.enums.CommonEnum;
import com.game.common.repo.cache.facade.FungoCacheArticle;
import com.game.common.repo.cache.facade.FungoCacheIndex;
import com.game.common.util.ValidateUtils;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.annotation.LogicCheck;
import com.game.common.vo.DelObjectListVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(value = "", description = "评价接口")
public class EvaluateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluateController.class);

    @Autowired
    private IEvaluateService evaluateService;
    @Autowired
    private FungoCacheArticle fungoCacheArticle;
    @Autowired
    private FungoCacheIndex fungoCacheIndex;


    @ApiOperation(value = "评论帖子/心情", notes = "")
    @PostMapping(value = "/api/content/comment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "content", value = "内容", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "target_id", value = "帖子id", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "target_type", value = "资源类型【1：帖子，2：心情】", paramType = "path", dataType = "string")

    })
    @LogicCheck(loginc = {"BANNED_TEXT"})
    public ResultDto<CommentOut> addComment(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody CommentInput commentInput) throws Exception {
        String appVersion = "2.5.1";
        if(StringUtils.isNoneBlank(request.getHeader("appversion"))){
            appVersion = request.getHeader("appversion");
        }
        return this.evaluateService.addComment(memberUserPrefile.getLoginId(), commentInput, appVersion);
    }


    @ApiOperation(value = "评论详情", notes = "")
    @GetMapping(value = "/api/content/comment/{commentId}")
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


    @ApiOperation(value = "心情评论详情", notes = "")
    @GetMapping(value = "/api/content/message/{messageId}")
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


    @ApiOperation(value = "帖子/心情评论列表", notes = "")
    @PostMapping(value = "/api/content/comments")
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

    @ApiOperation(value = "删除评论详情", notes = "")
    @DeleteMapping(value = "/api/content/comment")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "comment_idS", value = "帖子id集合", paramType = "path", dataType = "string"),
            @ApiImplicitParam(name = "user_id", value = "用户id", paramType = "path", dataType = "string")
    })
    public ResultDto<String> getCommentDetail(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody DelObjectListVO commentIds) {
        try {
            ValidateUtils.is(commentIds).notNull();
            String memberId = "";
            if (memberUserPrefile != null) {
                memberId = memberUserPrefile.getLoginId();
            }
            int type = commentIds.getType();
            List<String> ids = commentIds.getCommentIds();
            ResultDto<String> resultDto =  this.evaluateService.delCommentList(memberId, type,ids);
            if(Integer.valueOf(CommonEnum.SUCCESS.code()).equals(resultDto.getStatus())){
                // 文章和心情评论缓存
                fungoCacheArticle.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_POST_CONTENT_COMMENTS, "", null);
                //我的評論redis緩存
                fungoCacheIndex.excIndexCache(false, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_USER_COMMENTS, "", null);

            }
            return resultDto;
        }catch (Exception e){
            LOGGER.error( "删除评论详情异常,id集合:"+commentIds.toString(),e );
            return ResultDto.error( "-1","删除评论详情异常" );
        }
    }




}
