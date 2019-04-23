package com.game.common.dto;

import com.game.common.enums.CommonEnum;
import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractResultDto implements Serializable {
	protected int status;
	protected String code;
	protected String message;
	private int showState;
    
	public AbstractResultDto() {
		super();
		this.code = CommonEnum.SUCCESS.code();
		status=Integer.valueOf(code);
		this.message = CommonEnum.SUCCESS.message();
	}

	public boolean testSuccess() {
		return CommonEnum.SUCCESS.code().equals(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getShowState() {
		return showState;
	}

	public void setShowState(int showState) {
		this.showState = showState;
	}
	
	public void show(String message) {
		this.message = message;
		this.showState = 1;
	}
	
	
}
