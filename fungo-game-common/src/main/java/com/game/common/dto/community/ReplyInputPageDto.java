package com.game.common.dto.community;


import com.game.common.api.InputPageDto;

public class ReplyInputPageDto  extends InputPageDto {
	private static final long serialVersionUID = 1L;
	private String target_id;
	private String user_id;
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
}
