package com.fungo.system.helper.mq;


/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
public interface StreamClient {   //消息接受发送接口

    public static final String TESTMESSAGE = "testMessage";

//    @Input(TESTMESSAGE)
//    SubscribableChannel input();  //用于接受消息
//
//    @Output(TESTMESSAGE)
//    MessageChannel output();  //用于发送消息

}
