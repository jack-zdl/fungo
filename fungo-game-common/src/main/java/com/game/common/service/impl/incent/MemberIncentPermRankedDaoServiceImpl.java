package com.game.common.service.impl.incent;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.game.common.entity.incent.IncentMbPermRanked;
import com.game.common.mapper.IncentMbPermRankedDao;
import com.game.common.service.incent.IMemberIncentPermRankedDaoService;
import org.springframework.stereotype.Service;

@Service
public class MemberIncentPermRankedDaoServiceImpl extends ServiceImpl<IncentMbPermRankedDao, IncentMbPermRanked>
        implements IMemberIncentPermRankedDaoService {
}
