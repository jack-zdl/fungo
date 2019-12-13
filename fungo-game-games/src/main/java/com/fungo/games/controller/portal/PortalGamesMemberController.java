package com.fungo.games.controller.portal;

import com.fungo.games.service.IEvaluateService;
import com.fungo.games.service.IGameService;
import com.fungo.games.service.portal.PortalGamesIGameService;
import com.fungo.games.service.portal.ProtalGameIEvaluateService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.evaluation.MyEvaluationBean;
import com.game.common.dto.game.MermberSearchInput;
import com.game.common.dto.game.MyGameBean;
import com.game.common.dto.game.MyGameInputPageDto;
import com.game.common.util.CommonUtil;
import com.game.common.util.annotation.Anonymous;
import io.swagger.annotations.Api;
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
 *  PC2.0会员相关接口
 * <p>
 *
 * @version V3.0.0
 * @Author lyc
 * @create 2019/6/12 14:18
 */
@RestController
@Api(value = "", description = "PC2.0会员相关接口")
public class PortalGamesMemberController {

    @Autowired
    private IGameService iGameService;

    @Autowired
    private IEvaluateService iEvaluateService;
    @Autowired
    private PortalGamesIGameService portalGamesIGameService;




    @ApiOperation(value = "PC2.0我的游戏列表", notes = "PC2.0我的游戏列表")
    @RequestMapping(value = "/api/portal/games/mine/gameList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyGameBean> getGameList(MemberUserProfile memberUserPrefile, @RequestBody MyGameInputPageDto inputPage, HttpServletRequest request) {
        String os = (String) request.getAttribute("os");
        return portalGamesIGameService.getMyGameList(memberUserPrefile.getLoginId(), inputPage,os);
    }



    @ApiOperation(value = "PC2.0我的游戏列表", notes = "PC2.0我的游戏列表")
    @RequestMapping(value = "/api/portal/games/mine/otherGameList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyGameBean> otherGameList(MemberUserProfile memberUserPrefile, @RequestBody MyGameInputPageDto inputPage, HttpServletRequest request) {
        String os = (String) request.getAttribute("os");
        return portalGamesIGameService.getOtherGameList(memberUserPrefile.getLoginId(), inputPage,os);
    }



    @ApiOperation(value = "PC2.0我的游戏评测(2.4.3)", notes = "PC2.0我的游戏评测")
    @RequestMapping(value = "/api/portal/games/mine/evaluationList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
//		String loginId = memberUserPrefile.getLoginId();
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return iEvaluateService.getMyEvaluationList(memberId, input);
    }


    /**
     * 功能描述:PC2.0我的下载的游戏列表
     * @return: com.game.common.dto.FungoPageResultDto<com.game.common.dto.game.MyGameBean>
     * @auther: dl.zhang
     * @date: 2019/8/20 13:50
     */
    @ApiOperation(value = "PC2.0我的下载的游戏列表", notes = "PC2.0我的游戏列表")
    @RequestMapping(value = "/api/portal/games/mine/download/gameList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyGameBean> getDownloadGameList(MemberUserProfile memberUserPrefile, @RequestBody MyGameInputPageDto inputPage, HttpServletRequest request) {
        String os = (String) request.getAttribute("os");
        return portalGamesIGameService.getMyDownloadGameList(memberUserPrefile.getLoginId(), inputPage,os);
    }
}
