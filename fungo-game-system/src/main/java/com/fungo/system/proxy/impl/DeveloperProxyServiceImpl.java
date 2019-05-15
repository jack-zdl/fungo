package com.fungo.system.proxy.impl;


import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IDeveloperProxyService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.game.*;
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

	@Autowired
	private CommunityFeignClient communityFeignClient;

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





	@HystrixCommand(fallbackMethod = "hystrixSelectReleaseLog",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public  List<GameReleaseLogDto> selectGameReleaseLog(GameReleaseLogDto gameReleaseLog) {
		FungoPageResultDto<GameReleaseLogDto>  gameReleases = gamesFeignClient.selectOne(gameReleaseLog);
		return gameReleases.getData();
	}

	@Override
	public ResultDto<String> addGameTag(List<String> tags, String categoryId, String gameId) {
		return null;
	}

	@HystrixCommand(fallbackMethod = "hystrixSelectGame",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public GameDto selectGame(String gameId) {
		return gamesFeignClient.selectOne(gameId);
	}

	@HystrixCommand(fallbackMethod = "hystrixSelectCount",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public int selectCount(GameSurveyRelDto gameSurveyRel) {
		return gamesFeignClient.selectCount(gameSurveyRel);
	}

	@HystrixCommand(fallbackMethod = "hystrixSelectGameEvaluationCount",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public int selectGameEvaluationCount(GameEvaluationDto gameEvaluation) {
		return gamesFeignClient.selectGameEvaluationCount(gameEvaluation);
	}

	@HystrixCommand(fallbackMethod = "hystrixSelectPostCount",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public int selectPostCount(CmmPostDto cmmPostDto) {
		return communityFeignClient.selectPostCount(cmmPostDto);
	}


	@HystrixCommand(fallbackMethod = "hystrixGetMemberIdByTargetId",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public String getMemberIdByTargetId(Map<String, String> map) {
		return gamesFeignClient.getMemberIdByTargetId(map);
	}

	public int hystrixSelectPostCount(CmmPostDto cmmPostDto){
		return 0;
	}
	public int hystrixSelectGameEvaluationCount(GameEvaluationDto gameEvaluation){
		return 0;
	}

	public int hystrixSelectCount(GameSurveyRelDto gameSurveyRel){
		return 0;
	}

	public FungoPageResultDto<GameOutBean> hystrixGameList(List<String> collect,int page, int limit){
		FungoPageResultDto<GameOutBean>  gameOutBeans = new FungoPageResultDto();
		GameOutBean gameOutBean = new GameOutBean();
		gameOutBeans.setData(Arrays.asList(gameOutBean));
		gameOutBeans.setMessage("");
		return  FungoPageResultDto.error("-1", "访问games微服务发生失败");
	}

	public List<GameReleaseLogDto> hystrixSelectReleaseLog(GameReleaseLogDto gameReleaseLog){
		return null;
	}

	public GameDto hystrixSelectGame(String gameId){
		return null;
	}

	public boolean hystrixUpdateCounter(Map<String,String> map){
		return  false;
	}
}
