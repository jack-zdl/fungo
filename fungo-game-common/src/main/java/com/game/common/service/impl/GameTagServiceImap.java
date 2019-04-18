package com.game.common.service.impl;

import com.game.common.entity.GameTag;
import com.game.common.mapper.GameTagDao;
import com.game.common.service.GameTagService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
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
