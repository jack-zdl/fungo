package com.game.common.service.impl;

import com.game.common.entity.MooMessage;
import com.game.common.mapper.MooMessageDao;
import com.game.common.service.MooMessageService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 心情评论 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-04-20
 */
@Service
public class MooMessageServiceImap extends ServiceImpl<MooMessageDao, MooMessage> implements MooMessageService {
	
}
