package com.game.common.service.impl.account;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.game.common.entity.incent.IncentAccountScore;
import com.game.common.mapper.IncentAccountScoreDao;
import com.game.common.service.account.IncentAccountScoreService;

/**
 * <p>
 * 用户分数类账户
积分、经验值等分值 服务实现类
 * </p>
 *
 * @author lzh
 * @since 2018-12-03
 */
@Service
public class IncentAccountScoreServiceImap extends ServiceImpl<IncentAccountScoreDao, IncentAccountScore> implements IncentAccountScoreService {
	
}
