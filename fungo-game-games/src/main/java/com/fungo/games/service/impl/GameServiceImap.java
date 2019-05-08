package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.service.GameService;
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
