package com.fungo.system.ts.mq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fungo.system.service.SystemService;
import com.fungo.system.ts.mq.service.UserMqConsumeService;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import com.game.common.dto.system.TaskDto;
import com.game.common.ts.mq.dto.MQResultDto;
import com.game.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class UserMqConsumeServiceImpl implements UserMqConsumeService {

    private final SystemService systemService;

    @Autowired
    public UserMqConsumeServiceImpl(SystemService systemService){
        this.systemService = systemService;
    }

    /**
     * 处理mq的消息
     *
     * @param msgDate mq的消息体
     */
    @Override
    public boolean processMsg(String msgDate) {
        JSONObject json = JSONObject.parseObject(msgDate);
        Integer type = json.getInteger("type");
        String body = json.getString("body");
        //扣减用户经验值和等级
        if(MQResultDto.CommunityEnum.CMT_POST_MQ_TYPE_DELETE_POST_SUBTRACT_EXP_LEVEL.getCode()==type){
            return processExpChange(body);
        }

        //添加用户动作行为数据
        if(MQResultDto.CommunityEnum.CMT_ACTION_MQ_TYPE_ACTION_ADD.getCode() == type||MQResultDto.GameMQDataType.GAME_DATA_TYPE_BASACTIONINSERT.getCode()==type){
            return processAddAction(body);
        }

        //处理用户任务
        if(MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_DO_TASK.getCode() == type){
            return processTask(body);
        }

        //消息通知:
        if(MQResultDto.CommunityEnum.CMT_POST_MOOD_MQ_TYPE_ADD_NOTICE.getCode() == type){
            return processNotice(body);
        }

        // game逻辑块变动 BasAction 查询后判断是否修改时间
        if(MQResultDto.GameMQDataType.GAME_DATA_TYPE_SELECTONEANDUPDATEALLCOLUMNBYID.getCode() == type){
            return processGameBasActionChange(body);
        }

        return false;
    }

    // 消息通知逻辑
    private boolean processNotice(String body) {

        //TODO
        return false;
    }

    /**
     * 处理用户任务
     */
    private boolean processTask(String body) {
        TaskDto taskDto = JSON.parseObject(body, TaskDto.class);
        ResultDto<Map<String, Object>> resultDto = systemService.exTask(taskDto);
        return resultDto.isSuccess();
    }

    /**
     * 添加用户动作行为数据
     */
    private boolean processAddAction(String body) {
        BasActionDto actionDto = JSON.parseObject(body, BasActionDto.class);
        ResultDto<String> resultDto = systemService.addAction(actionDto);
        return resultDto.isSuccess();
    }

    /**
     * 扣减用户经验值和等级
     */
    private boolean processExpChange(String body){
        Map map = JSON.parseObject(body, Map.class);
        String userId = String.valueOf(map.get("mb_id"));
        String score = String.valueOf(map.get("score"));
        if(StringUtil.isNull(userId)||StringUtil.isNull(score)) {
            return false;
        }
        ResultDto<String> resultDto = systemService.processUserScoreChange(userId, Integer.parseInt(score));
        return resultDto.isSuccess();
    }

    /**
     * game逻辑块变动 BasAction 查询后判断是否修改时间
     */
    private boolean processGameBasActionChange(String body) {
        Map map = JSON.parseObject(body, Map.class);
        ResultDto resultDto = systemService.updateActionUpdatedAtByCondition(map);
        return resultDto.isSuccess();
    }


}
