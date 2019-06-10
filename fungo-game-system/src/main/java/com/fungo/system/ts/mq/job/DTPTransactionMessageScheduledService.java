package com.fungo.system.ts.mq.job;


import com.fungo.system.ts.mq.entity.TransactionMessageDomain;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.enums.DTPMessageStatusEnum;
import com.game.common.ts.mq.enums.TSMQPublicEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("dTPTransactionMessageScheduledService")
public class DTPTransactionMessageScheduledService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DTPTransactionMessageScheduledService.class);


    @Value("${message.max.send.times}")
    private Integer msgMaxSendTimes;


    @Value("${message.handle.duration}")
    private Integer msgHandleDuration;


    @Value("${message.send.1.time}")
    private Integer msgSendTimeOne;


    @Value("${message.send.2.time}")
    private Integer msgSendTimeTwo;

    @Value("${message.send.3.time}")
    private Integer msgSendTimeThree;

    @Value("${message.send.4.time}")
    private Integer msgSendTimeFour;

    @Value("${message.send.5.time}")
    private Integer msgSendTimeFive;

    @Autowired
    private ITransactionMessageService dTPTransactionMessageService;


    public void handleWaitingConfirmTimeOutMessages(Map<String, Object> param) {
        try {
            // 每页条数
            int numPerPage = 1000;
            // 一次最多处理页数
            int maxHandlePageCount = 3;
            //获取超过指定时间段 的时间点
            Date delayDate = getCreateTimeBeforeDate();
            TransactionMessageDomain messageDomain = new TransactionMessageDomain();
            // 取状态为“待确认”的消息
            messageDomain.setStatus(DTPMessageStatusEnum.WAITING_CONFIRM.getCode());
            // 取存放了多久的消息
            messageDomain.setCreateTime(delayDate);

            Map<String, TransactionMessageDomain> messageMapResult = getMessageMap(numPerPage, maxHandlePageCount, messageDomain, param);
            //执行处理
            this.excuteHandleWaitingConfirmTimeOutMessages(messageMapResult);

        } catch (Exception e) {
            LOGGER.error("处理[waiting_confirm]状态的消息异常", e);
        }

    }


    public void handleSendingTimeOutMessage(Map<String, Object> param) {

        try {
            // 每页条数
            int numPerPage = 1000;
            // 一次最多处理页数
            int maxHandlePageCount = 3;
            //获取超过指定时间段 的时间点
            Date delayDate = getCreateTimeBeforeDate();

            TransactionMessageDomain messageDomain = new TransactionMessageDomain();
            // 状态为“发送中”的消息
            messageDomain.setStatus(DTPMessageStatusEnum.SENDING.getCode());
            // 取存放了多久的消息
            messageDomain.setCreateTime(delayDate);
            // 取存活的发送中消息
            messageDomain.setAreadlyDead(TSMQPublicEnum.NO.getCode());

            Map<String, TransactionMessageDomain> messageMapResult = getMessageMap(numPerPage, maxHandlePageCount, messageDomain, param);
            //执行处理
            this.excuteHandleSendingTimeOutMessage(messageMapResult);
        } catch (Exception e) {
            LOGGER.error("处理发送中的消息异常", e);
        }
    }


    /**
     * 处理状态为“待确认”但已超时的消息.
     *
     * @param messageMap
     */
    private void excuteHandleWaitingConfirmTimeOutMessages(Map<String, TransactionMessageDomain> messageMap) {

        if (null == messageMap || messageMap.isEmpty()) {
            return;
        }
        LOGGER.info("开始处理[waiting_confirm]状态的消息,总条数[" + messageMap.size() + "]");

        // 单条消息处理（目前该状态的消息，消费队列全部是accounting，如果后期有业务扩充，需做队列判断，做对应的业务处理。）
        for (Map.Entry<String, TransactionMessageDomain> entry : messageMap.entrySet()) {
            TransactionMessageDomain message = entry.getValue();
            try {

                LOGGER.info("开始处理[waiting_confirm]消息ID为[" + message.getMessageId() + "]的消息");

                dTPTransactionMessageService.confirmAndSendMessage(message.getMessageId());

                LOGGER.info("结束处理[waiting_confirm]消息ID为[" + message.getMessageId() + "]的消息");
            } catch (Exception e) {
                LOGGER.error("处理[waiting_confirm]消息ID为[" + message.getMessageId() + "]的消息异常：", e);
            }
        }

    }


    /**
     * 处理[SENDING]状态的消息
     *
     * @param messageMap
     */
    private void excuteHandleSendingTimeOutMessage(Map<String, TransactionMessageDomain> messageMap) {

        if (null == messageMap || messageMap.isEmpty()) {
            return;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LOGGER.info("开始处理[SENDING]状态的消息,总条数[" + messageMap.size() + "]");

        // 根据配置获取通知间隔时间
        Map<Integer, Integer> notifyParam = getSendTime();

        // 单条消息处理
        for (Map.Entry<String, TransactionMessageDomain> entry : messageMap.entrySet()) {
            TransactionMessageDomain message = entry.getValue();
            try {
                LOGGER.info("开始处理[SENDING]消息ID为[" + message.getMessageId() + "]的消息");

                // 判断发送次数
                int maxTimes = msgMaxSendTimes;

                LOGGER.info("[SENDING]消息ID为[" + message.getMessageId() + "]的消息,已经重新发送的次数[" + message.getMessageSendTimes() + "]");

                // 如果超过最大发送次数直接退出
                if (maxTimes < message.getMessageSendTimes()) {
                    // 标记为死亡
                    dTPTransactionMessageService.setMessageToAreadlyDead(message.getMessageId());
                    continue;
                }

                // 判断是否达到发送消息的时间间隔条件
                int reSendTimes = message.getMessageSendTimes();
                int times = notifyParam.get(reSendTimes == 0 ? 1 : reSendTimes);
                long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
                long needTime = currentTimeInMillis - times * 60 * 1000;
                long hasTime = message.getEditTime().getTime();

                // 判断是否达到了可以再次发送的时间条件
                if (hasTime > needTime) {
                    LOGGER.info("currentTime[" + sdf.format(new Date()) + "],[SENDING]消息上次发送时间[" + sdf.format(message.getEditTime()) + "],必须过了[" + times + "]分钟才可以再发送。");
                    continue;
                }

                // 重新发送消息
                LOGGER.info("[SENDING]消息ID为[" + message.getMessageId() + "]的消息,执行重新发送");

                TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
                //bean copy dto-->entity
                BeanUtils.copyProperties(message, transactionMessageDto);

                dTPTransactionMessageService.reSendMessage(transactionMessageDto);

                LOGGER.info("结束处理[SENDING]消息ID为[" + message.getMessageId() + "]的消息");

            } catch (Exception e) {
                LOGGER.error("处理[SENDING]消息ID为[" + message.getMessageId() + "]的消息异常：", e);
            }
        }

    }


    /**
     * 根据分页参数及查询条件批量获取消息数据
     *
     * @param numPerPage         每页记录数
     * @param maxHandlePageCount 最多获取页数
     * @param messageDomain      业务查询条件
     * @return
     */
    private Map<String, TransactionMessageDomain> getMessageMap(int numPerPage, int maxHandlePageCount, TransactionMessageDomain messageDomain, Map<String, Object> param) {

        // 当前页
        int pageNum = 1;
        // 总页数
        int pageCount = 1;
        //每页条数
        int pageSize = 1000;

        Map<String, TransactionMessageDomain> messageMap = new HashMap<String, TransactionMessageDomain>();

        TransactionMessageDto transactionMessageDto = new TransactionMessageDto();
        if (null != messageDomain){
            BeanUtils.copyProperties(messageDomain, transactionMessageDto);
        }

        Map<String, Object> msgMap = dTPTransactionMessageService.listPage(pageNum, numPerPage, transactionMessageDto);

        List<TransactionMessageDomain> recordList = (List<TransactionMessageDomain>) msgMap.get("msgList");
        if (null == recordList || recordList.isEmpty()) {
            return messageMap;
        }

        Long rows = (Long) msgMap.get("rows");
        pageCount = (rows.intValue() - 1) / numPerPage + 1;

        for (final Object obj : recordList) {
            TransactionMessageDomain message = (TransactionMessageDomain) obj;
            messageMap.put(String.valueOf(message.getMessageId()), message);
        }

        if (pageCount > maxHandlePageCount) {
            pageCount = maxHandlePageCount;
        }

        for (pageNum = 2; pageNum <= pageCount; pageNum++) {

            msgMap = dTPTransactionMessageService.listPage(pageNum, numPerPage, transactionMessageDto);
            recordList = (List<TransactionMessageDomain>) msgMap.get("msgList");

            if (null == recordList || recordList.isEmpty()) {
                break;
            }

            for (final Object obj : recordList) {
                TransactionMessageDomain message = (TransactionMessageDomain) obj;
                messageMap.put(String.valueOf(message.getMessageId()), message);
            }
        }

        msgMap = null;
        recordList = null;

        return messageMap;
    }


    /**
     * 获取配置的开始处理的时间
     *
     * @return
     */
    private String getStrCreateTimeBefore() {
        //最大180S,3分钟
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(currentTimeInMillis - msgHandleDuration * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }


    /**
     * 获取配置的开始处理的时间
     *
     * @return
     */
    private Date getCreateTimeBeforeDate() {
        //最大180S,3分钟
        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date date = new Date(currentTimeInMillis - msgHandleDuration * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


    /**
     * 根据配置获取通知间隔时间
     *
     * @return
     */
    private Map<Integer, Integer> getSendTime() {
        Map<Integer, Integer> notifyParam = new HashMap<Integer, Integer>();
        notifyParam.put(1, msgSendTimeOne);
        notifyParam.put(2, msgSendTimeTwo);
        notifyParam.put(3, msgSendTimeThree);
        notifyParam.put(4, msgSendTimeFour);
        notifyParam.put(5, msgSendTimeFive);
        return notifyParam;
    }

}
