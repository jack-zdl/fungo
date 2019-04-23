package com.game.common.dto;

public class Trait {
//	map.put("trait1", "画面");
//	map.put("trait2", "音乐");
//	map.put("trait3", "氪金");
//	map.put("trait4", "剧情");
//	map.put("trait5", "玩法");

	private String key;
	private String value;
	
	public String getKey() {
		return key;
	}
	public String getValue() {
		return value;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Trait(String key, String value) {
		this.key = key;
		this.value = value;
	}
	
	
	
	
}
