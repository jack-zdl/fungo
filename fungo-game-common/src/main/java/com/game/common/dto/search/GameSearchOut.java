package com.game.common.dto.search;

import io.swagger.annotations.ApiModelProperty;

public class GameSearchOut {

	private String objectId;
	private String name;
	private String link_community;
	private String icon;
	private int recommend_num;
	private int unrecommend_num;
	private int evaluation_num;
	private long game_size;
	private int score;
	private String createdAt;
	private String updatedAt;
	private String developer;
	private String tag;
	
	private String cover_image;
	private String intro;
	private double rating;
	@ApiModelProperty(value="安卓游戏状态",example="")
	private int androidState;
	@ApiModelProperty(value="ios游戏状态",example="")
	private int iosState;
	@ApiModelProperty(value="安卓游戏包名",example="")
	private String androidPackageName;
	private String itunesId;
	@ApiModelProperty(value="下载地址",example="")
	private String apkUrl;
	
	private String category;
	
//	@ApiModelProperty(value="是否同意条款",example="")
//	private boolean isClause=false;
//	@ApiModelProperty(value="是否绑定appleId",example="")
//	private boolean isBinding=false;
//	@ApiModelProperty(value="是否预约",example="")
//	private boolean isMake;
	
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
	public String getLink_community() {
		return link_community;
	}
	public void setLink_community(String link_community) {
		this.link_community = link_community;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public Integer getRecommend_num() {
		return recommend_num;
	}
	public void setRecommend_num(Integer recommend_num) {
		this.recommend_num = recommend_num;
	}
	public Integer getUnrecommend_num() {
		return unrecommend_num;
	}
	public void setUnrecommend_num(Integer unrecommend_num) {
		this.unrecommend_num = unrecommend_num;
	}
	public Integer getEvaluation_num() {
		return evaluation_num;
	}
	public void setEvaluation_num(Integer evaluation_num) {
		this.evaluation_num = evaluation_num;
	}
	

	public long getGame_size() {
		return game_size;
	}
	public void setGame_size(long game_size) {
		this.game_size = game_size;
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
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getCover_image() {
		return cover_image;
	}
	public String getIntro() {
		return intro;
	}
	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}
	public void setIntro(String intro) {
		this.intro = intro;
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
	public String getAndroidPackageName() {
		return androidPackageName;
	}
	public void setAndroidPackageName(String androidPackageName) {
		this.androidPackageName = androidPackageName;
	}
	public String getItunesId() {
		return itunesId;
	}
	public void setItunesId(String itunesId) {
		this.itunesId = itunesId;
	}
	public String getApkUrl() {
		return apkUrl;
	}
	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}

	
	
	
}