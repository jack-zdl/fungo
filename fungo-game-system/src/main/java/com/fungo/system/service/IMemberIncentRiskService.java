package com.fungo.system.service;

import com.game.common.dto.ResultDto;
import com.game.common.util.exception.BusinessException;

import java.util.Map;

/**
 * <p>
 * <p>
 * 用户领域-权益风控业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberIncentRiskService {


    /**
     * 用户等级 风控
     * @param rank_id 级别、身份、荣誉规则ID
     * @param task_id 任务规则、业务功能ID
     * @return
     */
    public boolean isMatchLevel(String rank_id, String task_id) throws BusinessException;


    /**
     * 用户执行任务和等级权限
     * @param rank_id
     * @param fun_idt
     * @return
     * @throws BusinessException
     */
    boolean isMatchLevel(String rank_id, int fun_idt) throws BusinessException;

    /**
     * 检查用户是否有未完成新手任务
     * @param userId
     * @param os
     * @return
     */
    public ResultDto<Map<String, Object>> checkeUnfinshedNoviceTask(String userId, String os);


}
