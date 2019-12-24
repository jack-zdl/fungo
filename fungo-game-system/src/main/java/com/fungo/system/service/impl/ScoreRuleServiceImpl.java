package com.fungo.system.service.impl;

import com.fungo.system.service.IMemberIncentDoTaskFacadeService;
import com.fungo.system.service.IScoreRuleService;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.enums.FunGoIncentTaskV246Enum;

import javax.annotation.Resource;

/**
 * <p></p>
 * @Author: dl.zhang
 * @Date: 2019/12/23
 */
public class ScoreRuleServiceImpl implements IScoreRuleService {

    //用户成长业务
    @Resource(name = "memberIncentDoTaskFacadeServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;

    /**
     * 功能描述: todo
     * @date: 2019/12/23 14:00
     */
    @Override
    public void achieveScoreRule(String memberId,int type) {
        try {
            //V2.4.6版本任务
            //1 经验值
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_NICKNAME_EXP.code());

            //2 fungo币
            iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, FunGoIncentTaskV246Enum.TASK_GROUP_NEWBIE_UPATE_NICKNAME_COIN.code());

        }catch (Exception e){

        }
    }
}
