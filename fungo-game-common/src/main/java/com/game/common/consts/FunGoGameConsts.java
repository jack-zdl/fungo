package com.game.common.consts;


/**
 * <p>
 *   系统常量
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class FunGoGameConsts {


    //----------------------------------公共常量------------------------------------------------
    /**
     * is_active
     *  是否激活
     *          是否启用
     *          0-未启动
     *          1-启用
     */
    /**
     * 0-未启动
     */
    public static final  int FUNGO_PUBLIC_IS_ACTIVE_DISABLE = 0;

    /**
     * 1-启用
     */
    public static final  int FUNGO_PUBLIC_IS_ACTIVE_ABLE = 1;


    /**
     *  无父节点
     *  -1
     */
    public static final  int FUNGO_PUBLIC_PARENT_ID_IS_NULL = -1;

    //---------------------------------公共常量-end----------------------------------------------



 //----------------------------scoreRule--任务规则类常量-----------------------------------
    /**
     * task_type
     * 任务类型
     *          1 分值
     *             11 任务 获取经验值
     *          2 虚拟币
     *              21 营销活动  获取fungo币
     *              22  签到获取fungo币
     *              23 任务 获取fungo币
     *          3 分值和虚拟币共有
     *          4 系统模块
     */


    /**
     * 3 分值和虚拟币共有
     */
    public static final  int  TASK_RULE_TASK_TYPE_SCOREANDCOIN = 3;


    /**
     * 4 系统模块
     */
    public static final  int  Task_rule_task_type_sys_fun = 4;


    /**
     * 11 任务 获取经验值
     */
    public static final  int  TASK_RULE_TASK_TYPE_SCORE = 11;



    /**
     *  21 营销活动  获取fungo币
     */
    public static final  int  TASK_RULE_TASK_TYPE_COIN_MARKET = 21;



    /**
     *   22  签到获取fungo币
     */
    public static final  int  TASK_RULE_TASK_TYPE_COIN_SIGN_IN = 22;


    /**
     *    23 任务 获取fungo币
     */
    public static final  int  TASK_RULE_TASK_TYPE_COIN_TASK = 23;





    //----------------------------scoreRule--任务规则类常量---end--------------------------------




    //-------------------------用户级别、身份、荣誉规则--常量-----------------------------------
    /**
     * t_incent_rule_rank
     * rank_type
     * 权益类型
     *          1 级别
     *          2 身份
     *          3 荣誉
     *          4 特权服务
     */

    /**
     * 1 级别
     */
    public static final int INCENT_RULE_RANK_TYPE_LEVEL = 1;


    /**
     * 2 身份
     */
    public static final int INCENT_RULE_RANK_TYPE_DIGNITY = 2;

    /**
     * 3 荣誉
     */
    public static final int INCENT_RULE_RANK_TYPE_HONORS = 3;


    /**
     * rank_flag
     * 身份标识
     *          1 官方类型
     *          2 评测师类型
     *          3 普通类型
     *          0 无
     */

    /**
     *  0 无
     */
    public static final int INCENT_RULE_RANK_FLAG_HONORS_NULL = 0;

    /**
     * 1 官方类型誉
     */
    public static final int INCENT_RULE_RANK_FLAG_HONORS_OFFICIAL = 1;


    /**
     * 2 评测师类型
     */
    public static final int INCENT_RULE_RANK_FLAG_HONORS_EVALUATIONER = 2;


    /**
     * 3 普通类型
     */
    public static final int INCENT_RULE_RANK_FLAG_HONORS_GENERAL = 3;

    //-------------------------用户级别、身份、荣誉规则--常量--end---------------------------------

    
    
   
    //----------------------------用户账户类--常量-----------------------------------
    
    /**
     * account_type
     *            账户类型
     *              1 分值类
     *                 11 经验值
     *                 12 积分
     *              2 虚拟币类
     */


    /**
     *  1 分值类
     */
    public static final int INCENT_ACCOUNT_TYPE_SCORE = 1;
    
    /**
     *  11 经验值类型
     */
    public static final int INCENT_ACCOUNT_TYPE_EXP = 11;
    
    /**
     * 12 积分类型
     */
    public static final int INCENT_ACCOUNT_TYPE_POINT = 12;
    
    /**
     * 2 虚拟币类
     */
    public static final int INCENT_ACCOUNT_TYPE_FUNCOIN = 2;



    /**
     *  1 分值类  账户类型数据ID
     *  即   t_incent_account_group表中的 id值
     */
    public static final int INCENT_ACCOUNT_TYPE_SCORE_ID = 1;


    /**
     *  2 虚拟币类  账户类型数据ID
     *  即   t_incent_account_group表中的 id值
     */
    public static final int INCENT_ACCOUNT_TYPE_COIN_ID = 3;
  //----------------------------用户账户类--常量--end---------------------------------



    //----------------------------游戏工具--常量--start---------------------------------

    /**
     * tools_platform
     * 工具平台
     *      1 android
     *      2 ios
     */

    /**
     *  tools_platform
     *               1 android
     */
    public static final  int GAME_TOOL_TOOLS_PLATFORM_ANDROID = 1 ;


    /**
     *  tools_platform
     *               2 ios
     */
    public static final  int GAME_TOOL_TOOLS_PLATFORM_IOS = 2 ;

    //----------------------------游戏工具--常量--end---------------------------------



    //----------------------------ehcache--常量--start---------------------------------
    /**
     * 缓存名称
     */
    public static  final String CACHE_EH_NAME = "fungoEhcache";

    /**
     * 缓存key 前缀
     *              按业务功能
     */

    /**
     * 发布
     *
     */
    public static  final String CACHE_EH_KEY_PRE_POST = "FG_eh_publish";



    /**
     * 社区
     *
     */
    public static  final String CACHE_EH_KEY_PRE_COMMUNITY = "FG_eh_community";


    /**
     * 发现
     *
     */
    public static  final String CACHE_EH_KEY_PRE_DISCOVER = "FG_eh_discover";


    /**
     * 会员
     *
     */
    public static  final String CACHE_EH_KEY_PRE_MEMBER = "FG_eh_member";

    /**
     * 系统级数据
     *
     */
    public static  final String CACHE_EH_KEY_PRE_SYSTEM = "FG_eh_Sytem";
    //----------------------------ehcache--常量--end---------------------------------

    public static final String CACHE_MALL_KEY = "mall";

//-----------
}
