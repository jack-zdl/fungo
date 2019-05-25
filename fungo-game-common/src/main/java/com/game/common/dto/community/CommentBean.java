package com.game.common.dto.community;

import java.io.Serializable;
import java.util.Date;

public class CommentBean implements Serializable {

	private String id;
	private String content;
	private int type;//评论主体类型
	private int targetType;//评论对象类型
	private String targetId;//评论对象id
	private String createdAt;
	private Date updatedAt;
	private int replyNum;
	private int likeNum;
	private String username;//用户id
	private String communityId;//关联社区id
	private String poster; //帖子发布人id
	private String postId; //帖子id
	private String title;
	private String mooder; //心情发布人id
	private String moodeId;//心情id
	
	private String replyToId;//回复二级回复的用户id
	private String replyContentId;//回复二级回复的id
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getReplyNum() {
		return replyNum;
	}
	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}
	public int getLikeNum() {
		return likeNum;
	}
	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMooder() {
		return mooder;
	}
	public void setMooder(String mooder) {
		this.mooder = mooder;
	}
	public String getPostId() {
		return postId;
	}
	public void setPostId(String postId) {
		this.postId = postId;
	}
	public String getMoodeId() {
		return moodeId;
	}
	public void setMoodeId(String moodeId) {
		this.moodeId = moodeId;
	}
	public int getTargetType() {
		return targetType;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getReplyToId() {
		return replyToId;
	}
	public void setReplyToId(String replyToId) {
		this.replyToId = replyToId;
	}
	public String getReplyContentId() {
		return replyContentId;
	}
	public void setReplyContentId(String replyContentId) {
		this.replyContentId = replyContentId;
	}
	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	
	
}
