package com.fungo.system.dao;

import com.fungo.api.InputPageDto;

public class FollowInptPageDao extends InputPageDto{
	private static final long serialVersionUID = 1L;
	private String memberId;
	private int type=1;
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	
	
}
