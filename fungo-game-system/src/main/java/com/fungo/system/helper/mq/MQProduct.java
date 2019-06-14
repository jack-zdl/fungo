package com.fungo.system.helper.mq;

import com.fungo.system.helper.RabbitMQProduct;
import com.fungo.system.ts.mq.service.ITransactionMessageService;
import com.game.common.dto.GameDto;
import com.game.common.dto.community.CmmCommunityDto;
import com.game.common.dto.community.CmmPostDto;
import com.game.common.dto.community.MooMoodDto;
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
    private RabbitMQProduct rabbitMQProduct;

    public void communityInsert(CmmCommunityDto c){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_COMMUNITYINSERT.getCode());
        mqResultDto.setBody(c);
        rabbitMQProduct.mqCommunity(mqResultDto);
    }

    public void postUpdate(CmmPostDto c){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_POST_UPDATE.getCode());
        mqResultDto.setBody(c);
        rabbitMQProduct.mqCommunity(mqResultDto);
    }

    public void moodUpdate(MooMoodDto c){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_MOOD_UPDATE.getCode());
        mqResultDto.setBody(c);
        rabbitMQProduct.mqCommunity(mqResultDto);
    }

    public void gameInsert(GameDto game){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMEINSERT.getCode());
        mqResultDto.setBody(game);
        rabbitMQProduct.mqGames(mqResultDto);
    }

    public void gameUpdate(GameDto game){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMEUPDATE.getCode());
        mqResultDto.setBody(game);
        rabbitMQProduct.mqGames(mqResultDto);
    }

    public void addGameTag(List<String> tegList , String categoryId,String id){
        Map<String,Object> map = new ConcurrentHashMap<>();
        map.put("tegList",tegList);
        map.put("categoryId",categoryId);
        map.put("id",id);
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_MQ_DATA_TYPE_ADDGAMETAG.getCode());
        mqResultDto.setBody(map);
        rabbitMQProduct.mqGames(mqResultDto);
    }

    public void gamereleaselogInsert(GameReleaseLogDto gameReleaseLogDto){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMERELEASELOG.getCode());
        mqResultDto.setBody(gameReleaseLogDto);
        rabbitMQProduct.mqGames(mqResultDto);
    }

    public void updateCounter(Map<String, String> map  ){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_COUNTER.getCode());
        mqResultDto.setBody(map);
        rabbitMQProduct.mqGames(mqResultDto);
    }


    public void communityUpdate(Map c){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_MQ_DATA_TYPE_COMMUNITY_UPDATE.getCode());
        mqResultDto.setBody(c);
        rabbitMQProduct.mqCommunity(mqResultDto);
    }

    public void gamesUpdate(Map c){
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_MQ_DATA_TYPE_GAMES_UPDATE.getCode());
        mqResultDto.setBody(c);
        rabbitMQProduct.mqCommunity(mqResultDto);
    }

    /**
     * 游戏下载量变化
     * @param ssmap
     */
    public void updateGameDownNumAndBoomDownloadNum(Map<String, String> ssmap) {
        MQResultDto mqResultDto = new MQResultDto();
        mqResultDto.setType(MQResultDto.SystemMQDataType.SYSTEM_DATA_TYPE_GAMES_DOWNLOAD.getCode());
        mqResultDto.setBody(ssmap);
        rabbitMQProduct.mqGames(mqResultDto);
    }
}
