package com.fungo.system.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.IncentAccountScore;
import com.fungo.system.service.IMemberAccountScoreDaoService;
import com.fungo.system.service.MSServiceMemberAccountService;
import com.game.common.consts.FunGoGameConsts;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MSServiceMemberAccountServiceImpl implements MSServiceMemberAccountService {

    private static final Logger logger = LoggerFactory.getLogger(MSServiceMemberAccountServiceImpl.class);


    @Autowired
    private IMemberAccountScoreDaoService accountScoreDaoService;


    @Override
    public boolean subtractAccountScore(String mb_id, Integer score) throws IOException {


        if (score <= 0) {
            return false;
        }

        if (StringUtils.isBlank(mb_id)) {
            return false;
        }

        try {
            //更新账户
            IncentAccountScore scoreAccount = accountScoreDaoService.selectOne(new EntityWrapper<IncentAccountScore>().eq("mb_id", mb_id).eq("account_group_id",
                    FunGoGameConsts.INCENT_ACCOUNT_TYPE_SCORE_ID));
            if (scoreAccount == null) {
                return false;
            }

            logger.info("扣减用户积分--用户经验值账户历史数据-scoreAccount:{}", JSON.toJSONString(scoreAccount));

            Long lastCasVersion = scoreAccount.getCasVersion();

            //初始化
            if (null == lastCasVersion) {
                lastCasVersion = 0L;
            }

            scoreAccount.setCasVersion(lastCasVersion + 1);
            scoreAccount.setScoreUsable(scoreAccount.getScoreUsable().subtract(new BigDecimal(score)));
            scoreAccount.setUpdatedAt(new Date());

            Map<String, Object> criteriaMap = new HashMap<String, Object>();
            criteriaMap.put("mb_id", mb_id);
            criteriaMap.put("cas_version", lastCasVersion);


            EntityWrapper<IncentAccountScore> scoreAccountEntityWrapper = new EntityWrapper<IncentAccountScore>();

            scoreAccountEntityWrapper.allEq(criteriaMap);

            logger.info("扣减用户积分--用户经验值账户新数据-scoreAccount:{}", JSON.toJSONString(scoreAccount));

            boolean updateStatus = accountScoreDaoService.update(scoreAccount, scoreAccountEntityWrapper);

            logger.info("扣减用户积分--用户经验值账户更新结果-updateStatus:{}", updateStatus);

            return updateStatus;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    //----------
}
