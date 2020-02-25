package com.fungo.system.controller;

import com.fungo.system.service.IMakeAndInviteGameService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.community.FollowUserOutBean;
import com.game.common.dto.mark.MakeInputPageDto;
import com.game.common.util.annotation.MD5ParanCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "", description = "预约/邀请")
public class MakeAndInviteGameController {

    @Autowired
    private IMakeAndInviteGameService makeAndInviteGameService;

    @ApiOperation(value = "邀请用户列表(v2.3)", notes = "")
    @RequestMapping(value = "/api/invite/users", method = RequestMethod.POST)
    @ApiImplicitParams({})
    @MD5ParanCheck(param = {"gameId","page","limit"})
    public FungoPageResultDto<FollowUserOutBean> getInviteUserList(MemberUserProfile memberUserPrefile, @RequestBody MakeInputPageDto inputPageDto) throws Exception {
        return makeAndInviteGameService.getInviteUserList(memberUserPrefile.getLoginId(), inputPageDto);
    }

}
