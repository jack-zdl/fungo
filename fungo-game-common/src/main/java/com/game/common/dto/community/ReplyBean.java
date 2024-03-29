package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;

public class ReplyBean {
	private String content;
	private AuthorBean author;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private int like_num;

	/**
	 * 回复对象id(2.4.3)
	 */
	private String replyToId;
    /**
	 * 回复对象昵称(2.4.3)
	 */
	private String replyToName;

	private boolean liked;

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
	public int getLike_num() {
		return like_num;
	}
	public void setLike_num(int like_num) {
		this.like_num = like_num;
	}
	public String getReplyToId() {
		return replyToId;
	}
	public void setReplyToId(String replyToId) {
		this.replyToId = replyToId;
	}
	public String getReplyToName() {
		return replyToName;
	}
	public void setReplyToName(String replyToName) {
		this.replyToName = replyToName;
	}

	public boolean isLiked() {
		return liked;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}
}
