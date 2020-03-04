package com.fungo.system.dto;


import com.game.common.dto.AuthorBean;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.GameOut;

public class CollectionOutBean {
	private String content;
	private String cover_image;
	private String title;
	private AuthorBean author;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private String video;

	private GameDto game;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCover_image() {
		return cover_image;
	}
	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
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
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}

	public GameDto getGame() {
		return game;
	}

	public void setGame(GameDto game) {
		this.game = game;
	}
}
