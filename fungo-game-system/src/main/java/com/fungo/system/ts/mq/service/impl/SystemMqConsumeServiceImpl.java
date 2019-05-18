package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.entity.TransactionMessageDomain;
import com.fungo.system.ts.mq.service.SystemMqConsumeService;
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
        return false;
    }
}
