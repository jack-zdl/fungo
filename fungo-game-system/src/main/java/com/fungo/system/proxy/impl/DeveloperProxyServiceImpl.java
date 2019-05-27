package com.fungo.system.proxy.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.feign.CommunityFeignClient;
import com.fungo.system.feign.GamesFeignClient;
import com.fungo.system.proxy.IDeveloperProxyService;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.GameDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.game.*;
import com.game.common.enums.CommonEnum;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class DeveloperProxyServiceImpl implements IDeveloperProxyService {

	private static final Logger logger = LoggerFactory.getLogger(DeveloperProxyServiceImpl.class);

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
		try{
		FungoPageResultDto<Map> re = communityFeignClient.queryCmmPostEssenceList();

				FungoPageResultDto<GameOutBean>  gameOutBeans = gamesFeignClient.getGameList(gameItemInput);
				return  gameOutBeans;
			}catch (Exception e){
				logger.error("远程调用异常:"+e);
			}
		return new FungoPageResultDto<GameOutBean>();
	}

//	updateCounter


	@HystrixCommand(fallbackMethod = "hystrixUpdateCounter",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	public boolean updateCounter(Map<String,String> map) {
		return gamesFeignClient.updateCounter(map);
	}


	@HystrixCommand(fallbackMethod = "hystrixUpdateCounter",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	public boolean updatecommunityCounter(Map<String,String> map) {
		return gamesFeignClient.updateCounter(map);
	}

	@HystrixCommand(fallbackMethod = "hystrixSelectReleaseLog",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public  List<GameReleaseLogDto> selectGameReleaseLog(GameReleaseLogDto gameReleaseLog) {
		Page<GameReleaseLogDto> gameReleases = gamesFeignClient.getGameReleaseLogPage(gameReleaseLog);
		return gameReleases.getRecords();
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
		return gamesFeignClient.gameSurveySelectCount(gameSurveyRel);
	}

	@HystrixCommand(fallbackMethod = "hystrixSelectGameEvaluationCount",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public int selectGameEvaluationCount(GameEvaluationDto gameEvaluation) {
		return gamesFeignClient.gameEvaluationSelectCount(gameEvaluation);
	}

	@HystrixCommand(fallbackMethod = "hystrixSelectPostCount",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )
	@Override
	public int selectPostCount(CmmPostDto cmmPostDto) {
		return communityFeignClient.queryCmmPostCount(cmmPostDto).getData();
	}


	/*@HystrixCommand(fallbackMethod = "hystrixGetMemberIdByTargetId",ignoreExceptions = {Exception.class},
			commandProperties=@HystrixProperty(name="execution.isolation.strategy", value="SEMAPHORE") )*/
	@Override
	public String getMemberIdByTargetId(Map<String, String> map) {
		return gamesFeignClient.getMemberIdByTargetId(map);
	}

	public int hystrixSelectPostCount(CmmPostDto cmmPostDto){
		logger.warn("DeveloperProxyServiceImpl.selectPostCount根据主键查询游戏评价异常");
		return 0;
	}

	public int hystrixSelectGameEvaluationCount(GameEvaluationDto gameEvaluation){
		logger.warn("DeveloperProxyServiceImpl.selectGameEvaluationCount根据主键查询游戏评价异常");
		return 0;
	}

	public int hystrixSelectCount(GameSurveyRelDto gameSurveyRel){
		logger.warn("DeveloperProxyServiceImpl.selectCount根据主键查询游戏评价异常");
		return 0;
	}

	public FungoPageResultDto<GameOutBean> hystrixGameList(List<String> collect,int page, int limit){
		logger.warn("DeveloperProxyServiceImpl.gameList根据主键查询游戏评价异常");
		FungoPageResultDto<GameOutBean>  gameOutBeans = new FungoPageResultDto();
		GameOutBean gameOutBean = new GameOutBean();
		gameOutBeans.setData(Arrays.asList(gameOutBean));
		gameOutBeans.setMessage("");
		return  FungoPageResultDto.error("-1", "访问games微服务发生失败");
	}

	public List<GameReleaseLogDto> hystrixSelectReleaseLog(GameReleaseLogDto gameReleaseLog){
		logger.warn("DeveloperProxyServiceImpl.ReleaseLog根据主键查询游戏评价异常");
		return null;
	}

	public GameDto hystrixSelectGame(String gameId){
		logger.warn("DeveloperProxyServiceImpl.game根据主键查询游戏评价异常");
		return null;
	}

	public boolean hystrixUpdateCounter(Map<String,String> map){
		logger.warn("DeveloperProxyServiceImpl.UpdateCounter根据主键查询游戏评价异常");
		return  false;
	}
}
