package com.game.common.dto.community;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@ApiModel(value="关注用户对象",description="关注用户对象")
public class FollowUserOutBean {
	@ApiModelProperty(value="会员名称",example="")
	private String username;
	@ApiModelProperty(value="头像",example="")
	private String avatar;
	@ApiModelProperty(value="会员ID",example="")
	private String objectId;
	@ApiModelProperty(value="会员级别",example="")
	private int level;
	@ApiModelProperty(value="创建时间",example="")
	private String createdAt;
	@ApiModelProperty(value="更新时间",example="")
	private String updatedAt;
	@ApiModelProperty(value="会员号",example="")
	private String memberNo;
	@ApiModelProperty(value="是否关注",example="")
	private boolean isFollowed;
	
	private String dignityImg;
	
	private List<HashMap<String,Object>> statusImg = new ArrayList<>();
	
	@ApiModelProperty(value="签名",example="")
	private String sign;
	
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public String getMemberNo() {
		return memberNo;
	}
	public void setMemberNo(String memberNo) {
		this.memberNo = memberNo;
	}
	public boolean isFollowed() {
		return isFollowed;
	}
	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}
	public String getDignityImg() {
		return dignityImg;
	}
	public void setDignityImg(String dignityImg) {
		this.dignityImg = dignityImg;
	}
	public List<HashMap<String, Object>> getStatusImg() {
		return statusImg;
	}
	public void setStatusImg(List<HashMap<String, Object>> statusImg) {
		this.statusImg = statusImg;
	}

}
