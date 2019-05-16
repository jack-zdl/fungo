package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModelProperty;

public class GameItemInput extends InputPageDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("游戏合集id")
	private String group_id;

	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}



	
	
}
