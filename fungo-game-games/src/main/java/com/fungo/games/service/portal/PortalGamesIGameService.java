package com.fungo.games.service.portal;


import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.game.*;


public interface PortalGamesIGameService {


	/**
	 * 用户最近评论的游戏
	 * @param userId
	 * @param input
	 * @return
	 */
	FungoPageResultDto<GameOutPage> searchGamesByDownload(String userId, InputPageDto input);

}
