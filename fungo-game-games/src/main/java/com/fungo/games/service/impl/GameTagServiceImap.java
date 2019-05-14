package com.fungo.games.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.games.dao.GameTagDao;
import com.fungo.games.entity.GameTag;
import com.fungo.games.service.GameTagService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 游戏标签 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-05-09
 */
@Service
public class GameTagServiceImap extends ServiceImpl<GameTagDao, GameTag> implements GameTagService {
	
}
