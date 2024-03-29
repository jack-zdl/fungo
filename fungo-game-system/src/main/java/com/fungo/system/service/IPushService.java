package com.fungo.system.service;

import com.alibaba.fastjson.JSONObject;
import com.fungo.system.dto.DeviceInput;
import com.game.common.dto.ResultDto;

public interface IPushService {

	public void push(String memberId, int targetType, String appVersion) throws Exception;

	public ResultDto<String> unbindById(String memberId, String deviceId);

	public ResultDto<String> bindDevice(String memberId, DeviceInput input);

	public ResultDto<String> controlPush(JSONObject map);
}
