package com.game.common.dto.game;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="游戏对象",description="我的游戏对象")
public class MyGameBean {
    @ApiModelProperty(value="游戏名称",example="")
	private String gameName;
    @ApiModelProperty(value="游戏ID",example="")
	private String gameId;
    @ApiModelProperty(value="游戏Icon",example="")
	private String gameIcon;
    @ApiModelProperty(value="内容",example="")
	private String gameContent;
    @ApiModelProperty(value="ios状态",example="")
	private int iosState;
    @ApiModelProperty(value="android状态",example="")
	private int androidState;
    @ApiModelProperty(value="消息数量",example="")
	private int msgCount;
    @ApiModelProperty(value="手机型号",example="")
	private String phoneModel;
	@ApiModelProperty(value = "游戏评分(v2.4)", example = "")
	private double rating;

	@ApiModelProperty(value="游戏数字id",example="")
	private Long gameIdtSn;

	public Long getGameIdtSn() {
		return gameIdtSn;
	}

	public void setGameIdtSn(Long gameIdtSn) {
		this.gameIdtSn = gameIdtSn;
	}

	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getGameIcon() {
		return gameIcon;
	}
	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}
	public String getGameContent() {
		return gameContent;
	}
	public void setGameContent(String gameContent) {
		this.gameContent = gameContent;
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

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
}
