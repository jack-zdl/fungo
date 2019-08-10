package com.game.common.dto.community;


import com.game.common.api.InputPageDto;

public class PostInputPageDto extends InputPageDto {
	private static final long serialVersionUID = 1L;

	/**
	 * 文章数据行ID
	 */
	private Long rowId;

	/**
	 * 最后更新时间 yyyy-MM-dd HH:mm:ss
	 */
	private String lastUpdateDate;

	private String community_id;


	public String getCommunity_id() {
		return community_id;
	}
	public void setCommunity_id(String community_id) {
		this.community_id = community_id;
	}

	public Long getRowId() {
		return rowId;
	}

	public void setRowId(Long rowId) {
		this.rowId = rowId;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
}
