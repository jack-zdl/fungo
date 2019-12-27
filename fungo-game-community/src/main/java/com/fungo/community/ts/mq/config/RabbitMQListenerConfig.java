package com.fungo.community.ts.mq.config;

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

import java.nio.charset.Charset;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:rabbitMQ 监听器配置
 * @Date: Create in 2019-05-14
 */
@Configuration
@AutoConfigureAfter(RabbitMQConfig.class)
public class RabbitMQListenerConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQListenerConfig.class);

    @Autowired
    private MQDataReceiveService mQDataReceiveService;

    //监听社区-文章队列
    @Bean("mqTopicCommunityPostMessageListenerContainer")
    public MessageListenerContainer mqTopicCommunityPostMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_COMMUNITY_POST.getName());
        container.setMessageListener(mqTopicMessageListener());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }

    //社区-心情队列
  /*  @Bean("mqTopicCommunityMoodMessageListenerContainer")
    public MessageListenerContainer mqTopicCommunityMoodMessageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_COMMUNITY_MOOD.getName());
        container.setMessageListener(mqTopicMessageListener());
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        return container;
    }*/


    @Bean("MQTopicQueueListener")
    public ChannelAwareMessageListener mqTopicMessageListener() {
        return new ChannelAwareMessageListener() {
            @Override
            public void onMessage(Message message, Channel channel) throws Exception {
                String msgBody = StringUtils.toEncodedString(message.getBody(), Charset.forName("UTF-8"));
                LOGGER.info("MQTopicQueueListener-onMessage-msgBody:{}", msgBody);
                //同步业务处理
                mQDataReceiveService.onMessageWithMQTopic(msgBody);
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            }
        };
    }


    //---------
}
