package com.fungo.system.proxy.impl;


import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IDeveloperProxyService;
import com.game.common.config.MyThreadLocal;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.game.GameInputPageDto;
import com.game.common.dto.game.GameItemInput;
import com.game.common.dto.game.GameOutBean;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DeveloperProxyServiceImpl implements IDeveloperProxyService {

	@Autowired
	private GamesFeignClient gamesFeignClient;

	@HystrixCommand(fallbackMethod = "hystrixGameList",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public FungoPageResultDto<GameOutBean> gameList(List<String> collect,int page, int limit) {
		String ids = collect.toString();
		GameItemInput gameItemInput = new GameItemInput();
		gameItemInput.setGroup_id(ids);
		gameItemInput.setLimit(limit);
		gameItemInput.setPage(page);
		GameInputPageDto gameInputPageDto = new GameInputPageDto();
		gameInputPageDto.setLimit(limit);
		gameInputPageDto.setPage(page);
		gameInputPageDto.setId_list(collect.toArray(new String[collect.size()]));
		FungoPageResultDto<GameOutBean>  gameOutBeans = gamesFeignClient.getGameList(gameItemInput);
		return gameOutBeans;
	}

//	updateCounter

	@HystrixCommand(fallbackMethod = "hystrixUpdateCounter",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	public boolean updateCounter(Map<String,String> map) {
		return gamesFeignClient.updateCounter(map);
	}


	public boolean hystrixUpdateCounter(Map<String,String> map){
		return  false;
	}


	public FungoPageResultDto<GameOutBean> hystrixGameList(List<String> collect,int page, int limit){
		FungoPageResultDto<GameOutBean>  gameOutBeans = new FungoPageResultDto();
		GameOutBean gameOutBean = new GameOutBean();
		gameOutBeans.setData(Arrays.asList(gameOutBean));
		gameOutBeans.setMessage("");
		return  FungoPageResultDto.error("-1", "访问games微服务发生失败");
	}
}
