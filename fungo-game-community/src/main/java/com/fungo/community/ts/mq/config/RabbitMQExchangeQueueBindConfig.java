package com.fungo.community.ts.mq.config;

import com.game.common.ts.mq.config.RabbitMQConfig;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:rabbitMQ exchange和queue绑定配置
 * 其中:exchange 消息发送模式：
 * 直连路由(Direct)
 * 通配符(Topic)
 * queue定义： orderTopicQueue
 * @Date: Create in 2019-05-14
 */
@Configuration
@AutoConfigureAfter(RabbitMQConfig.class)
public class RabbitMQExchangeQueueBindConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQExchangeQueueBindConfig.class);

    //init exchage
    @Bean
    TopicExchange createTopicExchange() {
        //配置持久化
        Map<String, Object> arguments = new HashMap<>(4);
        TopicExchange contractTopicExchange = new TopicExchange(RabbitMQEnum.Exchange.EXCHANGE_TOPIC.getName(), true, false, arguments);
        LOGGER.info("RabbitMqExchangeQueueBindConfig-createTopicExchange-create-success");
        return contractTopicExchange;
    }


    @Bean
    DirectExchange createDirectExchange() {
        //配置持久化
        Map<String, Object> arguments = new HashMap<>(4);
        DirectExchange contractDirectExchange = new DirectExchange(RabbitMQEnum.Exchange.EXCHANGE_DIRECT.getName(), true, false, arguments);
        LOGGER.info("RabbitMqExchangeQueueBindConfig-createDirectExchange-create-success");
        return contractDirectExchange;
    }


    //queue bing exchange
    @Bean("topicQueue")
    Queue topicQueue() {
        //队列持久化
        Queue queue = new Queue(RabbitMQEnum.MQQueueName.MQ_QUEUE_TOPIC_NAME_DEFAULT.getName(), true, false, false);
        LOGGER.info("RabbitMqExchangeQueueBindConfig-topicQueue-create-success");
        return queue;
    }

    @Bean("directQueue")
    Queue directQueue() {
        //队列持久化
        Queue queue = new Queue(RabbitMQEnum.MQQueueName.MQ_QUEUE_DIRECT_NAME_DEFAULT.getName(), true, false, false);
        LOGGER.info("RabbitMqExchangeQueueBindConfig-directQueue-create-success");
        return queue;
    }

    @Bean("bindingDirectExchageQueue")
    Binding bindingDirectExchageQueue() {
        Binding binding = BindingBuilder.bind(directQueue()).to(createDirectExchange()).with(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName());
        LOGGER.info("RabbitMqExchangeQueueBindConfig-bindingDirectExchageQueue-create-success");
        return binding;
    }

    @Bean("bindingTopicExchageQueue")
    Binding bindingTopicExchageQueue() {
        Binding binding = BindingBuilder.bind(topicQueue()).to(createTopicExchange()).with(RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName());
        LOGGER.info("RabbitMqExchangeQueueBindConfig-bindingTopicExchageQueue-create-success");
        return binding;
    }

}
