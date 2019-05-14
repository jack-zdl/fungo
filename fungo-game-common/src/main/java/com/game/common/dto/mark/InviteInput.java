package com.game.common.dto.mark;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="邀请游戏评论对象",description="邀请游戏评论对象")
public class InviteInput {
	@ApiModelProperty(value="游戏id",example="")
	private String gameId;
	
	@ApiModelProperty(value="用户id",example="")
	private String memberId;

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
	
}
