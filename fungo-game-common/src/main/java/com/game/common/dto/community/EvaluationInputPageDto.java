package com.game.common.dto.community;


import com.game.common.api.InputPageDto;

public class EvaluationInputPageDto extends InputPageDto {
	private static final long serialVersionUID = 1L;
	private String game_id;
	private String user_id;
	public String getGame_id() {
		return game_id;
	}
	public void setGame_id(String game_id) {
		this.game_id = game_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
