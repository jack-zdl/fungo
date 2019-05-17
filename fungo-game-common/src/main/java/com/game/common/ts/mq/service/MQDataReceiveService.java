package com.game.common.ts.mq.service;


/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description: MQ 数据接收业务接口
 *
 * @Date: Create in 2018/5/8
 */
public interface MQDataReceiveService {



    /**
     *  直连路由(Direct)
     * @param msgData
     * @return true 消息被正常处理，false未被处理
     * @throws Exception
     */
    public boolean onMessageWithMQDirect(String msgData) throws Exception;


    /**
     * 通配符(Topic)
     * @param msgData 消息数据
     * @return true 消息被正常处理，false未被处理
     * @throws Exception
     */
    public boolean onMessageWithMQTopic(String msgData) throws Exception;


}
