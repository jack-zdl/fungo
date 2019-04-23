package com.game.common.dto;


public class MemberUserProfile implements UserProfile {
	private static final long serialVersionUID = 1L;
	private String loginId;
	private String name;
	@Override
	public String getLoginId() {
		return this.loginId;
	}
	@Override
	public String getName() {
		return this.name;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public void setName(String name) {
		this.name = name;
	}
}
