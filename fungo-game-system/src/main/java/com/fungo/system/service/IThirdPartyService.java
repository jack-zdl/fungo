package com.fungo.system.service;

import com.fungo.system.dto.BindInfo;
import com.fungo.system.dto.ThirdLoginInput;
import com.fungo.system.entity.LoginMemberBean;
import com.game.common.dto.ResultDto;

import java.util.List;

public interface IThirdPartyService {
	//第三方登录
	public ResultDto<LoginMemberBean> thirdPartyLogin(ThirdLoginInput input, String channel, String appVersion ,String deviceId) throws Exception;

	//绑定第三方账号
	public ResultDto<String> thirdUserBind(String userId, ThirdLoginInput input, String channel) throws Exception;

	//解除第三方绑定
	public ResultDto<String> thirdUserUnbind(String userId, Integer platformType);

	//第三方绑定信息
	public ResultDto<List<BindInfo>> thirdUserUnbind(String userId);
}
