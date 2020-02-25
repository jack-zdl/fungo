package com.fungo.games.controller.portal;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fungo.games.entity.Game;
import com.fungo.games.entity.GameInvite;
import com.fungo.games.facede.IEvaluateProxyService;
import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.helper.MQProduct;
import com.fungo.games.service.GameInviteService;
import com.fungo.games.service.GameService;
import com.fungo.games.service.IMakeAndInviteGameService;
import com.game.common.bean.advice.BasNoticeDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.MsgTplBean;
import com.game.common.dto.ResultDto;
import com.game.common.dto.mark.BindingAppleInputBean;
import com.game.common.dto.mark.InviteInput;
import com.game.common.dto.mark.MakeCheckOut;
import com.game.common.dto.mark.MakeInput;
import com.game.common.dto.user.MemberDto;
import com.game.common.util.CommonUtil;
import com.game.common.util.UUIDUtils;
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
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Api(value = "", description = "预约/邀请")
public class PortalGameMakeAndInviteGameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PortalHomePageController.class);

    @Autowired
    private IMakeAndInviteGameService makeAndInviteGameService;
    @Autowired
    private GameService gameService;
    @Autowired
    private GameInviteService gameInviteService;
    @Autowired
    private IEvaluateProxyService iEvaluateProxyService;
    @Autowired
    private MQProduct mqProduct;
    @Autowired
    private SystemFeignClient systemFeignClient;

    @ApiOperation(value = "预约接口(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/game", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> makeGame(MemberUserProfile memberUserPrefile, @RequestBody MakeInput makeInput) throws Exception {
        return makeAndInviteGameService.makGame(memberUserPrefile.getLoginId(), makeInput.getGameId(), makeInput.getPhoneModel());
    }

    @ApiOperation(value = "取消预约", notes = "")
    @RequestMapping(value = "/api/portal/games/make/cancel", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> unmakeGame(MemberUserProfile memberUserPrefile, @RequestBody MakeInput makeInput) {
        return makeAndInviteGameService.unmakeGame(memberUserPrefile.getLoginId(), makeInput.getGameId(), makeInput.getPhoneModel());
    }

    @ApiOperation(value = "绑定appleId", notes = "")
    @RequestMapping(value = "/api/portal/games/make/bindingAppleID", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> bindingAppleID(MemberUserProfile memberUserPrefile, @RequestBody BindingAppleInputBean makeInput) {
        return makeAndInviteGameService.bindingAppleID(memberUserPrefile.getLoginId(), makeInput);
    }

    @ApiOperation(value = "游戏测试条款(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/game/clause", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<Map<String, String>> getgameClause(@Anonymous MemberUserProfile memberUserPrefile) {
        ResultDto<Map<String, String>> re = new ResultDto<>();
        Map<String, String> map = new HashMap<>();
        map.put("url", "http://static.fungoweb.com/static/site/pages/testxy.html");
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "游戏测测试帮助(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/game/help", method = RequestMethod.GET)
    @ApiImplicitParams({})
    public ResultDto<Map<String, String>> getgameTestHelp() {
        ResultDto<Map<String, String>> re = new ResultDto<>();
        Map<String, String> map = new HashMap<>();
        map.put("url", "http://static.fungoweb.com/static/site/pages/testHelp.html");
        re.setData(map);
        return re;
    }

    @ApiOperation(value = "游戏测试验证(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/game/check", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<MakeCheckOut> getgameCheck(MemberUserProfile memberUserPrefile, @RequestBody MakeInput makeInput) {
        return makeAndInviteGameService.getgameCheck(memberUserPrefile.getLoginId(), makeInput.getGameId(), makeInput.getPhoneModel());
    }

    @ApiOperation(value = "同意游戏测试条款(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/make/game/agree", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> getgameAgree(MemberUserProfile memberUserPrefile, @RequestBody MakeInput makeInput) {
        return makeAndInviteGameService.getgameAgree(memberUserPrefile.getLoginId(), makeInput.getGameId(), makeInput.getPhoneModel());
    }

    /*@ApiOperation(value = "邀请用户列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/invite/users", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<FollowUserOutBean> getInviteUserList(MemberUserProfile memberUserPrefile, @RequestBody MakeInputPageDto inputPageDto) throws Exception {
        return makeAndInviteGameService.getInviteUserList(memberUserPrefile.getLoginId(), inputPageDto);
    }*/


    @ApiOperation(value = "邀请(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/invite/game", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> getInviteGaem(MemberUserProfile memberUserPrefile, HttpServletRequest request, @RequestBody InviteInput inputPageDto) {
        String appVersion = "";
        appVersion = request.getHeader("appversion");
        //IncentRanked ranked = incentRankedService.selectOne(new EntityWrapper<IncentRanked>().eq("mb_id", memberUserPrefile.getLoginId()).eq("rank_type", 1));
//		if(ranked.getCurrentRankId() < 2) {
//			return ResultDto.error("-1","需要到2级才能邀请玩家");
//		}
        if (CommonUtil.isNull(inputPageDto.getMemberId())) {
            return ResultDto.error("-1", "未找到邀请对象");
        }
        if (inputPageDto.getMemberId().equals(memberUserPrefile.getLoginId())) {
            return ResultDto.error("-1", "不能邀请你自己");
        }
        GameInvite one = gameInviteService.selectOne(new EntityWrapper<GameInvite>().eq("member_id", memberUserPrefile.getLoginId()).eq("game_id", inputPageDto.getGameId()).eq("invite_member_id", inputPageDto.getMemberId()).eq("state", 0));
        if (one == null) {
            String noticeId = "";
            GameInvite gameInvite = new GameInvite();
//            迁移微服务 根据用户id获取member对象 feign执行
//            2019-05-13
//            lyc
//            Member m = memberService.selectById(memberUserPrefile.getLoginId());
            MemberDto memberDto = new MemberDto();
            memberDto.setId(memberUserPrefile.getLoginId());
            MemberDto m = iEvaluateProxyService.getMemberDtoBySelectOne(memberDto);
            String name = "";
            if (m != null) {
                name = m.getUserName();
            } else {
                name = memberUserPrefile.getName();
            }
            //判断用户是否已被邀请
            GameInvite tem = gameInviteService.selectOne(new EntityWrapper<GameInvite>().eq("game_id", inputPageDto.getGameId()).eq("invite_member_id", inputPageDto.getMemberId()));
            if (tem != null) {
                noticeId = tem.getNoticeId();
                int count = gameInviteService.selectCount(new EntityWrapper<GameInvite>().eq("game_id", inputPageDto.getGameId()).eq("invite_member_id", inputPageDto.getMemberId()).orderBy( "created_at",false ));
                List<GameInvite>  gameInvites = gameInviteService.selectList(new EntityWrapper<GameInvite>().eq("game_id", inputPageDto.getGameId()).eq("invite_member_id", inputPageDto.getMemberId()).orderBy( "created_at",true ));
                ResultDto<MemberDto>  memberDtoResultDto = systemFeignClient.getMembersByid(gameInvites.get(0).getMemberId());
                String memberName = "";
                if(memberDtoResultDto != null && memberDtoResultDto.getData() != null){
                    MemberDto memberDto1 = memberDtoResultDto.getData();
                    memberName = memberDto1.getUserName();
                }
                Game g = gameService.selectById(inputPageDto.getGameId());
                MsgTplBean msg = this.getMsg(inputPageDto.getGameId(), "<span sytle='color:#242529'>" + memberName + "</span> 等" + (count + 1) + "人 邀请你评测 <a href='#' style='color: red;' >" + g.getName() + "游戏</a> 快去发表你的看法吧");
//                迁移微服务 逻辑块变动,根据id修改basNotice数据
//                2019-05-13
//                lyc
                /*BasNotice notice = new BasNotice();
                notice = notice.selectById(noticeId);
                //未读消息
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    notice.setData(objectMapper.writeValueAsString(msg));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notice.updateById();*/
                BasNoticeDto basNoticeDto = new BasNoticeDto();
                basNoticeDto.setId(noticeId);
                //未读消息
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    basNoticeDto.setData(objectMapper.writeValueAsString(msg));
                } catch (JsonProcessingException e) {
                    LOGGER.error( "邀请(v2.3)异常",e);
                }
//                逻辑块变动,根据id修改basNotice数据
                mqProduct.basNoticeUpdateById(basNoticeDto);
            } else {
                Game g = gameService.selectById(inputPageDto.getGameId());
                if(g==null){
                    return ResultDto.error("-1", "未找到相关游戏");
                }
                MsgTplBean msg = this.getMsg(inputPageDto.getGameId(), "<span sytle='color:#242529'>" + name + "</span> 邀请你评测 <a href='#' style='color: red;' >" + g.getName() + "游戏</a> 快去写一些你的看法吧");
//                迁移微服务 添加插入BasNotice数据返回主键
//                2019-05-13
//                lyc
                /*BasNotice notice = new BasNotice();
                notice.setCreatedAt(new Date());
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    notice.setData(objectMapper.writeValueAsString(msg));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notice.setIsRead(0);
                notice.setMemberId(inputPageDto.getMemberId());
                notice.setType(6);
                notice.setUpdatedAt(new Date());
                notice.insert();
                noticeId = notice.getId();*/
                BasNoticeDto basNoticeDto = new BasNoticeDto();
                basNoticeDto.setCreatedAt(new Date());
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    basNoticeDto.setData(objectMapper.writeValueAsString(msg));
                } catch (Exception e) {
                    LOGGER.error( "邀请(v2.3)basnotice信息异常",e);
                }
                basNoticeDto.setIsRead(0);
                basNoticeDto.setMemberId(inputPageDto.getMemberId());
                basNoticeDto.setType(6);
                basNoticeDto.setUpdatedAt(new Date());
                noticeId = UUIDUtils.getUUID();
                basNoticeDto.setId(noticeId);
                mqProduct.basNoticeInsertAndGameInviteReturnId(basNoticeDto);
            }
            gameInvite.setCreatedAt(new Date());
            gameInvite.setGameId(inputPageDto.getGameId());
            gameInvite.setInviteMemberId(inputPageDto.getMemberId());
            gameInvite.setMemberId(memberUserPrefile.getLoginId());
            gameInvite.setState(0);
            gameInvite.setUpdatedAt(new Date());
            gameInvite.setNoticeId(noticeId);
            gameInvite.insert();
            try {
                mqProduct.push(inputPageDto.getMemberId(), 3, appVersion);
            } catch (Exception e) {
                LOGGER.error( "邀请(v2.3)MQ信息异常",e);
            }
        }
        ResultDto<String> re = new ResultDto<String>();
        re.setMessage("邀请成功");
        return re;
    }

    @ApiOperation(value = "取消邀请(v2.3)", notes = "")
    @RequestMapping(value = "/api/portal/games/invite/cancel", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public ResultDto<String> getInviteCancel(MemberUserProfile memberUserPrefile, @RequestBody InviteInput inputPageDto) {
        GameInvite one = gameInviteService.selectOne(new EntityWrapper<GameInvite>().eq("member_id", memberUserPrefile.getLoginId())
                .eq("game_id", inputPageDto.getGameId()).eq("invite_member_id", inputPageDto.getMemberId()).eq("state", 0));
        if (one != null) {
            one.setState(-1);
            one.updateById();
        }
        ResultDto<String> re = new ResultDto<String>();
        re.setMessage("取消邀请成功");
        return re;
    }

    public MsgTplBean getMsg(String gameId, String content) {
        MsgTplBean msg = new MsgTplBean();
        msg.setActionType("1");
        msg.setContent(content);
        msg.setHref("");
        msg.setTargetId(gameId);
        msg.setTargetType("3");
        msg.setUserId("0b8aba833f934452992a972b60e9ad10");
        msg.setUserAvatar("http://output-mingbo.oss-cn-beijing.aliyuncs.com/official.png");
        msg.setUserName("FunGo大助手");
        msg.setUserType("1");
        msg.setMsgTime(DateTools.fmtDate(new Date()));
        return msg;
    }

}
