package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoodBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String content;
	private AuthorBean author;
	private List<String> images;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private String cover_image;
	private boolean is_liked;
	private int comment_num;
	private int like_num;

	/**
	 * 视频(2.4.3)
	 */
	private String video;

	/**
	 * 游戏链接列表(2.4.3) 内容 gameId gameName gameIcon"
	 */
	private List<Map<String,Object>> gameList = new ArrayList<>();
	
	private String videoCoverImage;

    /**
     * 视频详情(2.4.3)
	 */
	private List<StreamInfo> videoList = new ArrayList<>();
	
	public int getComment_num() {
		return comment_num;
	}
	public void setComment_num(int comment_num) {
		this.comment_num = comment_num;
	}
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public String getCover_image() {
		return cover_image;
	}
	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public boolean isIs_liked() {
		return is_liked;
	}
	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public List<Map<String, Object>> getGameList() {
		return gameList;
	}
	public void setGameList(List<Map<String, Object>> gameList) {
		this.gameList = gameList;
	}
	public String getVideoCoverImage() {
		return videoCoverImage;
	}
	public void setVideoCoverImage(String videoCoverImage) {
		this.videoCoverImage = videoCoverImage;
	}
	public List<StreamInfo> getVideoList() {
		return videoList;
	}
	public void setVideoList(List<StreamInfo> videoList) {
		this.videoList = videoList;
	}

	
	
}
