package com.fungo.games.service.impl;

import com.fungo.games.entity.BasLog;
import com.game.common.dto.WebLog;
import com.game.common.log.IRequestLogger;
import org.springframework.stereotype.Service;

@Service
public class RequestLoggerServiceImpl implements IRequestLogger {

	@Override
	public void log(WebLog wlog) {
		BasLog log=new BasLog();
		log.setBizCode(wlog.getBizCode());
		log.setBizId(wlog.getBizId());
		log.setBrand(wlog.getBrand());
		log.setChannel(wlog.getChannel());
		log.setCreatedAt(wlog.getCreatedAt());
		log.setEndTime(wlog.getEndTime());
		log.setInputData(wlog.getInputData());
		log.setIp(wlog.getIp());
		log.setMemberId(wlog.getMemberId());
		log.setMethod(wlog.getMethod());
		log.setOutData(wlog.getOutData());

		String path = wlog.getPath();
		if("POST".equals(wlog.getMethod())){

		}else if (null != path && path.length() > 0) {
			path = path.substring(0, path.lastIndexOf("/"));
		}
		log.setPath(path);


		log.setReCode(wlog.getReCode());
		log.setRunTime(wlog.getRunTime());
		log.setUpdatedAt(wlog.getUpdatedAt());
		log.setToken(wlog.getToken());
		
		log.setAppversion(wlog.getAppversion());
		log.setHeight(wlog.getHeight());
		log.setWidth(wlog.getWidth());
		log.setVersion(wlog.getVersion());
		log.setUdid(wlog.getUdid());
		log.insert();
	}

}
