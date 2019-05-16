package com.game.common.dto.evaluation;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;

@ApiModel(value="我的评测",description="我的评测")
public class MyEvaluationBean {

	@ApiModelProperty(value="游戏id",example="")
	private String gameId;
	@ApiModelProperty(value="游戏评论id",example="")
	private String evaId;
	@ApiModelProperty(value="游戏名称",example="")
	private String gameName;
	@ApiModelProperty(value="我的评分",example="")
	private double rating;
	@ApiModelProperty(value="评论类容",example="")
	private String content;
	@ApiModelProperty(value="最后更新时间",example="")
	private String updatedAt;
	@ApiModelProperty(value="游戏icon",example="")
	private String icon;
	@ApiModelProperty(value="图片列表",example="")
	private List<String> Images = new ArrayList<>();
	
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public List<String> getImages() {
		return Images;
	}
	public void setImages(List<String> images) {
		Images = images;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getEvaId() {
		return evaId;
	}
	public void setEvaId(String evaId) {
		this.evaId = evaId;
	}

	
}
