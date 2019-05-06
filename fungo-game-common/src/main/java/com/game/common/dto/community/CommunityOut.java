package com.game.common.dto.community;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunityOut {

	private String name;
	private String objectId;
	private String cover_image;
	private String icon;
	private String intro;
	private String link_game;
	private String createdAt;
	private String updatedAt;
	private int type;
	private int state;
	private int followee_num;
	private int hot_value;
	private boolean is_followed;
	@ApiModelProperty(value="社区成员数(2.4.3)",example="")
	private int memberNum;
	@ApiModelProperty(value="玩家排行(2.4.3) objectId avatar",example="")
	private List<Map<String,Object>> eliteMembers = new ArrayList<>();;
	@ApiModelProperty(value="置顶帖(2.4.3) objectId title ",example="")
	private List<Map<String,String>> topicPosts = new ArrayList<>();
	
	public Boolean getIs_followed() {
		return is_followed;
	}
	public void setIs_followed(Boolean is_followed) {
		this.is_followed = is_followed;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getCover_image() {
		return cover_image;
	}
	public void setCover_image(String cover_image) {
		this.cover_image = cover_image;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getLink_game() {
		return link_game;
	}
	public void setLink_game(String link_game) {
		this.link_game = link_game;
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
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public Integer getFollowee_num() {
		return followee_num;
	}
	public void setFollowee_num(Integer followee_num) {
		this.followee_num = followee_num;
	}
	public Integer getHot_value() {
		return hot_value;
	}
	public void setHot_value(Integer hot_value) {
		this.hot_value = hot_value;
	}
	public int getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(int memberNum) {
		this.memberNum = memberNum;
	}

	public List<Map<String, String>> getTopicPosts() {
		return topicPosts;
	}
	public void setTopicPosts(List<Map<String, String>> topicPosts) {
		this.topicPosts = topicPosts;
	}
	public List<Map<String, Object>> getEliteMembers() {
		return eliteMembers;
	}
	public void setEliteMembers(List<Map<String, Object>> eliteMembers) {
		this.eliteMembers = eliteMembers;
	}


	
	
}
