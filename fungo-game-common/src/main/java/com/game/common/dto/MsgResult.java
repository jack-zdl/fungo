package com.game.common.dto;

public class MsgResult {
	private String code;
	private String msg;
	private boolean isSuccess;
	public static MsgResult success(){
		MsgResult mre=new MsgResult();
		mre.setSuccess(true);
		return mre;
	}
	public static MsgResult error(String msg){
		MsgResult mre=new MsgResult();
		mre.setSuccess(false);
		mre.setMsg(msg);
		return mre;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	
}
