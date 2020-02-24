package com.fungo.community.helper;

import com.game.common.dto.community.CmmCommunityDto;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
//@Component
public class MQClient {

    private static final Logger logger = LoggerFactory.getLogger(MQClient.class);

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_COMMUNITY_INSERT)
    public void topicReceiveCommunityInsert(@Payload CmmCommunityDto cmmCommunityDto, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        System.out.println("mq消费信息" + cmmCommunityDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        channel.basicAck(envelope.getDeliveryTag(), true);
                    }catch (Exception e) {
                        logger.error("游戏插入失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                    }finally {
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_COMMUNITY_INSERT, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }

    }


}
