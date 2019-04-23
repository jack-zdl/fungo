package com.fungo.system.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="开发者",description="开发者")
public class DeveloperBean {
	@ApiModelProperty(value="名称 ",example="")
	private String companyName;// 名称  
	@ApiModelProperty(value=" 联系人名称   ",example="")
	private String liaisonName;//
	@ApiModelProperty(value="是 联系人手机  ",example="")
	private String liaisonPhone;//
	@ApiModelProperty(value="是 联系人邮箱   ",example="")
	private String liaisonEmail;// string 
	@ApiModelProperty(value="联系人地址   ",example="")
	private String liaisonAdress;// 
	@ApiModelProperty(value="是 联系人身份证  ",example="")
	private String liaisonIdNumber;// string 
	@ApiModelProperty(value="身份证正面   ",example="")
	private String liaisonIdImageFront;// string 是 身份证正面  
	@ApiModelProperty(value="身份证反面  ",example="")
	private String liaisonIdImageBack;// string 是 身份证反面  
	@ApiModelProperty(value=" 工作室全称   ",example="")
	private String companyFullName;// string 是 工作室全称  
	@ApiModelProperty(value="是 工作室简称  ",example="")
	private String companyShortName;// string 是 工作室简称  
	@ApiModelProperty(value="logo ",example="")
	private String logo;// string 是   
	@ApiModelProperty(value="否 营业执照注册号  ",example="")
	private String businessLicense;// string 否 营业执照注册号  
	@ApiModelProperty(value="营业执照注册图片  ",example="")
	private String businessLicenseImage;// string 否 营业执照注册图片  
	@ApiModelProperty(value=" ",example="")
	private String type;// number 是   
	@ApiModelProperty(value=" 网文经营许可证 ",example="")
	private String businessPermit;// string 是 网文经营许可证  
	@ApiModelProperty(value="网文经营许可图片   ",example="")
	private String businessPermitImage;// string 是 网文经营许可图片  
	@ApiModelProperty(value="网文经营许可到期日期 ",example="")
	private String businessPermitLimitDate;// date 是 网文经营许可到期日期 
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
	public String getType() {
		return type;
	}
	public void setType(String type) {
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
	public String getBusinessPermitLimitDate() {
		return businessPermitLimitDate;
	}
	public void setBusinessPermitLimitDate(String businessPermitLimitDate) {
		this.businessPermitLimitDate = businessPermitLimitDate;
	}
	

}
