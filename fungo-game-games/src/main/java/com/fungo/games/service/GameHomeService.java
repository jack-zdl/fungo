package com.fungo.games.service;


import com.fungo.games.entity.HomePage;
import com.game.common.api.InputPageDto;
import com.game.common.bean.AdminCollectionGroup;
import com.game.common.bean.NewGameBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.vo.AdminCollectionVo;

import java.util.List;

public interface GameHomeService {


	/**
	 * 首页显示内容
	 * @return
	 */
	ResultDto<List<HomePage>> queryHomePage(InputPageDto inputPageDto);


	/**
	 * 新游信息查询
	 * @return
	 */
	ResultDto<List<NewGameBean>> queryNewGame(InputPageDto inputPageDto);


	/**
	 * 查看往期新游信息
	 * @return
	 */
	ResultDto<List<NewGameBean>> queryOldGame(InputPageDto inputPageDto);

	/**
	 * 合集信息查询
	 * @return
	 */
	public ResultDto<List<AdminCollectionGroup>> queryCollectionGroup(AdminCollectionVo input);


}
