package com.game.common.dto.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="我的评论",description="我的评论")
public class MyCommentBean {

	
	@ApiModelProperty(value="目标类型",example="")
	private Integer targetType;
	@ApiModelProperty(value="评论类型",example="")
	private Integer commentType;
	@ApiModelProperty(value="被回复目标的内容",example="")
	private String targetConetnt;
	@ApiModelProperty(value="被回复目标的内容是否已删除",example="")
	private int targetDelType;
	@ApiModelProperty(value="评论内容",example="")
	private String commentConetnt;
	@ApiModelProperty(value="评论的内容是否已删除",example="")
	private int commentDelType;
	@ApiModelProperty(value="目标id",example="")
	private String targetId;
	@ApiModelProperty(value="评论id",example="")
	private String commentId;
	@ApiModelProperty(value="創建时间",example="")
	private String createdAt;
	@ApiModelProperty(value="最后修改时间",example="")
	private String updatedAt;
	@ApiModelProperty(value="被回复用户id",example="")
	private String replyToId;
	@ApiModelProperty(value="被回复用户昵称",example="")
	private String replyToName;
	
	@ApiModelProperty(value="被回复二级回复的id",example="")
	private String replyToConentId;
	@ApiModelProperty(value="视频地址",example="")
	private String video;
	/**
	 * 功能描述: 父级id
	 * @auther: dl.zhang
	 * @date: 2019/9/18 18:25
	 */
	private String parentId;

	private Long gameIdtSn;

	private int grandfatherStatus;
	
	public Integer getTargetType() {
		return targetType;
	}
	public void setTargetType(Integer targetType) {
		this.targetType = targetType;
	}
	public Integer getCommentType() {
		return commentType;
	}
	public void setCommentType(Integer commentType) {
		this.commentType = commentType;
	}
	public String getTargetConetnt() {
		return targetConetnt;
	}
	public void setTargetConetnt(String targetConetnt) {
		this.targetConetnt = targetConetnt;
	}
	public String getCommentConetnt() {
		return commentConetnt;
	}
	public void setCommentConetnt(String commentConetnt) {
		this.commentConetnt = commentConetnt;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
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
	public String getReplyToConentId() {
		return replyToConentId;
	}
	public void setReplyToConentId(String replyToConentId) {
		this.replyToConentId = replyToConentId;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}

	public int getTargetDelType() {
		return targetDelType;
	}

	public void setTargetDelType(int targetDelType) {
		this.targetDelType = targetDelType;
	}

	public int getCommentDelType() {
		return commentDelType;
	}

	public void setCommentDelType(int commentDelType) {
		this.commentDelType = commentDelType;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Long getGameIdtSn() {
		return gameIdtSn;
	}

	public void setGameIdtSn(Long gameIdtSn) {
		this.gameIdtSn = gameIdtSn;
	}

	public int getGrandfatherStatus() {
		return grandfatherStatus;
	}

	public void setGrandfatherStatus(int grandfatherStatus) {
		this.grandfatherStatus = grandfatherStatus;
	}
}
