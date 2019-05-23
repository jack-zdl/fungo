package com.fungo.system.controller;

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
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @Autowired
    private BasActionDao actionDao;

    @Autowired
    private MemberService imemberService;

    @Autowired
    private IUserService iUserService;

    @Autowired
    private BasActionService actionService;

    @Autowired
    private FungoCacheMember fungoCacheMember;

    @Autowired
    private IGameProxyService gameProxyService;

    @Autowired
    private ICommunityProxyService communityProxyService;


    @ApiOperation(value = "获取我的收藏", notes = "获取我的收藏")
    @RequestMapping(value = "/api/mine/collection", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<CollectionOutBean> getCollection(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPage) {
        return memberService.getCollection(memberUserPrefile.getLoginId(), inputPage);
    }

    @ApiOperation(value = "获取关注内容 ", notes = "获取关注内容 ")
    @RequestMapping(value = "/api/mine/follow", method = RequestMethod.POST)
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

        return memberService.getFollower(myId, memberId, inputPage);
    }

    @ApiOperation(value = "获取我的粉丝", notes = "获取我的粉丝")
    @RequestMapping(value = "/api/mine/fans", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getFollowee(MemberUserProfile memberUserPrefile, @RequestBody FollowInptPageDao inputPage) throws Exception {
        String memberId = inputPage.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "找不到目标");
        }
        String myId = memberUserPrefile.getLoginId();

        return memberService.getFollowee(myId, memberId, inputPage);
    }

    //暂时无用
    @ApiOperation(value = "浏览记录", notes = "浏览记录")
    @RequestMapping(value = "/api/mine/history", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getHistory(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPage) {
        return memberService.getHistory(memberUserPrefile.getLoginId(), inputPage);
    }


    @ApiOperation(value = "获取点赞我的", notes = "获取点赞我的")
    @RequestMapping(value = "/api/mine/like", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getLikeNotice(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPage) throws Exception {

        String appVersion = "";
        appVersion = request.getHeader("appversion");
//		System.out.println("appVersion:"+appVersion);
        return memberService.getLikeNotice(memberUserPrefile.getLoginId(), inputPage, appVersion);
    }


    @ApiOperation(value = "获取评论我的", notes = "获取评论我的")
    @RequestMapping(value = "/api/mine/comment", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "请求页", paramType = "form", dataType = "number"),
            @ApiImplicitParam(name = "limit", value = "每页条数", paramType = "form", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getCommentNotice(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InputPageDto inputPage) throws Exception {

        String appVersion = "";
//		appVersion = (String) request.getAttribute("appVersion");
        appVersion = request.getHeader("appversion");
//		System.out.println("appVersion:"+appVersion);
        return memberService.getCommentNotice(memberUserPrefile.getLoginId(), inputPage, appVersion);
    }

    @ApiOperation(value = "获取我的未读消息", notes = "获取我的未读消息")
    @RequestMapping(value = "/api/mine/notification", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Object>> getUnReadNotice(MemberUserProfile memberUserPrefile, HttpServletRequest request) {
        String appVersion = "";
        appVersion = request.getHeader("appversion");


        ResultDto<Map<String, Object>> re = new ResultDto<Map<String, Object>>();
        Map<String, Object> resultMap = memberService.getUnReadNotice(memberUserPrefile.getLoginId(), appVersion);
        if (null != resultMap && !resultMap.isEmpty()) {
            re.setData(resultMap);
        }
        return re;
    }

    @ApiOperation(value = "获取系统消息", notes = "获取系统消息")
    @RequestMapping(value = "/api/mine/system", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<SysNoticeBean> getSystemNotice(MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPage) {
        return memberService.getSystemNotice(memberUserPrefile.getLoginId(), inputPage);
    }

    //暂时无用
    @ApiOperation(value = "获取我的时间线", notes = "获取我的时间线")
    @RequestMapping(value = "/api/mine/timeline", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<Map<String, Object>> getTimeLine(MemberUserProfile memberUserPrefile) {
        return memberService.getTimeLine(memberUserPrefile.getLoginId());
    }

    @ApiOperation(value = "我的游戏列表", notes = "我的游戏列表")
    @RequestMapping(value = "/api/mine/gameList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyGameBean> getGameList(MemberUserProfile memberUserPrefile, @RequestBody MyGameInputPageDto inputPage, HttpServletRequest request) {
        String os = (String) request.getAttribute("os");
        return memberService.getGameList(memberUserPrefile.getLoginId(), inputPage,os);
    }


//	//获取用户动态
//	public ResultDto<String> getFeed(String memberId);


    @ApiOperation(value = "获取用户时间线", notes = "获取用户时间线")
    @RequestMapping(value = "/api/user/timeline", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "cardId", value = "用户id", paramType = "path", dataType = "number")
    })
    public FungoPageResultDto<Map<String, Object>> getUserTimeline(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody TimeLineInputPage inputpage) throws ParseException {
        FungoPageResultDto<Map<String, Object>> re = null;

        String keySuffix = JSON.toJSONString(inputpage);
        re = (FungoPageResultDto<Map<String, Object>>) fungoCacheMember.getIndexCache(FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_TIME_LINE, keySuffix);
        if (null != re  && null != re.getData() && re.getData().size() > 0) {
            return re;
        }

        re = new FungoPageResultDto<Map<String, Object>>();
        String[] types = {"8", "9", "11", "14"};
        Page<BasAction> page = actionService.selectPage(new Page<BasAction>(inputpage.getPage(), inputpage.getLimit()), new EntityWrapper<BasAction>()
                .in("type", types).ne("state", "-1").eq("member_id", inputpage.getCardId()).orderBy("updated_at", false));
        List<BasAction> plist = page.getRecords();
        List<String> postIdList = new ArrayList<String>();
        List<String> evaluationIdList = new ArrayList<String>();
        List<String> moodIdList = new ArrayList<>();
        for (BasAction basAction : plist) {
            int actionType = basAction.getType();
            if (11 == actionType) {
                postIdList.add(basAction.getTargetId());
            } else if (14 == actionType) {
                moodIdList.add(basAction.getTargetId());
            } else {
                evaluationIdList.add(basAction.getTargetId());
                //457 收藏 关注 下载
            }/*else if(4 == actionType) {
				
			}else if(5 == actionType) {
				
			}else if(7 == actionType) {
				
			}*/
        }

        List<Map<String, Object>> listDate = new ArrayList<Map<String, Object>>();
        if (postIdList.size() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("memberId", inputpage.getCardId());
            map.put("postIdList", postIdList);
            //  @todo 5.22  废弃
            listDate =   communityProxyService.getPostFeeds(map); // actionDao.getPostFeeds(map);
        }
        if (evaluationIdList.size() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("memberId", inputpage.getCardId());
            map.put("evaluationIdList", evaluationIdList);
            // @todo 5.22 废弃
            List<Map<String, Object>> list1 =  gameProxyService.getEvaluationFeeds(map); // actionDao.getEvaluationFeeds(map);
            listDate.addAll(list1);
        }
        if (moodIdList.size() != 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("memberId", inputpage.getCardId());
            map.put("moodIdList", moodIdList);
            // @todo 5.22 废弃
            List<Map<String, Object>> list2 = communityProxyService.getMoodFeeds(map);  //actionDao.getMoodFeeds(map);
            listDate.addAll(list2);
        }

        if (listDate.size() > 1) {
            Collections.sort(listDate, new DateFun());

        }

        List<Map<String, Object>> resultDate = new ArrayList<Map<String, Object>>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date today = format.parse(format.format(new Date()));
        Long time = (long) (24 * 60 * 60 * 1000); // 一天
        for (Map<String, Object> m : listDate) {//根据日期分组
            Date theDate = format.parse((String) m.get("createdAt"));
            Long interval = (today.getTime() - theDate.getTime()) / time;
            if (0 == interval) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("title", "今天");
                map1.put("feed_type", "-1");
                if (!resultDate.contains(map1)) {
                    resultDate.add(map1);
                }
            } else if (1 == interval) {
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("title", "昨天");
                map1.put("feed_type", "-1");
                if (!resultDate.contains(map1)) {
                    resultDate.add(map1);
                }
            } else {
                Map<String, Object> map1 = new HashMap<String, Object>();
                map1.put("title", format.format(theDate));
                map1.put("feed_type", "-1");
                if (!resultDate.contains(map1)) {
                    resultDate.add(map1);
                }
            }
            m.put("content", CommonUtils.filterWord((String) m.get("content")));
            resultDate.add(m);
        }

        re.setData(resultDate);
        PageTools.pageToResultDto(re, page);

        //redis cache
        fungoCacheMember.excIndexCache(true, FungoCoreApiConstant.FUNGO_CORE_API_MEMBER_MINE_TIME_LINE, keySuffix, re);
        return re;
    }




    @ApiOperation(value = "其他会员信息", notes = "其他会员信息")
    @RequestMapping(value = "/api/user/card/{cardId}", method = RequestMethod.GET)
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
        AuthorBean author = iUserService.getUserCard(cardId, memberId);

        re.setData(author);
        return re;
    }



    @ApiOperation(value = "web端会员信息", notes = "web端会员信息")
    @RequestMapping(value = "/api/user/webInfo", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public ResultDto<AuthorBean> getWebUserInfo(MemberUserProfile memberUserPrefile) {
        String memberId = memberUserPrefile.getLoginId();

        return memberService.getUserInfoPc(memberId);
    }



    @ApiOperation(value = "我的游戏评测(2.4.3)", notes = "我的游戏评测")
    @RequestMapping(value = "/api/mine/evaluationList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return memberService.getMyEvaluationList(memberId, input);
    }




    @ApiOperation(value = "我的文章(2.4.3)", notes = "我的文章")
    @RequestMapping(value = "/api/mine/posts", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyPublishBean> getMyPosts(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return memberService.getMyPosts(memberId, input);
    }



    @ApiOperation(value = "我的心情(2.4.3)", notes = "我的心情")
    @RequestMapping(value = "/api/mine/moods", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyPublishBean> getMyMoods(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return memberService.getMyMoods(memberId, input);
    }

    @ApiOperation(value = "我的评论(2.4.3)", notes = "我的评论")
    @RequestMapping(value = "/api/mine/comments", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyCommentBean> getMyComments(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return memberService.getMyComments(memberId, input);
    }




    @ApiOperation(value = "我的等级信息(2.4.3)", notes = "我的等级信息(2.4.3)")
    @RequestMapping(value = "/api/user/incents/spirit/ranks", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<MemberLevelBean> getMemberLevel(MemberUserProfile memberUserPrefile) {
        String loginId = memberUserPrefile.getLoginId();
        return memberService.getMemberLevel(loginId);

    }



    @ApiOperation(value = "获取我的发布", notes = "")
    @RequestMapping(value = "/api/mine/publish", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String, Integer>> getPublishCount(MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) {
        String memberId = "";
        if (CommonUtil.isNull(memberId)) {
            return ResultDto.error("-1", "未指定用户");
        }
//		String loginId = memberUserPrefile.getLoginId();
        return memberService.getPublishCount(memberId);
    }


}


//时间排序 倒序
class DateFun implements Comparator<Map<String, Object>> {
    @Override
    public int compare(Map<String, Object> m1, Map<String, Object> m2) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date t1 = format.parse((String) m1.get("createdAt"));
            Date t2 = format.parse((String) m2.get("createdAt"));
//			return (int)(format.parse((String)m1.get("createdAt")).getTime() - format.parse((String)m2.get("createdAt")).getTime());
            if (t1.before(t2)) {
                return 1;
            } else {
                return -1;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            throw new BusinessException("-1", "操作失败");
        }

    }
}