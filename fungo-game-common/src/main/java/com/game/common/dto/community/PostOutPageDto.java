package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;

public class PostOutPageDto {

	private int feed_type = 1;
	private AuthorBean author;

	private String community_id;
	private String community_name;
	private String community_icon;
	private String title;
	private String content;
	private String cover_image;
	private int like_num;
	private int comment_num;
	private String editedAt;
	private String createdAt;
	private boolean isLike;
	private String video;
	
	private String post_id;
	

	/**
	 * 精华状态(2.4.3) 1:普通 2:精华
	 */
	private int type;
	
	private int essenceState;

	public Integer getFeed_type() {
		return feed_type;
	}

	public void setFeed_type(Integer feed_type) {
		this.feed_type = feed_type;
	}


	public String getCommunity_id() {
		return community_id;
	}

	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}

	
	public String getCommunity_name() {
		return community_name;
	}

	public void setCommunity_name(String community_name) {
		this.community_name = community_name;
	}

	public String getCommunity_icon() {
		return community_icon;
	}

	public void setCommunity_icon(String community_icon) {
		this.community_icon = community_icon;
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

	public String getCover_image() {
		return cover_image;
	}

	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}

	public Integer getLike_num() {
		return like_num;
	}

	public void setLike_num(Integer like_num) {
		this.like_num = like_num;
	}

	public Integer getComment_num() {
		return comment_num;
	}

	public void setComment_num(Integer comment_num) {
		this.comment_num = comment_num;
	}

	public String getEditedAt() {
		return editedAt;
	}

	public void setEditedAt(String editedAt) {
		this.editedAt = editedAt;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isLike() {
		return isLike;
	}

	public void setLike(boolean isLike) {
		this.isLike = isLike;
	}

	public String getVideo() {
		return video;
	}

	public void setVideo(String video) {
		this.video = video;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public AuthorBean getAuthor() {
		return author;
	}

	public void setAuthor(AuthorBean author) {
		this.author = author;
	}

	public int getEssenceState() {
		return essenceState;
	}

	public void setEssenceState(int essenceState) {
		this.essenceState = essenceState;
	}

	public String getPost_id() {
		return post_id;
	}

	public void setPost_id(String post_id) {
		this.post_id = post_id;
	}

	
	

}
