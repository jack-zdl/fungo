package com.fungo.system.service;


import com.fungo.system.dto.ThirdLoginInput;
import com.game.common.dto.ResultDto;


public interface ThirdLoginService {
	/**
	 * 验证用户是否合法
	 * @param platformType
	 * @param accessToken
	 * @param openid
	 * @return
	 */
	ResultDto<String> checkThirdParty(Integer platformType, String accessToken, String openid, String channel)throws Exception;
	ResultDto<ThirdLoginInput> sinaVerify(String host, String code) throws Exception;
	ResultDto<ThirdLoginInput> qqVerify(String host, String code) throws Exception;
	ResultDto<ThirdLoginInput> wxVerify(String host, String code) throws Exception;
	
}
