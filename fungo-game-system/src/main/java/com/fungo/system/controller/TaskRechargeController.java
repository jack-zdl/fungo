package com.fungo.system.controller;

import com.fungo.system.service.TaskRechargeService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskRechargeController {
    @Autowired
    private TaskRechargeService taskRechargeService;


    public ResultDto<String> completeRechargeTask(MemberUserProfile memberUserPrefile,Integer rechargeType,String orderId){
        String loginId = memberUserPrefile.getLoginId();
        if(StringUtil.isNull(loginId)||StringUtil.isNull(orderId)){
            return  ResultDto.error("-1","参数错误");
        }
        taskRechargeService.completeRechargeTask(loginId,rechargeType,orderId);
        return ResultDto.success();
    }


}
