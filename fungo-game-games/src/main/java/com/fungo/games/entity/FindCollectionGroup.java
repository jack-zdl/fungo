package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 发现页游戏合集组
 * </p>
 *
 * @author Carlos
 * @since 2018-06-28
 */
@TableName("t_find_collection_group")
public class FindCollectionGroup extends Model<FindCollectionGroup> {

	private static final long serialVersionUID = 1L;

	private String id;
	/**
	 * 游戏集合名称
	 */
	private String name;
	/**
	 * 状态
	 */
	private Integer state;
	/**
	 * 合集介绍
	 */
	private String detail;
	/**
	 * 是否上线 -1:下线 0:上线
	 */
	@TableField("is_online")
	private String isOnline;
	/**
	 * 合集封面地址
	 */
	@TableField("cover_picture")
	private String coverPicture;
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
	 * 创建人
	 */
	@TableField("created_by")
	private String createdBy;
	/**
	 * 修改人
	 */
	@TableField("updated_by")
	private String updatedBy;
	/**
	 * 排序号
	 */
	@TableField("sort")
	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public String getCoverPicture() {
		return coverPicture;
	}

	public void setCoverPicture(String coverPicture) {
		this.coverPicture = coverPicture;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
