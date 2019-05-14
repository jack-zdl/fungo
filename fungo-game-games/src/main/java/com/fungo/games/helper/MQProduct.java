package com.fungo.games.helper;

import com.game.common.dto.GameDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.community.CmmCommunityDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2019/4/26
 */
@Component
public class MQProduct {
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    @Autowired
//    private  AmqpTemplate template;


//    /** DIRECT模式
//     * @param routingKey 路由关键字
//     * @param msg 消息体
//     */
//    public void sendDirectMsg(String routingKey, String msg) {
//        template.convertAndSend(MQConfig.DIRECT_QUEUE, msg);
//    }
//
//    /**
//     * sendFanout
//     * @param message
//     */
//    public void sendFanout(Object message){
//        String msg = (String) message;
//        template.convertAndSend(MQConfig.FANOUT_EXCHANGE,"",msg);
//    }
//
//    /**
//     * @param routingKey 路由关键字
//     * @param msg 消息体
//     * @param exchange 交换机   TOPIC模式
//     */
//    public void sendExchangeMsg(String exchange, String routingKey, String msg) {
//        template.convertAndSend(exchange, routingKey, msg);
//    }

    /**
     * use Topic Pattern
     * "topic.key1"  路由键
     * @param message
     */
    public void sendTopic(String topicExchange,String topicKey,Object message){
//        rabbitTemplate.setConfirmCallback((message,))
//        template.convertAndSend(topicExchange,topicKey,message);  // 可以匹配到 topic.# and topic.key1
//        template.convertAndSend(MQConfig.TOPIC_EXCHANGE,"topic.key2",msg+"2");  // 可以匹配到 topic.#
    }

//    /**
//     * @param map 消息headers属性
//     * @param exchange 交换机    header模式
//     * @param msg 消息体
//     */
//    public void sendHeadersMsg(String exchange, String msg, Map<String, Object> map) {
//        template.convertAndSend(exchange, null, msg, message -> {
//            message.getMessageProperties().getHeaders().putAll(map);
//            return message;
//        });
//    }

    /*public void communityInsert(CmmCommunityDto c){
        sendTopic(MQConfig.TOPIC_EXCHANGE_COMMUNITY_INSERT,MQConfig.TOPIC_KEY_COMMUNITY_INSERT,c);
    }*/

    public void gameInsert(GameDto game){
        sendTopic(MQConfig.TOPIC_EXCHANGE_GAME_INSERT,MQConfig.TOPIC_KEY_GAME_INSERT,game);
    }

    public void gameUpdate(GameDto game){
        sendTopic(MQConfig.TOPIC_EXCHANGE_GAME_UPDATE,MQConfig.TOPIC_KEY_GAME_UPDATE,game);
    }

    /*public void addGameTag(List<String> tegList , String categoryId, String id){
        Map<String,Object> map = new ConcurrentHashMap<>();
        map.put("tegList",tegList);
        map.put("categoryId",categoryId);
        map.put("id",id);
        sendTopic(MQConfig.TOPIC_EXCHANGE_GAME_TAG,MQConfig.TOPIC_KEY_GAME_TAG,map);
    }

    public void gamereleaselogInsert(GameReleaseLogDto gameReleaseLogDto){
        sendTopic(MQConfig.TOPIC_EXCHANGE_GAMERELEASELOG_INSERT,MQConfig.TOPIC_KEY_GAMERELEASELOG_INSERT,gameReleaseLogDto);
    }


    public void basActionInsert(BasActionDto basActionDto){
        sendTopic(MQConfig.TOPIC_EXCHANGE_BASACTION_INSERT,MQConfig.TOPIC_KEY_BASACTION_INSERT,basActionDto);
    }*/

    public void basActionInsertAllColumn(BasActionDto basActionDto) {
        sendTopic(MQConfig.TOPIC_EXCHANGE_BASACTION_INSERTALLCOLUMN,MQConfig.TOPIC_KEY_BASACTION_INSERTALLCOLUMN,basActionDto);
    }

    public void selectOneAndUpdateAllColumnById(String memberId, String targetId, int targetType, int type, int state) {
        Map<String,Object> map = new ConcurrentHashMap<>();
        map.put("memberId",memberId);
        map.put("targetId",targetId);
        map.put("targetType",targetType);
        map.put("type",type);
        map.put("state",state);
        sendTopic(MQConfig.TOPIC_EXCHANGE_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID,MQConfig.TOPIC_KEY_BASACTION_SELECTONEANDUPDATEALLCOLUMNBYID,map);
    }
}
