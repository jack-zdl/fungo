package com.game.common.dto.evaluation;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class EvaluationOutBean {
	private String content;
	private String phone_model;
	private int state;
	private int like_num;
	private boolean is_liked;
	private boolean is_recommend;
	private AuthorBean author;
	private List<String> images =new ArrayList<String>();
	private int reply_count=0;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private int rating;
	/**
	 * 功能描述: 针对游戏的平均评分
	 * @auther: dl.zhang
	 * @date: 2019/8/9 10:20
	 */
	private BigDecimal gameRating;
	@ApiModelProperty(value="2.4上一安利",example="")
	private String preEvaId;
	@ApiModelProperty(value="2.4下一安利",example="")
	private String nextEvaId;
	private int sort;
	private String gameId;
	private String gameName;
	private String gameIcon;
	private String gameIntro;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhone_model() {
		return phone_model;
	}
	public void setPhone_model(String phone_model) {
		this.phone_model = phone_model;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public boolean isIs_liked() {
		return is_liked;
	}
	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
	}
	public boolean isIs_recommend() {
		return is_recommend;
	}
	public void setIs_recommend(boolean is_recommend) {
		this.is_recommend = is_recommend;
	}
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int reply_count) {
		this.reply_count = reply_count;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
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
	public int getRating() {
		return rating;
	}
	public void setRating(int rating) {
		this.rating = rating;
	}

	public BigDecimal getGameRating() {
		return gameRating;
	}

	public void setGameRating(BigDecimal gameRating) {
		this.gameRating = gameRating;
	}

	public String getPreEvaId() {
		return preEvaId;
	}
	public String getNextEvaId() {
		return nextEvaId;
	}
	public int getSort() {
		return sort;
	}
	public void setPreEvaId(String preEvaId) {
		this.preEvaId = preEvaId;
	}
	public void setNextEvaId(String nextEvaId) {
		this.nextEvaId = nextEvaId;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	public String getGameId() {
		return gameId;
	}
	public String getGameName() {
		return gameName;
	}
	public String getGameIcon() {
		return gameIcon;
	}
	public String getGameIntro() {
		return gameIntro;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public void setGameIcon(String gameIcon) {
		this.gameIcon = gameIcon;
	}
	public void setGameIntro(String gameIntro) {
		this.gameIntro = gameIntro;
	}
	
	
}
