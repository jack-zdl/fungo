package com.game.common.dto.game;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddGameInputBean {

	private String gameId;
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	@ApiModelProperty(value="游戏名 ",example="")
	private String name;
	@ApiModelProperty(value="国家",example="")
	private String origin;
	@ApiModelProperty(value="游戏大小",example="")
	private  int size;
	private String developer;
	private List<String> developerList = new ArrayList<>();
	private List<String> images = new ArrayList<>();
	@ApiModelProperty(value="标签id列表",example="")
	private List<String> tagList = new ArrayList<>();
	@ApiModelProperty(value="其它证明文件",example="")
	private List<String> credentials = new ArrayList<>();
	private String icon;
	private String coverImage;
	private String gameIntro;
	private String communityIntro;
	private String video;
	@ApiModelProperty(value="分类标签(官方标签)id",example="")
	private String categoryId;
	private String detail;
	private String version;
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
	@ApiModelProperty(value="安卓状态 0:待开启，1预约。2.测试，3已上线",example="")
	private String androidState;
	@ApiModelProperty(value="IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载",example="")
	private String iOState;
	@ApiModelProperty(value="苹果商店",example="")
	private String itunesId;
	@ApiModelProperty(value="测试数量",example="")
	private int testNumber;
	@ApiModelProperty(value="测试日期",example="")
	private Date tsetDate;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public List<String> getDeveloperList() {
		return developerList;
	}
	public void setDeveloperList(List<String> developerList) {
		this.developerList = developerList;
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
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public String getGameIntro() {
		return gameIntro;
	}
	public void setGameIntro(String gameIntro) {
		this.gameIntro = gameIntro;
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
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
	public String getAndroidState() {
		return androidState;
	}
	public void setAndroidState(String androidState) {
		this.androidState = androidState;
	}
	public String getiOState() {
		return iOState;
	}
	public void setiOState(String iOState) {
		this.iOState = iOState;
	}
	public String getItunesId() {
		return itunesId;
	}
	public void setItunesId(String itunesId) {
		this.itunesId = itunesId;
	}
	public int getTestNumber() {
		return testNumber;
	}
	public void setTestNumber(int testNumber) {
		this.testNumber = testNumber;
	}
	public Date getTsetDate() {
		return tsetDate;
	}
	public void setTsetDate(Date tsetDate) {
		this.tsetDate = tsetDate;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	
	
	
}
