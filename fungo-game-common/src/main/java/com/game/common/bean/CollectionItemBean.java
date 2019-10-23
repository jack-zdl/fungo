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
	 * 标签
	 */
	private List<String> labels;
	/**
	 * 评分
	 */
	private String score;
	/**
	 * 游戏介绍(合集项表)
	 */
	private String detail;
	/**
	 * 排序号
	 */
	private Integer sort;
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
	 * 修改人
	 */
	private String updatedBy;
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

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}
}
