package com.game.common.enums;

/**
 * <p>
 * 业务功能模块编码定义
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
public enum FunctionalModuleEnum {


    // 首页


    // 发布


    // 社区
    COMM_SEND_DISCUSS_MOOD_REPLY(70, "发布帖子评论/心情评论"),
    COMM_SEND_POST_MOOD_VIDEO_DISABLE(71, "发布帖子/心情（不可选视频）"),
    COMM_SEND_POSTS_GAME(72, "发布游戏测评）"),
    COMM_SEND_POST_MOOD_VIDEO_ABLE(73, "发布帖子/心情（不可选视频）"),
    COMM_COMM_QA(74, "社区问答"),
    COMM_SEND_GAME_LABEL(75, "社区问答")
    //发现


    //我的
    
   


    ;
    private int code;
    private String message;

    private FunctionalModuleEnum(int code, String message) {
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
