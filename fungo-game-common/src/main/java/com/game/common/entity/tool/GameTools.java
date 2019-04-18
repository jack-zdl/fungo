package com.game.common.entity.tool;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏工具管理表
如工具软件的管理
 * </p>
 *
 * @author lzh
 * @since 2018-12-14
 */
@TableName("t_game_tools")
public class GameTools extends Model<GameTools> {

    private static final long serialVersionUID = 1L;

	private Integer id;
	@TableField("tools_name")
	private String toolsName;
	@TableField("tools_intro")
	private String toolsIntro;
	@TableField("tools_icon")
	private String toolsIcon;
	private String version;
    /**
     * 工具平台
		1 android
		2 ios
     */
	@TableField("tools_platform")
	private Integer toolsPlatform;
    /**
     * 创建时间
     */
    @JsonIgnore
	@TableField("created_at")
	private Date createdAt;
    /**
     * 更新时间
     */
	@JsonIgnore
	@TableField("updated_at")
	private Date updatedAt;
    /**
     * 创建人
     */
	@JsonIgnore
	@TableField("created_by")
	private String createdBy;
    /**
     * 创建人名称
     */
	@JsonIgnore
	@TableField("created_name")
	private String createdName;


	private String ext1;
	private String ext2;
	private String ext3;

	/**
	 * 工具分类id
	 * 1 VPN
	 * 2 加速器
	 * 3三件套
	 */
	@TableField("tools_type")
	private  Integer toolsType;


	/**
 	 *	工具标题
	 */
	@TableField("type_title")
	private  String  typeTitle;


	/**
	 *	文件保存位置URL
	 */
	@TableField("origin_url")
	private  String originUrl;


	/**
	 *	排序
	 */
	@TableField("sort")
	private Integer sort;

	/**
	 * app的唯一id
	 */
	@TableField("app_id")
	private String appId;


	/**
	 * app软件包的大小
	 * 单位：字节数 KB
	 */
	@TableField("app_size")
	private Long appSize;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToolsName() {
		return toolsName;
	}

	public void setToolsName(String toolsName) {
		this.toolsName = toolsName;
	}

	public String getToolsIntro() {
		return toolsIntro;
	}

	public void setToolsIntro(String toolsIntro) {
		this.toolsIntro = toolsIntro;
	}

	public String getToolsIcon() {
		return toolsIcon;
	}

	public void setToolsIcon(String toolsIcon) {
		this.toolsIcon = toolsIcon;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getToolsPlatform() {
		return toolsPlatform;
	}

	public void setToolsPlatform(Integer toolsPlatform) {
		this.toolsPlatform = toolsPlatform;
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

	public String getCreatedName() {
		return createdName;
	}

	public void setCreatedName(String createdName) {
		this.createdName = createdName;
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

	public Integer getToolsType() {
		return toolsType;
	}

	public void setToolsType(Integer toolsType) {
		this.toolsType = toolsType;
	}

	public String getTypeTitle() {
		return typeTitle;
	}

	public void setTypeTitle(String typeTitle) {
		this.typeTitle = typeTitle;
	}

	public String getOriginUrl() {
		return originUrl;
	}

	public void setOriginUrl(String originUrl) {
		this.originUrl = originUrl;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Long getAppSize() {
		return appSize;
	}

	public void setAppSize(Long appSize) {
		this.appSize = appSize;
	}
}
