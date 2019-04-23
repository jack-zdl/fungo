package com.fungo.system.service.impl;

import com.alibaba.fastjson.JSONObject;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.dto.DeviceInput;
import com.fungo.system.entity.MemberDevice;
import com.fungo.system.service.IMemberNoticeService;
import com.fungo.system.service.IMemberService;
import com.fungo.system.service.IPushService;
import com.fungo.system.service.MemberDeviceService;
import com.game.common.dto.ResultDto;
import com.game.common.util.AesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class PushServiceImpl implements IPushService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PushServiceImpl.class);
	
	@Value("${fungo.api.sKey}")
  	private String key;
	
	
//	@Autowired
//	private PushConfig pushConfig;
	@Autowired
	private MemberDeviceService deviceService;
//	@Autowired
//	private IAliyunPushServices pushService;
	@Autowired
	private IMemberService memberService;

	//@Autowired
	//private IWebPushMessage wsPushMessage;

	@Autowired
	private IMemberNoticeService iMemberNoticeService;

	public DefaultAcsClient initConfig() {
		String accessKeyId = "" ; // pushConfig.getAccessKeyId();
		String accessKeySecret = "" ; // pushConfig.getAccessKeySecret();
		Long appKey = (long) 25040749;
		String regionId = "" ; // pushConfig.getRegionId();
		
		IClientProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
		DefaultAcsClient client = new DefaultAcsClient(profile);
		
		return client;
	}
	
	@Override
	public void push(String memberId,int targetType,String appVersion) throws Exception {
//		List<MemberDevice> list = deviceService.selectList(new EntityWrapper<MemberDevice>().eq("member_id",memberId).eq("state", 1));
//		if(list.size() > 0) {
//			List<DeviceBean> devices = new ArrayList<>();
//			for(MemberDevice device:list) {
//				DeviceBean b = new DeviceBean();
//				b.setDeviceId(device.getDeviceId());
//				b.setDeviceType(device.getDeviceType());
//				devices.add(b);
//			}
//			ResultDto<Map<String, Object>> unReadNotice = memberService.getUnReadNotice(memberId);
//			
//			PushBizMsgTpl<Map<String,Object>> msg= new PushBizMsgTpl<>();
//			msg.setMemberId(memberId);
//			msg.setTargetType(targetType);
//			msg.setMsgPushType(1);
//			msg.setData(unReadNotice.getData());
//			ObjectMapper mapper = new ObjectMapper();
//			String message = mapper.writeValueAsString(msg);
//			
//			wsPushMessage.sendMessage(memberId, "007", msg);
//			pushService.pushToDevice(devices,"Fungo",message);
//		}
		
		 Map<String, Object> unReadNoticeMap = memberService.getUnReadNotice(memberId,appVersion);
		if (null == unReadNoticeMap || unReadNoticeMap.isEmpty()){
			return;
		}

		/*
		PushBizMsgTpl<Map<String,Object>> msg = new PushBizMsgTpl<>();
		msg.setMemberId(memberId);
		msg.setTargetType(targetType);
		msg.setMsgPushType(1);
		msg.setData(unReadNotice.getData());


//		ObjectMapper mapper = new ObjectMapper();
//		String message = mapper.writeValueAsString(msg);
*/

		unReadNoticeMap.put("memberId", memberId);
		unReadNoticeMap.put("targetType", targetType);
		unReadNoticeMap.put("msgPushType", 1);



		//把推送给用户的数据保存到数据库临时存储
		iMemberNoticeService.addMbNotice(memberId, 7, unReadNoticeMap );
		//wsPushMessage.sendMessage(memberId, MessageUtils.msgType_007, msg);
	}
	

	
	@Transactional
	@Override
	public ResultDto<String> bindDevice(String memberId, DeviceInput input) {
		MemberDevice dv1 = deviceService.selectOne(new EntityWrapper<MemberDevice>().eq("member_id",memberId).eq("device_id", input.getDeviceId()));
		List<MemberDevice> vlist = deviceService.selectList(new EntityWrapper<MemberDevice>().eq("device_id",input.getDeviceId()));
		if(dv1 == null) {//如果数据库没记录
			if(vlist.size() > 0) {//将这个设备其它的用户解绑
				unbindDevice(vlist);
			}
			MemberDevice dv = new MemberDevice();
			dv.setDeviceId(input.getDeviceId());
			dv.setMemberId(memberId);
			Date date = new Date();
			dv.setCreatedAt(date);
			dv.setUpdatedAt(date);
			dv.setAccountId(input.getAccountId());
			dv.setDeviceType(input.getPhoneModel());
			dv.setState(1);
			deviceService.insert(dv);
		}else if(dv1.getState() == 0) {//如果这个设备与用户已经解绑
			if(vlist.size() > 0) {
				unbindDevice(vlist);
			}
			dv1.setState(1);
			deviceService.updateById(dv1);
		}
		return ResultDto.success();
	}
	
	@Transactional
	public ResultDto<String> unbindDevice(List<MemberDevice> list) {
		for(MemberDevice device:list) {
			device.setState(0);
		}
		deviceService.updateBatchById(list);
		return ResultDto.success();
//		MemberDevice dv1 = deviceService.selectOne(new EntityWrapper<MemberDevice>().eq("memberId",memberId).eq("deviceId", deviceId));
	}
	
	@Override
	@Transactional
	public ResultDto<String> unbindById(String memberId,String deviceId) {
		MemberDevice dv = deviceService.selectOne(new EntityWrapper<MemberDevice>().eq("member_id",memberId).eq("device_id", deviceId));
		if(dv != null) {
			dv.setState(0);
			deviceService.updateById(dv);
		}
		return ResultDto.success();
	}

	@Override
	public ResultDto<String> controlPush(JSONObject map) {
		
		
		//注意空指针
		String targetId = "";
		Integer targetType = -1;
		String memberId = "";
		String sign = "";
		Integer action = -1;
		
		if(map.get("targetId") == null) {
			return null;
		}else {
			targetId = (String) map.get("targetId");
			
		}
		
		if(map.get("targetType") == null) {
			return null;
		}else {
			targetType = (Integer) map.get("targetType");
			
		}
		
		if(map.get("sign") == null) {
			return null;
		}else {
			sign = (String) map.get("sign");
		}
		
		if(map.get("memberId") == null) {
			return null;
		}else {
			memberId = (String) map.get("memberId");
			
		}
		
//		if(map.get("action") == null) {
//			return null;
//		}else {
//			action = (Integer) map.get("action");
//			
//		}
		LOGGER.info("管控台向用户发送通知 用户id-memberId:{}", memberId);
		
		//加密对比
		try {
			String encrypt = AesUtil.getInstance().encrypt((String)map.get("targetId")+map.get("targetType")+key);
			if(!sign.equals(encrypt)) {
				LOGGER.info("向用户发送通知失败,加密串不符合! 用户id-memberId:{}", memberId);
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//判定通过 发消息
		try {
			//版本适配
//			List<LinkSession> list = LocalLinkSessionPoolsImpl.getInstance().queryByUserId(memberId);
//			if(list.size() > 0) {
//				Session session = list.get(0).getSession();
//				Map headers = (Map) session.getUserProperties().get("headers");
//				String appVersion = "";
//				appVersion = (String) headers.get("appversion");
//				
//				this.push(memberId, targetType,appVersion);
//			}
			this.push(memberId, targetType,"2.4.4");
			
		} catch (Exception e) {
			LOGGER.info("向用户发送通知消息报错，用户id-memberId：{}", memberId);
			e.printStackTrace();
		}
		
		
		return ResultDto.success();
	}
	
	

}
