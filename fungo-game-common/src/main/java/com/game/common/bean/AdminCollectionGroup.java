package com.game.common.bean;



import java.util.Date;
import java.util.List;

public class AdminCollectionGroup {

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
	private String isOnline;
	/**
	 * 合集封面地址
	 */
	private String coverPicture;
	/**
	 * 创建时间
	 */
	private Date createdAt;
	/**
	 * 更新时间
	 */
	private Date updatedAt;
	/**
	 * 创建人
	 */
	private String createdBy;
	/**
	 * 修改人
	 */
	private String updatedBy;
	/**
	 * 排序号
	 */
	private Integer sort;
	/**
	 * 游戏数量
	 */
	private Integer number;
	/**
	 * 合集游戏详情
	 */
	private List<CollectionItemBean> list;

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<CollectionItemBean> getList() {
		return list;
	}

	public void setList(List<CollectionItemBean> list) {
		this.list = list;
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

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
