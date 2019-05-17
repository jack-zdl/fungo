package com.fungo.community.service;

import com.game.common.dto.ActionInput;

/**
 * 用户发布,点赞等操作调用
 * @author A
 *
 */
public interface ICounterService {

	//计数器
	public boolean addCounter(String tableType, String fieldType, String id);

	//计数器 减数
	public boolean subCounter(String tableType, String fieldType, String id);

	//表字段 减数
	public boolean subCounter(String memberId, int type, ActionInput inputDto);
}
