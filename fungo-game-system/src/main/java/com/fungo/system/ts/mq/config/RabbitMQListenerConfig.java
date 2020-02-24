package com.fungo.system.ts.mq.config;

import com.game.common.ts.mq.config.RabbitMQConfig;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.ts.mq.service.MQDataReceiveService;
import com.rabbitmq.client.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.nio.charset.Charset;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:rabbitMQ 监听器配置
 * @Date: Create in 2019-05-14
 */
@Configuration
@AutoConfigureAfter(RabbitMQConfig.class)
@Order(2)
public class RabbitMQListenerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListenerConfig.class);

    @Autowired
    private MQDataReceiveService mQDataReceiveService;


    //------direct---
  /*  @Bean("MQDirectQueueContainer")
    public MessageListenerContainer mqDirectMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMQEnum.MQQueueName.MQ_QUEUE_DIRECT_NAME_DEFAULT.getName());
        container.setMessageListener(mqDirectMessageListener());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }*/

    @Bean("MQDirectQueueListener")
    public ChannelAwareMessageListener mqDirectMessageListener() {
        return new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {

                String msgBody = StringUtils.toEncodedString(message.getBody(), Charset.forName("UTF-8"));

                //同步 业务处理
               boolean isExcute = mQDataReceiveService.onMessageWithMQDirect(msgBody);
               if (isExcute) {
                   channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                   //channel.basicNack(message.getMessageProperties().getDeliveryTag(),false,true);
               }
            }
        };
    }


    //--------topic------

//    @Bean("MQTopicQueueContainer")
//    public MessageListenerContainer mqTopicMessageListenerContainer(ConnectionFactory connectionFactory) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//        container.setConnectionFactory(connectionFactory);
//        container.setQueueNames(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_DEFAULT.getName());
//        container.setMessageListener(mqTopicMessageListener());
//        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
//        return container;
//    }
//
//
//    @Bean("MQTopicQueueListener")
//    public ChannelAwareMessageListener mqTopicMessageListener() {
//        return new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//
//                String msgBody = StringUtils.toEncodedString(message.getBody(), Charset.forName("UTF-8"));
//                LOGGER.info("MQTopicQueueListener-onMessage-msgBody:{}", msgBody);
//
//                //同步业务处理
////                mQDataReceiveService.onMessageWithMQTopic(msgBody);
//                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//            }
//        };
//    }
        /*************  **************/


    @Bean("MqTopicMessageCountListenerContainer")
    public MessageListenerContainer mqTopicMessageCountListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM.getName(),RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_SYSTEM_USER.getName());
        container.setMessageListener(mqTopicMessageCountListener());
        //手动确认消息
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }


    @Bean("MQTopicQueueCountListener")
    public ChannelAwareMessageListener mqTopicMessageCountListener() {
        return new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {

                String msgBody = StringUtils.toEncodedString(message.getBody(), Charset.forName("UTF-8"));
                //同步业务处理
                mQDataReceiveService.onMessageWithMQTopic(msgBody);
                //无论是否成功都ack  - 重试交给job
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        };
    }


}
