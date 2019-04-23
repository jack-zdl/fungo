package com.fungo.system.dto;

import com.game.common.api.InputPageDto;
import io.swagger.annotations.ApiModelProperty;

public class AccountRecordInput extends InputPageDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value="账户类型 1:获取 2:消耗",example="")
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	

}
