package com.game.common.dto.advert;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="广告banner",description="广告banner")
public class AdvertOutBean {
    @ApiModelProperty(value="业务类型",example="")
	private int bizType;
    @ApiModelProperty(value="业务主键",example="")
	private String bizId;
    @ApiModelProperty(value="名称",example="")
	private String name;
    @ApiModelProperty(value="标题",example="")
	private String title;
    @ApiModelProperty(value="描述",example="")
	private String content;
    @ApiModelProperty(value="图片",example="")
	private String imageUrl;

	/**
	 * 游戏icon URL
	 */
	private String gameIconURL;

    
	public int getBizType() {
		return bizType;
	}
	public void setBizType(int bizType) {
		this.bizType = bizType;
	}
	public String getBizId() {
		return bizId;
	}
	public void setBizId(String bizId) {
		this.bizId = bizId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getGameIconURL() {
		return gameIconURL;
	}

	public void setGameIconURL(String gameIconURL) {
		this.gameIconURL = gameIconURL;
	}
}
