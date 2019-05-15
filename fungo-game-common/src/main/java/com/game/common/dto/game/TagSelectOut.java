package com.game.common.dto.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagSelectOut {

	private List<Map<String,Object>> tagList = new ArrayList<>();
	private List<Map<String,String>> selectedList = new ArrayList<>();
	public List<Map<String, Object>> getTagList() {
		return tagList;
	}
	public void setTagList(List<Map<String, Object>> tagList) {
		this.tagList = tagList;
	}
	public List<Map<String, String>> getSelectedList() {
		return selectedList;
	}
	public void setSelectedList(List<Map<String, String>> selectedList) {
		this.selectedList = selectedList;
	}
	
	
}
