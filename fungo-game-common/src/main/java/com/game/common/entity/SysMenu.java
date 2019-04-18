package com.game.common.entity;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 菜单表
 * </p>
 *
 * @author lzh
 * @since 2018-07-25
 */
@TableName("t_sys_menu")
public class SysMenu extends Model<SysMenu> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 父类编码
     */
	@TableField("parent_code")
	private String parentCode;
    /**
     * 父类节点路径
     */
	@TableField("parent_codes")
	private String parentCodes;
    /**
     * 排序
     */
	@TableField("tree_sort")
	private Integer treeSort;
    /**
     * 是否叶子节点  1末尾节点
     */
	@TableField("tree_leaf")
	private Integer treeLeaf;
    /**
     * 层
     */
	@TableField("tree_level")
	private Integer treeLevel;
    /**
     * 名称
     */
	@TableField("tree_names")
	private String treeNames;
    /**
     * 菜单名称
     */
	private String name;
    /**
     * 菜单代码
     */
	@TableField("menu_code")
	private String menuCode;
    /**
     * 菜单类型，1菜单，2权限
     */
	private Integer type;
    /**
     * 菜单跳转地址
     */
	private String href;
    /**
     * 目标
     */
	private String target;
    /**
     * 菜单图标
     */
	private String icon;
    /**
     * 权限码
     */
	private String permission;
    /**
     * 是否显示，1显示，0不显示
     */
	@TableField("is_show")
	private Integer isShow;
    /**
     * 备注
     */
	private String remarks;
    /**
     * 状态
     */
	private Integer state;
    /**
     * 次方
     */
	private String power;
    /**
     * 次方组
     */
	@TableField("power_group")
	private Integer powerGroup;
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


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getParentCodes() {
		return parentCodes;
	}

	public void setParentCodes(String parentCodes) {
		this.parentCodes = parentCodes;
	}

	public Integer getTreeSort() {
		return treeSort;
	}

	public void setTreeSort(Integer treeSort) {
		this.treeSort = treeSort;
	}

	public Integer getTreeLeaf() {
		return treeLeaf;
	}

	public void setTreeLeaf(Integer treeLeaf) {
		this.treeLeaf = treeLeaf;
	}

	public Integer getTreeLevel() {
		return treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	public String getTreeNames() {
		return treeNames;
	}

	public void setTreeNames(String treeNames) {
		this.treeNames = treeNames;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public Integer getPowerGroup() {
		return powerGroup;
	}

	public void setPowerGroup(Integer powerGroup) {
		this.powerGroup = powerGroup;
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
