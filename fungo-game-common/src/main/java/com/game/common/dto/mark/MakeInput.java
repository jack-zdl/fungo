package com.game.common.dto.mark;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="预约对象",description="预约对象")
public class MakeInput {
	@ApiModelProperty(value="游戏id",example="")
	private String gameId;
	@ApiModelProperty(value="手机型号 （Ios，Android）",example="")
	private String phoneModel;
	
	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}
	
}
