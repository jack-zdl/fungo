package com.fungo.system.service;

import com.fungo.framework.exceptions.BusinessException;

import java.util.Map;

/**
 * <p>
 * <p>
 *     用户成长体系之用户执行任务业务层:
 *                      V2.4.6版本任务
 *                  任务种类:
 *                          新手任务，起始编号 1701
 *                          每日任务，起始编号 8485
 *                          每周任务，起始编号 8706
 *                          精品任务，起始编号 9728
 * </p>
 *
 * @author mxf
 * @since 2019-03-26
 */
public interface IMemberIncentDoTaskFacadeService {


    /**
     * 执行任务
     * @param mb_id 登录用户id
     * @param task_type 任务组标记：
     *                                新手任务，起始编号 1701
     *                                每日任务，起始编号 8485
     *                                每周任务，起始编号 8706
     *                                精品任务，起始编号 9728
     * @param task_type 任务类型：
     *                              任务类型
     *                                      1 分值
     *                                         11 任务 获取经验值
     *                                      2 虚拟币
     *                                          21 营销活动  获取fungo币
     *                                          22  签到获取fungo币
     *                                                220 V2.4.6签到版本签到获取fungo币
     *                                          23 任务 获取fungo币
     *                                      3 分值和虚拟币共有
     *                                      4 系统模块
     * @param type_code_idt     任务编码
     * @return {success:任务执行结果 false: 失败 true: 成功  ,  msg:任务执行描述}
     * @throws BusinessException
     */
    public Map<String, Object> exTask(String mb_id, int task_group_flag, int task_type, int type_code_idt) throws Exception;


    /**
     * 执行任务
     * @param mb_id 登录用户id
     * @param task_type 任务组标记：
     *                                新手任务，起始编号 1701
     *                                每日任务，起始编号 8485
     *                                每周任务，起始编号 8706
     *                                精品任务，起始编号 9728
     * @param task_type 任务类型：
     *                              任务类型
     *                                      1 分值
     *                                         11 任务 获取经验值
     *                                      2 虚拟币
     *                                          21 营销活动  获取fungo币
     *                                          22  签到获取fungo币
     *                                                220 V2.4.6签到版本签到获取fungo币
     *                                          23 任务 获取fungo币
     *                                      3 分值和虚拟币共有
     *                                      4 系统模块
     * @param type_code_idt     任务编码
     * @param target_id     任务执行对象的id(如：发布了文章，即文章的id)
     * @return {success:任务执行结果 false: 失败 true: 成功  ,  msg:任务执行描述}
     * @throws BusinessException
     */
    public Map<String, Object> exTask(String mb_id, int task_group_flag, int task_type, int type_code_idt, String target_id) throws Exception;

}
