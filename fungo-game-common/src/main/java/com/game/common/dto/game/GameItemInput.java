package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class GameItemInput extends InputPageDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("游戏合集id")
	private String group_id;
	@ApiModelProperty("名字")
	private String name;
	public String getGroup_id() {
		return group_id;
	}
	public void setGroup_id(String group_id) {
		this.group_id = group_id;
	}



	
	
}
