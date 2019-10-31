package com.fungo.system.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 版本渠道表
 * </p>
 *
 * @author lzh
 * @since 2018-10-30
 */
@TableName("t_sys_version_channel")
public class SysVersionChannel extends Model<SysVersionChannel> {

    private static final long serialVersionUID = 1L;
	@TableId(value = "id",type = IdType.UUID)
	private String id;
    /**
     * 版本ID
     */
	@TableField("version_id")
	private String versionId;
    /**
     * 游戏下载开关
     */
	@TableField("game_download_switch")
	private Integer gameDownloadSwitch;
	@TableField("index_banner_switch")
	private Integer indexBannerSwitch;
    /**
     * 渠道代号(001:vivo,002:oppo,003:xiaomi,004:tencent,005:huawei,006:samsung,007:baidu,008:ali)
     */
	@TableField("channel_code")
	private String channelCode;
	@TableField("created_at")
	private Date createdAt;
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 是否强制更新
     */
	@TableField("is_force")
	private Integer isForce;

	/**
	 * 此渠道是否隐藏白金VIP 1 关闭 0 开启
	 * @date: 2019/10/31 11:00
	 */
	@TableField("is_hide")
	private Integer isHide;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
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

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
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

	public Integer getIsForce() {
		return isForce;
	}

	public void setIsForce(Integer isForce) {
		this.isForce = isForce;
	}

	public Integer getIsHide() {
		return isHide;
	}

	public void setIsHide(Integer isHide) {
		this.isHide = isHide;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
