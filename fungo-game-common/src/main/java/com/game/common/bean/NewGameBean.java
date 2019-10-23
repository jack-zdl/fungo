package com.game.common.bean;


import java.util.Date;
import java.util.List;

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
	 * 状态
	 */
	private Integer state;
	/**
	 * 游戏说明
	 */
	private String detail;
	/**
	 * 是否是圈子
	 */
	private boolean circle;
	/**
	 * 游戏状态(安卓状态  [安卓状态 0:待开启，1预约。2.测试，3已上线'] [IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载'])
	 */
	private Integer status;
	/**
	 * 标签
	 */
	private List<String> labels;
	/**
	 * 游戏评分
	 */
	private String rating;
	/**
	 * 安卓状态  '安卓状态 0:待开启，1预约。2.测试，3已上线',
	 */
	private Integer androidState;
	/**
	 * 地区
	 */
	private String region;
	/**
	 * 是否支持加速
	 */
	private boolean vpn;
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
	 * IOS状态
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
	 *   游戏图标
	 * @return
	 */
	private String icon;

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public boolean isCircle() {
		return circle;
	}

	public void setCircle(boolean circle) {
		this.circle = circle;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public boolean isVpn() {
		return vpn;
	}

	public void setVpn(boolean vpn) {
		this.vpn = vpn;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
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

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getChooseDate() {
		return chooseDate;
	}

	public void setChooseDate(Date chooseDate) {
		this.chooseDate = chooseDate;
	}
}
