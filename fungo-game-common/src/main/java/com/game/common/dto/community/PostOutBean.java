package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;


@ApiModel(value="帖子对象",description="帖子对象")
public class PostOutBean {

	public PostOutBean(){

	}

	@ApiModelProperty(value="会员信息",example="")
	private AuthorBean author;

	@ApiModelProperty(value="帖子id",example="")
	private String postId;

	@ApiModelProperty(value="图片",example="")
	private String imageUrl;

	@ApiModelProperty(value="视频地址v2.4",example="")
	private String videoUrl;

	@ApiModelProperty(value="标题",example="")
	private String title;

	@ApiModelProperty(value="内容",example="")
	private String content;

	@ApiModelProperty(value="点赞数",example="")
	private int likeNum;
	@ApiModelProperty(value="是否点攒",example="")
	private boolean isLiked;
	@ApiModelProperty(value="回复数",example="")
	private int replyNum;
	
	@ApiModelProperty(value="社区id",example="")
	private String communityId;

	@ApiModelProperty(value="社区图标",example="")
	private String communityIcon;

	@ApiModelProperty(value="社区名称",example="")
	private String communityName;

	@ApiModelProperty(value="是否点赞",example="")
	private boolean is_liked;

	@ApiModelProperty(value="图片数组",example="")
	private List<String> images = new ArrayList<>();

	@ApiModelProperty(value="最后更新时间",example="")
	private String updated_at;

	@ApiModelProperty(value="发布时间",example="")
	private String createdAt;

	@ApiModelProperty(value="帖子状态 1:普通 2:精华 3:置顶",example="")
	private int type;

	private String videoCoverImage;


	/**
	 * 文章数据行ID
	 */
	private Long rowId;




	public AuthorBean getAuthor() {
		return author;
	}

	public void setAuthor(AuthorBean author) {
		this.author = author;
	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
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

	public int getLikeNum() {
		return likeNum;
	}

	public void setLikeNum(int likeNum) {
		this.likeNum = likeNum;
	}

	public boolean isLiked() {
		return isLiked;
	}

	public void setLiked(boolean liked) {
		isLiked = liked;
	}

	public int getReplyNum() {
		return replyNum;
	}

	public void setReplyNum(int replyNum) {
		this.replyNum = replyNum;
	}

	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}

	public String getCommunityIcon() {
		return communityIcon;
	}

	public void setCommunityIcon(String communityIcon) {
		this.communityIcon = communityIcon;
	}

	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

	public boolean isIs_liked() {
		return is_liked;
	}

	public void setIs_liked(boolean is_liked) {
		this.is_liked = is_liked;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public String getUpdated_at() {
		return updated_at;
	}

	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getVideoCoverImage() {
		return videoCoverImage;
	}

	public void setVideoCoverImage(String videoCoverImage) {
		this.videoCoverImage = videoCoverImage;
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

}
