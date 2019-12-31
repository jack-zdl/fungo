package com.fungo.system.service;

import com.fungo.system.entity.IncentTasked;

/**
 * <p> 規則任務 </p>
 * @Author: dl.zhang
 * @Date: 2019/12/23
 */
public interface IScoreRuleService {

    int achieveMultiScoreRule(String memberId, String ext2Task,String  objectId);

    void achieveScoreRule(String memberId,String ext2Task);

    IncentTasked updateExtBygetTasked(String memberId, int task_type);
}
