package com.game.common.enums;


/**
 * <p>
 *    任务编码定义，任务分为：
 *                          1. 获取经验值任务
 *                          2. 获取FunGo虚拟币任务
 *                          3. 签到获取FunGo币任务
 * </p>
 *    任务有效期：
 *             V 2.4.3 版本起至V2.4.6版本前
 *             V2.4.6及其以后废弃
 *
 * @author mxf
 * @since 2018-12-04
 */
public enum FunGoTaskV243Enum {

    //===================================================1. 获取经验值任务===============================================================
    //==========1 新手任务
    N_UPDATE_NIKE_ICON_INTRO_EXP(10,"修改修改昵称、头像、个人简介"),
    N_BIND_APPID_EXP(11,"绑定APPID"),
    N_BIND_THIRD_EXP(12,"绑定第三方账户"),
    N_EYE_USER_EXP(13,"关注用户"),

    //==========2 社区任务
    CM_SEND_ARTICLE_EXP(20,"发布文章"),
    CM_SEND_MOOD_EXP(21,"发布心情"),
    CM_CLICK_LIKE_EXP(22,"点赞他人内容"),
    CM_VIEWS_OTHER_EXP(23,"评论/回复他人内容"),
    CM_SHARED_ARTICLE_EXP(24,"成功分享文章"),
    CM_REPORT_ILLEGAL_EXP(25,"成功举报违规内容"),
    CM_ADVISE_SYS_EXP(26,"成功反馈意见/建议"),

    //==========3 游戏任务
    GM_DOWNLOAD_EXP(30,"下载游戏"),
    GM_SHARED_EXP(31,"成功分享游戏"),
    GM_SUB_EXP(32,"预约游戏"),
    GM_SEND_SAY_EXP(33,"发布游戏评论"),
    GM_REPLY_EXP(34,"回复游戏评论"),
    GM_CLICK_LIKE_EXP(35,"点赞他人游戏评价"),



    //==========4 精品任务
    BTQ_SAY_COLLECT_SHARD_EXP(40,"文章被评论/收藏/分享"),
    BTQ_CLICK_LIKE_EXP(41,"被点赞"),
    BTQ_BY_SAY_EXP(42,"游戏评价被评论"),
    BTQ_SAY_UP_ANLI_EXP(43,"评论上安利墙"),
    BTQ_ACTICLE_CHOI_EXP(44,"文章上精选"),


    //====================================================1. 获取经验值任务 end ==============================================================



    //===================================================2. 获取FunGo虚拟币任务===============================================================

    //==========1 新手任务
    N_UPDATE_NIKE_ICON_INTRO_COIN(100 , "修改修改昵称、头像、个人简介"),
    N_BIND_APPID_COIN(110 , "绑定APPID"),
    N_BIND_THIRD_COIN(120 , "绑定第三方账户"),


    //==========2 社区任务
    CM_SHARED_ARTICLE_COIN(240 , "修改昵称、头像、个人简介"),
    CM_ADVISE_SYS_COIN(260 , "成功反馈意见/建议"),



    //==========3 游戏任务
    GM_DOWNLOAD_COIN(300 , "下载游戏"),
    GM_SHARED_COIN(310 , "成功分享游戏"),
    GM_SUB_COIN(320 , "预约游戏"),


    //==========4 精品任务
    BTQ_SAY_UP_ANLI_COIN(430 , "评论上安利墙"),
    BTQ_ACTICLE_CHOI_COIN(440 , "文章上精选"),


    //====================================================2. 获取FunGo虚拟币任务 end ==============================================================



    //===================================================3. 签到获取FunGo币任务===============================================================

    CHECK_IN_ONE_TO_SEX_COIN(60,"成功签到1-6天"),
    CHECK_IN_SEVEN_COIN(61,"成功签到7天"),
    CHECK_IN_EIGHT_TO_THIRTEEN_COIN(62,"成功签到8-13天"),
    CHECK_IN_FOURTEEN_COIN(63,"成功签到14天"),
    CHECK_IN_FIFTEEN_TO_TWENTYNINE_COIN(64,"成功签到15-29天"),
    CHECK_IN_THIRTY_COIN(65,"成功签到30天"),


    //===================================================3. 签到获取FunGo币任务 end===============================================================


    ;
    private int code;
    private String message;

    private FunGoTaskV243Enum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return this.code;
    }

    public String message() {
        return this.message;
    }

}
