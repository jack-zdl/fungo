package com.fungo.system.ts.mq.service.impl;

import com.fungo.system.ts.mq.service.UserMqConsumeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserMqConsumeServiceImpl implements UserMqConsumeService {

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
