package com.game.common.dto.game;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModelProperty;

public class MermberSearchInput extends InputPageDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="用户id",example="")
	private String memberId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	
	

}
