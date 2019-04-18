package com.game.common.service;

import java.util.Date;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.api.ResultDto;
import com.game.common.entity.MessageCode;

/**
 * <p>
 * 短信验证 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-04-13
 */
public interface MessageCodeService extends IService<MessageCode> {
	public ResultDto<String> sendCode(String type ,String phone,String code ,int times)throws Exception;	
	public ResultDto<String> checkCode(String type ,String phone,String code);
	public ResultDto<String> updateCheckCodeSuccess(String msgId);
	public ResultDto<String> checkCodeAndSuccess(String type ,String phone,String code);
}
