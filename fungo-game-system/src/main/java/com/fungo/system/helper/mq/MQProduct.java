package com.fungo.system.helper.mq;

import com.fungo.system.helper.RabbitMQProduct;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.dto.GameDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.game.GameReleaseLogDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.ts.mq.dto.TransactionMessageDto;
import org.checkerframework.checker.units.qual.A;
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

    @Autowired
    private ITransactionMessageService iTransactionMessageService;

    @Autowired
    private RabbitMQProduct rabbitMQProduct;

    /**
     * use Topic Pattern
     * "topic.key1"  路由键
     * @param message
     */
    public void sendTopic(String topicExchange,String topicKey,Object message){
    }


    public void communityInsert(CmmCommunityDto c){
        sendTopic(MQConfig.TOPIC_EXCHANGE_COMMUNITY_INSERT,MQConfig.TOPIC_KEY_COMMUNITY_INSERT,c);
    }

    public void gameInsert(GameDto game,int type){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(type);
        mqResultDto.setBody(game);
        rabbitMQProduct.mqGames(mqResultDto);
    }

    public void gameUpdate(GameDto game){
        sendTopic(MQConfig.TOPIC_EXCHANGE_GAME_UPDATE,MQConfig.TOPIC_KEY_GAME_UPDATE,game);
    }

    public void addGameTag(List<String> tegList , String categoryId,String id){
        Map<String,Object> map = new ConcurrentHashMap<>();
        map.put("tegList",tegList);
        map.put("categoryId",categoryId);
        map.put("id",id);
        sendTopic(MQConfig.TOPIC_EXCHANGE_GAME_TAG,MQConfig.TOPIC_KEY_GAME_TAG,map);
    }

    public void gamereleaselogInsert(GameReleaseLogDto gameReleaseLogDto){
        sendTopic(MQConfig.TOPIC_EXCHANGE_GAMERELEASELOG_INSERT,MQConfig.TOPIC_KEY_GAMERELEASELOG_INSERT,gameReleaseLogDto);
    }


}
