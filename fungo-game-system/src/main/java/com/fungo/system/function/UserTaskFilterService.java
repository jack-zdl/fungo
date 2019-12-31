package com.fungo.system.function;

import com.fungo.system.service.IScoreRuleService;
import com.game.common.consts.FunGoGameConsts;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/30
 */
@Service
public class UserTaskFilterService {

    @Autowired
    private IScoreRuleService scoreRuleServiceImpl;

    public void updateUserTask(String memberId){
        scoreRuleServiceImpl.updateExtBygetTasked( memberId,FunGoGameConsts.TASK_RULE_TASK_TYPE_SCORE );
        scoreRuleServiceImpl.updateExtBygetTasked( memberId, FunGoGameConsts.TASK_RULE_TASK_TYPE_COIN_TASK );
    }
}
