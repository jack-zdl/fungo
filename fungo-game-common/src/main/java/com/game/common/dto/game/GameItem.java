package com.game.common.dto.game;

import io.swagger.annotations.ApiModelProperty;

public class GameItem {

	private String name;
	private String icon;
	private String objectId;
	private double rating;
	@ApiModelProperty("IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载")
	private int iosState;
	@ApiModelProperty("安卓状态 0:待开启，1预约。2.测试，3已上线")
	private int androidState;
	
	@ApiModelProperty(value="是否同意条款",example="")
	private boolean isClause=false;
	@ApiModelProperty(value="是否绑定appleId",example="")
	private boolean isBinding=false;
	@ApiModelProperty(value="是否预约",example="")
	private boolean isMake;
	
	@ApiModelProperty(value="安卓游戏包名",example="")
	private String androidPackageName;
	private String itunesId;
	@ApiModelProperty(value="下载地址",example="")
	private String apkUrl;
	
	private String category;
	//安卓状态描述
	private String androidStatusDesc;
	//ios状态描述
	private String iosStatusDesc;
	//ios状态描述
	private String tags;
	//图片s
	private String images;
	//游戏图标
	private String coverImage;
	
	

	private String origin;

	private String version;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public int getIosState() {
		return iosState;
	}
	public void setIosState(int iosState) {
		this.iosState = iosState;
	}
	public int getAndroidState() {
		return androidState;
	}
	public void setAndroidState(int androidState) {
		this.androidState = androidState;
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
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
