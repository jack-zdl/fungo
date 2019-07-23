package com.fungo.system.service.portal;

import com.game.common.api.InputPageDto;
import com.game.common.dto.FungoPageResultDto;
import com.game.common.dto.ResultDto;
import com.game.common.dto.advert.AdvertOutBean;
import com.game.common.dto.index.CardIndexBean;

import java.util.List;

public interface PortalSystemIIndexService {

	/**
	 * 首页显示内容
	 * @param input 入参
	 * @return
	 */
	FungoPageResultDto<CardIndexBean> index(InputPageDto input);

	ResultDto<List<AdvertOutBean>> getAdvertWithPc();

}
