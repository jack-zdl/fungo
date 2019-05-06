package com.fungo.games.helper;

import com.fungo.games.entity.Game;
import com.fungo.games.service.GameService;
import com.game.common.dto.GameDto;
import com.rabbitmq.client.*;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
@Component
public class MQClient {

    private static final Logger logger = LoggerFactory.getLogger(MQClient.class);

    @Autowired
    private GameService gameService;

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_UPDATE)
    public void topicReceiveGameUpdate(@Payload GameDto gameDto, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
        boolean autoAck=false;
        //消息消费完成确认
        try {
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
            Consumer consumer =  new DefaultConsumer(channel)
            {
                @Override
                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
                    try {
                        System.out.println("receive direct message:" + gameDto.toString());

                        Game game = new Game();
                        BeanUtils.copyProperties(gameDto, game);
                        gameService.insert(game);
                        channel.basicAck(envelope.getDeliveryTag(), true);
//                        channel.basicReject(envelope.getDeliveryTag(), false);
//                        channel.basicNack(envelope.getDeliveryTag(), false, true);
//                        channel.basicReject(envelope.getDeliveryTag(), true);  //true: 重新放入队列
//                        channel.basicAck(envelope.getDeliveryTag(), false);// 确认消费
                    }catch (Exception e) {
                        logger.error("游戏插入失败",e);
                        channel.abort();  //此操作中的所有异常将被丢弃
                    }finally {
//                        channel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            };
            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
        }catch (IOException e) {
            logger.error("mq消费异常",e);
            e.printStackTrace();
        }

    }

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_INSERT)
    public void topicReceiveGameInsert( GameDto gameDto){
        boolean autoAck=false;
//        //消息消费完成确认
//        try {
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
//            Consumer consumer =  new DefaultConsumer(channel)
//            {
//                @Override
//                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
//                    try {
//                        System.out.println("receive direct message:" + gameDto.toString());
//
//                        Game game = new Game();
//                        BeanUtils.copyProperties(gameDto, game);
//                        gameService.insert(game);
//                        channel.basicAck(envelope.getDeliveryTag(), true);
////                        channel.basicReject(envelope.getDeliveryTag(), false);
////                        channel.basicNack(envelope.getDeliveryTag(), false, true);
////                        channel.basicReject(envelope.getDeliveryTag(), true);  //true: 重新放入队列
////                        channel.basicAck(envelope.getDeliveryTag(), false);// 确认消费
//                    }catch (Exception e) {
//                        logger.error("游戏插入失败",e);
//                        channel.abort();  //此操作中的所有异常将被丢弃
//                    }finally {
////                        channel.basicAck(envelope.getDeliveryTag(),false);
//                    }
//                }
//            };
//            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,consumer);
//        }catch (IOException e) {
//            logger.error("mq消费异常",e);
//            e.printStackTrace();
//        }

    }

}
