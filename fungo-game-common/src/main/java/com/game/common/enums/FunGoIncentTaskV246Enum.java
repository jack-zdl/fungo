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
    TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_COIN(8485, "分享1篇好文章", "分享成功", "Fun币+A！"), TASK_GROUP_EVERYDAY_SHARE_1_ARTICLE_EXP(84850, "分享1篇好文章", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_SHARE_1_GAME_COIN(8486, "分享1款好游戏", "分享成功", "Fun币+A！"), TASK_GROUP_EVERYDAY_SHARE_1_GAME_EXP(84860, "分享1款好游戏", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_COIN(8487, "第1次发文章", "发布成功", "Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_ARTICLE_EXP(84870, "第1次发文章", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_COIN(8488, "第1次发心情", "发布成功", "Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_MOOD_EXP(84880, "第1次发心情", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_COIN(8489, "第1次发评论","发布成功","Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_COMMENT_EXP(84890, "第1次发评论", "", "经验+B！"),
    TASK_GROUP_EVERYDAY_FISRT_LIKE_COIN(8490, "第1次点赞", "发布成功", "Fun币+A"), TASK_GROUP_EVERYDAY_FISRT_SEND_LIKE_EXP(84900, "第1次点赞"),


    //==========  每周任务，起始编号 8706
    TASK_GROUP_WEEKLY_FIRST_DOWN_GAME_COIN(8706, "第1次下载游戏", "下载成功", "Fun币+A"), TASK_GROUP_WEEKLY_FIRST_DOWN_GAME_EXP(87060, "第1次下载游戏", "", "经验+B！"),
    TASK_GROUP_WEEKLY_FIRST_SUBSCRIBE_GAME_COIN(8707, "第1次预约游戏", "预约成功", "Fun币+A！"), TASK_GROUP_WEEKLY_FIRST_SUBSCRIBEGAME_EXP(87070, "第1次预约游戏", "", "经验+B！"),
    TASK_GROUP_WEEKLY_2_ANLI_HOT_COIN(8708, "有2条游戏评论上热门/安利墙"), TASK_GROUP_WEEKLY_2_ANLI_HOT_EXP(87080, "有2条游戏评论上热门/安利墙"),
    TASK_GROUP_WEEKLY_2_RECOM_TOP_COIN(8709, "有2篇文章上推荐/置顶"), TASK_GROUP_WEEKLY_2_RECOM_TOP_EXP(87090, "有2篇文章上推荐/置顶"),


    //==========  精品任务，起始编号 9728
    TASK_GROUP_EXCELLENT_GAME_COMM_HOT_COIN(9728, "游戏评论上热门"), TASK_GROUP_EXCELLENT_GAME_COMM_HOT_EXP(97280, "游戏评论上热门"),
    TASK_GROUP_EXCELLENT_GAME_COMM_ANLI_COIN(9729, "游戏评论上安利墙"), TASK_GROUP_EXCELLENT_GAME_COMM_ANLI_EXP(97290, "游戏评论上安利墙"),
    TASK_GROUP_EXCELLENT_ARTICLE_TOP_COIN(9730, "文章被置顶"), TASK_GROUP_EXCELLENT_ARTICLE_TOP_EXP(97300, "文章被置顶"),
    TASK_GROUP_EXCELLENT_ARTICLE_RECOM_COIN(9731, "文章上推荐"), TASK_GROUP_EXCELLENT_ARTICLE_RECOM_EXP(97310, "文章上推荐");


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
