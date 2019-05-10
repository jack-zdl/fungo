package com.game.common.dto.community;


import com.game.common.dto.AuthorBean;

public class ReplyOutPageDto {
	private String content;
	private AuthorBean author;
	private AuthorBean reply_to;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private int like_num;
	private boolean is_liked;
	
	public boolean isIs_liked() {
		return is_liked;
	}
	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
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
	public AuthorBean getReply_to() {
		return reply_to;
	}
	public void setReply_to(AuthorBean reply_to) {
		this.reply_to = reply_to;
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
	
}