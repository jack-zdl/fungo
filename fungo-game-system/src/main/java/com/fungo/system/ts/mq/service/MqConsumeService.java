package com.fungo.system.ts.mq.service;

public interface MqConsumeService {

    void processMsg(String msgDate);
}
