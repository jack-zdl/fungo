package com.fungo.system.dto;


import com.game.common.dto.AuthorBean;

import java.io.Serializable;

public class CommentBeanOut implements Serializable {
	private static final long serialVersionUID = 1L;
	private String objectId;
	private String content;
	private int reply_num;
	private int like_num;
	private int state;
	private String createdAt;
	private String updatedAt;
	private AuthorBean author;
	private int floor;
	private boolean is_liked;
	
	public int getFloor() {
		return floor;
	}
	public void setFloor(int floor) {
		this.floor = floor;
	}
	public boolean isIs_liked() {
		return is_liked;
	}
	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getReply_num() {
		return reply_num;
	}
	public void setReply_num(int reply_num) {
		this.reply_num = reply_num;
	}
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
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
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	
}

