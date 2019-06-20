package com.game.common.dto.community;

import com.game.common.dto.GameDto;

import java.io.Serializable;
import java.util.List;

public class PostInput implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 文章id(2.4.3)
	 */
	private String postId;
	private String title;
	private String content;
	private String user_id;
	private String community_id;
	private String html;
	private String cover_image;
	private String origin;
	private String[] images;
	private String video; //视频URL


	/**
	 *
	 * 视频ID
	 */
	private  String videoId;


	 /**
	  * 游戏链接(2.4.3)
	  */
	private List<String> gameList;


	/**
	 * 文章关联游戏id(2.5)
	 */
	private List<String> includeGameList;

	/**
	 * 圈子id
	 */
	private String circleId;

	/**
	 * 标签id
	 */
	private String tagId;

	public List<String> getIncludeGameList() {
		return includeGameList;
	}

	public void setIncludeGameList(List<String> includeGameList) {
		this.includeGameList = includeGameList;
	}

	public String getCircleId() {
		return circleId;
	}

	public void setCircleId(String circleId) {
		this.circleId = circleId;
	}

	public String getTagId() {
		return tagId;
	}

	public void setTagId(String tagId) {
		this.tagId = tagId;
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
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getCommunity_id() {
		return community_id;
	}
	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}
	public String getHtml() {
		return html;
	}
	public void setHtml(String html) {
		this.html = html;
	}
	public String getCover_image() {
		return cover_image;
	}
	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String[] getImages() {
		return images;
	}
	public void setImages(String[] images) {
		this.images = images;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}
	public List<String> getGameList() {
		return gameList;
	}
	public void setGameList(List<String> gameList) {
		this.gameList = gameList;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}


	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
}
