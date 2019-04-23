package com.fungo.system.dto;

import io.swagger.annotations.ApiModel;

@ApiModel(value="推送拓展字段",description="推送拓展字段")
public class PushDemo {

	private int like;
	private int like_count;
	private int comment_count;
	public int getLike() {
		return like;
	}
	public int getLike_count() {
		return like_count;
	}
	public int getComment_count() {
		return comment_count;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public void setLike_count(int like_count) {
		this.like_count = like_count;
	}
	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}
	
	
}
