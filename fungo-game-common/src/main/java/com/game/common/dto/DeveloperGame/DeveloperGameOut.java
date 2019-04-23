package com.game.common.dto.DeveloperGame;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeveloperGameOut {

	private String gameId;
	private String name;
	@ApiModelProperty(value="审批状态 0: 未审核 1: 审核中 2: 审核通过  3: 审核失败",example="")
	private int checkState;
	private String updatedAt;
	private String icon;
	private String gameIntro;
	private String developer;
	private String coverImage;
	private String communityIntro;
	private String video;
	private String categoryId;
	private String detail;
	private String updateLog;
	private String apk;
	private String isbnId;
	@ApiModelProperty(value="核发单图片",example="")
	private String isbnImage;
	@ApiModelProperty(value="软件著作权登记号",example="")
	private String copyrightId;
	@ApiModelProperty(value="软件著作权照片",example="")
	private String copyrightImage;
	@ApiModelProperty(value="游戏备案通知号",example="")
	private String issueId;
	@ApiModelProperty(value="苹果商店",example="")
	private String itunesId;
	@ApiModelProperty(value="测试日期",example="")
	private String tsetDate;
	private List<String> images = new ArrayList<>();
	private List<Map<String,Object>> developerList = new ArrayList<>();
	private List<String> tagList = new ArrayList<>();
	@ApiModelProperty(value="其它证明文件",example="")
	private List<String> credentials = new ArrayList<>();
	@ApiModelProperty(value="IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载",example="")
	private int androidState;
	@ApiModelProperty(value="安卓状态 0:待开启，1预约。2.测试，3已上线",example="")
	private int iOState;
	@ApiModelProperty(value="测试数量",example="")
	private int testNumber;
	@ApiModelProperty(value="游戏版本",example="")
	private String version;
	private String communityId;
	
	
	private String origin;
	private int gameSize;
	private String gameLogId;
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	

	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getGameIntro() {
		return gameIntro;
	}
	public void setGameIntro(String gameIntro) {
		this.gameIntro = gameIntro;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public String getCommunityIntro() {
		return communityIntro;
	}
	public void setCommunityIntro(String communityIntro) {
		this.communityIntro = communityIntro;
	}

	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getUpdateLog() {
		return updateLog;
	}
	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}
	public String getApk() {
		return apk;
	}
	public void setApk(String apk) {
		this.apk = apk;
	}
	public String getIsbnId() {
		return isbnId;
	}
	public void setIsbnId(String isbnId) {
		this.isbnId = isbnId;
	}
	public String getIsbnImage() {
		return isbnImage;
	}
	public void setIsbnImage(String isbnImage) {
		this.isbnImage = isbnImage;
	}
	public String getCopyrightId() {
		return copyrightId;
	}
	public void setCopyrightId(String copyrightId) {
		this.copyrightId = copyrightId;
	}
	public String getCopyrightImage() {
		return copyrightImage;
	}
	public void setCopyrightImage(String copyrightImage) {
		this.copyrightImage = copyrightImage;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	}
	public String getItunesId() {
		return itunesId;
	}
	public void setItunesId(String itunesId) {
		this.itunesId = itunesId;
	}
	public String getTsetDate() {
		return tsetDate;
	}
	public void setTsetDate(String tsetDate) {
		this.tsetDate = tsetDate;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public List<String> getTagList() {
		return tagList;
	}
	public void setTagList(List<String> tagList) {
		this.tagList = tagList;
	}
	public List<String> getCredentials() {
		return credentials;
	}
	public void setCredentials(List<String> credentials) {
		this.credentials = credentials;
	}
	public int getAndroidState() {
		return androidState;
	}
	public void setAndroidState(int androidState) {
		this.androidState = androidState;
	}
	public int getiOState() {
		return iOState;
	}
	public void setiOState(int iOState) {
		this.iOState = iOState;
	}
	public int getTestNumber() {
		return testNumber;
	}
	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public int getCheckState() {
		return checkState;
	}
	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getGameLogId() {
		return gameLogId;
	}
	public void setGameLogId(String gameLogId) {
		this.gameLogId = gameLogId;
	}
	public List<Map<String, Object>> getDeveloperList() {
		return developerList;
	}
	public void setDeveloperList(List<Map<String, Object>> developerList) {
		this.developerList = developerList;
	}
	public int getGameSize() {
		return gameSize;
	}
	public void setGameSize(int gameSize) {
		this.gameSize = gameSize;
	}
	
	
}
