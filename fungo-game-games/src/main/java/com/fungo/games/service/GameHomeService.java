package com.fungo.games.service;


import com.game.common.api.InputPageDto;
import com.game.common.bean.AdminCollectionGroup;
import com.game.common.bean.HomePageBean;
import com.game.common.bean.NewGameBean;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.vo.AdminCollectionVo;

public interface GameHomeService {


	/**
	 * 首页显示内容
	 * @return
	 */
	FungoPageResultDto<HomePageBean> queryHomePage(InputPageDto inputPageDto,String memberId, String os);


	/**
	 * 新游信息查询
	 * @return
	 */
	FungoPageResultDto<NewGameBean> queryNewGame(InputPageDto inputPageDto,String memberId, String os);


	/**
	 * 查看往期新游信息
	 * @return
	 */
	FungoPageResultDto<NewGameBean> queryOldGame(InputPageDto inputPageDto);

	/**
	 * 合集组信息列表查询
	 * @return
	 */
	FungoPageResultDto<AdminCollectionGroup> queryCollectionGroup(AdminCollectionVo input);


	/**
	 * 合集项信息查询
	 * @return
	 */
	ResultDto<AdminCollectionGroup> queryCollectionItem(AdminCollectionVo input,String memberId, String os);


}
