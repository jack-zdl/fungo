package com.game.common.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author lzh
 * @since 2018-07-25
 */
@TableName("t_sys_role_menu")
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 菜单id
     */
	@TableField("menu_id")
	private String menuId;
    /**
     * 角色ID
     */
	@TableField("role_id")
	private String roleId;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
