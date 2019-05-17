package com.fungo.system.helper.mq;

import com.game.common.dto.GameDto;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MQ1Client {

    private static final Logger logger = LoggerFactory.getLogger(MQClient.class);

    @Autowired
    private MQProduct mqProduct;




    /**
     * 更新游戏
     * @param gameDto
     * @param message
     * @param deliveryTag
     * @param channel
     */
    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_UPDATE)
    public void topicReceiveGameUpdate(@Payload GameDto gameDto, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        logger.info("mq消费开始"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
        System.out.println("mq消费信息" + gameDto.toString());
        boolean autoAck=false;
        //消息消费完成确认
        try {
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {
                        if(1 == 1){
                            throw new Exception("自定义异常");
                        }
                        channel.basicAck(envelope.getDeliveryTag(), false); //确认消费
                    }catch (Exception e) {
                        logger.error("游戏更新失败",e);
                        channel.basicReject(envelope.getDeliveryTag(), true); // 消费者拒绝消费,重新放入队列
                        //channel.abort();  //此操作中的所有异常将被丢弃
                    }finally {
//                        channel.basicAck(envelope.getDeliveryTag(),false);
                        logger.info("mq消费结束"+MQConfig.TOPIC_QUEUE_GAME_UPDATE);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }

    }





}
