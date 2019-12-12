package com.game.common.bean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class CollectionItemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	/**
	 * 合集项ID
	 */
	private String formId;

	/**
	 * 游戏id
	 */
	private String gameId;
	/**
	 * 游戏名称
	 */
	private String name;
	/**
	 * 评分
	 */
	private Double score;
	/**
	 * 游戏介绍(游戏表android_status_desc字段)
	 */
	private String androidStatusDesc;
	/**
	 * 游戏介绍(合集项detail字段)
	 */
	private String detail;
	/**
	 * 排序号
	 */
	private Integer sort;
	/**
	 * 'IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载'
	 */
	private Integer iosState;
	/**
	 * '安卓状态 0:待开启，1预约。2.测试，3已上线'
	 */
	private Integer androidState;
	/**
	 * 游戏表标签
	 */
	private String tags;
	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * 游戏图标
	 */
	private String icon;
	/**
	 * 预约标识
	 */
	private Boolean make;
	/**
	 * 'isClause标识'
	 */
	private boolean isClause;
	/**
	 * 圈子id
	 */
	private String circleId;
	/**
	 * 是否支持加速
	 */
	private boolean canFast;
	/**
	 *  安卓下载地址
	 * @return
	 */
	private String apk;
	/**
	 *  游戏大小
	 * @return
	 */
	private String gameSize;
	/**
	 *  安卓包名
	 * @return
	 */
	private String androidPackageName;
	/**
	 *  版本号
	 * @return
	 */
	private String version;
	/**
	 *  主版本号
	 * @return
	 */
	private String versionMain;
	/**
	 *  子版本号
	 * @return
	 */
	private String versionChild;
	/**
	 * 是否支持加速
	 */
	private String origin;
	/**
	 *  图片s
	 * @return
	 */
	private String images;
	/**
	 *  游戏图标
	 * @return
	 */
	private String coverImage;


}
