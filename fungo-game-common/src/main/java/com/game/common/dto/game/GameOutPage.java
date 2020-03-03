package com.game.common.dto.game;

import io.swagger.annotations.ApiModelProperty;

public class GameOutPage {

	private String objectId;
	private String name;
	private String icon;
	private double recommend_total_rate;
	private int recommend_total_count;
	private String createdAt;
	private String updatedAt;
	@ApiModelProperty(value="2.4.3游戏评分",example="")
	private double rating;
	
	@ApiModelProperty(value="安卓游戏状态(2.4.3)",example="")
	private int androidState;
	@ApiModelProperty(value="ios游戏状态(2.4.3)",example="")
	private int iosState;
	@ApiModelProperty(value="评论数量(2.4.3)",example="")
	private int comment_num;
	@ApiModelProperty(value="关联社区id(2.4.3)",example="")
	private String link_community;
	@ApiModelProperty(value="游戏大小(2.4.3)",example="")
	private long game_size;
	@ApiModelProperty(value="安卓包名(2.4.3)",example="")
	private String androidPackageName;
	@ApiModelProperty(value="下载地址(2.4.3)",example="")
	private String apkUrl;
	
	private String category;
	private String itunesId;
	
	@ApiModelProperty(value="是否同意条款",example="")
	private boolean isClause=false;
	@ApiModelProperty(value="是否绑定appleId",example="")
	private boolean isBinding=false;
	@ApiModelProperty(value="是否预约",example="")
	private boolean isMake;

	private Long gameIdtSn;
	//安卓状态描述
	private String androidStatusDesc;
	//ios状态描述
	private String iosStatusDesc;
	//ios状态描述
	private String tags;

	private String version;

	
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getRecommend_total_count() {
		return recommend_total_count;
	}
	public void setRecommend_total_count(Integer recommend_total_count) {
		this.recommend_total_count = recommend_total_count;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public double getRecommend_total_rate() {
		return recommend_total_rate;
	}
	public void setRecommend_total_rate(double recommend_total_rate) {
		this.recommend_total_rate = recommend_total_rate;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public int getAndroidState() {
		return androidState;
	}
	public void setAndroidState(int androidState) {
		this.androidState = androidState;
	}
	public int getIosState() {
		return iosState;
	}
	public void setIosState(int iosState) {
		this.iosState = iosState;
	}
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}
	public String getLink_community() {
		return link_community;
	}
	public void setLink_community(String link_community) {
		this.link_community = link_community;
	}

	public long getGame_size() {
		return game_size;
	}
	public void setGame_size(long game_size) {
		this.game_size = game_size;
	}
	public String getAndroidPackageName() {
		return androidPackageName;
	}
	public void setAndroidPackageName(String androidPackageName) {
		this.androidPackageName = androidPackageName;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getItunesId() {
		return itunesId;
	}
	public void setItunesId(String itunesId) {
		this.itunesId = itunesId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public boolean isClause() {
		return isClause;
	}
	public void setClause(boolean isClause) {
		this.isClause = isClause;
	}
	public boolean isBinding() {
		return isBinding;
	}
	public void setBinding(boolean isBinding) {
		this.isBinding = isBinding;
	}
	public boolean isMake() {
		return isMake;
	}
	public void setMake(boolean isMake) {
		this.isMake = isMake;
	}


	public Long getGameIdtSn() {
		return gameIdtSn;
	}

	public void setGameIdtSn(Long gameIdtSn) {
		this.gameIdtSn = gameIdtSn;
	}

	public String getAndroidStatusDesc() {
		return androidStatusDesc;
	}

	public void setAndroidStatusDesc(String androidStatusDesc) {
		this.androidStatusDesc = androidStatusDesc;
	}

	public String getIosStatusDesc() {
		return iosStatusDesc;
	}

	public void setIosStatusDesc(String iosStatusDesc) {
		this.iosStatusDesc = iosStatusDesc;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public void setRecommend_total_count(int recommend_total_count) {
		this.recommend_total_count = recommend_total_count;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
