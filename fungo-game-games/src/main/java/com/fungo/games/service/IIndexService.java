package com.fungo.games.service;


import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;

import java.util.Map;

public interface IIndexService {

	//pc端发现页游戏合集
	FungoPageResultDto<Map<String, Object>> pcGameGroup(InputPageDto input);
}
