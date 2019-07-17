package com.fungo.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.game.common.dto.AuthorBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.search.SearCount;

import java.io.IOException;

/**
 * 搜索方法
 * @author 
 *
 */
public interface ISeacherService {

	//热门游戏
	public ResultDto getKeywords() throws JsonProcessingException, IOException;


	//搜索用户
	public FungoPageResultDto<AuthorBean> searchUsers(String keyword, int page, int limit, String userId) throws Exception;

	//搜索结果数量
	public ResultDto<SearCount> getSearchCount(String keyword);

	public void updateGameKeywords();
}
