package com.fungo.games.service;


import com.game.common.dto.ResultDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ITagService {
	
	//获得全部游戏标签
	public ResultDto<List> getTagListAll();
	


}
