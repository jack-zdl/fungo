package com.game.common.entity.incent;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * <p>
 * 用户分值类账户
 *		积分、经验值等分值
 * </p>
 *
 * @author lzh
 * @since 2018-12-04
 */
@TableName("t_incent_account_score")
public class IncentAccountScore extends Model<IncentAccountScore> {

    private static final long serialVersionUID = 1L;

	private Long id;
	/**
     * 账号类型ID
     */
	@TableField("account_group_id")
	private Long accountGroupId;
    /**
     * 账户号
     */
	@TableField("account_code")
	private String accountCode;
    /**
     * 账户名称
     */
	@TableField("account_name")
	private String accountName;
    /**
     * 会员ID
     */
	@TableField("mb_id")
	private String mbId;
    /**
     * 会员账号名
     */
	@TableField("mb_user_name")
	private String mbUserName;
    /**
     * 可用分值
     */
	@TableField("score_usable")
	private BigDecimal scoreUsable;
    /**
     * 冻结分值
     */
	@TableField("score_freeze")
	private BigDecimal scoreFreeze;
    /**
     * 是否激活
1 激活
2 未激活
     */
	@TableField("is_activate")
	private Integer isActivate;
    /**
     * 是否冻结
1 已冻结
2 未冻结
     */
	@TableField("is_freeze")
	private Integer isFreeze;
    /**
     * 用户设置的支付密码
     */
	@TableField("pay_pwd")
	private String payPwd;
    /**
     * 重置支付密码的手机号
     */
	@TableField("reset_pay_pwd_phone")
	private String resetPayPwdPhone;
    /**
     * 账户权益清零周期
1 季度
2 半年
3 一年
     */
	@TableField("clear_zero_period")
	private Integer clearZeroPeriod;
    /**
     * 账户权益清零时间
     */
	@TableField("clear_zero_time")
	private Date clearZeroTime;
    /**
     * 账号失效日期
     */
	@TableField("disabled_time")
	private Date disabledTime;
    /**
     * 分值账户类型
1 经验值
2  积分
     */
	@TableField("score_type")
	private Integer scoreType;
    /**
     * 扩展字段1
     */
	private String ext1;
    /**
     * 扩展字段2
     */
	private String ext2;
    /**
     * 扩展字段3
     */
	private String ext3;
    /**
     * 数据版本
     */
	@TableField("cas_version")
	private Long casVersion;

	/**
	 * 创建时间
	 */
	@TableField("created_at")
	private Date createdAt;
	/**
	 * 更新时间
	 */
	@TableField("updated_at")
	private Date updatedAt;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAccountGroupId() {
		return accountGroupId;
	}

	public void setAccountGroupId(Long accountGroupId) {
		this.accountGroupId = accountGroupId;
	}

	public String getAccountCode() {
		return accountCode;
	}

	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getMbId() {
		return mbId;
	}

	public void setMbId(String mbId) {
		this.mbId = mbId;
	}

	public String getMbUserName() {
		return mbUserName;
	}

	public void setMbUserName(String mbUserName) {
		this.mbUserName = mbUserName;
	}

	public BigDecimal getScoreUsable() {
		return scoreUsable;
	}

	public void setScoreUsable(BigDecimal scoreUsable) {
		this.scoreUsable = scoreUsable;
	}

	public BigDecimal getScoreFreeze() {
		return scoreFreeze;
	}

	public void setScoreFreeze(BigDecimal scoreFreeze) {
		this.scoreFreeze = scoreFreeze;
	}

	public Integer getIsActivate() {
		return isActivate;
	}

	public void setIsActivate(Integer isActivate) {
		this.isActivate = isActivate;
	}

	public Integer getIsFreeze() {
		return isFreeze;
	}

	public void setIsFreeze(Integer isFreeze) {
		this.isFreeze = isFreeze;
	}

	public String getPayPwd() {
		return payPwd;
	}

	public void setPayPwd(String payPwd) {
		this.payPwd = payPwd;
	}

	public String getResetPayPwdPhone() {
		return resetPayPwdPhone;
	}

	public void setResetPayPwdPhone(String resetPayPwdPhone) {
		this.resetPayPwdPhone = resetPayPwdPhone;
	}

	public Integer getClearZeroPeriod() {
		return clearZeroPeriod;
	}

	public void setClearZeroPeriod(Integer clearZeroPeriod) {
		this.clearZeroPeriod = clearZeroPeriod;
	}

	public Date getClearZeroTime() {
		return clearZeroTime;
	}

	public void setClearZeroTime(Date clearZeroTime) {
		this.clearZeroTime = clearZeroTime;
	}

	public Date getDisabledTime() {
		return disabledTime;
	}

	public void setDisabledTime(Date disabledTime) {
		this.disabledTime = disabledTime;
	}

	public Integer getScoreType() {
		return scoreType;
	}

	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}

	public String getExt1() {
		return ext1;
	}

	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}

	public String getExt2() {
		return ext2;
	}

	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}

	public String getExt3() {
		return ext3;
	}

	public void setExt3(String ext3) {
		this.ext3 = ext3;
	}

	public Long getCasVersion() {
		return casVersion;
	}

	public void setCasVersion(Long casVersion) {
		this.casVersion = casVersion;
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

}
