package com.fungo.system.proxy;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.GameOutBean;
import com.game.common.dto.game.GameReleaseLog;
import com.game.common.dto.GameDto;

import java.util.List;

public interface IDeveloperProxyService {

	/**
	 * 开发者游戏列表
	 * @param collect
	 * @return
	 */
	FungoPageResultDto<GameOutBean> gameList(List<String> collect,int page, int limit );

	List<GameReleaseLog> selectGameReleaseLog(GameReleaseLog gameReleaseLog);

	ResultDto<String> addGameTag(List<String> tags, String categoryId, String gameId);

	GameDto selectGame(String gameId);
}
