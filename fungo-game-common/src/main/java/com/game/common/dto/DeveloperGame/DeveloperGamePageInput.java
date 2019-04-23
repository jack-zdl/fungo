package com.game.common.dto.DeveloperGame;

import com.game.common.api.InputPageDto;

public class DeveloperGamePageInput extends InputPageDto {

	private String key;
	private String gameId;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	
}
