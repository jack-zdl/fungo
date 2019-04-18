package com.fungo.system.service;


import com.fungo.framework.exceptions.BusinessException;
import com.fungo.system.entity.IncentTaskedOut;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * <p>
 *      用户成长体系之
 *                      获取用户已任务完成情况业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberIncentTaskedService {


    /**
     * 获取分值类任务规则及其对应的FunGo虚拟币规则
     * @param task_type 任务类型
     *                                  1 分值
     *                                      11 任务 获取经验值
     *                                  2 虚拟币
     *                                      21 营销活动  获取fungo币
     *                                      22 签到获取fungo币
     *                                      23 任务 获取fungo币
     *                                  3 分值和虚拟币共有
     *                                  4 系统模块
     * @throws BusinessException
     */
    public List<Map<String, Object>> getTaskRule(int task_type) throws BusinessException;



    /**
     * 获取用户任务完成进度数据
     * @param memberId 会员ID
     * @param task_type
     * @return
     * @throws BusinessException
     */
    public List<Map<String, Object>> getMemberTaskProgress(String memberId, int task_type) throws BusinessException;


    /**
     * 获取用户已经完成的任务数据
     * @param memberId 用户ID
     * @param task_type 任务类型
     * @param task_id 任务ID
     * @return 返回  {1:taskid,2:taskname,3:score,4:type,5:count,6:date,7:incomie_freg_type} (任务id,任务名称，获取的分值|虚拟币数，任务类型，完成数量,任务完成时间,任务频率 )
     */
    public IncentTaskedOut getTasked(String memberId, int task_type, String task_id);

}
