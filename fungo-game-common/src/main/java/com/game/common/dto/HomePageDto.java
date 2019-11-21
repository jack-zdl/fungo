package com.game.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class HomePageDto {

	private String id;
	/**
	 * 游戏id
	 */
	private String gameId;
	/**
	 * 状态列表
	 */
	private List<String> totalStates;
	/**
	 * 状态列表
	 */
	private List<String> states;

	private int startOffset;

	private int pageSize;

}
