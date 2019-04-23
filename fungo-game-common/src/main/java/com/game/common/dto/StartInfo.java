package com.game.common.dto;

public class StartInfo {

//	private final int 不会再玩 = 1;
//	private final int 很烂 = 2;
//	private final int 极差 = 3;
//	private final int 较差 = 4;
//	private final int 有待观察 = 5;
//	private final int 可以尝试 = 6;
//	private final int 不错= 7;
//	private final int 好玩 = 8;
//	private final int 推荐下载 = 9;
//	private final int 强烈安利 = 10;
	
	private String key;
	private int value;
	
	public StartInfo(String key, int value) {
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public int getValue() {
		return value;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public void setValue(int value) {
		this.value = value;
	}
	
	
	
}
