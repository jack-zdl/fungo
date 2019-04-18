package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 短信验证
 * </p>
 *
 * @author lzh
 * @since 2018-04-13
 */
@TableName("t_bas_message_code")
public class MessageCode extends Model<MessageCode> {

    private static final long serialVersionUID = 1L;

	private String id;
	
	private int state;
	@TableField("re_msg")
	private String reMsg;
    /**
     * 消息类型
     */
	@TableField("msg_type")
	private String msgType;
    /**
     * 短信验证码
     */
	@TableField("msg_code")
	private String msgCode;
    /**
     * 手机号
     */
	@TableField("phone_number")
	private String phoneNumber;
    /**
     * 是否已经使用  1:使用，0未使用
     */
	@TableField("is_used")
	private String isUsed;
    /**
     * 到期时间
     */
	private Date expiration;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getReMsg() {
		return reMsg;
	}

	public void setReMsg(String reMsg) {
		this.reMsg = reMsg;
	}

}
