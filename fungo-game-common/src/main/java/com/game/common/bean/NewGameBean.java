package com.game.common.bean;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class NewGameBean {

	private String id;

	private long time;//时间

	/**
	 * 游戏id
	 */
	private String gameId;
	/**
	 * 游戏集合名称
	 */
	private String name;
	/**
	 * 游戏说明(游戏表)
	 */
	private String androidStatusDesc;
	/**
	 * 是否是圈子
	 */
	private String circleId;
	/**
	 * 安卓状态  '安卓状态 0:待开启，1预约。2.测试，3已上线',
	 */
	private Integer androidState;
	/**
	 * 地区
	 */
	private String origin;
	/**
	 * 评分
	 */
	private Double score;
	/**
	 * 是否支持加速
	 */
	private boolean canFast;
	/**
	 * 排序号
	 */
	private Integer sort;
	/**
	 * 游戏有效时间开始
	 */
	private Date startTime;
	/**
	 * 游戏有效时间结束
	 */
	private Date endTime;
	/**
	 * 选择日期
	 */
	private Date chooseDate;

	/**
	 * IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载'
	 */
	private Integer iosState;
	/**
	 * 标签
	 */
	private String tags;

	/**
	 *
	 */
	private int startOffset;
	/**
	 *
	 */
	private int pageSize;

	/**
	 * 游戏图标
	 * @return
	 */
	private String icon;
	/**
	 *  预约标识
	 * @return
	 */
	private Boolean make;
	/**
	 * 'isClause标识'
	 */
	private boolean isClause;

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

}
