package com.fungo.system.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fungo.system.entity.BasAction;
import com.fungo.system.service.BasActionService;
import com.fungo.system.service.IBasActionService;
import com.game.common.consts.Setting;
import com.game.common.dto.ActionInput;
import com.game.common.dto.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p></p>
 *
 * @Author: dl.zhang
 * @Date: 2020/2/27
 */
@Service
public class ActionServiceImap implements IBasActionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionServiceImpl.class);

    @Autowired
    private BasActionService actionService;

    @Override
    public ResultDto<Map<String, Object>> getCollectByGame(String gameId, String memberId) {
        ResultDto<Map<String, Object>> resultDto = null;
        Map<String, Object> resultMap = new HashMap<>( );
        try {
            BasAction basAction =  getAction(memberId, Setting.ACTION_TYPE_COLLECT,gameId, ActionInput.ActionEnum.GAME.getKey());
            if(basAction != null){
                resultMap.put( "collect",true );
            }else {
                resultMap.put( "collect",false );
            }
            resultDto = ResultDto.success( resultMap );
        }catch (Exception e){
            resultMap.put( "collect",false );
            resultDto = ResultDto.success( resultMap );
        }
        return resultDto;
    }


    //获取用户行为记录
    public BasAction getAction(String memberId, int type, String targetId,String targetType) {
        return this.actionService.selectOne(new EntityWrapper<BasAction>()
                .eq("member_id", memberId)
                .eq("target_id", targetId)
                .eq("target_type", targetType)
                .eq("type", type)
                .eq("state", 0));
    }

    public void insertKafkaLog(){
        LOGGER.error("错误日志推送kafka");
    }

}
