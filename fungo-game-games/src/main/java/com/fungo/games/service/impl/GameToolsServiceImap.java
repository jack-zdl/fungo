package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.fungo.games.dao.GameToolsDao;
import com.fungo.games.entity.GameTools;
import com.fungo.games.service.GameToolsService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏工具管理表
如工具软件的管理 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-12-14
 */
@Service
public class GameToolsServiceImap extends ServiceImpl<GameToolsDao, GameTools> implements GameToolsService {
	
}
