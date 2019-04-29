package com.game.common.dto.game;

import io.swagger.annotations.ApiModelProperty;

public class GamePackageInput {

	@ApiModelProperty(value="加密串",example="")
	private String gameToken;

	public String getGameToken() {
		return gameToken;
	}

	public void setGameToken(String gameToken) {
		this.gameToken = gameToken;
	}
	
	
}
