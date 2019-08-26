package com.fungo.games.service.portal;


import com.fungo.games.utils.PCGameGroupVO;
import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;

import java.util.Map;

public interface PortalGamesIIndexService {

	//pc端发现页游戏合集
	FungoPageResultDto<Map<String, Object>> pcGameGroup(PCGameGroupVO input);
}
