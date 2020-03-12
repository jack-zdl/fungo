package com.fungo.system.controller;

import com.alibaba.fastjson.JSONObject;
import com.fungo.system.dto.DeviceInput;
import com.fungo.system.dto.PushBizMsgTpl;
import com.fungo.system.dto.PushDemo;
import com.fungo.system.service.IPushService;
import com.game.common.dto.MemberUserProfile;
import com.game.common.dto.ResultDto;
import com.game.common.util.annotation.Anonymous;
import com.game.common.util.annotation.MD5ParanCheck;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import java.text.ParseException;

@RestController
@Api(value="",description="推送")
public class  PushController {
	@Autowired
	private IPushService pushService;
	
	@ApiOperation(value="绑定设备", notes="")
	@RequestMapping(value="/api/push/bind", method= RequestMethod.POST)
	@ApiImplicitParams({})
	@MD5ParanCheck()
	public ResultDto<String> bindDevice(MemberUserProfile memberUserPrefile, @RequestBody DeviceInput input) throws Exception {
		return pushService.bindDevice(memberUserPrefile.getLoginId(), input);
	}

	@ApiOperation(value="解绑设备", notes="")
	@RequestMapping(value="/api/push/unbind", method= RequestMethod.POST)
	@ApiImplicitParams({})
	@MD5ParanCheck()
	public ResultDto<String> unbindDevice(MemberUserProfile memberUserPrefile,@RequestBody DeviceInput input) throws Exception {
		return pushService.unbindById(memberUserPrefile.getLoginId(), input.getDeviceId());
	}
	
	@ApiOperation(value="推送返回(非调用)", notes="")
	@RequestMapping(value="/api/push/info", method= RequestMethod.POST)
	@ApiImplicitParams({})
	@MD5ParanCheck()
	public PushBizMsgTpl<PushDemo> pushReturn(MemberUserProfile memberUserPrefile, @RequestBody DeviceInput input) throws Exception {
		
		return null;
	}
	
//	@RequestMapping(value="/api/push/controlpush", method= RequestMethod.POST)
	public ResultDto<String> getCommunityStatistics(@Anonymous MemberUserProfile memberUserPrefile, @RequestBody JSONObject map) throws ParseException {
				
//		System.out.println("收到请求...");
//		System.out.println(map.get("personName"));
		
		pushService.controlPush(map);
		
		return ResultDto.success();		
		
	}
	
}
