package com.fungo.games.service;

/**
 * 用户发布,点赞等操作调用
 * @author A
 *
 */
public interface ICounterService {
	//计数器
	public boolean addCounter(String tableType, String fieldType, String id);
	//计数器
	public boolean subCounter(String tableType, String fieldType, String id);
}
