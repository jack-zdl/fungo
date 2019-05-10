package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MoodOutBean {
	@ApiModelProperty(value="会员信息",example="")
	private AuthorBean author;
	@ApiModelProperty(value="内容",example="")
	private String content;
	@ApiModelProperty(value="图片",example="")
	private String coverImage;
	@ApiModelProperty(value="心情图片列表",example="")
	private List<String> images;
	@ApiModelProperty(value="心情id",example="")
	private String moodId;
	@ApiModelProperty(value="创建时间",example="")
	private String createdAt;
	@ApiModelProperty(value="更新时间",example="")
	private String updatedAt;
	@ApiModelProperty(value="点赞数",example="")
	private int likeNum;
	@ApiModelProperty(value="是否点攒",example="")
	private boolean isLiked;
	@ApiModelProperty(value="回复数",example="")
	private int replyNum;
	@ApiModelProperty(value="计算距离当前时间",example="")
	private String timer;
	@ApiModelProperty(value="视频",example="")
	private String video;
	@ApiModelProperty(value="游戏链接",example="")
	private List<HashMap<String,Object>> gameList = new ArrayList<>();
	@ApiModelProperty(value="视频封面",example="")
	private String videoCoverImage;
	
	private ArrayList videoList = new ArrayList<>();


	/**
	 * 文章数据行ID
	 */
	private Long rowId;

	
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCoverImage() {
		return coverImage;
	}
	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
	}
	public List<String> getImages() {
		return images;
	}
	public void setImages(List<String> images) {
		this.images = images;
	}
	public String getMoodId() {
		return moodId;
	}
	public void setMoodId(String moodId) {
		this.moodId = moodId;
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
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public boolean isLiked() {
		return isLiked;
	}
	public void setLiked(boolean isLiked) {
		this.isLiked = isLiked;
	}
	public int getReplyNum() {
		return replyNum;
	}
	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}
	public String getTimer() {
		return timer;
	}
	public void setTimer(String timer) {
		this.timer = timer;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public List<HashMap<String, Object>> getGameList() {
		return gameList;
	}
	public void setGameList(List<HashMap<String, Object>> gameList) {
		this.gameList = gameList;
	}
	public String getVideoCoverImage() {
		return videoCoverImage;
	}
	public void setVideoCoverImage(String videoCoverImage) {
		this.videoCoverImage = videoCoverImage;
	}

	public ArrayList getVideoList() {
		return videoList;
	}

	public void setVideoList(ArrayList videoList) {
		this.videoList = videoList;
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}
}
