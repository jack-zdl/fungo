package com.game.common.ts.mq.service;


import com.game.common.ts.mq.dto.MQMessageSender;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description: MQ 数据发送业务接口
 *
 * @Date: Create in 2018/5/8
 */
public interface MQDataSendService {

    /**
     * 直连路由(Direct)
     * @param msg
     * @throws Exception
     */
    public void sendtMQDirect(MQMessageSender msg) throws Exception;


    /**
     * 通配符(Topic)
     * @param msg
     * @throws Exception
     */
    public void sendMQTopic(MQMessageSender msg) throws Exception;
}
