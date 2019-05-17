package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.system.ts.mq.entity.TransactionMessageDomain;
import com.fungo.system.ts.mq.service.SystemMqConsumeService;

public class SystemMqConsumeServiceImpl implements SystemMqConsumeService {

    @Override
    public void processMqMsg(String msgDate) {
        //
        TransactionMessageDomain transactionMessageDomain = JSON.parseObject(msgDate, TransactionMessageDomain.class);

    }
}
