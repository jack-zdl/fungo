package com.fungo.system.service.impl;

import com.fungo.system.service.IMemberIncentDoTaskFacadeService;
import com.fungo.system.service.TaskRechargeService;
import com.game.common.consts.MemberIncentTaskConsts;
import com.game.common.dto.ResultDto;
import com.game.common.enums.FunGoIncentTaskV246Enum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.Map;


@Service
public class TaskRechargeServiceImpl implements TaskRechargeService {

    @Resource(name = "memberIncentExcellentTaskServiceImpl")
    private IMemberIncentDoTaskFacadeService iMemberIncentDoTaskFacadeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskRechargeServiceImpl.class);

    /**
     *  充值类挑战任务
     * @param memberId 充值用户
     * @param rechargeType 充值类型 1-月卡 2-季卡 3-年卡
     * @param orderId 充值对应的订单id
     */
    @Override
    @Transactional
    public ResultDto<String> completeRechargeTask(String memberId, Integer rechargeType, String orderId) {
        int rechargeExpTaskCode = getRechargeExpTaskCode(rechargeType);
        int rechargeCoinTaskCode = getRechargeCoinTaskCode(rechargeType);
        if(rechargeCoinTaskCode == -1 || rechargeExpTaskCode==-1){
            LOGGER.error("客户端-充值任务 rechargeType 错误");
            return ResultDto.error("-1","客户端-充值任务 充值类型 错误");
        }
        LOGGER.info("开始执行充值挑战任务,用户 {} 通过订单 {} 完成 {} 类型充值任务",memberId,orderId,rechargeType);
        Map<String, Object> expResult = null;
        Map<String, Object> coinResult = null;
        try{
            //1 经验值
            expResult = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT.code(),
                    MemberIncentTaskConsts.INECT_TASK_SCORE_EXP_CODE_IDT, rechargeExpTaskCode,orderId);
            //2 fungo币
            coinResult = iMemberIncentDoTaskFacadeService.exTask(memberId, FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT.code(),
                    MemberIncentTaskConsts.INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT, rechargeCoinTaskCode,orderId);
        }catch (Exception  e){
            LOGGER.error("充值任务异常",e);
        }
        if(expResult!=null&&coinResult!=null){
            Object expSuccess = expResult.get("success");
            Object coinSuccess = coinResult.get("success");
            if(expSuccess!=null&&coinSuccess!=null&&(Boolean)expSuccess&&(Boolean)coinSuccess){
                LOGGER.info("执行成功充值挑战任务,用户 {} 通过订单 {} 完成 {} 类型充值任务",memberId,orderId,rechargeType);
                return ResultDto.success();
            }
        }
        LOGGER.info("执行失败充值挑战任务,用户 {} 通过订单 {} 完成 {} 类型充值任务",memberId,orderId,rechargeType);
        // 整个事务都需要回滚
        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return ResultDto.error("-1","任务失败");
    }

    /**
     * 获取 任务编码
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

    /**
     * 获取
     */
    private int getRechargeCoinTaskCode(Integer rechargeType){
        if(rechargeType == null){
            return -1;
        }
        switch (rechargeType) {
            case 1:
                return FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_MONTH_COIN.code();
            case 2:
                return  FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_QUARTER_COIN.code();
            case 3:
                return  FunGoIncentTaskV246Enum.TASK_GROUP_EXCELLENT_RECHARGE_YEAR_COIN.code();
            default:
                return -1;
        }
    }


}
