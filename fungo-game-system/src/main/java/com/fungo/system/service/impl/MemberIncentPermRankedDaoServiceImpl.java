package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fungo.system.dao.IncentMbPermRankedDao;
import com.fungo.system.entity.IncentMbPermRanked;
import com.fungo.system.service.IMemberIncentPermRankedDaoService;
import org.springframework.stereotype.Service;

@Service
public class MemberIncentPermRankedDaoServiceImpl extends ServiceImpl<IncentMbPermRankedDao, IncentMbPermRanked>
        implements IMemberIncentPermRankedDaoService {
}
