package com.game.common.consts;


/**
 * <p>
 *   用户成长体系之任务类型code常量
 *                                 任务类型:
 *                                           1 分值
 *                                              11 任务 获取经验值
 *                                           2 虚拟币
 *                                               21 营销活动  获取fungo币
 *                                               22 签到获取fungo币
 *                                               23 任务 获取fungo币
 *                                           3 分值和虚拟币共有
 *                                           4 系统模块 (暂时不用)
 *
 * </p>
 *
 * @author mxf
 * @since 2019-03-22
 */
public class MemberIncentTaskConsts {


    /**
     *   第1类任务 分值类
     *    11 获取经验值类任务
     */
    public static final int INECT_TASK_SCORE_EXP_CODE_IDT = 11;


    /**
     *   第2类任务 虚拟币类
     *     21 营销活动类获取fungo币
     */
    public static final int INECT_TASK_VIRTUAL_COIN_MARKET_CODE_IDT = 21;

    /**
     *   第2类任务 虚拟币类
     *      22  签到获取fungo币
     */
    public static final int INECT_TASK_VIRTUAL_COIN_CHECKIN_CODE_IDT = 22;

    /**
     *   第2类任务 虚拟币类
     *      220  签到获取fungo币 (V2.4.6版本)
     */
    public static final int INECT_TASK_VIRTUAL_COIN_CHECKIN_CODE_IDT_V246 = 220;


    /**
     *   第2类任务 虚拟币类
     *       23 任务 获取fungo币
     */
    public static final int INECT_TASK_VIRTUAL_COIN_TASK_CODE_IDT = 23;


    /**
     *   第3类  任务分值和虚拟币共有
     *
     */
    public static final int INECT_TASK_VIRTUALCOIN_AND_SCORE_CODE_IDT = 3;


}
