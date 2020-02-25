package com.fungo.system.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.entity.Member;
import com.fungo.system.service.ICommunityService;
import com.fungo.system.service.IUserService;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.community.FollowUserOutBean;
import com.game.common.util.CommonUtil;
import com.game.common.util.PageTools;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.date.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(value = "", description = "推荐")
public class RecommendController {

    private static final Logger LOGGER = LoggerFactory.getLogger( RecommendController.class);

    @Autowired
    private ICommunityService iCommunityService;

    @Autowired
    private IUserService userService;

    /**
     * 玩家推荐规则：
     *   规则一 玩家推荐：
     *     发布文章数 大于10 或者 游戏评论数大于14
     *   规则二 玩家已关注列表替换规则：
     *    1.显示已经关注用户数量：最大10个
     *    2.若有新的推荐用户，且未关注，替换到玩家已关注列表的前面。且 该类别数量恒定10人
     *
     */
    @ApiOperation(value = "推荐用户列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/recommend/users", method = RequestMethod.POST)
    @ApiImplicitParams({
    })
    public FungoPageResultDto<FollowUserOutBean> getDynamicsUsersList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody InputPageDto inputPageDto) {
        FungoPageResultDto<FollowUserOutBean> re = new FungoPageResultDto<FollowUserOutBean>();
        List<FollowUserOutBean> list = new ArrayList<FollowUserOutBean>();
        re.setData(list);
        String memberId = "";
        if (memberUserPrefile != null) {
            memberId = memberUserPrefile.getLoginId();
        }
        //按规则一 查询出官方推荐的玩家数据
        List<Member> members = iCommunityService.getRecomMembers(inputPageDto.getLimit(), memberId);
        Page<Member> pageFormat = pageFormat(members, inputPageDto.getPage(), inputPageDto.getLimit());
        members = pageFormat.getRecords();
        for (Member member : members) {
            FollowUserOutBean bean = new FollowUserOutBean();
            bean.setAvatar(member.getAvatar());
            bean.setCreatedAt(DateTools.fmtDate(member.getCreatedAt()));
            bean.setLevel(member.getLevel());
            bean.setMemberNo(member.getMemberNo());
            bean.setObjectId(member.getId());
            bean.setUpdatedAt(DateTools.fmtDate(member.getUpdatedAt()));
            bean.setUsername(member.getUserName());
            bean.setFollowed(member.isFollowed());
            try {
                bean.setStatusImg(userService.getStatusImage(member.getId()));
            } catch (Exception e) {
                LOGGER.error( "推荐用户列表异常",e );
            }
            if (!CommonUtil.isNull(member.getSign())) {
                bean.setSign(member.getSign());
            } else {
                bean.setSign("活跃达人");
            }
            list.add(bean);
        }
        PageTools.pageToResultDto(re, pageFormat);
        return re;
    }

    //手动分页
    public Page<Member> pageFormat(List<Member> members, int page, int limit) {
        int totalCount = members.size();//总条数
        int totalPage = (int) Math.ceil((double) totalCount / limit);//总页数
        if (members.size() == 0) {
        } else if (page == totalPage) {
            members = members.subList(limit * (page - 1), totalCount);
        } else {
            members = members.subList(limit * (page - 1), limit * page);
        }
        Page<Member> memberPage = new Page<>(page, limit);
        memberPage.setRecords(members);
        memberPage.setCurrent(page);
        memberPage.setTotal(totalCount);
        return memberPage;
    }

}
