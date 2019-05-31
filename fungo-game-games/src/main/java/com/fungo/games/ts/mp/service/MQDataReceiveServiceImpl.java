package com.fungo.games.ts.mp.service;

import com.alibaba.fastjson.JSON;
import com.fungo.games.consts.MQMessageTypeConstant;
import com.fungo.games.feign.MQFeignClient;
import com.fungo.games.service.IMQService;
import com.game.common.dto.GameDto;
import com.game.common.dto.game.GameEvaluationDto;
import com.game.common.dto.game.GameReleaseLogDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import com.game.common.ts.mq.service.MQDataReceiveService;
import com.game.common.util.UniqueIdCkeckUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;


@Service("mQDataReceiveService")
public class MQDataReceiveServiceImpl implements MQDataReceiveService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQDataReceiveServiceImpl.class);

    @Autowired
    private IMQService imqService;

    @Autowired
    private MQFeignClient mqFeignClient;


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
    @Transactional
    public boolean onMessageWithMQTopic(String msgData) {
        if (StringUtils.isBlank(msgData)) {
            return true;
        }
        //调用业务方处理消息
        LOGGER.info("MQDataReceiveServiceImpl-onMessageWithMQTopic-msg:{}", msgData);
        Long messageId = null;
        try {
            TransactionMessageDto transactionMessageDto = JSON.parseObject(msgData, TransactionMessageDto.class);
//        消息体内容
            String messageBody = transactionMessageDto.getMessageBody();
            messageId = transactionMessageDto.getMessageId();
            //校验是否重复消费
            if (!UniqueIdCkeckUtil.checkUniqueIdAndSave(messageId.toString())) {
                LOGGER.warn("系统服务-重复消费驳回,消息内容:"+messageId);
                return true;
            }
            MQResultDto mqResultDto = JSON.parseObject(messageBody, MQResultDto.class);
//        消息大类型 社区:3,4,7 系统 1,2
            if (MQMessageTypeConstant.getCommunityList().contains(transactionMessageDto.getMessageDataType())) {
    //            社区
                if (mqResultDto.getType() == MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_GAME_EVALUATION_ADD.getCode()){
                    //                添加游戏评论
                    Object body = mqResultDto.getBody();
                    GameEvaluationDto gameEvaluationDto = JSON.parseObject(body.toString(), GameEvaluationDto.class);
    //                mq添加游戏评论
                    return businessBoolean(imqService.mqGameEvaluationInsert(gameEvaluationDto),messageId);
                }else if (mqResultDto.getType() == MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_GAME_EVALUATION_UPDATE.getCode()){
                    //                修改游戏评论
                    Object body = mqResultDto.getBody();
                    GameEvaluationDto gameEvaluationDto = JSON.parseObject(body.toString(), GameEvaluationDto.class);
    //                mq修改游戏评论
                    return businessBoolean(imqService.mqGameEvaluationUpdate(gameEvaluationDto),messageId);
                }else{
                    if (messageId != null) {
                        UniqueIdCkeckUtil.deleteUniqueId(messageId.toString());
                    }
                    return false;
                }
            } else {
                if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMEUPDATE.getCode()) {
    //                游戏更新
                    Object body = mqResultDto.getBody();
                    GameDto gameDto1 = JSON.parseObject(body.toString(), GameDto.class);
    //                mq游戏更新
                    return businessBoolean(imqService.mqGameUpdate(gameDto1),messageId);
                }else if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMEINSERT.getCode()) {
    //                游戏添加
                    Object body = mqResultDto.getBody();
                    GameDto gameDto1 = JSON.parseObject(body.toString(), GameDto.class);
    //                mq游戏添加
                    return businessBoolean(imqService.mqGameInsert(gameDto1),messageId);
                }else if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMERELEASELOG.getCode()) {
    //                插入游戏版本日志审批
                    Object body = mqResultDto.getBody();
                    GameReleaseLogDto gameReleaseLogDto = JSON.parseObject(body.toString(), GameReleaseLogDto.class);
    //                mq插入游戏版本日志审批
                    return businessBoolean(imqService.mqGameReleaseLogInsert(gameReleaseLogDto),messageId);
                }else if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_COUNTER.getCode()) {
    //                计数器(动态表)
                    Object body = mqResultDto.getBody();
                    Map map = JSON.parseObject(body.toString(), Map.class);
    //                mq计数器(动态表)
                    return businessBoolean(imqService.mqCounterUpdate(map),messageId);
                }else if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_MQ_DATA_TYPE_ADDGAMETAG.getCode()) {
    //                根据后台标签id集合，分类标签，游戏id
                    Object body = mqResultDto.getBody();
                    Map map = JSON.parseObject(body.toString(), Map.class);
    //                mq根据后台标签id集合，分类标签，游戏id
                    return businessBoolean(imqService.mqAddGametag(map),messageId);
                }else if (mqResultDto.getType() == MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMES_DOWNLOAD.getCode()) {
                    //                游戏下载量变化
                    Object body = mqResultDto.getBody();
                    Map map = JSON.parseObject(body.toString(), Map.class);
                    //                游戏下载量变化
                    return businessBoolean(imqService.mqUpdateGameDownLoadNum(map),messageId);
                }else{
                    if (messageId != null) {
                        UniqueIdCkeckUtil.deleteUniqueId(messageId.toString());
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            LOGGER.error("系统服务消费消息异常:" + msgData, e);
            //手动开启事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            //删除唯一请求标志,使逻辑可以重试
            if (messageId != null) {
                UniqueIdCkeckUtil.deleteUniqueId(messageId.toString());
            }
        }
        return false;
    }

    private Boolean businessBoolean(Boolean bool,Long messageId){
        if (bool) {
            //删除消息
            mqFeignClient.deleteMessageByMessageId(messageId);
        }else{
            //执行不成功，不确认消费，让定时任务重试（删除唯一请求标志）
            UniqueIdCkeckUtil.deleteUniqueId(messageId.toString());
        }
        return bool;
    }
}
