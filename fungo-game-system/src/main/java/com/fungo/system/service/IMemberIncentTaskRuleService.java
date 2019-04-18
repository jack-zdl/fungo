package com.fungo.system.service;

import com.fungo.system.entity.ScoreGroup;
import com.fungo.system.entity.ScoreRule;

import java.util.List;

/**
 * <p>
 * <p>
 *      用户成长体系之
 *                      任务规则业务层
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public interface IMemberIncentTaskRuleService {


    /**
     * 获取当前有效的任务组
     * @return
     */
    public List<ScoreGroup> getScoreGroups();


    /**
     * 获取当前有效的任务组下的所有任务规则数据
     * @param taskGroupIds 任务组id数组
     * @return
     */
    public List<ScoreRule> getScoreRules(String[] taskGroupIds);


    /**
     * 根据任务组的标识查询任务组数据
     * @param task_flag
     * @return
     */
    public ScoreGroup getScoreGroups(int task_flag);


    /**
     * 获取当前有效的任务组下的所有任务规则数据
     * @param code_idt 任务规则编码
     * @return
     */
    public ScoreRule getScoreRule(int code_idt);


}
