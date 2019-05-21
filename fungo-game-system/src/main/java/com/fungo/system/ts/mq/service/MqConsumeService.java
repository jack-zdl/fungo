package com.fungo.system.ts.mq.service;

public interface MqConsumeService {

    /**
     * 处理mq的消息
     * @param msgDate mq的消息体
     * @return 处理结果
     */
    boolean processMsg(String msgDate) throws Exception;
}
