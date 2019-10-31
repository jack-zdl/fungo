package com.fungo.games.entity;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 管控台发现合集项
 * </p>
 *
 * @author Carlos
 * @since 2018-06-28
 */
@TableName("t_game_collection_term")
public class FindCollectionItem extends Model<FindCollectionItem> {

    private static final long serialVersionUID = 1L;

	private String id;
    /**
     * 合集ID
     */
	@TableField("form_id")
	private String formId;

	/**
	 * 游戏id
	 */
	@TableField("game_id")
	private String gameId;
    /**
     * 游戏介绍
     */
	private String detail;
    /**
     * 排序号
     */
	@TableField("sort")
	private String sort;
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
	 * 状态
	 */
	private Integer state;

	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
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
