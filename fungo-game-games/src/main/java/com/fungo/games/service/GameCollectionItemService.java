package com.fungo.games.service;

import com.baomidou.mybatisplus.service.IService;
import com.fungo.games.entity.GameCollectionItem;
import com.game.common.dto.index.CardIndexBean;

/**
 * <p>
 * 游戏合集项 服务类
 * </p>
 *
 * @author lzh
 * @since 2018-06-28
 */
public interface GameCollectionItemService extends IService<GameCollectionItem> {
    public CardIndexBean selectedGames();
}
