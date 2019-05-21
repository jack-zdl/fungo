package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.entity.BasNotice;
import com.fungo.system.service.IPushService;
import com.fungo.system.service.SystemService;
import com.fungo.system.ts.mq.service.SystemMqConsumeService;
import com.game.common.bean.advice.BasNoticeDto;
import com.game.common.dto.ResultDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class SystemMqConsumeServiceImpl implements SystemMqConsumeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SystemMqConsumeServiceImpl.class);

    @Autowired
    private SystemService systemService;

    @Autowired
    private IPushService pushService;

    /**
     * 处理mq的消息
     *
     * @param msgDate mq的消息体
     */
    @Override
    public boolean processMsg(String msgDate) throws Exception {
        JSONObject json = JSONObject.parseObject(msgDate);
        Integer type = json.getInteger("type");
        String body = json.getString("body");

        // 处理通知消息体变更
        if(MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASNOTICEUPDATEBYID.getCode() == type){
            return processChangeNoticeDate(body);
        }

        // 添加一个通知记录
        if(MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASNOTICEINSERT.getCode() == type){
            return processInsertNotice(body);
        }

        //推送逻辑
        if(MQResultDto.GameMQDataType.GAME_DATA_TYPE_PUSH.getCode() == type){
            return processPush(body);
        }
        return false;
    }

    private boolean processPush(String body) throws Exception {
        Map map = JSON.parseObject(body, Map.class);
        String inviteMemberId = String.valueOf(map.get("inviteMemberId"));
        String targetType = String.valueOf(map.get("code"));
        String appVersion =  String.valueOf(map.get("appVersion"));
        if(StringUtil.isNull(inviteMemberId)||StringUtil.isNull(targetType)||StringUtil.isNull(appVersion)){
            LOGGER.warn("SystemMqConsumeServiceImpl.processPush参数缺失:"+body);
            return false;
        }
        pushService.push(inviteMemberId,Integer.parseInt(targetType),appVersion);
        return true;
    }

    /**
     * 添加一个通知记录
     */
    private boolean processInsertNotice(String body) {
        BasNotice basNotice = JSON.parseObject(body, BasNotice.class);
        basNotice.insert();
        return true;
    }

    /**
     * 处理通知消息体变更
     */
    private boolean processChangeNoticeDate(String body) {
        BasNoticeDto basNoticeDto = JSON.parseObject(body, BasNoticeDto.class);
        ResultDto<String> resultDto = systemService.updateNoticeDate(basNoticeDto.getId(), basNoticeDto.getData());
        return  resultDto.isSuccess();
    }




}

