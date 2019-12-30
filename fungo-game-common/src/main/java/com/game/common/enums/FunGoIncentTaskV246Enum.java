package com.game.common.enums;

/**
 * <p>
 *    V2.4.6用户成长体系之任务业务
 *                      任务编码定义，任务分为：
 *                          1. 获取经验值任务
 *                          2. 获取FunGo虚拟币任务
 *                          3. 签到获取FunGo币任务
 * </p>
 *    任务有效期:
 *             V2.4.6版本起
 *             由于每个版本任务的规则不同，所以暂时按任务实现业务层
 *
 *
 * @author mxf
 * @since 2019-03-25
 */
public enum FunGoIncentTaskV246Enum {


    // ---------------------------------------任务组分类----------------------------------------
    // ==========  新手任务，起始编号 1701
    TASK_GROUP_NEWBIE(1701, "新手任务"),

    // ==========  每日任务，起始编号 8485
    TASK_GROUP_EVERYDAY(8485, "每日任务"),

    //==========  每周任务，起始编号 8706
    TASK_GROUP_WEEKLY(8706, "每周任务"),

    //==========  精品任务，起始编号 9728
    TASK_GROUP_EXCELLENT(9728, "精品任务"),

    //---------------------------------------任务组下的具体任务规则----------------------------------------
    //==========  新手任务，起始编号 1701
    //fungo币任务 | 经验值任务
    TASK_GROUP_NEWBIE_UPATE_NICKNAME_COIN(1701, "修改昵称"), TASK_GROUP_NEWBIE_UPATE_NICKNAME_EXP(17010, "修改昵称"),
    TASK_GROUP_NEWBIE_UPATE_AVATAR_COIN(1702, "修改头像"), TASK_GROUP_NEWBIE_UPATE_AVATAR_EXP(17020, "修改头像"),
    TASK_GROUP_NEWBIE_UPATE_RESUME_COIN(1703, "修改个人简介"), TASK_GROUP_NEWBIE_UPATE_RESUME_EXP(17030, "修改个人简介"),
    TASK_GROUP_NEWBIE_SNS_QQ_COIN(1704, "绑定QQ"), TASK_GROUP_NEWBIE_NS_QQ_EXP(17040, "绑定QQ"),
    TASK_GROUP_NEWBIE_SNS_WX_COIN(1705, "绑定微信"), TASK_GROUP_NEWBIE_NS_WX_EXP(17050, "绑定微信"),
    TASK_GROUP_NEWBIE_SNS_WB_COIN(1706, "绑定微博"), TASK_GROUP_NEWBIE_NS_WB_EXP(17060, "绑定微博"),
    TASK_GROUP_NEWBIE_FIRST_DOWN_COIN(1707, "首次下载游戏"), TASK_GROUP_NEWBIE_FIRST_DOWN_EXP(17070, "首次下载游戏"),
    TASK_GROUP_NEWBIE_WATCH_3_USER_COIN(1708, "关注3位Fun友"), TASK_GROUP_NEWBIE_WATCH_3_USER_EXP(17080, "关注3位Fun友"),


    //==========  每日任务，起始编号 8485
    //fungo币任务 | 经验值任务
    TASK_GROUP_EVERYDAY_FIRST_SCORE_GAME_COIN(8491, "为游戏打分", "发布成功", "Fun币+A！"), TASK_GROUP_EVERYDAY_FIRST_SCORE_GAME_EXP(84910, "为游戏打分", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_COIN(8485, "分享一篇文章", "分享成功", "Fun币+A！"), TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_EXP(84850, "分享一篇文章", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_SHARE_1_GAME_COIN(8486, "分享一款游戏", "分享成功", "Fun币+A！"), TASK_GROUP_EVERYDAY_SHARE_1_GAME_EXP(84860, "分享一款游戏", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_COIN(8487, "发布文章", "发布成功", "Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP(84870, "发布文章", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN(8488, "发布心情", "发布成功", "Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP(84880, "发布心情", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN(8489, "第一次回复","发布成功","Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP(84890, "第一次回复", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_LIKE_COIN(8490, "点赞5次", "发布成功", "Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_LIKE_EXP(84900, "点赞5次"),
    TASK_GROUP_EVERYDAY_SHARE_1_FAST_COIN(8492, "成功分享一次加速器邀请海报", "分享成功", "Fun币+A！"), TASK_GROUP_EVERYDAY_SHARE_1_FAST_EXP(84920, "成功分享一次加速器邀请海报", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_USE_FAST_COIN(8493, "使用10min加速器", "分享成功", "Fun币+A！"), TASK_GROUP_EVERYDAY_USE_FAST_EXP(84930, "使用10min加速器", "", "经验+B！"),

    //==========  每周任务，起始编号 8706
    TASK_GROUP_WEEKLY_FIRST_DOWN_GAME_COIN(8706, "第1次下载游戏", "下载成功", "Fun币+A"), TASK_GROUP_WEEKLY_FIRST_DOWN_GAME_EXP(87060, "第1次下载游戏", "", "经验+B！"),
    TASK_GROUP_WEEKLY_FIRST_SUBSCRIBE_GAME_COIN(8707, "第1次预约游戏", "预约成功", "Fun币+A！"), TASK_GROUP_WEEKLY_FIRST_SUBSCRIBEGAME_EXP(87070, "第1次预约游戏", "", "经验+B！"),
    TASK_GROUP_WEEKLY_2_ANLI_HOT_COIN(8708, "有2条游戏评论上热门/安利墙"), TASK_GROUP_WEEKLY_2_ANLI_HOT_EXP(87080, "有2条游戏评论上热门/安利墙"),
    TASK_GROUP_WEEKLY_2_RECOM_TOP_COIN(8709, "有2篇文章上推荐/置顶"), TASK_GROUP_WEEKLY_2_RECOM_TOP_EXP(87090, "有2篇文章上推荐/置顶"),


    //==========  精品任务，起始编号 9728
    TASK_GROUP_EXCELLENT_GAME_COMM_HOT_COIN(9728, "游戏评论上热门"), TASK_GROUP_EXCELLENT_GAME_COMM_HOT_EXP(97280, "游戏评论上热门"),
    TASK_GROUP_EXCELLENT_GAME_COMM_ANLI_COIN(9729, "游戏评论上安利墙"), TASK_GROUP_EXCELLENT_GAME_COMM_ANLI_EXP(97290, "游戏评论上安利墙"),
    TASK_GROUP_EXCELLENT_ARTICLE_TOP_COIN(9730, "文章被置顶"), TASK_GROUP_EXCELLENT_ARTICLE_TOP_EXP(97300, "文章被置顶"),
    TASK_GROUP_EXCELLENT_ARTICLE_RECOM_COIN(9731, "文章被加精"), TASK_GROUP_EXCELLENT_ARTICLE_RECOM_EXP(97310, "文章被加精"),
    TASK_GROUP_EXCELLENT_RECHARGE_MONTH_COIN(9732, "成功充值一次加速器月卡"), TASK_GROUP_EXCELLENT_RECHARGE_MONTH_EXP(97320, "成功充值一次加速器月卡"),
    TASK_GROUP_EXCELLENT_RECHARGE_QUARTER_COIN(9733, "成功充值一次加速器季卡"), TASK_GROUP_EXCELLENT_RECHARGE_QUARTER_EXP(97330, "成功充值一次加速器季卡"),
    TASK_GROUP_EXCELLENT_RECHARGE_YEAR_COIN(9734, "成功充值一次加速器年卡"), TASK_GROUP_EXCELLENT_RECHARGE_YEAR_EXP(97340, "成功充值一次加速器年卡");



    //任务编码
    private int code;
    //任务编码说明
    private String codeDesc;

    //任务执行结果状态描述
    private String statusDesc;
    //完成任务的提示：
    private String message;


    private FunGoIncentTaskV246Enum(int code, String codeDesc) {
        this.code = code;
        this.codeDesc = codeDesc;
    }

    private FunGoIncentTaskV246Enum(int code, String codeDesc, String message) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.message = message;
    }

    private FunGoIncentTaskV246Enum(int code, String codeDesc, String statusDesc, String message) {
        this.code = code;
        this.codeDesc = codeDesc;
        this.statusDesc = statusDesc;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String codeDesc() {
        return this.codeDesc;
    }


    public String statusDesc() {
        return this.statusDesc;
    }

    public String message() {
        return this.message;
    }


}
