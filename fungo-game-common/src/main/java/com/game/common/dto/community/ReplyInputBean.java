package com.game.common.dto.community;

import io.swagger.annotations.ApiModelProperty;

public class ReplyInputBean {
	private String content;
	private String target_id;
	private int target_type;
	@ApiModelProperty(value="(2.4.3)回复会员id",example="")
	private String reply_to;
	@ApiModelProperty(value="(2.4.3)被回复的二级回复id,如果没有不传",example="")
	private String reply_to_content_id;
	
	private String os;
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public String getReply_to() {
		return reply_to;
	}
	public void setReply_to(String reply_to) {
		this.reply_to = reply_to;
	}
	public int getTarget_type() {
		return target_type;
	}
	public void setTarget_type(int target_type) {
		this.target_type = target_type;
	}
	public String getReply_to_content_id() {
		return reply_to_content_id;
	}
	public void setReply_to_content_id(String reply_to_content_id) {
		this.reply_to_content_id = reply_to_content_id;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}

	
}
