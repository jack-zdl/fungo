package com.game.common.dto.community;

import io.swagger.annotations.ApiModelProperty;

/**
 * 社区对象
 */
public class CommunityOutBean {
	@ApiModelProperty(value="",example="")
	private String name;
	@ApiModelProperty(value="社区id",example="")
	private String communityId;
	@ApiModelProperty(value="是否关注",example="")
	private boolean isFollowed;
	@ApiModelProperty(value="图标",example="")
	private String icon;
	@ApiModelProperty(value="创建时间",example="")
	private String createdAt;
	@ApiModelProperty(value="热度",example="")
	
	private int hotNun;
	@ApiModelProperty(value="内容",example="")
	private String intro;

	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCommunityId() {
		return communityId;
	}
	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	public boolean isFollowed() {
		return isFollowed;
	}
	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public int getHotNun() {
		return hotNun;
	}
	public void setHotNun(int hotNun) {
		this.hotNun = hotNun;
	}
	
}
