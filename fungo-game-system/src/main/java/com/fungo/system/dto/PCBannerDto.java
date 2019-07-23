package com.fungo.system.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/23
 */
@Setter
@Getter
@ToString
public class PCBannerDto {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 跳转类型，1：App内部跳转，2:跳转url
     */
    private Integer actionType;

    /**
     * 位置_id
     */
    private String positionId;

    /**
     * 位置代码
     */
    private String positionCode;

    /**
     * 发布日期
     鸡汤文所属日期
     */
    private Date releaseTime;

    /**
     * 业务类型，3：游戏，1：帖子
     */
    private int targetType;

    /**
     * 业务id，（游戏id或帖子id）
     */
    private String targetId;

    /**
     * 标题
     */
    private String title;

    /**
     * 简介
     鸡汤文内容
     */
    private String intro;

    /**
     * 标签（推荐理由）
     */
    private String tag;

    /**
     * 图片
     */
    private String coverImage;

    /**
     * 功能描述: 2.5使用的活动图片
     * @auther: dl.zhang
     * @date: 2019/7/16 14:45
     */
    private String coverImgNew;

    /**
     * 外链地址
     鸡汤文图片链接
     */
    private String href;

    /**
     * 状态 -1:删除,  0：上线，1：草稿，2：下线
     */
    private Integer state;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建人名称
     */
    private String createdName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 业务类型:
     1  广告
     2  签到文案
     */
    private Integer advType;

    /**
     * 活动推广标题
     */
    private String  generalizeTitle;

    /**
     * 活动开始时间
     */
    private Date beginDate;

    /**
     *  活动结束时间
     */
    private Date endDate;

    /**
     * 游戏id
     */
    private String gameId;

    /**
     * 游戏名称
     */
    private String gameName;

    /**
     * 功能描述: PC2.0使用的图片
     * @auther: dl.zhang
     * @date: 2019/7/23 11:25
     */
    private String bannerImage;
}
