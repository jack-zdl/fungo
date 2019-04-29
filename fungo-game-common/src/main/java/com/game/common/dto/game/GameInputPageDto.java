package com.game.common.dto.game;

import com.game.common.api.InputPageDto;

public class GameInputPageDto extends InputPageDto {
	private static final long serialVersionUID = 1L;
	private String[] id_list;
	private int state;
	private String tag;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String[] getId_list() {
		return id_list;
	}
	public void setId_list(String[] id_list) {
		this.id_list = id_list;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	
}
