package com.fungo.system.service.portal;

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
public interface PortalSystemISeacherService {
	//搜索用户
	public FungoPageResultDto<AuthorBean> searchUsers(String keyword, int page, int limit, String userId) throws Exception;
}
