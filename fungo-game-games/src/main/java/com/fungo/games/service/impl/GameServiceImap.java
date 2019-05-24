package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.games.dao.GameDao;
import com.fungo.games.entity.Game;
import com.fungo.games.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private GameDao gameDao;

    @Override
    public Map<String, Game> listGame(List<String> ids) {
        Map<String, Game> map = new HashMap<>();
        if(ids==null||ids.isEmpty()){
            return  map;
        }
        return gameDao.listGame(ids);
    }
}
