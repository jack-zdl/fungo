package com.game.common.dto.mark;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="邀请对象",description="预约对象")
public class MakeInputPageDto extends InputPageDto {
	private static final long serialVersionUID = 1L;
	@ApiModelProperty(value="游戏id",example="")
	private String gameId;

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
