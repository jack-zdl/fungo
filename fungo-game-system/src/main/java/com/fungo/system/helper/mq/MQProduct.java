package com.fungo.system.helper.mq;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
@RestController
//@EnableBinding(StreamClient.class)
public class MQProduct {


    @Autowired
    private  AmqpAdmin admin;

    @Autowired
    private  AmqpTemplate template;


    /** DIRECT模式
     * @param routingKey 路由关键字
     * @param msg 消息体
     */
    public void sendDirectMsg(String routingKey, String msg) {
        template.convertAndSend(MQConfig.DIRECT_QUEUE, msg);
    }

    /**
     * sendFanout
     * @param message
     */
    public void sendFanout(Object message){
        String msg = (String) message;
        template.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
    }

    /**
     * @param routingKey 路由关键字
     * @param msg 消息体
     * @param exchange 交换机   TOPIC模式
     */
    public void sendExchangeMsg(String exchange, String routingKey, String msg) {
        template.convertAndSend(exchange, routingKey, msg);
    }

    /**
     * use Topic Pattern
     * "topic.key1"  路由键
     * @param message
     */
    public void sendTopic(Object message){
        String msg = (String) message;
        template.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key1",msg+"1");  // 可以匹配到 topic.# and topic.key1
        template.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");  // 可以匹配到 topic.#
    }

    /**
     * @param map 消息headers属性
     * @param exchange 交换机    header模式
     * @param msg 消息体
     */
    public void sendHeadersMsg(String exchange, String msg, Map<String, Object> map) {
        template.convertAndSend(exchange, null, msg, message -> {
            message.getMessageProperties().getHeaders().putAll(map);
            return message;
        });
    }

    @RequestMapping("/send")
    public void send(){
//        sendDirectMsg("cord", String.valueOf(System.currentTimeMillis()));
        sendTopic("topic-exchange");
//        BossMessage message = new BossMessage<>();
//        message.setData(brand);
//        message.setOpType(MqMessageProducer.ADD);
//        message.setDataType(MqMessageProducer.BRAND);
//        channel.send(MessageBuilder.withPayload(JSON.toJSONString("dasdsdasdasdasd")).build());
        //        streamClient.output().send(MessageBuilder.withPayload("it is test message.").build()); //构建消息并且发送
    }
}
