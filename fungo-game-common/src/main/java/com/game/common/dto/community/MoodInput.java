package com.game.common.dto.community;

import com.game.common.dto.InputDto;

import java.util.List;

public class MoodInput extends InputDto {
	private static final long serialVersionUID = 1L;
	private List<String> cover_image;
	private String content;


	/**
	 * 视频(2.4.3)
	 */
	private String video;

	/**
	 *
	 * 视频ID
	 */
	private  String videoId;

	/**
	 * 游戏链接(2.4.3)
	 */
	private List<String> gameList;
	
	public List<String> getCover_image() {
		return cover_image;
	}
	public void setCover_image(List<String> cover_image) {
		this.cover_image = cover_image;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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


	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
}
