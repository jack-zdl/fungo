package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModelProperty;

public class GameListVO extends InputPageDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("游戏合集id")
	private String gameids;

	private String key;

	private String gameId;

	public String getGameids() {
		return gameids;
	}

	public void setGameids(String gameids) {
		this.gameids = gameids;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
