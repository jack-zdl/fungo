package com.game.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameStatusDescUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameStatusDescUtil.class);


    public static String gameStatusDesc(String databaseDesc){
        if(CommonUtil.isNull(databaseDesc)){
            return null;
        }
        try {
            JSONObject jsonObject = JSONObject.parseObject(databaseDesc);
            String dsc = jsonObject.getString("dsc");
            if(CommonUtil.isNull(dsc)){
                return null;
            }
            long currentTime = System.currentTimeMillis();

            Long starDate = jsonObject.getLong("starDate");

            Long endDate = jsonObject.getLong("endDate");

            if(starDate == null && endDate == null){
                return dsc;
            }

            if(currentTime>=starDate && currentTime<= endDate ){
                return dsc;
            }
        }catch (Exception e){
            LOGGER.error("转换游戏状态描述错误",e);
        }
        return null;
    }
}
