package com.game.common.util.exception;


import com.game.common.enums.IEnum;

public class CommonException extends RuntimeException {
	static final long serialVersionUID = 1L;

	private Integer status;
	private String code;
	private String message;
	private Object data;

	public CommonException() {}

	public CommonException(String code, String message) {
		super(message);
		this.code = code;
	}

	public CommonException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public CommonException(IEnum ienum) {
		super(ienum.message());
		this.status = 0;
		this.code = ienum.code();
		this.message = ienum.message();
	}

	public CommonException(String message) {
		super(message);
	}

	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException(Integer status,String code,String message){
		this.status = status;
		this.code = code;
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
