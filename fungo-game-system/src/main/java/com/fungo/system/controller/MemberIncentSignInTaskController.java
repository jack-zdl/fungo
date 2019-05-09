package com.fungo.system.controller;

import com.fungo.system.service.IMemberIncentSignInTaskService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

/**
 * <p>
 *      用户签到任务controller
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@RestController
public class MemberIncentSignInTaskController {


    @Autowired
    private IMemberIncentSignInTaskService iMemberIncentSignInTaskService;

    /**
     * 登录用户执行签到
     * @param memberUserPrefile
     * @return
     */
    @RequestMapping(value = "/api/user/incents/signin", method = RequestMethod.POST)
    public ResultDto<Map> checkIn(MemberUserProfile memberUserPrefile) {
        String userId = memberUserPrefile.getLoginId();
        return iMemberIncentSignInTaskService.exSignIn(userId);
    }


    /**
     * 用户获取签到信息
     * @param memberUserPrefile
     * @return
     */
    @RequestMapping(value = "/api/user/incents/signin", method = RequestMethod.GET)
    public ResultDto<Map<String, Object>> getMemberSignInInfo(MemberUserProfile memberUserPrefile) {
        String userId = memberUserPrefile.getLoginId();
        return iMemberIncentSignInTaskService.getMemberSignInInfo(userId);
    }

    //---------
}
