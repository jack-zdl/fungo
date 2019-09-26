package com.fungo.system.controller.portal;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.dao.BasActionDao;
import com.fungo.system.dto.*;
import com.fungo.system.service.IMemberService;
import com.fungo.system.service.IUserService;
import com.fungo.system.service.impl.MemberServiceImpl;
import com.fungo.system.service.portal.PortalSystemIMemberService;
import com.fungo.system.service.portal.PortalSystemIUserService;
import com.game.common.api.InputPageDto;
import com.game.common.consts.FungoCoreApiConstant;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.MyCommentBean;
import com.game.common.dto.community.MyPublishBean;
import com.game.common.enums.AbstractResultEnum;
import com.game.common.repo.cache.facade.FungoCacheMember;
import com.game.common.util.CommonUtil;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@Api(value = "", description = "会员相关接口")
public class PortalSystemMemberController {

    private static final Logger logger = LoggerFactory.getLogger(PortalSystemMemberController.class);

    @Autowired
    private PortalSystemIMemberService memberService;
    @Autowired
    private IMemberService iMemberService;
    @Autowired
    private PortalSystemIUserService portalSystemIUserService;
    @Autowired
    private FungoCacheMember fungoCacheMember;

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

    @ApiOperation(value = "PC2.0我的心情(2.4.3)", notes = "我的心情")
    @RequestMapping(value = "/api/portal/system/mine/moods", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyPublishBean> getMyMoods(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return iMemberService.getMyMoods(memberId, input);
    }

    @ApiOperation(value = "PC2.0我的文章(2.4.3)", notes = "我的文章")
    @RequestMapping(value = "/api/portal/system/mine/posts", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyPublishBean> getMyPosts(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return iMemberService.getMyPosts(memberId, input);
    }

    @ApiOperation(value = "PC2.0我的评论(2.4.3)", notes = "我的评论")
    @RequestMapping(value = "/api/portal/system/mine/comments", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyCommentBean> getMyComments(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return iMemberService.getMyComments(memberId, input);
    }


    @ApiOperation(value = "PC2.0其他会员信息", notes = "PC2.0其他会员信息")
    @RequestMapping(value = "/api/portal/system/user/card/{cardId}", method = RequestMethod.GET)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cardId", value = "用户id", paramType = "path", dataType = "number")
    })
    public ResultDto<AuthorBean> getUserCard(@Anonymous MemberUserProfile memberUserPrefile, @PathVariable("cardId") String cardId) {
//		ResultDto<CardBean> re =new ResultDto<CardBean>();
//		CardBean bean =new CardBean();
//		re.setData(bean);
//		Member m=this.imemberService.selectById(cardId);
//		if(m==null) {return ResultDto.error("224","用户不存在");}
//		bean.setAvatar(m.getAvatar());
//		bean.setCreatedAt(DateTools.fmtDate(m.getCreatedAt()));
//		bean.setEmail(m.getEmail());
//		bean.setEmailVerified(false);
//
//		bean.setGender(m.getGender());
//		bean.setMobilePhoneNumber(m.getMobilePhoneNum());
//		bean.setIs_followed(false);
//		if(memberUserPrefile!=null) {
//			BasAction action=actionService.selectOne( new EntityWrapper<BasAction>().eq("type", "5").eq("member_id", memberUserPrefile.getLoginId()).eq("target_id", cardId).notIn("state", "-1"));
//			if(action!=null) {
//				bean.setIs_followed(true);
//			}
//		}
//		int followed = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).notIn("state", "-1").eq("target_id",cardId));
//		bean.setFollowee_num(followed);//粉丝
//		int follower = actionService.selectCount(new EntityWrapper<BasAction>().eq("type", 5).notIn("state", "-1").eq("target_type",0).eq("member_id",  cardId));
//		bean.setFollower_num(follower);//关注
//		bean.setLevel(m.getLevel());
//		bean.setMobilePhoneVerified(false);
//		bean.setObjectId(m.getId());
//		bean.setSign(m.getSign());
//		bean.setUpdatedAt(DateTools.fmtDate(m.getUpdatedAt()));
//		bean.setUsername(m.getUserName());
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        ResultDto<AuthorBean> re = new ResultDto<AuthorBean>();
        AuthorBean author = portalSystemIUserService.getUserCard(cardId, memberId);
        re.setData(author);
        return re;
    }



    @ApiOperation(value = "获取关注内容 ", notes = "获取关注内容 ")
    @RequestMapping(value = "/api/portal/system/mine/follow", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "关注目标的类型[0:用户,4:社区]", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getFollower(MemberUserProfile memberUserPrefile, @RequestBody FollowInptPageDao inputPage) throws Exception {
        String memberId = inputPage.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "找不到目标");
        }
        String myId = memberUserPrefile.getLoginId();

        return portalSystemIUserService.getFollower(myId, memberId, inputPage);
    }

    @ApiOperation(value = "获取我的粉丝", notes = "获取我的粉丝")
    @RequestMapping(value = "/api/portal/system/mine/fans", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getFollowee(MemberUserProfile memberUserPrefile, @RequestBody FollowInptPageDao inputPage) throws Exception {
        String memberId = inputPage.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "找不到目标");
        }
        FungoPageResultDto<Map<String, Object>> re = null;
        String myId = memberUserPrefile.getLoginId();
        //redis cache
        String keyPrefix = FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_FANS + myId;
        String keySuffix = JSON.toJSONString(inputPage);
        try {
            re = (FungoPageResultDto<Map<String, Object>>) fungoCacheMember.getIndexCache(keyPrefix, memberId + keySuffix);
            if (null != re && null != re.getData() && re.getData().size() > 0) {
                return re;
            }
            re =  portalSystemIUserService.getFollowee(myId, memberId, inputPage);
            //redis cache
            fungoCacheMember.excIndexCache(true, keyPrefix, keySuffix, re);
        }catch (Exception e){
            logger.error("PortalSystemMemberController.getFollowee获取我的粉丝异常",e);
            re = FungoPageResultDto.FungoPageResultDtoFactory.buildError("获取我的粉丝异常"+ AbstractResultEnum.CODE_SYSTEM_TWO.getFailevalue());
        }
        return  re;
    }

}

