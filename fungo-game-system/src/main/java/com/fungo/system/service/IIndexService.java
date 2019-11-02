package com.fungo.system.service;

import com.fungo.system.entity.Banner;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.index.CardIndexBean;
import com.game.common.dto.index.CircleCardDataBean;

import java.util.List;
import java.util.Map;

public interface IIndexService {


	/**
	 * 首页显示内容
	 * @param input 入参
	 * @param os app 平台类型
	 * @param iosChannel ios渠道标识
	 * @param app_channel app渠道编码
	 * @param appVersion app release版本号
	 * @return
	 */
	FungoPageResultDto<CardIndexBean> index(InputPageDto input, String os, String iosChannel, String app_channel, String appVersion);

	/**
	 * 首页显示内容
	 * @param input 入参
	 * @param os app 平台类型
	 * @param iosChannel ios渠道标识
	 * @param app_channel app渠道编码
	 * @param appVersion app release版本号
	 * @return
	 */
	FungoPageResultDto<CardIndexBean> circleEventList(InputPageDto input, String os, String iosChannel, String app_channel, String appVersion);


	/**
	 * 首页显示内容
	 * @param input 入参
	 * @return
	 */
	FungoPageResultDto<CardIndexBean> index(InputPageDto input);



	/**
	 * 首页显示内容
	 * @param os app 平台类型
	 * @param iosChannel ios渠道标识
	 * @param app_channel app渠道编码
	 * @param appVersion app release版本号
	 * @return
	 */
	ResultDto<CircleCardDataBean> queryHomePage(String os, String iosChannel, String app_channel, String appVersion);


	/**
	 * app端获取管控台设置的启动页banner
	 * @param os app 平台类型
	 * @param iosChannel ios渠道标识
	 * @param app_channel app渠道编码
	 * @param appVersion app release版本号
	 * @return
	 */
	ResultDto<List<CircleCardDataBean>> queryStartUp(String os, String iosChannel, String app_channel, String appVersion);


	/**
	 * app端获取管控台设置的开屏页banner
	 * @param os app 平台类型
	 * @param iosChannel ios渠道标识
	 * @param app_channel app渠道编码
	 * @param appVersion app release版本号
	 * @return
	 */
	ResultDto<CircleCardDataBean> queryOpenScreen(String os, String iosChannel, String app_channel, String appVersion);

}
