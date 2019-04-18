package com.game.common.entity.incent;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 用户账户之积分账户种类
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@TableName("t_incent_account_group")
public class IncentAccountGroup extends Model<IncentAccountGroup> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 账户类型名称
     */
	@TableField("account_group_name")
	private String accountGroupName;
    /**
     * 1 启用
2 停用
     */
	@TableField("is_active")
	private Integer isActive;
    /**
     * 账户类型简介
     */
	@TableField("account_group_intro")
	private String accountGroupIntro;
    /**
     * 账户类型来源
如：funGo平台，其他平台
1 fungo平台
2 其他平台
     */
	@TableField("account_group_origin")
	private Integer accountGroupOrigin;
    /**
     * 来源平台名称
     */
	@TableField("orgin_name")
	private String orginName;
    /**
     * 账户类型
1 分值类
2 虚拟币类
     */
	@TableField("account_type")
	private Integer accountType;
    /**
     * 虚拟币与人民币货币兑换率
     */
	@TableField("rmb_conv_rate")
	private BigDecimal rmbConvRate;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAccountGroupName() {
		return accountGroupName;
	}

	public void setAccountGroupName(String accountGroupName) {
		this.accountGroupName = accountGroupName;
	}

	public Integer getIsActive() {
		return isActive;
	}

	public void setIsActive(Integer isActive) {
		this.isActive = isActive;
	}

	public String getAccountGroupIntro() {
		return accountGroupIntro;
	}

	public void setAccountGroupIntro(String accountGroupIntro) {
		this.accountGroupIntro = accountGroupIntro;
	}

	public Integer getAccountGroupOrigin() {
		return accountGroupOrigin;
	}

	public void setAccountGroupOrigin(Integer accountGroupOrigin) {
		this.accountGroupOrigin = accountGroupOrigin;
	}

	public String getOrginName() {
		return orginName;
	}

	public void setOrginName(String orginName) {
		this.orginName = orginName;
	}

	public Integer getAccountType() {
		return accountType;
	}

	public void setAccountType(Integer accountType) {
		this.accountType = accountType;
	}

	public BigDecimal getRmbConvRate() {
		return rmbConvRate;
	}

	public void setRmbConvRate(BigDecimal rmbConvRate) {
		this.rmbConvRate = rmbConvRate;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
