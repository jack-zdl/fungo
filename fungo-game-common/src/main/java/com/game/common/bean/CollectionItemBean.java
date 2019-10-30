package com.game.common.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CollectionItemBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	/**
	 * 合集项ID
	 */
	private String groupId;

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
	private String score;
	/**
	 * 游戏介绍(游戏表android_status_desc字段)
	 */
	private String androidStatusDesc;
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
	private Boolean mark;
	/**
	 * 圈子id
	 */
	private String circleId;

	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}

	public Boolean getMark() {
		return mark;
	}

	public void setMark(Boolean mark) {
		this.mark = mark;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public Integer getIosState() {
		return iosState;
	}

	public void setIosState(Integer iosState) {
		this.iosState = iosState;
	}

	public Integer getAndroidState() {
		return androidState;
	}

	public void setAndroidState(Integer androidState) {
		this.androidState = androidState;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getAndroidStatusDesc() {
		return androidStatusDesc;
	}

	public void setAndroidStatusDesc(String androidStatusDesc) {
		this.androidStatusDesc = androidStatusDesc;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}
