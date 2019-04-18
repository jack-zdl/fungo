package com.game.common.service.impl.tasked;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.game.common.entity.incent.IncentTasked;
import com.game.common.mapper.IncentTaskedDao;
import com.game.common.service.tasked.IncentTaskedService;

/**
 * <p>
 * 用户权益-任务完成汇总表 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@Service
public class IncentTaskedServiceImap extends ServiceImpl<IncentTaskedDao, IncentTasked> implements IncentTaskedService {
	
}
