package com.game.common.entity;

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
 * @since 2018-09-26
 */
@TableName("t_sys_version")
public class SysVersion extends Model<SysVersion> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 版本号
     */
	private String version;
    /**
     * 是否强制更新 1:是 0:否
     */
	@TableField("is_force")
	private Integer isForce;
    /**
     * 手机型号 IOS Android
     */
	@TableField("mobile_type")
	private String mobileType;
	private String intro;
	@TableField("created_at")
	private Date createdAt;
	@TableField("updated_at")
	private Date updatedAt;
	@TableField("created_by")
	private String createdBy;
	private Integer state;
	@TableField("apk_url")
	private String apkUrl;
	@TableField("game_download_switch")
	private Integer gameDownloadSwitch;
	@TableField("index_banner_switch")
	private Integer indexBannerSwitch;
    /**
     * 内部版本号
     */
	private String code;
    /**
     * 是否最新版本
     */
	@TableField("new_version")
	private Integer newVersion;

	/**
	 * 是否需要授权
	 * 1  需登录
	 * 2 非登录
	 */
	@TableField("is_auth")
	private Integer isAuth;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getIsForce() {
		return isForce;
	}

	public void setIsForce(Integer isForce) {
		this.isForce = isForce;
	}

	public String getMobileType() {
		return mobileType;
	}

	public void setMobileType(String mobileType) {
		this.mobileType = mobileType;
	}

	public String getIntro() {
		return intro;
	}

	public void setIntro(String intro) {
		this.intro = intro;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public Integer getGameDownloadSwitch() {
		return gameDownloadSwitch;
	}

	public void setGameDownloadSwitch(Integer gameDownloadSwitch) {
		this.gameDownloadSwitch = gameDownloadSwitch;
	}

	public Integer getIndexBannerSwitch() {
		return indexBannerSwitch;
	}

	public void setIndexBannerSwitch(Integer indexBannerSwitch) {
		this.indexBannerSwitch = indexBannerSwitch;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getNewVersion() {
		return newVersion;
	}

	public void setNewVersion(Integer newVersion) {
		this.newVersion = newVersion;
	}


	public Integer getIsAuth() {
		return isAuth;
	}

	public void setIsAuth(Integer isAuth) {
		this.isAuth = isAuth;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
