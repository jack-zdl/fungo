package com.fungo.games.listener.service.impl;

import com.alibaba.fastjson.JSON;
import com.fungo.games.service.IMQService;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.GameReleaseLogDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.service.MQDataReceiveService;
import com.game.common.ts.mq.service.MQDataSendService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service("mQDataReceiveService")
public class MQDataReceiveServiceImpl implements MQDataReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQDataReceiveServiceImpl.class);

    @Autowired
    private IMQService imqService;


    @Override
    public boolean onMessageWithMQDirect(String msgData) throws Exception {
        if (StringUtils.isBlank(msgData)) {
            return true;
        }
        try {
            LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQDirect-msg:{}", msgData);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onMessageWithMQTopic(String msgData) {
        if (StringUtils.isBlank(msgData)) {
            return true;
        }
        //调用业务方处理消息
        LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQTopic-msg:{}", msgData);
        TransactionMessageDto transactionMessageDto = JSON.parseObject(msgData, TransactionMessageDto.class);
        String messageBody = transactionMessageDto.getMessageBody();
        MQResultDto mqResultDto = JSON.parseObject(messageBody, MQResultDto.class);
        if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMEUPDATE.getCode()){
//                游戏更新
            Object body = mqResultDto.getBody();
            GameDto gameDto1 = JSON.parseObject(body.toString(), GameDto.class);
//                mq游戏更新
            return imqService.mqGameUpdate(gameDto1);
        }
        if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMEINSERT.getCode()){
//                游戏添加
            Object body = mqResultDto.getBody();
            GameDto gameDto1 = JSON.parseObject(body.toString(), GameDto.class);
//                mq游戏添加
            return imqService.mqGameInsert(gameDto1);
        }
        if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMERELEASELOG.getCode()){
//                插入游戏版本日志审批
            Object body = mqResultDto.getBody();
            GameReleaseLogDto gameReleaseLogDto = JSON.parseObject(body.toString(), GameReleaseLogDto.class);
//                mq插入游戏版本日志审批
            return imqService.mqGameReleaseLogInsert(gameReleaseLogDto);
        }
        if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_counter.getCode()){
//                计数器(动态表)
            Object body = mqResultDto.getBody();
            Map map = JSON.parseObject(body.toString(), Map.class);
//                mq计数器(动态表)
            return imqService.mqCounterUpdate(map);
        }
        if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_counter.getCode()){
//                计数器(动态表)
            Object body = mqResultDto.getBody();
            Map map = JSON.parseObject(body.toString(), Map.class);
//                mq计数器(动态表)
            return imqService.mqCounterUpdate(map);
        }
        return false;
    }
}
