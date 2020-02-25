package com.fungo.games.controller;

import com.fungo.games.service.IEvaluateService;
import com.fungo.games.service.IGameService;
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
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@Api(value = "", description = "会员相关接口")
public class MemberController {

    @Autowired
    private IGameService iGameService;

    @Autowired
    private IEvaluateService iEvaluateService;

    @ApiOperation(value = "我的游戏列表", notes = "我的游戏列表")
    @RequestMapping(value = "/api/mine/gameList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyGameBean> getGameList(MemberUserProfile memberUserPrefile, @RequestBody MyGameInputPageDto inputPage, HttpServletRequest request) {
        String os = (String) request.getAttribute("os");
        return iGameService.getMyGameList(memberUserPrefile.getLoginId(), inputPage,os);
    }

    @ApiOperation(value = "我的游戏评测(2.4.3)", notes = "我的游戏评测")
    @RequestMapping(value = "/api/mine/evaluationList", method = RequestMethod.POST)
    @ApiImplicitParams({})
    public FungoPageResultDto<MyEvaluationBean> getMyEvaluationList(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody MermberSearchInput input) throws Exception {
        String memberId = input.getMemberId();
        if (CommonUtil.isNull(memberId)) {
            return FungoPageResultDto.error("-1", "未指定用户");
        }
        return iEvaluateService.getMyEvaluationList(memberId, input);
    }

}