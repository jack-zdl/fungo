package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.entity.TransactionMessageDomain;
import com.fungo.system.ts.mq.service.SystemMqConsumeService;
import com.game.common.ts.mq.dto.MQResultDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemMqConsumeServiceImpl implements SystemMqConsumeService {


    /**
     * 处理mq的消息
     *
     * @param msgDate mq的消息体
     */
    @Override
    public boolean processMsg(String msgDate) {
        MQResultDto mqResultDto = JSON.parseObject(msgDate,MQResultDto.class);
        switch (mqResultDto.getType()){
           case 1:
            break;
            /*case MQResultDto.CommunityEnum.CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL.getCode():
            break;*/
        }
        return false;
    }
}
