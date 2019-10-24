package com.game.common.bean;

public class HomePageBean {

	private String id;
	/**
	 * 游戏id
	 */
	private String gameId;
	/**
	 * 游戏集合名称
	 */
	private String name;
	/**
	 * 标签
	 */
	private String tags;
	/**
	 * 国家
	 */
	private String origin;
	/**
	 * 评分
	 */
	private Double score;
	/**
	 * 推荐语
	 */
	private String rmdLag;
	/**
	 * 推荐理由
	 */
	private String rmdReason;
	/**
	 * 视频地址
	 */
	private String video;
	/**
	 * 图片
	 */
	private String appImages;
	/**
	 * 图片
	 */
	private String pcImages;
	/**
	 * 'mark标识'
	 */
	private boolean  mark;
	/**
	 * '游戏图标'
	 */
	private String  icon;
	/**
	 * 'IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载
	 */
	private Integer iosState;
	/**
	 * '安卓状态 0:待开启，1预约。2.测试，3已上线
	 */
	private Integer androidState;
	/**
	 * 圈子主键
	 */
	private String circleId;

	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getRmdLag() {
		return rmdLag;
	}

	public void setRmdLag(String rmdLag) {
		this.rmdLag = rmdLag;
	}

	public String getRmdReason() {
		return rmdReason;
	}

	public void setRmdReason(String rmdReason) {
		this.rmdReason = rmdReason;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public String getAppImages() {
		return appImages;
	}

	public void setAppImages(String appImages) {
		this.appImages = appImages;
	}

	public String getPcImages() {
		return pcImages;
	}

	public void setPcImages(String pcImages) {
		this.pcImages = pcImages;
	}

}
