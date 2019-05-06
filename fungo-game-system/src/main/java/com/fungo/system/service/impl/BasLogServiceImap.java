package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.BasLogDao;
import com.fungo.system.entity.BasLog;
import com.fungo.system.service.BasLogService;
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
