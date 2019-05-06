package com.fungo.community.helper;

import com.game.common.entiry.Game;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
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

    @RabbitListener(queues = MQConfig.TOPIC_QUEUE_GAME_UPDATE)
    public void topicReceiveGameUpdate(@Payload Game game, Message message, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag, Channel channel){
//        System.out.println("receive direct message:" + game.toString());
        boolean autoAck=false;
        //消息消费完成确认
//        try {
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false,false);
//            Consumer consumer =  new DefaultConsumer(channel)
//            {
//                @Override
//                public void handleDelivery(String consumerTag,Envelope envelope,AMQP.BasicProperties properties,byte[] body) throws IOException {
//                    try {
//                        System.out.println("receive direct message:" + game.toString());
////                        channel.basicAck(envelope.getDeliveryTag(), true);
//                        channel.basicReject(envelope.getDeliveryTag(), false);
////                        channel.basicNack(envelope.getDeliveryTag(), false, true);
////                        channel.basicReject(envelope.getDeliveryTag(), true);  //true: 重新放入队列
////                        channel.basicAck(envelope.getDeliveryTag(), false);// 确认消费
//                    }catch (Exception e) {
//                        channel.abort();  //此操作中的所有异常将被丢弃
//                    }finally {
////                        channel.basicAck(envelope.getDeliveryTag(),false);
//                    }
//                }
//            };
//            channel.basicConsume(MQConfig.TOPIC_QUEUE_GAME_UPDATE, autoAck,"",consumer);
//        }catch (IOException e) {
//            e.printStackTrace();
//        }

    }


}
