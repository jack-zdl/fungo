package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 开发者信息
 * </p>
 *
 * @author lzh
 * @since 2018-06-08
 */
@TableName("t_developer")
public class Developer extends Model<Developer> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 公司名称
     */
	@TableField("company_name")
	private String companyName;
    /**
     * 联系人名称
     */
	@TableField("liaison_name")
	private String liaisonName;
    /**
     * 联系人手机
     */
	@TableField("liaison_phone")
	private String liaisonPhone;
    /**
     * 联系人邮箱
     */
	@TableField("liaison_email")
	private String liaisonEmail;
    /**
     * 联系人地址
     */
	@TableField("liaison_adress")
	private String liaisonAdress;
    /**
     * 联系人身份证
     */
	@TableField("liaisonId_number")
	private String liaisonIdNumber;
    /**
     * 身份证正面
     */
	@TableField("liaison_id_image_front")
	private String liaisonIdImageFront;
    /**
     * 身份证反面
     */
	@TableField("liaison_id_image_back")
	private String liaisonIdImageBack;
    /**
     * 工作室全称
     */
	@TableField("company_full_name")
	private String companyFullName;
    /**
     * 工作室简称
     */
	@TableField("company_short_name")
	private String companyShortName;
    /**
     * logo
     */
	private String logo;
    /**
     * 营业执照注册号
     */
	@TableField("business_license")
	private String businessLicense;
    /**
     * 营业执照注册图片
     */
	@TableField("business_license_image")
	private String businessLicenseImage;
    /**
     * 1.个人，2企业
     */
	private Integer type;
    /**
     * 网文经营许可证
     */
	@TableField("business_permit")
	private String businessPermit;
    /**
     * 网文经营许可图片
     */
	@TableField("business_permit_image")
	private String businessPermitImage;
    /**
     * 网文经营许可到期日期
     */
	@TableField("business_permit_limit_date")
	private Date businessPermitLimitDate;
    /**
     * 状态
     */
	private Integer state;
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
     * 审批状态
     */
	@TableField("approve_state")
	private Integer approveState;
    /**
     * 审批意见
     */
	@TableField("approve_info")
	private String approveInfo;
    /**
     * 审批时间
     */
	@TableField("approve_at")
	private Date approveAt;
    /**
     * 审批人
     */
	@TableField("approve_by")
	private String approveBy;
    /**
     * 审批人ID
     */
	@TableField("approve_id")
	private String approveId;
    /**
     * 会员ID
     */
	@TableField("member_id")
	private String memberId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLiaisonName() {
		return liaisonName;
	}

	public void setLiaisonName(String liaisonName) {
		this.liaisonName = liaisonName;
	}

	public String getLiaisonPhone() {
		return liaisonPhone;
	}

	public void setLiaisonPhone(String liaisonPhone) {
		this.liaisonPhone = liaisonPhone;
	}

	public String getLiaisonEmail() {
		return liaisonEmail;
	}

	public void setLiaisonEmail(String liaisonEmail) {
		this.liaisonEmail = liaisonEmail;
	}

	public String getLiaisonAdress() {
		return liaisonAdress;
	}

	public void setLiaisonAdress(String liaisonAdress) {
		this.liaisonAdress = liaisonAdress;
	}

	public String getLiaisonIdNumber() {
		return liaisonIdNumber;
	}

	public void setLiaisonIdNumber(String liaisonIdNumber) {
		this.liaisonIdNumber = liaisonIdNumber;
	}

	public String getLiaisonIdImageFront() {
		return liaisonIdImageFront;
	}

	public void setLiaisonIdImageFront(String liaisonIdImageFront) {
		this.liaisonIdImageFront = liaisonIdImageFront;
	}

	public String getLiaisonIdImageBack() {
		return liaisonIdImageBack;
	}

	public void setLiaisonIdImageBack(String liaisonIdImageBack) {
		this.liaisonIdImageBack = liaisonIdImageBack;
	}

	public String getCompanyFullName() {
		return companyFullName;
	}

	public void setCompanyFullName(String companyFullName) {
		this.companyFullName = companyFullName;
	}

	public String getCompanyShortName() {
		return companyShortName;
	}

	public void setCompanyShortName(String companyShortName) {
		this.companyShortName = companyShortName;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getBusinessLicenseImage() {
		return businessLicenseImage;
	}

	public void setBusinessLicenseImage(String businessLicenseImage) {
		this.businessLicenseImage = businessLicenseImage;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBusinessPermit() {
		return businessPermit;
	}

	public void setBusinessPermit(String businessPermit) {
		this.businessPermit = businessPermit;
	}

	public String getBusinessPermitImage() {
		return businessPermitImage;
	}

	public void setBusinessPermitImage(String businessPermitImage) {
		this.businessPermitImage = businessPermitImage;
	}

	public Date getBusinessPermitLimitDate() {
		return businessPermitLimitDate;
	}

	public void setBusinessPermitLimitDate(Date businessPermitLimitDate) {
		this.businessPermitLimitDate = businessPermitLimitDate;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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

	public Integer getApproveState() {
		return approveState;
	}

	public void setApproveState(Integer approveState) {
		this.approveState = approveState;
	}

	public String getApproveInfo() {
		return approveInfo;
	}

	public void setApproveInfo(String approveInfo) {
		this.approveInfo = approveInfo;
	}

	public Date getApproveAt() {
		return approveAt;
	}

	public void setApproveAt(Date approveAt) {
		this.approveAt = approveAt;
	}

	public String getApproveBy() {
		return approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	public String getApproveId() {
		return approveId;
	}

	public void setApproveId(String approveId) {
		this.approveId = approveId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
