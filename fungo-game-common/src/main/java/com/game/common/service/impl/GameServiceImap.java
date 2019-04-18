package com.game.common.service.impl;

import com.game.common.entity.Game;
import com.game.common.mapper.GameDao;
import com.game.common.service.GameService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-12-07
 */
@Service
public class GameServiceImap extends ServiceImpl<GameDao, Game> implements GameService {
	
}
