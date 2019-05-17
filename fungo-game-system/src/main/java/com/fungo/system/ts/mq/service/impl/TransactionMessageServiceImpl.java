package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fungo.system.ts.mq.dao.service.SysTsMsgDaoService;
import com.fungo.system.ts.mq.entity.TransactionMessageDomain;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.ts.mq.dto.MQMessageSender;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.DTPMessageStatusEnum;
import com.game.common.ts.mq.enums.RabbitMQEnum;
import com.game.common.ts.mq.enums.TSMQPublicEnum;
import com.game.common.ts.mq.service.MQDataSendService;
import com.game.common.util.PKUtil;
import com.game.common.util.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TransactionMessageServiceImpl implements ITransactionMessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionMessageServiceImpl.class);

    //全局id基于集群机器序号生成
    @Value("${sys.config.fungo.cluster.index}")
    private String clusterIndex;

    @Value("${message.max.send.times}")
    private Integer msgMaxSendTimes;

    @Autowired
    private SysTsMsgDaoService sysTsMsgDaoService;

    @Autowired
    private MQDataSendService mQDataSendService;


    @Override
    public Long saveMessageWaitingConfirm(TransactionMessageDto transactionMessageDto) throws BusinessException {

        if (transactionMessageDto == null) {
            throw new BusinessException("-1", "保存的消息为空");
        }

        if (StringUtils.isBlank(transactionMessageDto.getConsumerQueue())) {
            throw new BusinessException("-1", "消息的消费队列不能为空 ");
        }

        Date currentDate = new Date();
        int clusterIndex_i = Integer.parseInt(clusterIndex);


        TransactionMessageDomain messageDomain = new TransactionMessageDomain();

        //bean copy dto-->entity
        BeanUtils.copyProperties(transactionMessageDto, messageDomain);

        messageDomain.setMessageId(PKUtil.getInstance(clusterIndex_i).longPK());

        messageDomain.setStatus(DTPMessageStatusEnum.WAITING_CONFIRM.getCode());
        messageDomain.setAreadlyDead(TSMQPublicEnum.NO.getCode());
        messageDomain.setMessageSendTimes(0);


        messageDomain.setCreateTime(currentDate);
        messageDomain.setEditTime(currentDate);

        boolean isInsert = messageDomain.insert();

        LOGGER.info("--ts--mq---预存储消息--isInsert:{}---messageDomain:{}", isInsert, JSON.toJSON(messageDomain));

        return messageDomain.getMessageId();
    }


    @Override
    public void confirmAndSendMessage(Long messageId) throws BusinessException {
        final TransactionMessageDomain message = getMessageByMessageId(messageId);
        if (message == null) {
            throw new BusinessException("-1", "根据消息id查找的消息为空");
        }

        message.setStatus(DTPMessageStatusEnum.SENDING.getCode());
        message.setEditTime(new Date());

        //更新消息
        message.updateById();

        //向MQ发送
        String routingKey = message.getRoutingKey();
        if (StringUtils.isBlank(routingKey)) {
            routingKey = RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName();
        }

        MQMessageSender msgSender = new MQMessageSender();
        msgSender.setExchange(RabbitMQEnum.Exchange.EXCHANGE_TOPIC.getName());
        msgSender.setRoutingKey(routingKey);
        msgSender.setContent(JSONObject.toJSONString(message));

        try {
            mQDataSendService.sendMQTopic(msgSender);
            LOGGER.info("--ts--mq---确认并发送消息执行完成--msgSenderDto:{}", JSON.toJSON(msgSender));
        } catch (Exception e) {
            LOGGER.error("--ts--mq---确认并发送消息--发送失败", e);
        }
    }

    @Override
    public Long saveAndSendMessage(TransactionMessageDto transactionMessageDto) throws BusinessException {
        if (transactionMessageDto == null) {
            throw new BusinessException("-1", "保存的消息为空");
        }

        if (StringUtils.isBlank(transactionMessageDto.getConsumerQueue())) {
            throw new BusinessException("-1", "消息的消费队列不能为空 ");
        }


        TransactionMessageDomain messageDomain = new TransactionMessageDomain();

        //bean copy dto-->entity
        BeanUtils.copyProperties(transactionMessageDto, messageDomain);

        Date currentDate = new Date();
        int clusterIndex_i = Integer.parseInt(clusterIndex);

        messageDomain.setMessageId(PKUtil.getInstance(clusterIndex_i).longPK());
        messageDomain.setStatus(DTPMessageStatusEnum.SENDING.getCode());
        messageDomain.setAreadlyDead(TSMQPublicEnum.NO.getCode());
        messageDomain.setMessageSendTimes(0);
        messageDomain.setCreateTime(currentDate);
        messageDomain.setEditTime(currentDate);

        boolean isInsert = messageDomain.insert();

        //向MQ发送
        String routingKey = messageDomain.getRoutingKey();
        if (StringUtils.isBlank(routingKey)) {
            routingKey = RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName();
        }

        MQMessageSender msgSender = new MQMessageSender();
        msgSender.setExchange(RabbitMQEnum.Exchange.EXCHANGE_TOPIC.getName());
        msgSender.setRoutingKey(routingKey);
        msgSender.setContent(JSONObject.toJSONString(messageDomain));

        try {

            mQDataSendService.sendMQTopic(msgSender);

            LOGGER.info("--ts--mq---存储并发送消息执行完成--msgSenderDto:{}----isInsert:{}", JSON.toJSON(msgSender), isInsert);

        } catch (Exception e) {
            LOGGER.error("--ts--mq---存储并发送消息--发送失败", e);
        }

        return messageDomain.getMessageId();
    }


    @Override
    public void directSendMessage(TransactionMessageDto transactionMessageDto) throws BusinessException {
        if (transactionMessageDto == null) {
            throw new BusinessException("-1", "保存的消息为空");
        }

        if (StringUtils.isEmpty(transactionMessageDto.getConsumerQueue())) {
            throw new BusinessException("-1", "消息的消费队列不能为空 ");
        }

        //向MQ发送
        String routingKey = transactionMessageDto.getRoutingKey();
        if (StringUtils.isBlank(routingKey)) {
            routingKey = RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName();
        }

        MQMessageSender msgSender = new MQMessageSender();
        msgSender.setExchange(RabbitMQEnum.Exchange.EXCHANGE_TOPIC.getName());
        msgSender.setRoutingKey(routingKey);
        msgSender.setContent(JSONObject.toJSONString(transactionMessageDto));

        try {
            mQDataSendService.sendMQTopic(msgSender);
            LOGGER.info("--ts--mq---直接发送消息执行完成--msgSenderDto:{}", JSON.toJSON(msgSender));
        } catch (Exception e) {
            LOGGER.error("--ts--mq---直接发送消息--发送失败", e);
        }
    }


    @Override
    public void deleteMessageByMessageId(Long messageId) throws BusinessException {
        if (messageId <= 0) {
            throw new BusinessException("-1", "消息ID不能小于或者等于0");
        }
        boolean isDelete = sysTsMsgDaoService.deleteById(messageId);
        LOGGER.info("--ts--mq-----根据消息ID删除消息--messageId:{}--isDelete:{}", messageId, isDelete);
    }


    //------------------------------------------------------------------------------------------------------------
    @Override
    public void reSendMessage(TransactionMessageDto transactionMessageDto) throws BusinessException {
        if (transactionMessageDto == null) {
            throw new BusinessException("-1", "保存的消息为空");
        }

        if (StringUtils.isEmpty(transactionMessageDto.getConsumerQueue())) {
            throw new BusinessException("-1", "消息的消费队列不能为空 ");
        }

        TransactionMessageDomain messageDomain = new TransactionMessageDomain();

        //bean copy dto-->entity
        BeanUtils.copyProperties(transactionMessageDto, messageDomain);

        messageDomain.addSendTimes();
        messageDomain.setEditTime(new Date());
        messageDomain.updateById();

        //向MQ发送
        String routingKey = transactionMessageDto.getRoutingKey();
        if (StringUtils.isBlank(routingKey)) {
            routingKey = RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName();
        }
        MQMessageSender msgSender = new MQMessageSender();
        msgSender.setExchange(RabbitMQEnum.Exchange.EXCHANGE_TOPIC.getName());
        msgSender.setRoutingKey(routingKey);
        msgSender.setContent(JSONObject.toJSONString(messageDomain));

        try {
            mQDataSendService.sendMQTopic(msgSender);

            LOGGER.info("--ts--mq---重发消息执行完成--msgSenderDto:{}", JSON.toJSON(msgSender));
        } catch (Exception e) {
            LOGGER.error("--ts--mq---重发送消息--发送失败", e);
        }
    }


    @Override
    public void reSendMessageByMessageId(Long messageId) throws BusinessException {
        final TransactionMessageDomain message = getMessageByMessageId(messageId);
        if (message == null) {
            throw new BusinessException("-1", "根据消息id查找的消息为空");
        }

        int maxTimes = msgMaxSendTimes;
        if (message.getMessageSendTimes() >= maxTimes) {
            message.setAreadlyDead(TSMQPublicEnum.YES.getCode());
        }

        message.setEditTime(new Date());
        message.setMessageSendTimes(message.getMessageSendTimes() + 1);
        message.updateById();

        //向MQ发送
        String routingKey = message.getRoutingKey();
        if (StringUtils.isBlank(routingKey)) {
            routingKey = RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName();
        }
        MQMessageSender msgSender = new MQMessageSender();
        msgSender.setExchange(RabbitMQEnum.Exchange.EXCHANGE_TOPIC.getName());
        msgSender.setRoutingKey(routingKey);
        msgSender.setContent(JSONObject.toJSONString(message));

        try {
            mQDataSendService.sendMQTopic(msgSender);
            LOGGER.info("--ts--mq---根据messageId重发某条消息执行完成--msgSenderDto:{}", JSON.toJSON(msgSender));
        } catch (Exception e) {
            LOGGER.error("--ts--mq---根据messageId重发某条消息--发送失败", e);
        }
    }


    @Override
    public void setMessageToAreadlyDead(Long messageId) throws BusinessException {
        TransactionMessageDomain message = getMessageByMessageId(messageId);
        if (message == null) {
            throw new BusinessException("-1", "根据消息id查找的消息为空");
        }

        message.setAreadlyDead(TSMQPublicEnum.YES.getCode());
        message.setEditTime(new Date());
        boolean isUpdate = message.updateById();

        LOGGER.info("--ts--mq-----将消息标记为死亡消息--messageId:{}--isUpdate:{}", messageId, isUpdate);
    }


    @Override
    public void reSendAllDeadMessageByQueueName(String queueName, int batchSize) throws BusinessException {

        int numPerPage = 1000;
        if (batchSize > 0 && batchSize < 100) {
            numPerPage = 100;
        } else if (batchSize > 100 && batchSize < 5000) {
            numPerPage = batchSize;
        } else if (batchSize > 5000) {
            numPerPage = 5000;
        } else {
            numPerPage = 1000;
        }

        int pageNum = 1;
        int pageCount = 1;

        Map<String, TransactionMessageDomain> messageMap = new HashMap<String, TransactionMessageDomain>();

        TransactionMessageDto messageDto = new TransactionMessageDto();
        messageDto.setConsumerQueue(queueName);
        //查询已死亡的消息
        messageDto.setAreadlyDead(TSMQPublicEnum.YES.getCode());


        Map<String, Object> msgMap = listPage(pageNum, numPerPage, messageDto);

        List<TransactionMessageDomain> recordList = (List<TransactionMessageDomain>) msgMap.get("msgList");
        if (null == recordList || recordList.isEmpty()) {
            return;
        }
        Long rows = (Long) msgMap.get("rows");
        pageCount = (rows.intValue() - 1) / numPerPage + 1;

        for (final Object obj : recordList) {
            final TransactionMessageDomain message = (TransactionMessageDomain) obj;
            messageMap.put(String.valueOf(message.getMessageId()), message);
        }


        for (pageNum = 2; pageNum <= pageCount; pageNum++) {

            msgMap = listPage(pageNum, numPerPage, messageDto);

            recordList = (List<TransactionMessageDomain>) msgMap.get("msgList");
            if (null == recordList || recordList.isEmpty()) {
                break;
            }

            for (final Object obj : recordList) {
                final TransactionMessageDomain message = (TransactionMessageDomain) obj;
                messageMap.put(String.valueOf(message.getMessageId()), message);
            }
        }

        msgMap = null;
        recordList = null;

        for (Map.Entry<String, TransactionMessageDomain> entry : messageMap.entrySet()) {
            TransactionMessageDomain message = entry.getValue();

            message.setEditTime(new Date());
            message.setMessageSendTimes(message.getMessageSendTimes() + 1);
            message.updateById();

            //向MQ发送
            String routingKey = message.getRoutingKey();
            if (StringUtils.isBlank(routingKey)) {
                routingKey = RabbitMQEnum.QueueRouteKey.QUEUE_ROUTE_KEY_DEFAULT.getName();
            }
            MQMessageSender msgSender = new MQMessageSender();
            msgSender.setExchange(RabbitMQEnum.Exchange.EXCHANGE_TOPIC.getName());
            msgSender.setRoutingKey(routingKey);
            msgSender.setContent(JSONObject.toJSONString(message));

            try {
                mQDataSendService.sendMQTopic(msgSender);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Map<String, Object> listPage(int pageNum, int pageSize, TransactionMessageDto transactionMessageDto) throws BusinessException {

        EntityWrapper<TransactionMessageDomain> messageDomainEntityWrapper = new EntityWrapper<TransactionMessageDomain>();
        messageDomainEntityWrapper.orderBy("create_time", true);

        Page<TransactionMessageDomain> domainPage = new Page<>(pageNum, pageSize);

        Page<TransactionMessageDomain> selectPage = this.sysTsMsgDaoService.selectPage(domainPage, messageDomainEntityWrapper);

        Long total = 0L;
        List<TransactionMessageDomain> messageDomainList = null;

        if (null != selectPage) {
            //msgList
            messageDomainList = selectPage.getRecords();

            //rows
            int rows = selectPage.getTotal();
            total = Long.parseLong(String.valueOf(rows));
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("rows", total);
        resultMap.put("msgList", messageDomainList);

        return resultMap;
    }

    @Override
    public Map<String, Object> listPageWithLessCreate(TransactionMessageDto transactionMessageDto, Map<String, Object> param) throws BusinessException {
        return null;
    }


    @Override
    public TransactionMessageDomain getMessageByMessageId(Long messageId) throws BusinessException {
        return sysTsMsgDaoService.selectById(messageId);
    }

    //-------------

}
