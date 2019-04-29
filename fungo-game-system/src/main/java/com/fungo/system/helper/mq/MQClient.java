package com.fungo.system.helper.mq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
@Component
//@EnableBinding(StreamClient.class)
public class MQClient {

//    @StreamListener("testMessage2")  //监听testMessage这个消息队列, StreamClient类中必须定义相应的Input。
//    @RabbitListener(queues = "cord")
//    public void receiver(Object message){
//        System.out.println("接收到消息："+message);
//    }



    @RabbitListener(queues = MQConfig.DIRECT_QUEUE)
    public void receiver(Object message){
        System.out.println("DIRECT 直连交换机 接收到消息："+message);
    }
//
//    /**
//     * Direct Pattern.
//     * @param message
//     */
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE1)
    public void directReceive(String message){
        System.out.println("receive direct message:" + message);
    }


    @RabbitListener(queues = MQConfig.TOPIC_QUEUE2)
    public void directReceive2(String message){
        System.out.println("receive direct message:" + message);
    }


    /**
     * fanout Pattern.
     * @param message
     */
    @RabbitListener(queues = MQConfig.FANOUT_QUEUE1)
    public void fanoutReceive1(String message){
        System.out.println("receive fanout1 message:" + message);
    }
    /**
     * fanout Pattern.
     * @param message
     */
    @RabbitListener(queues = MQConfig.FANOUT_QUEUE2)
    public void fanoutReceive2(String message){
        System.out.println("receive fanout2 message:" + message);
    }


    /**
     * Header Pattern.
     * @param message
     */
    @RabbitListener(queues = MQConfig.HEADER_QUEUE)
    public void topicHeader(byte[] message){
        System.out.println("receive topic1 message:" + new String(message));
    }
}
