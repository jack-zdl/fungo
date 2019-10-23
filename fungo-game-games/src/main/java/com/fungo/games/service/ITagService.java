package com.fungo.games.service;


import com.fungo.games.entity.BasTag;
import com.game.common.dto.ResultDto;
import com.game.common.dto.game.BasTagGroupDto;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface ITagService {
	
	//获得全部游戏标签
	public ResultDto<List> getTagListAll();


    ResultDto<List<BasTag>> listPostTag();

    // 获取全部分类和标签
    ResultDto<List<BasTagGroupDto>> listAllGroupAndTag();
}
