package com.fungo.system.dto;

public class CommentInput {
	private String content;
	private String user_id;
	private String target_id;
	private int target_type;
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
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public int getTarget_type() {
		return target_type;
	}
	public void setTarget_type(int target_type) {
		this.target_type = target_type;
	}
	
	
}

