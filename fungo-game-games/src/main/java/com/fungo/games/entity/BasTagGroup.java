package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 标签组
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
@TableName("t_bas_tag_group")
public class BasTagGroup extends Model<BasTagGroup> {

    private static final long serialVersionUID = 1L;

	@TableId(value = "id",type = IdType.UUID)
	private String id;

	private String type;
    /**
     * 名称
     */
	private String name;
    /**
     * 图标
     */
	private String icon;
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

	@TableField("sort")
	private Integer sort;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
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

}
