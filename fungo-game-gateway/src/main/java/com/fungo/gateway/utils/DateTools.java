package com.fungo.gateway.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneOffset;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/12/6
 */
public class DateTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateTools.class);

    /**
     * 功能描述: 将当前时间加 指定得天数返回时间戳
     * @param: []
     * @return: java.lang.String
     * @date: 2019/11/21 10:27
     */
    public static long getAddDays(int days){
        try {
            LocalDate afterThreeDays = LocalDate.now().plusDays(days);
            long day1 = afterThreeDays.atStartOfDay( ZoneOffset.ofHours(8)).toInstant().toEpochMilli();
            return day1;
        }catch (Exception e){
            LOGGER.error( "将当前时间加指定得天数返回时间戳出现异常",e);
        }
        return System.currentTimeMillis()+100000;

    }
}
