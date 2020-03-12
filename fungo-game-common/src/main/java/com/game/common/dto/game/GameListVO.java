package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class GameListVO extends InputPageDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModelProperty("游戏合集id")
	private String gameids;

	private List<String> gameList;

	private String key;

	private String gameId;

	private String memberId;

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

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public List<String> getGameList() {
		return gameList;
	}

	public void setGameList(List<String> gameList) {
		this.gameList = gameList;
	}
}
