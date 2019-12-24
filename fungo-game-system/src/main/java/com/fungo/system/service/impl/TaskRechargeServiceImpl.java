package com.fungo.system.service.impl;

import com.fungo.system.service.IMemberIncentDoTaskFacadeService;
import com.fungo.system.service.TaskRechargeService;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class TaskRechargeServiceImpl implements TaskRechargeService {

    @Resource
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;

    @Override
    public void completeRechargeTask(String memberId, Integer rechargeType, String orderId) {
        try{
            //1 经验值
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_MONTH_EXP.code());
            //2 fungo币
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_MONTH_COIN.code());
            //end
        }catch (Exception  e){
            e.printStackTrace();
        }


       // type_code_idt

    }

    /**
     * 获取
     */
    private int getRechargeExpTaskCode(Integer rechargeType){
        if(rechargeType == null){
            return -1;
        }
        switch (rechargeType) {
            case 1:
              return FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_MONTH_EXP.code();
            case 2:
                return  FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_QUARTER_EXP.code();
            case 3:
                return  FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_YEAR_EXP.code();
            default:
                return -1;
        }
    }


}
