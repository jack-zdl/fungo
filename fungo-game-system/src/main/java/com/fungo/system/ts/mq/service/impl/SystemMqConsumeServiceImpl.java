package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fungo.system.service.SystemService;
import com.fungo.system.ts.mq.service.SystemMqConsumeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemMqConsumeServiceImpl implements SystemMqConsumeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemMqConsumeServiceImpl.class);

    @Autowired
    private SystemService systemService;
    /**
     * 处理mq的消息
     *
     * @param msgDate mq的消息体
     */
    @Override
    public boolean processMsg(String msgDate) {
        JSONObject json = JSONObject.parseObject(msgDate);
        Integer type = json.getInteger("type");
        String body = json.getString("body");

        return false;
    }


}

