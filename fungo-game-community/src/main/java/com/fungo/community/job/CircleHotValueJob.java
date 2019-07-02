package com.fungo.community.job;

import com.fungo.community.service.CircleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>定时任务</p>
 *
 * @Author: dl.zhang
 * @Date: 2019/7/2
 */
@Component
public class CircleHotValueJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircleHotValueJob.class);

    @Autowired
    private CircleService circleService;

    //每60秒 执行任务
//    @Scheduled(cron = "0/60 * * * * ?")
    public void execute() {

        LOGGER.info(".......CircleHotValueJob-execute-start...." );
        try {
            LOGGER.info("执行CircleHotValueJob任务开始");
            circleService.updateCircleHotValue();
            LOGGER.info("执行CircleHotValueJob任务结束");
            LOGGER.info("........睡眠1秒......");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
