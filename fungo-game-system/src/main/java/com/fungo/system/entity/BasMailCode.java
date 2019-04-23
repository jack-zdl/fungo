package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author lzh
 * @since 2018-10-10
 */
@TableName("t_bas_mail_code")
public class BasMailCode extends Model<BasMailCode> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 校验码
     */
	@TableField("verify_code")
	private String verifyCode;
    /**
     * 是否使用
     */
	@TableField("is_used")
	private Integer isUsed;
    /**
     * 到期时间
     */
	private Date expiration;
	private Integer state;
	@TableField("member_id")
	private String memberId;
	@TableField("phone_number")
	private String phoneNumber;
	@TableField("created_at")
	private Date createdAt;
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 邮箱地址
     */
	private String email;
    /**
     * 校验码 1:注册 2:修改密码
     */
	@TableField("code_type")
	private String codeType;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVerifyCode() {
		return verifyCode;
	}

	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}

	public Integer getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Integer isUsed) {
		this.isUsed = isUsed;
	}

	public Date getExpiration() {
		return expiration;
	}

	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
