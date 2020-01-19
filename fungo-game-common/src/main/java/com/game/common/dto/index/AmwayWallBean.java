package com.game.common.dto.index;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.List;

@ApiModel(value="安利墙",description="安利墙")
public class AmwayWallBean {
	@ApiModelProperty(value="会员信息",example="")
	private AuthorBean author;
	@ApiModelProperty(value="游戏id",example="")
	private String gameId;
	@ApiModelProperty(value="游戏图片",example="")
	private String gameImage;
	@ApiModelProperty(value="游戏icon",example="")
	private String gameIcon;
	@ApiModelProperty(value="游戏名称",example="")
	private String gameName;
	@ApiModelProperty(value="评价信息",example="")
	private String evaluation;
	@ApiModelProperty(value="是否推荐",example="")
	private boolean recommend;
	@ApiModelProperty(value="评价id",example="")
	private String evaluationId;
	@ApiModelProperty(value="2.4游戏评分",example="")
	private BigDecimal rating;
	/**
	 * 图片s
	 */
	private List<String> images;

	// 游戏编号
	private Long gameIdtSn;
	
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	
	public String getGameImage() {
		return gameImage;
	}
	public void setGameImage(String gameImage) {
		this.gameImage = gameImage;
	}
	public String getGameIcon() {
		return gameIcon;
	}
	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getEvaluation() {
		return evaluation;
	}
	public void setEvaluation(String evaluation) {
		this.evaluation = evaluation;
	}
	public boolean isRecommend() {
		return recommend;
	}
	public void setRecommend(boolean recommend) {
		this.recommend = recommend;
	}
	public String getEvaluationId() {
		return evaluationId;
	}
	public void setEvaluationId(String evaluationId) {
		this.evaluationId = evaluationId;
	}
	public BigDecimal getRating() {
		return rating;
	}
	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}

	public Long getGameIdtSn() {
		return gameIdtSn;
	}

	public void setGameIdtSn(Long gameIdtSn) {
		this.gameIdtSn = gameIdtSn;
	}
}
