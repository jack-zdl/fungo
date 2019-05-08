package com.fungo.system.dto;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="我的时间线输入对象",description="我的时间线输入对象")
public class TimeLineInputPage extends InputPageDto {
	private static final long serialVersionUID = 1L;
    @ApiModelProperty(value="用户id",example="")
	private String cardId;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

}
