package com.game.common.dto.mark;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="验证对象",description="预约对象")
public class MakeCheckOut {
	@ApiModelProperty(value="是否同意条款",example="")
	private boolean isClause;
	@ApiModelProperty(value="是否绑定appleId",example="")
	private boolean isBinding;
	@ApiModelProperty(value="是否以预约",example="")
	private boolean isMake;
	public boolean isClause() {
		return isClause;
	}
	public void setClause(boolean isClause) {
		this.isClause = isClause;
	}
	public boolean isBinding() {
		return isBinding;
	}
	public void setBinding(boolean isBinding) {
		this.isBinding = isBinding;
	}
	public boolean isMake() {
		return isMake;
	}
	public void setMake(boolean isMake) {
		this.isMake = isMake;
	}
	
}
