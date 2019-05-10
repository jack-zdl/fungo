package com.game.common.dto.community;


import com.game.common.dto.AuthorBean;

public class ReplyOutBean {
	private String content;
	private String target_id;
	private AuthorBean author;
	private int target_type;
	private String objectId;
	private String createdAt;
	private String updatedAt;
	private AuthorBean reply_to;

	public AuthorBean getReply_to() {
		return reply_to;
	}
	public void setReply_to(AuthorBean reply_to) {
		this.reply_to = reply_to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public AuthorBean getAuthor() {
		return author;
	}
	public void setAuthor(AuthorBean author) {
		this.author = author;
	}
	public int getTarget_type() {
		return target_type;
	}
	public void setTarget_type(int target_type) {
		this.target_type = target_type;
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
	
}
