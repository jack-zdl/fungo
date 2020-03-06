package com.game.common.dto.search;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

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

	private String coverImage;
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
	@ApiModelProperty(value="游戏关联的圈子",example="")
	private String link_circle;

	// 厂商
	private String company;
	// 发行商
	private String publisher;
	// 地区
	private String address;
	// 是否可以加速
	private boolean canFast;
	// 游戏状态
	private String gameStatus;

	private String tags;

	// 用户是否预约 1 已预约  0 未预约
	private boolean make;

	private String version;

	private Long gameIdtSn;

	private List<String> images = new ArrayList<>();

	private String gameContent;

	@ApiModelProperty(value="消息数量",example="")
	private int msgCount;
	@ApiModelProperty(value="手机型号",example="")
	private String phoneModel;

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

	public String getLink_circle() {
		return link_circle;
	}

	public void setLink_circle(String link_circle) {
		this.link_circle = link_circle;
	}

	public void setRecommend_num(int recommend_num) {
		this.recommend_num = recommend_num;
	}

	public void setUnrecommend_num(int unrecommend_num) {
		this.unrecommend_num = unrecommend_num;
	}

	public void setEvaluation_num(int evaluation_num) {
		this.evaluation_num = evaluation_num;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isCanFast() {
		return canFast;
	}

	public void setCanFast(boolean canFast) {
		this.canFast = canFast;
	}

	public String getGameStatus() {
		return gameStatus;
	}

	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public boolean isMake() {
		return make;
	}

	public void setMake(boolean make) {
		this.make = make;
	}

	public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

	public Long getGameIdtSn() {
		return gameIdtSn;
	}

	public void setGameIdtSn(Long gameIdtSn) {
		this.gameIdtSn = gameIdtSn;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public String getGameContent() {
		return gameContent;
	}

	public void setGameContent(String gameContent) {
		this.gameContent = gameContent;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
}
