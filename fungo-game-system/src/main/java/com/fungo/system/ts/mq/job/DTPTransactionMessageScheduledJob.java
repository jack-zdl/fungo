package com.fungo.system.ts.mq.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author:Mxf <a href="mailto:m-java@163.com">m-java@163.com</a>
 * @Description:分布式事务之消息状态监控定时任务<br/> 主要分为两步：  <br/>
 * 1.处理状态为“待确认”但已超时的消息 <br/>
 * 2.处理状态为“发送中”但超时没有被成功消费确认的消息 <br/>
 * @Date: Create in 2019-05-14
 */
@Component
public class DTPTransactionMessageScheduledJob   {

    private static final Logger LOGGER = LoggerFactory.getLogger(DTPTransactionMessageScheduledJob.class);

    @Autowired
    private DTPTransactionMessageScheduledService dTPTransactionMessageScheduledService;

    //每60秒 执行任务
    @Scheduled(cron = "0/60 * * * * ?")
    public void execute() {

        LOGGER.info(".......DTPTransactionMessageScheduledJob-execute-start...." );

        Map<String ,Object> param = new HashMap<String ,Object>();

        LOGGER.info("执行(处理[waiting_confirm]状态的消息)任务开始");
                 dTPTransactionMessageScheduledService.handleWaitingConfirmTimeOutMessages(param);
        LOGGER.info("执行(处理[waiting_confirm]状态的消息)任务结束");

        try {
            LOGGER.info("........睡眠1秒......");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LOGGER.info("执行(处理[SENDING]的消息)任务开始");
                    dTPTransactionMessageScheduledService.handleSendingTimeOutMessage(param);
        LOGGER.info("执行(处理[SENDING]的消息)任务结束");
    }
}
