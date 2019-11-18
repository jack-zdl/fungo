package com.game.common.dto.game;

import lombok.Data;



@Data
public class GameKuDto  {

    private String gameId;
    /**
     * 标签  形如 文字,推理,解谜,悬疑  标签以逗号隔开
     */
    private String tags;

    /**
     * 包大小  单位 B
     */
    private Long gameSize;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 国家  非 中国 汉字 一律为海外
     */
    private String origin;

    /**
     * IOS状态 0:待开启，1预约。2.测试，3已上线
     */
    private Integer iosState;

    /**
     * 安卓状态 0:待开启，1预约。2.测试，3已上线
     */
    private Integer androidState;

    /**
     * 安卓 包名称
     */
    private String androidPackageName;

    // v2.6新增
    /**
     *  安卓状态描述
     */
    private String androidStatusDesc;
    /**
     * ios 状态描述
     */
    private String iosStatusDesc;

    /**
     *  国内游戏不做判断 海外游戏 - 为null 默认true
     */
    private Boolean canFast;

    /**
     *  评分
     */
    private Double score;

    /**
     *  为 null 说明没有圈子
     */
    private String circleId;

    private  String apk;

    private String version;

    private Boolean make;


}
