package com.game.common.dto.community;


import com.game.common.api.InputPageDto;

public class CommentInputPageDto extends InputPageDto {
	private static final long serialVersionUID = 1L;
	private String post_id;
	private String mood_id;
	public String getPost_id() {
		return post_id;
	}
	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}
	public String getMood_id() {
		return mood_id;
	}
	public void setMood_id(String mood_id) {
		this.mood_id = mood_id;
	}
	
}
