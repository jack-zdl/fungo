package com.game.common.dto.game;

import io.swagger.annotations.ApiModelProperty;

public class GameOutBean {
	
	@ApiModelProperty(value="游戏id ",example="")
	private String gameId;
	@ApiModelProperty(value="游戏名 ",example="")
	private String name;
	@ApiModelProperty(value="审批状态 0未审核 1审核中 2通过 3审核失败",example="")
	private int checkState;
	private String editedAt;
	@ApiModelProperty(value="安卓状态 0: 待开启 1: 预约 2: 测试 3: 已上线",example="")
	private int androidState ;
	@ApiModelProperty(value="IOS状态 0:待开启，1预约。2.测试，3已上线,4：可下载",example="")
	private int iOState ;
	private String icon;
	private String coverImage;

	private String gamePackageName;

	private String gameVersion;
	
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
	public int getCheckState() {
		return checkState;
	}
	public void setCheckState(int checkState) {
		this.checkState = checkState;
	}
	public String getEditedAt() {
		return editedAt;
	}
	public void setEditedAt(String editedAt) {
		this.editedAt = editedAt;
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

	public String getGamePackageName() {
		return gamePackageName;
	}

	public void setGamePackageName(String gamePackageName) {
		this.gamePackageName = gamePackageName;
	}

	public String getGameVersion() {
		return gameVersion;
	}

	public void setGameVersion(String gameVersion) {
		this.gameVersion = gameVersion;
	}
}
