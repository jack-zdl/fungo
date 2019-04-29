package com.fungo.system.proxy;

import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.game.GameOutBean;
import java.util.List;

public interface IDeveloperProxyService {

	/**
	 * 开发者游戏列表
	 * @param collect
	 * @return
	 */
	public FungoPageResultDto<GameOutBean> gameList(List<String> collect,int page, int limit );


}
