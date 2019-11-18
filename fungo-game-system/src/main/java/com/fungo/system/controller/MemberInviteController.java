package com.fungo.system.controller;

import com.fungo.system.dto.InviteInfoVO;
import com.fungo.system.service.IMemberService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>邀请用户控制层</p>
 * @Date: 2019/10/18
 */
@RestController
@RequestMapping
public class MemberInviteController {

    @Autowired
    private IMemberService iMemberService;

    @GetMapping(value = "/api/system/member/invite")
    public ResultDto<InviteInfoVO> getInviteInfo(MemberUserProfile memberUserPrefile){
        String memberId = memberUserPrefile.getLoginId();
        return iMemberService.getInviteInfo( memberId);
    }
}
