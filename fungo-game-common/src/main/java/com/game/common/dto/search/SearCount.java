package com.game.common.dto.search;

import io.swagger.annotations.ApiModel;

@ApiModel(value="搜索结果总数统计",description="搜索结果总数统计")
public class SearCount {

	private int postCount;
	private int gameCount;
	private int userCount;
	private String keyword;
	
	public int getPostCount() {
		return postCount;
	}
	public int getGameCount() {
		return gameCount;
	}
	public int getUserCount() {
		return userCount;
	}
	public void setPostCount(int postCount) {
		this.postCount = postCount;
	}
	public void setGameCount(int gameCount) {
		this.gameCount = gameCount;
	}
	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	
}
