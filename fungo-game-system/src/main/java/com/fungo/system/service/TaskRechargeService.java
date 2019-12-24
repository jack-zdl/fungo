package com.fungo.system.service;

import com.game.common.dto.ResultDto;

public interface TaskRechargeService {

    ResultDto<String> completeRechargeTask(String loginId, Integer rechargeType, String orderId);
}
