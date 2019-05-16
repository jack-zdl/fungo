package com.game.common.dto.game;

public class TagInput {
	private static final long serialVersionUID = 1L;
	private String[] idList;
	private String gameId;
	private String filter;
	private String tagRelId;
	private Integer attitude;
	
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public String getTagRelId() {
		return tagRelId;
	}
	public void setTagRelId(String tagRelId) {
		this.tagRelId = tagRelId;
	}
	public Integer getAttitude() {
		return attitude;
	}
	public void setAttitude(Integer attitude) {
		this.attitude = attitude;
	}
	public String[] getIdList() {
		return idList;
	}
	public void setIdList(String[] idList) {
		this.idList = idList;
	}

	
	
}
