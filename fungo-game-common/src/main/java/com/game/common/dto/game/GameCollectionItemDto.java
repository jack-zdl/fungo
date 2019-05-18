package com.game.common.dto.game;



import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 游戏合集项
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
public class GameCollectionItemDto implements Serializable{



	private String id;
    /**
     * 合集ID
     */
	private String groupId;
    /**
     * 游戏id
     */
	private String gameId;
    /**
     * 创建时间
     */
	private Date createdAt;
    /**
     * 更新时间
     */
	private Date updatedAt;
    /**
     * 显示状态 1:已展示,2:待展示,3:已下线
     */
	private Integer showState;
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
     * 游戏项状态 -1:删除 0:正常
     */
	private Integer state;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
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

	public Integer getShowState() {
		return showState;
	}

	public void setShowState(Integer showState) {
		this.showState = showState;
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

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}
