package com.game.common.ts.mq.service.impl;


import com.game.common.ts.mq.dto.MQMessageSender;
import com.game.common.ts.mq.service.MQDataSendService;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("sQDataSendService")
public class MQDataSendServiceImpl implements RabbitTemplate.ConfirmCallback, MQDataSendService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQDataSendServiceImpl.class);

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MQDataSendServiceImpl(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitTemplate.setConfirmCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean b, String s) {

    }


    @Override
    public void sendtMQDirect(MQMessageSender msgSend) throws Exception {
        if (null == msgSend) {
            throw new BusinessException("-1", "MQMessageSender is null");
        }
        if (StringUtils.isEmpty(msgSend.getRoutingKey())) {
            throw new BusinessException("-1", "MQMessageSender 路由不能为空");
        }
        LOGGER.info("MQDataSendServiceImpl-sendtMQDirect-MQMessageSender:{}", msgSend.toString());
        excuteMQSend(msgSend);
    }


    @Override
    public void sendMQTopic(MQMessageSender msg) throws Exception {
        if (null == msg) {
            throw new BusinessException("-1", "MQMessageSender is null");
        }
        if (StringUtils.isEmpty(msg.getRoutingKey())) {
            throw new BusinessException("-1", "MQMessageSender 路由不能为空");
        }
        LOGGER.info("MQDataSendServiceImpl-sendMQTopic-MQMessageSender:{}", msg.toString());
        excuteMQSend(msg);
    }


    /**
     * 执行MQ发送
     * @param msgSend
     */
    private void excuteMQSend(MQMessageSender msgSend) {
        if (StringUtils.isNotEmpty(msgSend.getExchange())) {
            rabbitTemplate.convertAndSend(msgSend.getExchange(), msgSend.getRoutingKey(), msgSend.getContent(), new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //设置消息持久化
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    //设置消息过期时间 30秒没被消费会丢弃
                   // message.getMessageProperties().setExpiration("30000");
                    return message;
                }
            });
        } else {
            rabbitTemplate.convertAndSend(msgSend.getRoutingKey(), msgSend.getContent(), new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    //设置消息持久化
                    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return message;
                }
            });
        }
    }

//-----
}
