package com.fungo.system.dto;

//@ApiModel(value="push推送模板",description="push推送模板")
public class PushBizMsgTpl<T> {
//    @ApiModelProperty(value="消息类型（1:通知高亮）",example="")
	private int msgPushType;
//    @ApiModelProperty(value="会员id",example="")
	private String memberId;
//    @ApiModelProperty(value="业务类型（同系统中targetType）",example="")
	private int targetType;
//    @ApiModelProperty(value="拓展字段",example="")
	private T data;
	
	
	public int getMsgPushType() {
		return msgPushType;
	}
	public void setMsgPushType(int msgPushType) {
		this.msgPushType = msgPushType;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getTargetType() {
		return targetType;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
}
