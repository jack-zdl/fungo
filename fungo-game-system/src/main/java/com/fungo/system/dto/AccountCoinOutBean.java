package com.fungo.system.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 用户账户-虚拟币类数据封装
 * </p>
 *
 * @author mxf
 * @since 2018-12-03
 */
public class AccountCoinOutBean implements Serializable {

    /**
     * 账户ID
     */
    private Long id;


    /**
     * 账户类型ID
     */
    private Long accountGroupId;


    /**
     * 账户号
     */
    private String accountCode;


    /**
     * 账户名称
     */
    private String accountName;


    /**
     * 会员ID
     */
    private String mbId;


    /**
     * 会员账号名
     */
    private String mbUserName;


    /**
     * 可用币量
     */
    private BigDecimal coinUsable;


    /**
     * 冻结币量
     */
    private BigDecimal coinFreeze;


    /**
     * 是否激活
     * 1 激活
     * 2 未激活
     */
    private Integer isActivate;


    /**
     * 是否冻结
     * 1 已冻结
     * 2 未冻结
     */
    private Integer isFreeze;


    /**
     * 用户设置的支付密码
     */
    @JsonIgnore
    private String payPwd;


    /**
     * 重置支付密码的手机号
     */
    @JsonIgnore
    private String resetPayPwdPhone;


    /**
     * 账户权益清零周期
     * 1 季度
     * 2 半年
     * 3 一年
     */
    private Integer clearZeroPeriod;


    /**
     * 账户权益清零时间
     */
    private Date clearZeroTime;


    /**
     * 账号失效日期
     */
    private Date disabledTime;



    /**
     * 数据版本
     */
    @JsonIgnore
    private Long casVersion;


    /**
     * 创建时间
     */
    @JsonIgnore
    private Date createdAt;


    /**
     * 更新时间
     */
    @JsonIgnore
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

    public BigDecimal getCoinUsable() {
        return coinUsable;
    }

    public void setCoinUsable(BigDecimal coinUsable) {
        this.coinUsable = coinUsable;
    }

    public BigDecimal getCoinFreeze() {
        return coinFreeze;
    }

    public void setCoinFreeze(BigDecimal coinFreeze) {
        this.coinFreeze = coinFreeze;
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
    public String toString() {
        return "AccountCoinOutBean{" +
                "id=" + id +
                ", accountGroupId=" + accountGroupId +
                ", accountCode='" + accountCode + '\'' +
                ", accountName='" + accountName + '\'' +
                ", mbId='" + mbId + '\'' +
                ", mbUserName='" + mbUserName + '\'' +
                ", coinUsable=" + coinUsable +
                ", coinFreeze=" + coinFreeze +
                ", isActivate=" + isActivate +
                ", isFreeze=" + isFreeze +
                ", payPwd='" + payPwd + '\'' +
                ", resetPayPwdPhone='" + resetPayPwdPhone + '\'' +
                ", clearZeroPeriod=" + clearZeroPeriod +
                ", clearZeroTime=" + clearZeroTime +
                ", disabledTime=" + disabledTime +
                ", casVersion=" + casVersion +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", ext1='" + ext1 + '\'' +
                ", ext2='" + ext2 + '\'' +
                ", ext3='" + ext3 + '\'' +
                '}';
    }
}
