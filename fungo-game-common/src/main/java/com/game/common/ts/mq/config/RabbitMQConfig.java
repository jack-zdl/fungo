package com.game.common.ts.mq.config;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:rabbitMQ 参数配置
 *
 * @Date: Create in 2019-05-14
 */
@Configuration
public class RabbitMQConfig {

    //资源owner账户 ID 信息
    private static final long RESOURCE_OWNER_ID = 1811420479436984L;

    @Autowired
    private RabbitProperties rabbitProperties;


    @Bean
    public ConnectionFactory connectionFactory() {
        com.rabbitmq.client.ConnectionFactory rabbitConnectionFactory =
                new com.rabbitmq.client.ConnectionFactory();
        rabbitConnectionFactory.setHost(rabbitProperties.getHost());
        rabbitConnectionFactory.setPort(rabbitProperties.getPort());
        rabbitConnectionFactory.setVirtualHost(rabbitProperties.getVirtualHost());

        AliyunCredentialsProvider credentialsProvider = new AliyunCredentialsProvider(
                rabbitProperties.getUsername(), rabbitProperties.getPassword(), RESOURCE_OWNER_ID);
        rabbitConnectionFactory.setCredentialsProvider(credentialsProvider);
        rabbitConnectionFactory.setAutomaticRecoveryEnabled(true);
        rabbitConnectionFactory.setNetworkRecoveryInterval(5000);
        ConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitConnectionFactory);
        ((CachingConnectionFactory) connectionFactory).setPublisherConfirms(rabbitProperties.isPublisherConfirms());
        ((CachingConnectionFactory) connectionFactory).setPublisherReturns(rabbitProperties.isPublisherReturns());
        return connectionFactory;
    }


    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        return template;
    }

}
