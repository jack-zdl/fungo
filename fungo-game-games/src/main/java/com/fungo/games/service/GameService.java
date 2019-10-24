package com.fungo.games.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.games.entity.Game;
import com.game.common.dto.game.TagGameDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 游戏 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-12-07
 */
public interface GameService extends IService<Game> {

   Map<String,Game> listGame(List<String> ids);


    int countGameByTags(TagGameDto tagGameDto);

    List<Game> listGameByTags(TagGameDto tagGameDto);
}
