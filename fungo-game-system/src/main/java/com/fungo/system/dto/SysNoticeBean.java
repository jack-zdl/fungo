package com.fungo.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="系统消息对象",description="系统消息对象")
public class SysNoticeBean {
    @ApiModelProperty(value="消息id",example="")
	private String msgId;
//	private boolean isRead;
    @ApiModelProperty(value="用户id",example="")
	private String userId;//id
    @ApiModelProperty(value="用户类型 0 普通用户，1官方用户",example="")
	private int userType;//
    @ApiModelProperty(value="用户名称",example="")
	private String userName;//
    @ApiModelProperty(value="用户头像",example="")
	private String userAvatar;
    @ApiModelProperty(value="消息体",example="")
	private String content; //消息体
    @ApiModelProperty(value="跳转类型 (1内部跳转;2外连接;3:只做高亮)",example="")
	private int actionType;//
    @ApiModelProperty(value="跳转资源类型",example="")
	private int targetType;//业务类型
    @ApiModelProperty(value="业务id",example="")
	private String targetId;//业务id
    @ApiModelProperty(value="外链地址",example="")
	private String href;
    @ApiModelProperty(value="消息时间",example="")
	private String msgTime;
    
    @ApiModelProperty(value="视频地址",example="")
    private String video;
	@ApiModelProperty(value="父级id",example="")
    private String parentId;

	public String getMsgId() {
		return msgId;
	}
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserAvatar() {
		return userAvatar;
	}
	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getActionType() {
		return actionType;
	}
	public void setActionType(int actionType) {
		this.actionType = actionType;
	}
	public int getTargetType() {
		return targetType;
	}
	public void setTargetType(int targetType) {
		this.targetType = targetType;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getVideo() {
		return video;
	}
	public void setVideo(String video) {
		this.video = video;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
}