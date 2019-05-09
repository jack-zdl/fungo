package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * app推送设备token数据表
 * </p>
 *
 * @author mxf
 * @since 2019-05-06
 */
@TableName("t_sys_push_token")
public class SysPushToken extends Model<SysPushToken> {

    private static final long serialVersionUID = 1L;

	private Long id;
    /**
     * 设备的唯一标识
     */
	@TableField("device_imei")
	private String deviceImei;
    /**
     * 设备的国际移动用户识别码
     */
	@TableField("device_imsi")
	private String deviceImsi;
    /**
     * 设备的型号
     */
	@TableField("device_model")
	private String deviceModel;
    /**
     * 设备的生产厂商
     */
	@TableField("device_vendor")
	private String deviceVendor;
    /**
     * 设备的唯一标识
     */
	@TableField("device_uuid")
	private String deviceUuid;
    /**
     * 操作系统信息:1  android  ; 2 ios
     */
	private Integer os;
    /**
     * 手机wifi mac地址
     */
	@TableField("device_mac")
	private String deviceMac;
    /**
     * 服务商类型：
1 华为平台
2 小米
3  Oppo 
     */
	@TableField("sb_type")
	private Integer sbType;
    /**
     * 推送服务平台和设备绑定的token值
     */
	@TableField("d_token")
	private String dToken;
    /**
     * 安卓设备的ANDROID_ID
     */
	@TableField("android_id")
	private String androidId;
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
	private String ext1;
	private String ext2;
	private String ext3;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeviceImei() {
		return deviceImei;
	}

	public void setDeviceImei(String deviceImei) {
		this.deviceImei = deviceImei;
	}

	public String getDeviceImsi() {
		return deviceImsi;
	}

	public void setDeviceImsi(String deviceImsi) {
		this.deviceImsi = deviceImsi;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceVendor() {
		return deviceVendor;
	}

	public void setDeviceVendor(String deviceVendor) {
		this.deviceVendor = deviceVendor;
	}

	public String getDeviceUuid() {
		return deviceUuid;
	}

	public void setDeviceUuid(String deviceUuid) {
		this.deviceUuid = deviceUuid;
	}

	public Integer getOs() {
		return os;
	}

	public void setOs(Integer os) {
		this.os = os;
	}

	public String getDeviceMac() {
		return deviceMac;
	}

	public void setDeviceMac(String deviceMac) {
		this.deviceMac = deviceMac;
	}

	public Integer getSbType() {
		return sbType;
	}

	public void setSbType(Integer sbType) {
		this.sbType = sbType;
	}

	public String getDToken() {
		return dToken;
	}

	public void setDToken(String dToken) {
		this.dToken = dToken;
	}

	public String getAndroidId() {
		return androidId;
	}

	public void setAndroidId(String androidId) {
		this.androidId = androidId;
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
