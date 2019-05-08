package com.game.common.dto.community;

import com.game.common.dto.AuthorBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="社区玩家榜",description="社区玩家榜")
public class CommunityMember {

	@ApiModelProperty(value="用户基本信息",example="")
	private AuthorBean authorBean;
	@ApiModelProperty(value="贡献值",example="")
	private int merits;
	@ApiModelProperty(value="是否关注",example="")
	private boolean isFollowed;
	@ApiModelProperty(value="是否用户本人",example="")
	private boolean isYourself;
	
	public AuthorBean getAuthorBean() {
		return authorBean;
	}
	public void setAuthorBean(AuthorBean authorBean) {
		this.authorBean = authorBean;
	}
	public int getMerits() {
		return merits;
	}
	public void setMerits(int merits) {
		this.merits = merits;
	}
	public boolean isFollowed() {
		return isFollowed;
	}
	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}
	public boolean isYourself() {
		return isYourself;
	}
	public void setYourself(boolean isYourself) {
		this.isYourself = isYourself;
	}
	
	
	
}
