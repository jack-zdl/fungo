package com.fungo.games.service.impl;

import com.fungo.games.feign.SystemFeignClient;
import com.fungo.games.service.AsyncTaskService;
import com.game.common.dto.ResultDto;
import com.game.common.dto.action.BasActionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AsyncTaskServiceImpl implements AsyncTaskService {
    @Resource
    private SystemFeignClient systemFeignClient;


    private static final Logger LOGGER = LoggerFactory.getLogger(AsyncTaskServiceImpl.class);
    /**
     * 异步记录用户浏览游戏行为记录
     *
     * @param userId 用户id
     * @param gameId 游戏id
     */
    @Async
    public void recordGameView(String userId, String gameId) {
        BasActionDto basActionDto = new BasActionDto();
        basActionDto.setMemberId(userId);
        basActionDto.setTargetId(gameId);
        // 标识 游戏业务
        basActionDto.setTargetType(3);
        // 标识 浏览行为
        basActionDto.setType(12);
        basActionDto.setInformation("游戏浏览");
        ResultDto<String> resultDto = systemFeignClient.addAction(basActionDto);
        if(resultDto==null||!resultDto.isSuccess()){
            LOGGER.error("记录用户 {} 浏览 {} 游戏的行为失败", userId,gameId);
        }
    }


}
