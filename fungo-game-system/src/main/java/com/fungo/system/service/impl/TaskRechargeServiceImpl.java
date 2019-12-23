package com.fungo.system.service.impl;

import com.fungo.system.service.TaskRechargeService;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class TaskRechargeServiceImpl implements TaskRechargeService {

    @Override
    public void completeRechargeTask(String loginId, Integer rechargeType, String orderId) {

    }
}
