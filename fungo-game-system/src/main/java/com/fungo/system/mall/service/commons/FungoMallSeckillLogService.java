package com.fungo.system.mall.service.commons;


import com.alibaba.fastjson.JSON;
import com.auth0.jwt.internal.org.apache.commons.lang3.StringUtils;
import com.fungo.system.entity.ScoreLog;
import com.fungo.system.service.ScoreLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *    fungo商城-秒杀日志处理业务
 * </p>
 *
 * @author mxf
 * @since 2018-12-04
 */
@Service
public class FungoMallSeckillLogService {

    private static final Logger logger = LoggerFactory.getLogger(FungoMallSeckillLogService.class);


    @Autowired
    private ScoreLogService scoreLogService;


    /**
     * 记录用户添加fungo币日志
     * 产生类型
     *          2 管理员
     */
    public void addCoinMbToLog(String userId, String phone, String nickName, String fungoCoin,String goodsName) {

        ScoreLog newLog = new ScoreLog();

       newLog.setMemberId(userId);
        newLog.setTaskType(-1);

        newLog.setMbUserName(nickName);

        Integer score = 0;
        if (StringUtils.isNoneBlank(fungoCoin)) {
            score = Integer.parseInt(fungoCoin);
        }
        newLog.setScore(score);

        newLog.setProduceType(3);
        newLog.setRuleId("0");
        newLog.setRuleName(goodsName);

        newLog.setCreatedAt(new Date());
        newLog.setUpdatedAt(new Date());
        newLog.setCreatorName("商城秒杀消费");

        logger.info("记录添加Fungo日志：{}", JSON.toJSONString(newLog));

        scoreLogService.insert(newLog);

    }


}
