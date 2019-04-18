package com.game.common.service.impl;

import com.game.common.entity.BasLog;
import com.game.common.mapper.BasLogDao;
import com.game.common.service.BasLogService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 接口前台访问日志 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-07-09
 */
@Service
public class BasLogServiceImap extends ServiceImpl<BasLogDao, BasLog> implements BasLogService {
	
}
